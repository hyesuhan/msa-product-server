## MSA(MicroService Architecture)
> MSA에 대한 학습 저장소입니다. 이론과 실습을 정리합니다.

### 📂폴더 목록 보러가기(클릭🖱️)
1. [MSA란 무엇일까요?](https://github.com/hyesuhan/msa-product-server/blob/main/Docs/MSA.md)
2. [Spring Cloud란 무엇일까요?](https://github.com/hyesuhan/msa-product-server/blob/main/Docs/SpringCloud.md)
3. [서비스 디스커버리와 Eureka](https://github.com/hyesuhan/msa-product-server/blob/main/Docs/ServiceDiscovery.md)
4. [로드 밸런싱과 Ribbon](https://github.com/hyesuhan/msa-product-server/blob/main/Docs/%EB%A1%9C%EB%93%9C%EB%B0%B8%EB%9F%B0%EC%8B%B1.md)



### 🤔트러블 슈팅 정리
1. [세팅 관련 트러블 슈팅](https://github.com/hyesuhan/msa-product-server/blob/main/Docs/troubleShooting/%EC%84%9C%EB%B2%84%EC%84%B8%ED%8C%85.md)


### 🖐️학습 중 생긴 궁금증을 풀어봅니다!
> 차근차근 올라올 예정입니다.

1. [로드밸런싱: 서버 사이드, 네트워크, 단점, 실무](https://github.com/hyesuhan/msa-product-server/blob/main/Docs/questions/%EB%A1%9C%EB%93%9C%EB%B0%B8%EB%9F%B0%EC%8B%B1QA.md)

### 실행 방법
Product 인스턴스 3개, 대신 product-db 컨테이너는 1개로 공유할것입니다.
`docker-compose up -d` 후 Appliccation을 실행하면 됩니다.