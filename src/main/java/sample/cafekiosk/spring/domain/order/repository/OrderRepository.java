package sample.cafekiosk.spring.domain.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.type.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query("select o from Order o where o.registeredDateTime >= :startDateTime" +
      " and o.registeredDateTime < :endDateTime" +
      " and o.orderStatus = :orderStatus")
  List<Order> findOrdersBy(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus orderStatus);
}