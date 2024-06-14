package sample.cafekiosk.spring.domain.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import sample.cafekiosk.spring.domain.stock.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

  List<Stock> findAllByProductNumberIn(List<String> productNumbers);

}