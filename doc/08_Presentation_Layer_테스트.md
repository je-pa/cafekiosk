# Presentation Layer 
- 외부 세계의 요청을 가장 먼저 받는 계층
- 파라미터에 대한 최소한의 검증을 수행

## 테스트 방식
- Business Layer와 Persistence Layer는 Moccking 처리

## Presentation Layer 에서 파라미터에 대한 검증
```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
validation을 사용할 수 있다.

### validation의 책임 분리
아래의 코드는 request 클래스의 일부 코드라고 생각해보자.
```java
@Positive(message = "상품 가격은 양수여야 합니다.")
@Max(20)
private int price;
```
@Max(20)는 20자로 제한해준다. 해당 정책은 컨트롤러 레이어에서 막아줘야하는 책임이 맞을까?

변수형에 합당한 validation과 도메인 정책에 맞는 validation을 구분하자.

도메인 정책에 맞는 validation은 컨트롤러에서 잡아주기 보단, 
service 레이어나 entity 객체를 생성하는 시점에 생성자에서 검증을 하는 것이 더 맞을 수 있다.