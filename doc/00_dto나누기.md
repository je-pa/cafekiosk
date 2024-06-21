# Dto 나누기
`controller`와 `service`가 **동일한 dto**를 사용한다면(=`service`가 `controller`에 해당하는 dto를 사용한다면)
하위 레이어가 상위 레이어를 알아야 하는 형태라고 볼 수 있다.

상위 레이어는 하위 레이어를 호출해야하니 알아야 하는 것이 당연하다.

반면 하위레이어는 상위 레이어를 모르는 형태가 좋다.

service 레이어에서는 서비스 dto만 사용하고, controller에 대한 아무 정보도 없게 만들면
후에 모듈을 분리할 때 편하다.

## 필요없는 의존성과 책임 분리
예를들어 아래의 리퀘스트를 controller와 service 둘 다 사용한다고 생각해보자.
```java
@Getter
@NoArgsConstructor
public class OrderCreateRequest {

  @NotEmpty(message = "상품 번호 리스트는 필수입니다.")
  private List<String> productNumbers;

  @Builder
  private OrderCreateRequest(List<String> productNumbers) {
    this.productNumbers = productNumbers;
  }

  public OrderCreateServiceRequest toServiceRequest() {
    return OrderCreateServiceRequest.builder()
        .productNumbers(productNumbers)
        .build();
  }
}
```
같은 모듈이라면 크게 상관없을 수 있지만 분리를 하게된다면
service 모듈에 해당 클래스를 가져와야 할 것이다.

여기서 좋지 않은 점은 해당 클래스에 @NotEmpty가 있기 때문에 service가 validation 체크를 하지 않음에도 불구하고 
service 모듈에 validation 의존성이 필요하게 된다는 것이다.

반대로, 분리해서 사용한다면 controller만 validation을 가져가게 되어 책임 분리 면에서도 이점을 볼 수 있다. 

## 변경에 닫히도록
분리하지 않은 형태는 controller가 변하면 service도 변경을 해주어야 할 수 있다.

만약 다른 컨트롤러에서 동일한 서비스를 사용하고 싶다고 생각해보자.

분리가 되어있지 않다면 새로운 컨트롤러도 기존 컨트롤러의 dto를 알아야한다는 문제점이 있다.

controller가 변경이 되어도 service는 영향을 받지 않도록 하는 것이 좋다.