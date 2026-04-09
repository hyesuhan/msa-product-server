## Spring Cloud 개요
---
https://github.com/user-attachments/assets/1ddcb797-fd55-487c-ad65-5e5bf14f2fee

### Spring Cloud란?

마이크로 서비스 개발을 위해 다양한 도구와 서비스를 제공하는 스프링 프레임워크 확장 도구
마이크로 서비스 아키텍처를 쉽게 구현하고 운영 가능할 수 있게 도와줍니다.

🖐️ AWS, Azure, GCP 같은 클라우드 서비스가 있는데 왜 Spring Cloud를 쓰는건가요?
    바로 제어권(Control), 이식성(Portability), 개발 일관성(Consistency) 때문입니다.
    AWS의 ELB, Cloud Map 등을 사용하면 편리하지만, 코드가 AWS 특정 "인프라"에 종속되게 됩니다.
    Spring Cloud는 클라우드 서비스에 종속되지 않고, 다양한 클라우드 환경에서 동일한 코드로 운영할 수 있게 해줍니다.
        예를 들어 서비스 디스커버리를 위해서 Eureka를 쓰다가 Kubernets로 이전할 때, 애플리케이션 코드는 그래도 @EnableDiscoveryClient 하나도 동일하게 유지됩니다.
        또한 Spring Cloud 문서에서 "분산 시스템의 공통 패턴을 추상화하여, 어떤 개발 환경에서도 동일 코드로 동작하게 함"을 기술해 놓았습니다!
    뿐만 아니라, 로컬에서도 시스템 테스트를 하기 아주 유용합니다. Java 프로세스로 실행되기에 MSA 토폴로지를 구성해 디버깅할 수 있습니다.

**주요 기능**
> 지금은 간단히 이런 기능이 있다. 정도로만 언급하고 각 Chapter에서 자세히 다루도록 하겠습니다.
> 아래에서 **Bold** 로 표시된 모듈을 실습에 적용할 것 입니다.

1. 서비스 등록 및 디스커버리: **Eureka**, Consul, Zookeeper

2. 로드 밸런싱: **Ribbon**, Spring Cloud LoadBalancer

3. 서킷 브레이커: hystrix, **Resiliene4j**

4. API 게이트웨이: Zuuul, **Spring Cloud Gateway**

5. 구성 관리: Spring Cloud Config

6. 분산 추적: Spring Cloud Sleuth, Zipkin

7. 메시징: Spring Cloud Strean

## Spring Cloud 주요 모듈

특정 서버가 아닌 MSA 들이 공통적으로 가져야 하는 기능입니다.

### 서비스 등록 및 디스커버리

### Eureka
넷플릭스 개발 서비스 디스커버리 서버입니다.

마이크로 아키텍처에서 각 서비스의 위치를 동적으로 관리합니다.
    가장 오래되었고 안정적입니다. 또한 설정이 쉽다는 장점이 있습니다.
    Kubernets 환경으로 넘어가면서 별도 Eureka 서버 없이 K8s Service/DNS 로 많이 쓰기도 하는데 이번에는  Spring Cloud에 집중해볼 거기 때문에 Eureka를 택했습니다.

**[특징]**

서비스 레지스트리
    모든 서비스 인스턴스의 위치를 저장하는 중앙 저장소

헬스 체크(health-check)
    서비스의 인스턴스의 상태를 주기적으로 확인해 가용성 보장

### 로드 밸런싱

### Ribbon
넷플릭스 개발 클라이언트 사이드 로드 밸런서입니다.
서비스 인스턴스 간 부하를 분산을 해줍니다.
    사실 Ribbon이 과거의 표준이지만 지금은 유지 보수(이라고 하고 업데이트 안 한다고 말함) 중이어서 Spring Cloud LoadBalancer 로 대체되고 있습니다. 하지만 설정이 간단하고 레거시 시스템에서는 많이 사용되므로 이로 실습해 봅시다!

**[특징]**

서버 리스트 제공자
    Eureka로부터 서비스 인스턴스 리스트를 받아서 로드 밸런싱에 사용

로드밸런싱 알고리즘
    라운드 로빈, 가중치 기반 등 다양한 로드 밸런싱 알고리즘 지원

Failover
    요청 실패 시 다른 인스턴스로 자동 전환

### 서킷 브레이커

### Hystrix

넷플릭스 개발 서킷 브레이커 라이브러리
서비스 간 호출 실패를 감지하고 시스템 전체 안정성 유지해 줍니다.

**[특징]**

서킷 브레이커 상태는 Closed, Open, Half-Open 상태를 통해서 호출 실패를 관리한다.

호출 실패 시 대체 로직(Failback)을 제공해 시스템 안정성을 확보한다.

