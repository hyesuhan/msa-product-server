### 왜 Fallback 메서드가 안 불러와질까요?

원래 같으면 실패 시 Fallback 메서드가 불러와져야 하는데, 왜 안되는 지 모르겠다.

이는 로그와 페이지에서 확인을 했는데,

> 페이지에서는 WhiteLabel Error가 뜹니다..


> 로그에서는 다음과 같이 나옵니다.

<img width="528" height="253" alt="Image" src="https://github.com/user-attachments/assets/643e7da9-1041-4b8a-bc2d-23e903302e43" />


Resilience4J 문서를 보니 `fallbackMethod` 는 원본 메서드와 **동일 반환 타입**을 가져야 한다.

나는 Fallback 메서드에서는 전체 정보가 필요 없으니 새로운 dto를 만들어 return 했는데 이가 불가 하다는 것이다.

반환 타입이 다르다면 프록시에서 해당 fallback 메서드를 유효 핸들러로 인식하지 못하고 Exception 을 던지게 된다.

(그래서 이미지에서 `No fallback method found` 라는 메시지가 나오는 것이다.)

#### 해결
그래서 나는 ProductFallBackResponse에서 ProductResponse를 상속받아서 구현해주었다!

그러나 대신 ProductResponse 클래스 내부에 success 필드나 error Message필드를 포함하거나

정적 팩토리 패턴으로 ProductResponse 객체를 만들어서 반환하는 방법도 있을 것 같다.
