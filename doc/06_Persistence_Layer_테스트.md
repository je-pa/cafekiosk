# Persistence Layer 테스트
- Data Access의 역할
  - 데이터에 대한 CRUD 작업에 집중
- 비즈니스 가공 로직이 포함되어서는 안된다.

> ❓ JPA를 사용하면 쿼리문을 JPA가 잘 처리해줄 텐데 왜 테스트를 작성해야할까?
> 
> - 쿼리 메서드가 엄청 길어지면 **예측하기 어려워** 질 수 있다.
> - **파라미터**를 잘 못 줬을 수 있다.
> - **jpql이나 queryDSL**을 사용할 수도 있다.
> - 후에 **구현 방법이 변경**되었을 때 **테스트를 통해 보장**할 수 있다.
## 스프링 환경에서 repository 통합 테스트 진행해보기
> ❓ 사실상 repository 테스트는 단위 테스트 성격에 가깝다.
> 
> 스프링 서버를 띄우지만 레이어별로 끊어서 봤을 때 
> 데이터베이스에 액세스하는 계층이 Persistence Layer 역할을 한다.
> 
> 이때 repository는 데이터베이스에 액세스 하는 로직만 갖고 있기 때문에 
> 기능단위로 보면 단위 테스트 성격을 갖고 있다고 볼 수 있다.

### @SpringBootTest
스프링 서버를 띄어서 테스트 할 수 있게 해준다.
```java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @BootstrapWith(SpringBootTestContextBootstrapper.class)
    @ExtendWith(SpringExtension.class)
    public @interface SpringBootTest {
```

### @DataJpaTest
스프링 서버를 띄워 테스트 하는 것은 같지만 JPA 관련된 Bean들만 주입을 해주기 때문에 `@SpringBootTest`보단 가볍다.
```java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @BootstrapWith(DataJpaTestContextBootstrapper.class)
    @ExtendWith(SpringExtension.class)
    @OverrideAutoConfiguration(enabled = false)
    @TypeExcludeFilters(DataJpaTypeExcludeFilter.class)
    @Transactional
    @AutoConfigureCache
    @AutoConfigureDataJpa
    @AutoConfigureTestDatabase
    @AutoConfigureTestEntityManager
    @ImportAutoConfiguration
    public @interface DataJpaTest {
```

### @DataJpaTest vs @SpringBootTest
- 가장 큰 차이는 `@Transactional` 이다.
  - `@DataJpaTest`에는 `@Transactional`이 있지만 `@SpringBootTest`에는 없다.
  - 때문에 `@SpringBootTest`는 여러 테스트를 작동시키면 테스트끼리 영향을 줄 수 있다.
    - `@AfterEach`를 사용해 각 테스트가 끝나고 데이터를 정리해줄 수 있다.
      ```java
           @AfterEach
           void tearDown() {
           orderProductRepository.deleteAllInBatch();
           productRepository.deleteAllInBatch();
           orderRepository.deleteAllInBatch();
           }
      ```
> ✅ **`@Transactional` 주의사항!**
> 
> `@Transactional`을 사용하면 실제 테스트 하고자하는 로직에 `@Transactional`을 적용하지 않았음에도 적용한 것 처럼 작동한다.
> 
> 때문에 `@Transactional`적용해야 할 곳에 적용이 되지 않았어도 테스트가 성공해버려서 검증이 제대로 이루어지지 않기 때문에 주의가 필요하다. 