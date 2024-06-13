package sample.cafekiosk.spring.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.type.ProductSellingStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * select *
   * from product
   * where selling_status in ('SELLING', 'HOLD');
   */
  List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

}