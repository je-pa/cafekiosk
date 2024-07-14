# private 메서드는 테스트를 해야할까?
private 메서드를 테스트하고 싶어진다면 객체 분리를 고민해보아야 한다.

클라이언트 입장에서는 노출되지 않은 내부 기능까지 알 필요가 없다.

```java
/**
   * productNumber 생성(DB 마지막 저장된 Product의 상품번호에서 +1)
   * @return
   */
  private String createNextProductNumber() {
    String latestProductNumber = productRepository.findLatestProductNumber();
    if (latestProductNumber == null) {
      return "001";
    }

    int latestProductNumberInt = Integer.parseInt(latestProductNumber);
    int nextProductNumberInt = latestProductNumberInt + 1;

    return String.format("%03d", nextProductNumberInt);
  }
```

`ProductService`의 private 메서드를 테스트 하고싶다면 
해당 메서드의 product number를 구하는 책임을 분리해 보자.

```java
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
```

하나의 공개 api에서 하나의 public 메서드에서 하는 일이 많은지 의문을 가져보도록 하자.