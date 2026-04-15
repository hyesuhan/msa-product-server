## 🌉 API Gateway 란?

---

클라이언트의 요청을 받아서 서비스로 Routing 하고, 다양한 부가 기능을 제공하는 **중간 서버**입니다.

Client - Server 사이 단일 진입점 역할을 하며 크게 보안, 로깅, 모니터링, 필터링 등의 역할을 합니다.

**😒 그렇다면 그냥 Gateway와 차이점?**

일반적인 Gateway는 OSI 3~4 계층인 Network/Transport 계층에서 서로 다른 네트워크(Subnet)을 연결하는 역할을 합니다.

즉, 데이터 패킷 라우팅, IP 변환 등의 역할을 담당합니다.

그러나 API GateWay는 말처럼 API를 다룹니다. 즉, Application 계층인 OSI 7 layer을 다루는 서버입니다.

따라서 API 서비스 위주의 비즈니스 로직 처리를 담당하게 됩니다.

### 예시와 함께 주요 역할 살펴보기 🙄

1. **라우팅**

Client의 요청 URL, Header, Path 등을 확인해 미리 정의된 조건에 맞는 특정 서비스로 연결해줍니다.

라우팅이 바로 Gateway의 주요 기능입니다!

예를 들어 큰 쇼핑몰🛍️에 들어가면 안내원을 만나러 갑니다.

“배가 고파요” 라고 하면 푸드 코트로 보냅니다.

“옷을 사고 시퍼요” 라고 하면 의류 매장으로 보냅니다.

이 처럼 Client 가 각 매장 위치를 **몰라도** 안내 데스크인 Gateway 만 찾아가면 됩니다.

1. **인증 및 권한 부여(Authentication & Authorization)**

요청이 내부 시스템에 진입하기 전에 유효한 사용자인지(인증), 해당 리소스에 접근 권한이 있는지(권한)을 먼저 체크하게 됩니다.

이는 `GatewayFilter` 을 통해 구현되며, 주로 `TokenRelay` 나 커스텀 필터를 통해 수행합니다.

예를 들어 🍸클럽에 갔다고 합시다. 그런데 앞에 보안 요원이 있네요?

모든 손님은 들어가기 전 신분증(JWT 등)을 제시해 검사받아야 합니다.

신분증이 없거나 미성년자라면 아예 접근이 불가능합니다.

이를 통해 서비스는 일일이 신분증 검사 필요성이 줄어듭니다. (왜 없다 가 아닌 줄어든다는 나중에 볼 수 있어요)

1. **로드 밸런싱 (Load Balancing)**

동일 서비스를 제공하는 인스턴스가 여러 대 일때, 요청을 골고루 분산시켜 특정 서버에 부하가 물리지 않게 합니다.

`LoadBalancerClientFilter` 를 통해 Eureka 와 같은 서비스 디스커버리를 통해 찾은 인스턴스로 요청을 분배합니다.

예를 들어 🍣 맛집에 대기 줄 관리자입니다.

주방에 요리사(인스턴스)가 3명 이라면, 관리자는 주문을 고르게 배분해 요리가 빨리 나올 수 있게 조절합니다.

**😒 그렇다면 `@CircuitBreaker` 와 차이점이 무엇인가요?**

API Gateway는 서버 사이드 형태의 로드 밸런싱을 수행합니다.

클라이언트와 마이크로서비스 사이에 위치하며 클라이언트가 요청을 보내면 `lb://SERVICE-NAME` 형식을 해석해 인스턴스로 전달합니다.

즉, 외부 첫 진입점에서의 부하 분산을 담당하게 됩니다.

OpenFeign 과 Spring Cloud LoadBalancer 은 클라이언트 사이드 로드 밸런싱을 수행합니다.

서비스 A가 B를 호출할 때, 즉 서비스 내부에서 실행됩니다. A는 직접 B의 인스턴스 목록을 알고 있고, 스스로 누구에게 보낼지 결정합니다.

