## 03. 서비스 디스커버리

### 서비스 디스커버리란?
MSA에서 각 서비스의 위치를 **동적**으로 관리하고 찾아주는 기능입니다.
각 서비스는 등록 서버에서 자신의 위치를 등록하고 다른 서비스는 이를 조회해서 통신하게 딥니다.
> 서비스 등록, 서비스 조회, Health Check 등의 기능을 포함합니다.

MSA 자체가 서비스가 유동적으로 생성되고 사라지는 환경이기에, 예를 들어 '주문 서비스'가 트래픽이 몰려서 3대에서 10대로 늘어날 수 있고, 서버가 고장나 새로운 IP로 변경할 수도 있습니다.
이 때 **어떤 서비스가 현재 어떤 IP와 PORT에 있는가?**를 실시간으로 관리하고 찾아내는 기능을 해줍니다.


Service Discovery는 MSA의 개념 중 하나로 알면 되고, 이제 소개할 Eureka는 이를 구현하기 위한 프레임워크입니다.

### Eureka
#### Eureka란?
> 넷플릭스 개발 디스커버리 서버

모든 서비스 인스턴스의 위치를 저장하는 중앙 저장소 역할로 서비스 인스턴스 상태를 주기적으로 확인해 가용성을 보장해줍니다.
크게 두 부분으로 나뉘게 되는데 다음과 같습니다.
1. Eureka Server: 각 서비스(Instance)의 위치 정보를 저장하고 관리합니다.
2. Eureka Client: 각 서비스가 시작될 때 자신의 정보를 서버에 등록하고, 다른 서비스의 정보를 알고 싶을 때 서버로 부터 목록을 받아옵니다.

#### Eureka Server
> 서비스 Registry를 구성하는 중앙 서버입니다.

다음의 설정을 진행해야 합니다.
```yaml
server:
    port: 8761
eureka:
  client:
    register-with-eureka: false # Eureka Server는 자신을 등록할 필요가 없다.
    fetch-registry: false # Eureka Server는 다른 서비스의 정보를 가져올 필요가 없다.
  server:
    enable-self-preservation: false # 자기 보호 모드 비활성화, 로컬에만 설정
```

왜 포트가 8761인지 궁금했는데, 사실 Spring 개발할 때 자연스레 8080을 쓰게 되는 것처럼 Eureka Server도 8761을 쓰는 관례가 있다고 합니다.

#### Eureka Client
> 서비스가 시작될 때 자신의 정보를 Eureka Server에 등록하는 역할을 합니다.

`spring-cloud-starter-netflix-eureka-client` 의존성과 설정파일만 세팅하면 간단하게 등록이 됩니다.
```yaml
spring:
  application:
    name: order-service # 서비스 이름, 이만 있어도 등록됨

eureka:
  client:
    server-url:
      defaultZone: http://localhost:8761/eureka/ # Eureka Server 주소
    register-with-eureka: true # Eureka Server에 등록 여부
    fetch-registry: true # Eureka Server에서 다른 서비스 정보 가져올지 여부
  instance:
    hostname: localhost # 서비스 인스턴스의 호스트 이름
    prefer-ip-address: true # IP 주소를 호스트 이름 대신 사용 여부
    lease-renewal-interval-in-seconds: 10 # 서비스 인스턴스가 자신의 상태를 갱신하는 간격
    lease-expiration-duration-in-seconds: 30 # 서비스 인스턴스가 응답하지 않을 때까지 기다리는 시간
```

🖐️ 내부 동작 순서?
1. Bootstrap: Application 이 시작될 때 eureka-client 라이브러리가 있는지 확인합니다.
2. Naming: spring.application.name을 읽어서 서비스 이름을 결정합니다.
3. Registration: 설정된 defaultZone 주소로 이름과 주소를 보냅니다.
4. Heartbeat: 설정된 간격으로 자신의 상태를 갱신합니다.

### 서비스 등록 & 디스커버리
서비스 등록은 위에서 자세히 설명했으니 디스커버리 부분을 조금 설명하겠습니다.

서비스를 등록하면 클라이언트 애플리케이션은 Eureka 서버에서 필요한 서비스의 위치를 조회합니다.
약간 다르게 말하면 서버가 클라이언트가 되어서 다른 서버와 통신을 해야하는 상황입니다.
이 때 사용하는 게 무엇일까요? 바로 **RestTemplate**입니다.
-라고 했지만 이제 업데이트 안해서 그냥 그러려니 하고 넘겨도 좋습니다.-

만약 이로 개발하고 싶다면 `@LoadBalanced` 애노테이션을 통해 RestTemplate이 Eureka 서버에서 서비스 인스턴스 목록을 가져와서 로드 밸런싱을 할 수 있도록 설정할 수 있습니다.

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

```java
@RestController
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/order")
    public String order() {
        String serviceUrl = "http://app/api/product"; // Eureka에서 app 서비스의 URL을 가져옴
        return restTemplate.getForObject(serviceUrl, String.class);
        
    }
}
```