Hystrix Dashboard 를 통해 서킷 브레이커 상태를 모니터링할 수 있다.



### Resilience4j

자바 기반 경량 서킷 브레이커 라이브러리로 넷플릭스 Hystrix의 대안으로 개발되었습니다.
    Hystrix도 이제 개발이 중단되었습닏. Java 8 이상에 최적화 되어 있어서 함수형 프로그래밍을 지원합니다. 그래서 선택했습니다.

**[특징]**

호출 실패를 감지하고 서킷을 열어 추가 호출을 차단해 시스템 부하를 줄인다.

호출 실패 시 대체 로직을 실행한다. (Failback)

호출 응답 시간을 설정해 느린 서비스 호출에 대응 가능하다. (Timeout)

재시도 기능을 지원해 일시 네트워크 문제 등에 대응 가능하다.

## Spring Cloud 구성 요소의 활용

### API GateWay

MSA 환경에서 독립적인 하나의 서버로 존재하는 요소입니다.

### Zuul

넷플릭스 개발 API GW 로, 모든 서비스 요청을 중앙에서 Control 합니다.

**[특징]**

요청 URL에 따라 적절 서비스로 **라우팅**합니다.

요청 전후 다양한 작업을 수행 가능한 **Filter Chain**을 제공합니다.

요청 로그 및 Metric을 통해 서비스 상태 **Monitoring** 가능합니다.

### Cloud Gateway

스프링 클라우드에서 제공하는 API GW로, MSA에서 필수 역할입니다.
    Zuul 1은 동기 방식이라서 성능의 한계가 이써 Zuul 2는 비동기 방식으로 개선되었지만, Spring Cloud Gateway가 더 최신 기술로 개발되고 있어서 이번에는 Spring Cloud Gateway로 실습해 봅시다!

**[특징]**

요청을 받아 특정 서비스로 **라우팅**하고 필요 인증 및 **권한** 부여를 수행합니다.

외부 요청으로부터 애플리케이션을 보호하고, **보안** 정책을 적용합니다.

MSA 아키텍처에서 필요 요청 처리 및 분산 환경의 관리를 **효율**적으로 수행합니다.

## 구성 관리

### Spring Cloud Config

분산 환경에서 중앙 집중식 설정 관리를 제공합니다.

**[특징]**

중앙에서 설정 파일을 관리하고 각 서비스에 제공합니다. (**Config 서버**)

Config 서버에서 설정을 받아 사용하는 서비스(**Config Client**)

설정 변경 시 서비스 재시작 없이 실시간을 반영합니다.

## 예시로 알아보기

<aside>

***📋우리는 MSA 아키텍처의 커머스 사이트에서 주문을 하려고 한다.
현재 Order, Product_1, Product_2, User Application이 존재한다.***

</aside>

### 외부 설정 관리: Spring Cloud Config

---

모든 서비스(Order, Product, User) 의 설정값은 yaml 파일에 DB 접속 정보, 환경 변수 등 다양한 조건들이 설정되어 있다.

이에 대한 관리를 중앙에서 관리하게 하는 것이 Spring Cloud Config이다.

서비스가 시작될 때 Config Server에 접속해 필요 설정 정보를 받아와 설정 변경 시 서버 재시작 없이 반영 가능하다.

### 시스템의 입구: API Gateway

---

클라이언트는 주문 요청을 하게 되면 가장 먼저 지나는 지점이다.

다음의 기능을 수행한다.

1. 이 사용자가 로그인(인증)이 되었는가?
2. `Order` 서비스의 주소로 라우팅 하자.

### 서비스 위치 찾기: Eureka (Service Discovery)

---

각 서비스(Order, Product, User)가 현재 어느 IP와 PORT 에서 실행 중인지 기록되어 있다.

Order 서비스가 Product 서비스에게 상품 정보를 물어보려 할 때, Eureka에서 Product 서비스 주소를 요청한다.

### 부하 분산: Ribbon (Load Balancer)

---

만약 Product 서비스에 Product_1, Product_2 의 2개의 서비스가 있다고 하자.

이 때 요청을 골고루 분산해 주는 것이 Client Side Load Balancer 이다.

Ribbon 은 Eureka로 부터 받은 주소 목록들 중에서 하나를 선택해 요청을 보낸다.

### 장애 차단용: Resilience4J (Circuit Breaker)

---

특정 서비스에 장애가 발생했을 때 그 장애가 호출자나 상위 서비스까지 전파되지 않게 차단해준다.

예를 들어 User(회원조회) 서비스에서 장애가 발생해 서버 응답이 없다면, 이를 무한정 기다리거나 Product(상품 확인)까지 가지 않고 대신 Resilience4J를 통해 빠르게 에러를 내거나 미리 준비된 응답(Fallback)을 보내게 된다.