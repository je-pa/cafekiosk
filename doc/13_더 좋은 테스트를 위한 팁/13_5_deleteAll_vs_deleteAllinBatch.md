## Test Fixture 클렌징
아래에 product 3개를 저장하고 그 다음에 order 행위를 하여 검증을하는 코드가 있다.
```java
  @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
  @Test
  void createOrder() {
    // given
    LocalDateTime registeredDateTime = LocalDateTime.now();

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 3000);
    Product product3 = createProduct(HANDMADE, "003", 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    OrderCreateRequest request = OrderCreateRequest.builder()
        .productNumbers(List.of("001", "002"))
        .build();

    // when
    OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

    // then
    assertThat(orderResponse.getId()).isNotNull();
    assertThat(orderResponse)
        .extracting("registeredDateTime", "totalPrice")
        .contains(registeredDateTime, 4000);
    assertThat(orderResponse.getProducts()).hasSize(2)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("002", 3000)
        );
  }
```

product 1,2 번과 order 관계가 orderProduct 중간 테이블에 매핑되어 orderProduct와 order도 생길 것이다.

이런 것들을 어떻게 클렌징할 것인가?

- deleteAllInBatch
- transactional: 롤백 클렌징으로 쉽게 쓸수는 있지만 사아드 이펙트를 잘 알고 써야한다.

### deleteAll vs deleteAllinBatch
내부적으로는 order와 product만 생성했지만
관계에 의해 orderProduct도 생기기때문에 클렌징이 추가로 필요하다.
```java
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
private List<OrderProduct> orderProducts = new ArrayList<>();
```

```java
orderProductRepository.deleteAllInBatch();
productRepository.deleteAllInBatch();
orderRepository.deleteAllInBatch();
```
여기서 만약 순서를 바꿔서 product ➡ orderProduct ➡ order 순으로 삭제를 한다면

orderProdcut의 fk가 product를 참조하고 있어 product를 삭제 못하는 오류가 뜬다.
```java
JDBC exception executing SQL [delete from product p1_0] [Referential integrity constraint violation: "FKHNFGQYJX3I80QOYMRSSLS3KNO: PUBLIC.ORDER_PRODUCT FOREIGN KEY(PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) (CAST(1 AS BIGINT))"; SQL statement:
delete from product p1_0 [23503-224]] [n/a]; SQL [n/a]; constraint [FKHNFGQYJX3I80QOYMRSSLS3KNO: PUBLIC.ORDER_PRODUCT FOREIGN KEY(PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) (CAST(1 AS BIGINT)); SQL statement:
delete from product p1_0 [23503-224]]
org.springframework.dao.DataIntegrityViolationException: JDBC exception executing SQL [delete from product p1_0] [Referential integrity constraint violation: "FKHNFGQYJX3I80QOYMRSSLS3KNO: PUBLIC.ORDER_PRODUCT FOREIGN KEY(PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) (CAST(1 AS BIGINT))"; SQL statement:
delete from product p1_0 [23503-224]] [n/a]; SQL [n/a]; constraint [FKHNFGQYJX3I80QOYMRSSLS3KNO: PUBLIC.ORDER_PRODUCT FOREIGN KEY(PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) (CAST(1 AS BIGINT)); SQL statement:
delete from product p1_0 [23503-224]]
```
deleteAllInBatch는 깔끔하게 테이블 전체 데이터를 지울 수 있는 메서드인데 순서를 잘 고려해야한다.

deleteAll을 하면 먼저 전체 테이블을 조회한 후 orderProduct 건수마다 delete where id =? 를 날린다.

deleteAll을 하면 내부적으로
1. order를 전체를 조회해온 후
2. 건수마다 order을 조회하고
3. 조회된 order에 있는 orderProduct를 또 조회한다.
4. 그리고 조회된 orderProduct를 건수마다 delete를 날려 지운다.
```sql
    select {...} from orders o1_0
Hibernate: 
    select {...} from order_product op1_0 where op1_0.order_id=?
Hibernate: 
    delete from order_product where id=?
Hibernate: 
    delete from order_product where id=?
Hibernate: 
    delete from orders where id=?
Hibernate: 
    select {...}  from product p1_0
Hibernate: 
    delete from product where id=?
Hibernate: 
    delete from product where id=?
Hibernate: 
    delete from product where id=?
Hibernate: 
    select {...} from  stock s1_0
```

실제로 구현된 deleteAll을 보면 반복문을 도는 것을 볼 수 있다.

건건히 지우는 비용때문에 테스트가 느려질 수 있다.
```java
  @Transactional
  public void deleteAll() {
    Iterator var2 = this.findAll().iterator();

    while(var2.hasNext()) {
      T element = (Object)var2.next();
      this.delete(element);
    }
  }
```
또한 deleteAll이라고 순서를 고려하지 않아도 되는 것은 아니다.

product를 먼저 지우고 order를 지우면 orderProduct를 지우기 전에 product를 지우려 해서 오류가 뜰 것이다.

> @Transactional 을 사용하면 롤백이 편하지만 사이드 이팩트를 고려해야하고,
>
> 후에 스프링 배치 통합 테스트를 쓰면 여러 트랜잭션이 걸리기 때문에 @Transactional 를 사용하기 어렵기 때문에
>
> delete를 수동으로 해주어야한다.