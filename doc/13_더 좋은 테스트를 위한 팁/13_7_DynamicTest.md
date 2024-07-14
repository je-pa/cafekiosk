# @DynamicTest
여러 테스트가 하나의 공유변수를 가지고 테스트 하는 것을 지양해야한다.

이런 상황이 아닌 어떤 하나의 환경을 설정해놓고

환경 변화를 주며 중간중간 검증을 하는 등의 일련의 사용자 시나리오를 테스트하고 싶을 때,

사용하기 좋은 것이 다이나믹 테스트다.

```java
@DisplayName("재고 차감 시나리오")
@TestFactory
Collection<DynamicTest> stockDeductionDynamicTest() {
  // given
  Stock stock = Stock.create("001", 1);

  return List.of(
      DynamicTest.dynamicTest("재고를 주어진 개수만큼 차감할 수 있다.", () -> {
        // given
        int quantity = 1;

        // when
        stock.deductQuantity(quantity);

        // then
        assertThat(stock.getQuantity()).isZero();
      }),
      DynamicTest.dynamicTest("재고보다 많은 수의 수량으로 차감 시도하는 경우 예외가 발생한다.", () -> {
        // given
        int quantity = 1;

        // when // then
        assertThatThrownBy(() -> stock.deductQuantity(quantity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("차감할 재고 수량이 없습니다.");
      })
  );
}
```
다이나믹 테스트는 환경을 설정 후 리스트 형태로 다이나믹 테스트 여러건을 던지면서 단계적인 행위와 검증 시나리오를 
구성할 수 있다.

## 사용방법
1. @TestFactory 적용
2. return 값을 Iterable(컬렉션이나 스트림)한 타입 
3. 시나리오 작성

이런식으로 상태를 공유하며 테스트를 작성해볼 수 있게 된다.