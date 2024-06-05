package sample.cafekiosk.unit.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {

  // 주문일시
  private final LocalDateTime orderDateTime;

  // 주문 음료 리스트
  private final List<Beverage> beverages;

}