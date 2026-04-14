### 왜 Fallback 메서드가 안 불러와질까요?

원래 같으면 실패 시 Fallback 메서드가 불러와져야 하는데, 왜 안되는 지 모르겠다.

이는 로그와 페이지에서 확인을 했는데,

> 페이지에서는 WhiteLabel Error가 뜹니다..


> 로그에서는 다음과 같이 나옵니다.

[!image](https://private-user-images.githubusercontent.com/121243582/577732600-cc73de37-efdc-4874-8eff-d842454d97f9.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NzYxMzUyNzUsIm5iZiI6MTc3NjEzNDk3NSwicGF0aCI6Ii8xMjEyNDM1ODIvNTc3NzMyNjAwLWNjNzNkZTM3LWVmZGMtNDg3NC04ZWZmLWQ4NDI0NTRkOTdmOS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjYwNDE0JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI2MDQxNFQwMjQ5MzVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1jZjE2NGMxYzUwOWQ4YWE5NjA1ZjVlODQ2MzZlNWRkZjJjYmE0MjUwZmE4MWY1MTUzZWYwNTRlMzY4NzFhNzUyJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZyZXNwb25zZS1jb250ZW50LXR5cGU9aW1hZ2UlMkZwbmcifQ.XUu9FXv-SSnxVlkY0ocojWGoUOsJqj3kd3_41nsj0rs)

Resilience4J 문서를 보니 `fallbackMethod` 는 원본 메서드와 **동일 반환 타입**을 가져야 한다.

나는 Fallback 메서드에서는 전체 정보가 필요 없으니 새로운 dto를 만들어 return 했는데 이가 불가 하다는 것이다.

반환 타입이 다르다면 프록시에서 해당 fallback 메서드를 유효 핸들러로 인식하지 못하고 Exception 을 던지게 된다.

(그래서 이미지에서 `No fallback method found` 라는 메시지가 나오는 것이다.)

#### 해결
그래서 나는 ProductFallBackResponse에서 ProductResponse를 상속받아서 구현해주었다!

그러나 대신 ProductResponse 클래스 내부에 success 필드나 error Message필드를 포함하거나

정적 팩토리 패턴으로 ProductResponse 객체를 만들어서 반환하는 방법도 있을 것 같다.