일반적인 RestTemplate는 URL에 적힌 IP나 도메인을 보고 바로 가는데, MSA 환경에서는 서비스 IP가 동적으로 변할 수 있습니다.
그렇기 때문에 `@LoadBalanced` 애노테이션으로 Url의 호스트 부분을 서비스 명칭으로 인식합니다.

❕하지만❕ Spring 5 부터는 비동기 처리가 가능한 WebClient로 넘어갔고, MSA 에서는 `OpenFeign`을 선호합니다.

##### OpenFeign 이란?
> Netflix에서 개발한 선언적 HTTP 클라이언트입니다. (근데 이거 오픈 소스로 풀려서 Spring 에서 Spring Cloud OpenFeign으로 지원합니다.)

이게 무슨 말이냐면, 어떻게 통신하지? 가 아니라 어디로 보낼지만 인터페이스 정의만 하면 된다는 의미 입니다.
`@FeignClient` 에노테이션을 통해서 Spring이 런타임에 해당 인터페이스 구현체를 자동으로 만들어 구현 클래스를 만들 필요 없이(RestTemplate 처럼) 프로젝트 안에 있는 메서드 처럼 외부 API를 호출할 수 있게됩니다.

```java
@SpringBootApplication
@EnableFeignClients // Feign 클라이언트 활성화
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

```java
@FeignClient(name = "product-service") // Eureka에서 product-service를 찾아서 통신
public interface ProductClient {
    @GetMapping("/api/product")
    String getProduct();
}
```

```java
@RestController
public class OrderController {
    @Autowired
    private ProductClient productClient;
    
    @GetMapping("/order")
    public String order() {
        return productClient.getProduct(); // Feign이 알아서 Eureka에서 product-service 찾아서 통신
    }
}
```

#### Health Check 및 장애 처리
> Eureka 서버가 주기적으로 서비스 인스턴스 상태를 확인해서 가용성을 유지시켜 줍니다.

기본 헬스 체크 엔드포인트는 `/actuator/health` 입니다.
서비스 장애 시 Eureka 서버가 해당 인스턴스를 레지스트리에서 제거해서 다른 서비스의 접근을 차단할 수 있습니다.

#### Eureka Cluster 구성
> 고가용성을 위해 Eureka 또한 여러 인스턴스를 구성할 수 있습니다.

다중 인스턴스로 구성해서 서로를 레지스트리로 사용하도록 설정할 수 있습니다.
각 인스턴스는 Peer 로 등록해서 상호 Back-Up 할 수 있습니다.

```yaml
eureka:
  client:
  service-url:
    defaultZone: http://eureka-peer1:8761/eureka/,http://eureka-peer2:8761/eureka/ # 여러 Eureka 서버 주소
```

### 실습 관련
저는 계속 실습을 이어나갈 것 이기에, product, order, server 3개의 모듈을 만들었습니다!
이 때 server가 바로 eureka server가 될 것이고, 각각은 eureka client가 될 것입니다.
-나중에 user가 추가될 것 같습니다-

아 참고로 각 모듈 만들 때 Server는 Eureke server dependency를 나머지는 Eureka Client dependency를 추가해주시면 됩니다.
-추가로 SpringWeb도-

먼저 eureka server가 될 server 모듈에 아래의 환경 설정을 해주었습니다.
```yaml
spring:
  application:
    name: server

server:
    port: 19090

eureka:
  client:
    register-with-eureka: false # Eureka 서버는 Client 로 인식될 필요가 없다.
    fetch-registry: false # Eureka 서버는 다른 서비스의 레지스트리를 가져올 필요가 없다.
    service-url:
      defaultZone: http://localhost:19090/eureka/ # Eureka Client 와 통신할 Url을 지정한다.
  instance:
    hostname: localhost # Eureka 서버의 호스트 이름을 설정한다.
```
근데 저는 웃기게 자동으로 docker로 IP가 들어가더라고요.
IP를 본인 컴퓨터로 셋팅해도 좋고 저는 IP 안가려도 되서 docker로 놔두었습니다.

이제는 Client들을 등록하겠습니다.
```yaml
spring:
  application:
    name: product
server:
  port: 19092

eureka:
  client:
    service-url:
      defaultZone: http://localhost:19090/eureka/ # Eureka Client 와 통신할 Url을 지정한다.

```
저기서 각 모듈에 대해서 포트 번호만 수정해서 사용하면 됩니다.

먼저 Eureka Server 부터 띄워보겠습니다.
<img width="957" height="909" alt="Image" src="https://github.com/user-attachments/assets/b18cca7a-f9b1-43aa-af04-b97226c9af5a" />

다음 OrderApplication을 띄우면 Eureka Server에 Application이 잘 등록된 것이 보이나요?
<img width="958" height="940" alt="Image" src="https://github.com/user-attachments/assets/610b57bb-15d7-4d78-9cec-ab06d741f8fa" />

우리는 이제 Eureka 서버를 올리고 Client를 등록하는 방법까지 알았습니다!

