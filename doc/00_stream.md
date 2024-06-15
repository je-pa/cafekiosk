# stream
## List<> ➡️ List<>
### filter
```java
// 재고 차감 체크가 필요한 상품들 filter
  private static  extractStockProductNumbers() {
    List<Product> products;
    List<String> list = products.stream()
        .filter(product -> ProductType.containsStockType(product.getType()))
        .map(Product::getProductNumber)
        .collect(Collectors.toList());
  }
```
### 변환
```java
    List<String> productNumbers;
    List<Product> list = productNumbers.stream().map(productMap::get)
        .collect(Collectors.toList());
```

## List<> ➡️ Map<>
### Collectors.toMap(keyMapper, ValueMapper)
```java
    List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
    Map<String, Product> productMap = products.stream()
        .collect(Collectors.toMap(Product::getProductNumber, p -> p));
```
### Collectors.groupingBy(classifier, downstream)
```java
List<String> stockProductNumbers;
Map<String, Long> map = stockProductNumbers.stream()
    .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
```  