즉, Gateway를 거치지 않는 서비스 간 통신에서의 부하 분산을 담당합니다.

1. **모니터링 및 로깅 (Monitoring & Logging)**

Gateway 통과하는 모든 트래픽 상태, 응답 시간, 에러율 등을 기록하고 관리합니다.

글로벌 필터, Observability 설정을 통해 요청 시작 ~ 끝 지표를 수집합니다.

예를 들어 👁️‍🗨️ 쇼핑몰 입구의 CCTV 가 있다고 합시다.

오늘 몇 명이 방문했는지 (Traffic), 주로 몇 시에 오는지 (Peak Time), 입구에서 넘어진 사람은 없는지 (Error) 를 파악해 보고합니다.

1. **요청 및 응답 변환(Request & Response Transformation)**

요청이 서비스로 전달되기 전이나 서비스 응답이 클라이언트로 가기 전 내용을 수정하거나 헤더를 추가/삭제하는 기능입니다.

`AddRequestHeader` , `RewritePath` , `SetStatus` 등 내장 `GatewayFilterFactory` 를 통해 변경 가능합니다.

예를 들어 🇺🇲 미국인 CEO와 소통을 위한 통역사를 불렀습니다.

우리는 영어를 모르기에 한국어로 말하면 통역사인 Gateway가 이를 영어로 번역해 전달합니다.

반대로 영어로 말하면 우리가 이해할 수 있는 한국어로 번역해줍니다.

## Spring Cloud Gateway

> Spring Cloud Netflix 패키지의 일부로, MSA에 사용됩니다.
> 

당연히 위에서 말한 기능들을 제공합니다.

### 설정

다음의 의존성을 주입해야 합니다.

```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
```

### 라우팅 설정

설정 파일에서 다음과 같이 라우팅 설정을 정의 가능합니다.

```yaml
spring:
	cloud:
		gateway:
			discovery:
				locator:
					enabled: true # 서비스 디스커버리를 통해 동적 라우트 생성
			routes:
				id: users-service # 라우트 식별자
				uri: lb://users-service # 'users-service'라는 이름으로 로드밸런싱된 서비스로 라우팅
				predicates:
					Path=/users/** # wild-card, /users/ 로 들어오는 요청을 처리함
				id: orders-service
				uri: lb://orders-service
				predicates:
					Path=/orders/**
				
eureka:
	client:
		service-url:
			defaultZone: http://localhost:8761/eureka/  # 이 또한 유레카 서버로 등록해주어야 합니다.
```

## Spring Cloud Gateway 필터링

> 필터링(Filtering) 은 위에서 설명한 인증, 로깅, 변환 등을 관통하는 매커니즘입니다. 요청이 서비스에 도달 전과 후 실행되는 Interceptor 의 체인과 관련이 있습니다.
> 

왜 필터링이 필요할까요?

먼저 **공통 관심사의 분리**를 들 수 있겠습니다!

MSA에 주문, 결제, 상품 등이 서비스가 있다고 해봅시다. 모든 서비스에 “로그인 여부” 를 각각 넣는 것 대신 Gateway 필터에서 한 번 검사하고 통과한 요청에만 유효한 사용자 헤더를 넣어줄 수 있습니다.

다음은 **보안 및 트레픽 제어**가 가능하다는 점 입니다.

만약 특정 조건을 만족하지 못한다면 서비스에 도달하지 못하게 할 수 있습니다.

특정 사용자가 1초에 100번 넘게 요청을 보내면 필터에서 Rate Limiting 을 계산해 `429 Too Many Requests` 같은 에러를 반환하고 사전에 차단할 수 있습니다.

허용되지 않은 국가, IP 대역을 필터에서 거를 수 있습니다.

이 외에도 **데이터 가공 및 변환**을 해줄 수 있다는 장점이 있습니다.

이는 Response/Request DTO에 해당하는 것 뿐 아니라

