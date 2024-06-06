package sample.cafekiosk.unit;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

@Slf4j
public class CafeKioskRunner {

  public static void main(String[] args) {
    CafeKiosk cafeKiosk = new CafeKiosk();
    cafeKiosk.add(new Americano());
    log.info(">>> 아메리카노 추가");
    cafeKiosk.add(new Latte());
    log.info(">>> 라떼 추가");

    int totalPrice = cafeKiosk.calculateTotalPrice();
    log.info("총 주문가격 : {}", totalPrice);

    Order order = cafeKiosk.createOrder(LocalDateTime.now());
  }

}