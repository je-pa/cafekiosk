package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.repository.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.repository.ProductRepository;
import sample.cafekiosk.spring.domain.product.type.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.repository.StockRepository;

@RequiredArgsConstructor
@Service
public class OrderService {

  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final StockRepository stockRepository;

  @Transactional
  public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {
    List<String> productNumbers = request.getProductNumbers();
    List<Product> products = findProductsBy(productNumbers);

    deductStockQuantities(products);

    Order order = Order.create(products, registeredDateTime);
    Order savedOrder = orderRepository.save(order);
    return OrderResponse.of(savedOrder);
  }

  /**
   * 재고 차감 체크가 필요한 상품들 filter -> 재고 엔티티 조회 -> 상품별 counting -> 재고 차감 시도
   *
   * @param products
   */
  private void deductStockQuantities(List<Product> products) {
    List<String> stockProductNumbers = extractStockProductNumbers(products);

    Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
    Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

    for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
      Stock stock = stockMap.get(stockProductNumber);
      int quantity = productCountingMap.get(stockProductNumber).intValue();

      if (stock.isQuantityLessThan(quantity)) {
        throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
      }
      stock.deductQuantity(quantity);
    }
  }

  private List<Product> findProductsBy(List<String> productNumbers) {
    List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
    Map<String, Product> productMap = products.stream()
        .collect(Collectors.toMap(Product::getProductNumber, p -> p));

    return productNumbers.stream()
        .map(productMap::get)
        .collect(Collectors.toList());
  }

  // 재고 차감 체크가 필요한 상품들 filter
  private static List<String> extractStockProductNumbers(List<Product> products) {
    return products.stream()
        .filter(product -> ProductType.containsStockType(product.getType()))
        .map(Product::getProductNumber)
        .collect(Collectors.toList());
  }

  // 상품별 counting
  private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
    return stockProductNumbers.stream()
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
  }

  // 재고 엔티티 조회
  private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
    return stockRepository.findAllByProductNumberIn(stockProductNumbers).stream()
        .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
  }

}