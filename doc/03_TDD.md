# TDD: Test Driven Development
> 테스트를 먼저 작성한 후 프로덕션 코드를 작성하여 테스트가 구현 과정을 주도하도록 하는 방법론

## TDD 핵심 방법: 레드 그린 리펙토링
| 단계       | 내용                    | 설명                                       |
|----------|-----------------------|------------------------------------------|
| Red      | 실패하는 테스트 작성           | 일부러 실패 테스트를 작성하는 것이 아닌 처음이기 때문에 자연스러운 단계 |
| Green    | 테스트 통과 최소한의 구현        | 빠른 시간 내에 테스트를 통과 하도록 구현부를 작성한다.          | 
| Refactor | 테스트 통과를 유지하며 구현 코드 개선 | 초록불을 유지하며 리펙토링을 진행한다.                    |

## TDD의 핵심가치
> 클라이언트(사용자) 관점에서 피드백을 주는 Test Driven

### 선 기능 구현, 후 테스트 작성
- 테스트 자체의 누락 가능성
  - 테스트의 중요성은 알지만 기능이 완성되면 작동하는 것만 보고 테스트에 소홀해질 수 잇다.
- 특정 테스트 케이스만 검증할 가능성(해피케이스)
- 잘못된 구현을 다소 늦게 발견할 가능성
- 관점: 테스트는 구현부 검증을 위한 보조 수단으로 여겨진다.

### 선 테스트 작성, 후 기능 구현
- 복잡도가 낮고 테스트 가능한 코드로 구현할 수 있게 한다.
  - 유연하고 유지보수가 쉬워진다.
  - 앞서서 봤던 currentDateTime같은 경우 테스트를 먼저 작성하게 되면 테스트를 하기 위한 구조를 고민하게 된다.
- 쉽게 발견하기 어려운 엣지(Edge) 케이스를 놓치지 않게 해준다.
- 구현에 대한 빠른 피드백을 받을 수 있다.
- 과감한 리팩토링이 가능해진다.(테스트가 보장해준다.)
- 관점: 테스트와 상호 작용하며 발전하는 구현부로 여겨진다.

## 예시
### 총 금액 계산 테스트 먼저 작성
```java
@Test
void calculateTotalPrice() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();
    Latte latte = new Latte();

    cafeKiosk.add(americano);
    cafeKiosk.add(latte);

    int totalPrice = cafeKiosk.calculateTotalPrice();

    assertThat(totalPrice).isEqualTo(8500);
}
```
### 코드 작성
```java
public int calculateTotalPrice() {
    return 8500;
}
```

### 리펙토링
```java
public int calculateTotalPrice() {
    return beverages.stream()
        .mapToInt(Beverage::getPrice)
        .sum();
}
```