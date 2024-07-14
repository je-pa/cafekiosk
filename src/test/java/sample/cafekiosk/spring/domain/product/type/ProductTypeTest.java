package sample.cafekiosk.spring.domain.product.type;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ProductTypeTest {

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @CsvSource({"HANDMADE, false","BOTTLE, true","BAKERY, true"})
  @ParameterizedTest
  void containsStockType_ParameterizedTest1(ProductType givenType, boolean expected) {
    // when
    boolean result = ProductType.containsStockType(givenType);

    // then
    assertThat(result).isEqualTo(expected);
  }

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @MethodSource("provideProductTypesForCheckingStockType")
  @ParameterizedTest(name = "{index} ==> ''{0}'' 는 재고 관련 타입이 {1}")
  void containsStockType_ParameterizedTest2(ProductType givenType, boolean expected) {
    // when
    boolean result = ProductType.containsStockType(givenType);

    // then
    assertThat(result).isEqualTo(expected);
  }

  private static Stream<Arguments> provideProductTypesForCheckingStockType() {
    return Stream.of(
      Arguments.of(ProductType.HANDMADE, false),
      Arguments.of(ProductType.BOTTLE, true),
      Arguments.of(ProductType.BAKERY, true)
    );
  }

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType() {
    // given
    ProductType givenType = ProductType.HANDMADE;

    // when
    boolean result = ProductType.containsStockType(givenType);

    // then
    assertThat(result).isFalse();
  }

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType2() {
    // given
    ProductType givenType = ProductType.BAKERY;

    // when
    boolean result = ProductType.containsStockType(givenType);

    // then
    assertThat(result).isTrue();
  }

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType3() {
    // given
    ProductType givenType1 = ProductType.HANDMADE;
    ProductType givenType2 = ProductType.BOTTLE;
    ProductType givenType3 = ProductType.BAKERY;

    // when
    boolean result1 = ProductType.containsStockType(givenType1);
    boolean result2 = ProductType.containsStockType(givenType2);
    boolean result3 = ProductType.containsStockType(givenType3);

    // then
    assertThat(result1).isFalse();
    assertThat(result2).isTrue();
    assertThat(result3).isTrue();
  }

  @DisplayName("(여러 논리구조가 들어간) 지양해야하는 방법: 상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType_지양() {
    // given
    ProductType[] productTypes = ProductType.values();

    for (ProductType productType : productTypes) {
      if(productType == ProductType.HANDMADE){
        // when
        boolean result = ProductType.containsStockType(productType);

        // then
        assertThat(result).isFalse();
      }
      if(productType == ProductType.BAKERY || productType == ProductType.BOTTLE){
        // when
        boolean result = ProductType.containsStockType(productType);

        // then
        assertThat(result).isTrue();
      }

    }
  }


  }