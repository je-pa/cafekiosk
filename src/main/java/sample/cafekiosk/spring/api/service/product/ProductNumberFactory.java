package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sample.cafekiosk.spring.domain.product.repository.ProductRepository;

@RequiredArgsConstructor
@Component
public class ProductNumberFactory {

  private final ProductRepository productRepository;

  /**
   * productNumber 생성(DB 마지막 저장된 Product의 상품번호에서 +1)
   * @return
   */
  public String createNextProductNumber() {
    String latestProductNumber = productRepository.findLatestProductNumber();
    if (latestProductNumber == null) {
      return "001";
    }

    int latestProductNumberInt = Integer.parseInt(latestProductNumber);
    int nextProductNumberInt = latestProductNumberInt + 1;

    return String.format("%03d", nextProductNumberInt);
  }

}