만약 지금 `v2` 가 나온 상황인데 `v1` 인 구버전으로 요청을 보내면 이를 신버전인 `v2` 로 변환해줄 수 있습니다.

또한 응답 내용 중 불필요하거나 마스킹이 필요한 정보를 필터링 할 수 있습니다.

### 필터 종류

1. **Global Filter**

모든 요청에 대해 작동하는 필터입니다.

특정 라우트에 개별적으로 등록할 필요가 없으며 특히 전체 트래픽 로깅, 전역 인증 체크 등의 서비스 공통 적용 로직에 사용하게 됩니다.

1. **Gateway Filter**

특정 라우트에만 적용되는 필터입니다.

`GatewayFilterFactory` 를 통해 생성되며, `yaml` 파일에 직접 라우트 하위에 명시해야 합니다.

필터에 전달할 매개변수(config) 를 라우트마다 다르게 설정 가능합니다.

### 필터 구현

필터를 구현하려면 `GlobalFilter` 또는 `GatewayFilter` 인터페이스를 구현하고 `filter` 메서드를 오버라이딩하면 됩니다.

### 필터 주요 객체

이는 Spring Security와 비슷한 부분이 많다고 생각하기에 이와 구조를 연관지어서 고려하면 좋을 것 같습니다!

Spring Cloud Gateway는 WebFlux 위에서 동작하기에 서블릿 기반 `HttpServletRequest/Response` 대신 아래 객체를 사용하게 됩니다.

**😒 What is WebFlux**

Spring MVC 는 1요청이 1 thread 이기때문에 DB 조회에 10sec 가 걸린다면 Blocking 상태에 있을 것입니다. 만약 요청이 많다면 Thread가 부족해 서버가 터질 것입니다. 이를 해결한 것이 WebFlux 입니다.

비동기 서버 기반 논블로킹/이벤트 루프 기반 웹 프레임워크입니다.

IO 작업이 많은 곳에서 효율적입니다.

- 블로킹과 논블로킹
    
    제어권과 관련된 문제입니다.
    
    **블로킹**
    
    내가 호출한 함수가 작업이 끝날 때까지 나에게 제어권을 주지 않아서 내 Thread가 대기 상태에 멈춰있는 것 입니다.
    
    **논블로킹**
    
    함수를 호출하자마자 바로 제어권을 돌려받아, 내 Thread가 다른 일을 할 수 있는 상태입니다.
    

1. `Mono`

Reactive Programming 에서 0 또는 1 개의 데이터를 **비동기적**으로 처리합니다. (Publisher)

Gateway 필터는 비동기적으로 동작하기에 결과를 즉시 반환하는 것이 아닌, 나중에 처리되면 알려준다는 약속(Mono)를 반환합니다.

`Mono<Void>` 는 아무 데이터도 반환하지 않음을 의미 합니다.

필터가 특정 값을 반환하는 것이 아닌 ‘완료 여부’ 만 알려주면 될 때 사용합니다.

**😒Reactive Programming**

이벤트가 발생하면 그에 반응해 처리하는 방식입니다.

Data Stream 과 변경 사항 전파에 중점을 둔 프로그래밍 패러다임입니다.

😒 그러면 Event-Driven Architecture랑 차이가 뭐예요?

Reactive Programming은 애플리케이션 내부 코드 로직입니다.

함수, 객체 안 데이터 스트림 단위로 한 서버 안에서 CPU와 메모리를 효율적으로 쓰는 것을 관점으로 둡니다.

DB에서 데이터가 오면(EVENT), A 필터를 거쳐 응답으로 내보내는 행위를 합니다.

Mono 나 Flux가 Publisher 가 되고, 이를 사용하는 로직이 Listener/Subscriber 가 됩니다.

Event-Driven Architecture은 MSA 간 통신에 대한 아키텍처입니다.

