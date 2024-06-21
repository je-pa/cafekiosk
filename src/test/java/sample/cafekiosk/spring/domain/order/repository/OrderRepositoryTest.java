package sample.cafekiosk.spring.domain.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.order.type.OrderStatus.INIT;
import static sample.cafekiosk.spring.domain.order.type.OrderStatus.PAYMENT_COMPLETED;
import static sample.cafekiosk.spring.domain.product.type.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.type.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.type.OrderStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.repository.ProductRepository;
import sample.cafekiosk.spring.domain.product.type.ProductType;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ProductRepository productRepository;

  // TODO

  @DisplayName("원하는 주문상태를 가진 특정 기간 내의 주문들을 조회한다.")
  @Test
  void findOrdersBy(){
    // given
    LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 2000);
    Product product3 = createProduct(HANDMADE, "003", 3000);
    List<Product> products = List.of(product1, product2, product3);
    productRepository.saveAll(products);

    Order order1 = createAndSavePaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
    Order order2 = createAndSavePaymentCompletedOrder(now, products);
    Order order3 = createAndSavePaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
    Order order4 = createAndSave(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products, INIT);
    Order order5 = createAndSavePaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);

    // when
    List<Order> list = orderRepository.findOrdersBy(now, LocalDateTime.of(2023, 3, 6, 0, 0),
        PAYMENT_COMPLETED);

    // then
    assertThat(list).hasSize(2)
        .extracting("orderStatus", "registeredDateTime")
        .containsExactlyInAnyOrder(
            tuple(PAYMENT_COMPLETED, now),
            tuple(PAYMENT_COMPLETED, LocalDateTime.of(2023, 3, 5, 23, 59, 59))
        );
  }

  private Product createProduct(ProductType type, String productNumber, int price) {
    return Product.builder()
        .type(type)
        .productNumber(productNumber)
        .price(price)
        .sellingStatus(SELLING)
        .name("메뉴 이름")
        .build();
  }

  private Order createAndSavePaymentCompletedOrder(LocalDateTime now, List<Product> products) {
    return orderRepository.save(createAndSave(now, products, PAYMENT_COMPLETED));
  }

  private Order createAndSave(LocalDateTime now, List<Product> products, OrderStatus orderStatus) {
    Order order = Order.builder()
        .products(products)
        .orderStatus(orderStatus)
        .registeredDateTime(now)
        .build();
    return orderRepository.save(order);
  }
}