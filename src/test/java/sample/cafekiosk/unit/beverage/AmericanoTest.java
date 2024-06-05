package sample.cafekiosk.unit.beverage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AmericanoTest {
  @Test
  void getName() {
    Americano americano = new Americano();

    // junit
    assertEquals(americano.getName(), "아메리카노");

    // assertj
    assertThat(americano.getName()).isEqualTo("아메리카노");
  }

  @Test
  void getPrice() {
    Americano americano = new Americano();

    assertThat(americano.getPrice()).isEqualTo(4000);
  }
}