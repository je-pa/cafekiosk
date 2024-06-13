package sample.cafekiosk.spring.api.service.product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;
import sample.cafekiosk.spring.domain.product.repository.ProductRepository;
import sample.cafekiosk.spring.domain.product.type.ProductSellingStatus;

@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductRepository productRepository;

  public List<ProductResponse> getSellingProducts() {
    List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

    return products.stream()
        .map(ProductResponse::of)
        .collect(Collectors.toList());
  }

}