서비스와 서비스 사이 메시지를 단위로 하며 시스템 전체 결합도를 낮추고 확장성을 높이기 위한 디자인입니다.

주문 서비스에서 결제가 완료되면(EVENT), 배송 서비스와 알림 서비스에 알리는 행위를 합니다.

Kafaka나 RabiitMQ 같은 메시지 브로커가 Publisher와 Listener 사이를 중재합니다.

**😒동기와 비동기**

- 동기와 비동기
    
    **동기(Sync)**
    
    A와 B가 작업을 할 때 A가 끝나야 B가 수행할 수 있습니다.
    
    **비동기(Async)**
    
    A와 B가 작업을 할 때 각자 합니다. 대신 B가 작업이 끝나면 A에게 알려줍니다. (Callback)
    

1. `ServerWebExchange` 

HTTP 요청과 응답을 캡슐화한 객체입니다. 즉 HTTP 요청/응답의 전체 Context 를 담고 있습니다.

`exchage.getRequest()` 로 단일 HTTP 요청-응답 트랜젝션에 대한 서버측 HTTP 요청을 가져옵니다.

`exchange.getResponse()` 로 HTTP 응답을 가져옵니다.

예를 들어 필터 안에서 헤더 읽기, 쿠키 굽기, 필터 공유할 데이터 저장 등의 역할을 합니다.

1. `GatewayFilterChain`

여러 필터를 체인처럼 연결합니다. (필터들의 목록)

`chain.filter(exchange)` 는 다음 필터로 요청을 전달합니다.

### 필터 시점별 종류

1. **Pre Filter**

요청이 처리 되기 전에 실행됩니다.

이 필터에서는 요청을 가로채 필요 작업을 수행 후 체인의 다음 필터로 전달합니다.

이때, 추가적인 비동기 작업을 수행할 필요가 없기 때문에 then 메서드가 필요 없습니다.

이 때 인증/인가 체크, 요청 헤더 추가, 화이트리스트/블랙리스트 IP 필터링 등의 작업을 수행합니다.

```java
@Component
public class PreFilter implements GloblFiler, Ordered {
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		//request log
		System.out.println("Request: " + exchange.getRequest().getPath());
		return chain.filter(exchange);
		
	}
	
	@Override
	public int getOrder() {
		return -1; // 필터 순서를 지정합니다.
	}
}
```

😒 추가적인 비동기 작업을 수행할 필요가 없다?

Pre Filter의 로직이 단순한 동기식 검증이거나, 다음 필터로의 전달 자체가 곧 비동기의 시작점이기 때문에 이렇게 말하는 것 입니다!

단순 Validation 이나 전달이 목적이라면 추가적인 비동기 작업이 필요 없습니다.

만약 Pre Filter에서 Redis에서 화이트리스트 IP를 조회하는 경우 네트워크 I/O가 발생하기에 비동기 작업이 될 수 있습니다. 그러나 이 때는 `then` 이 아니라 Redis 조회 결과를 기다렸다가 다음 필터로 넘기는 `flatMap` 등을 사용해야 합니다.

즉, 요청을 보낸 뒤에 할 일이 없다 이지 필터 로직 자체가 비동기라는 의미는 아닙니다!

1. **Post Filter**

요청이 처리된 이후 응답이 반환되기 전에 실행됩니다.

체인의 다음 필터가 완료된 후 실행되어야 하는 추가 작업을 수행해야 합니다.

따라서 chain.filter(exchange) 를 호출해 다음 필터를 실행 후 , then 메서드를 사용해서 응답이 완료된 후 실행 작업을 정의합니다.

이 때 응답 헤더 추가, 응답 시간 계산(로깅), 민감한 데이터 마스킹, CORS 설정 등을 작업하게 됩니다.

```java
@Component
public class PostFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // 응답 로깅
            System.out.println("Response Status: " + exchange.getResponse().getStatusCode());
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
```

## 실습하기

는 추후에 업데이트 될 예정입니다.
