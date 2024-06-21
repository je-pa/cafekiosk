# Mock
의존성이 필요한 객체를 가짜 객체로 만들어 정상 동작할 것이라 가정하여 
테스트 하고자 하는 레이어에만 집중해서 테스트할 수 있게 도와주는 역할을 한다.

# MockMvc
Mock(가짜) 객체를 사용해 스프링 MVC 동작을 재현할 수 있는 테스트 프레임워크

## @WebMvcTest(controllers = {테스트하고자하는 컨트롤러 명}.class) 
전체 빈 컨텍스트를 다 띄우는 `@SpringBootTest`와 달리 테스트하고자 하는 컨트롤러 레이어만 테스트 하기위해 
컨트롤러 관련 빈들만 올릴 수 있는 좀 더 가벼운 어노테이션
```java
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;
```

## MockMvc 사용하기
- **post**
  - http body에 데이터를 담아 요청할 때 직렬화와 역직렬화 과정을 거친다.</br>
    때문에 json 형태로 직렬화 하여 content안에 byte 배열 형태나 string 형태로 넣어주면 된다.
  - 예제
    ```java
    mockMvc.perform(
    post("/api/v1/products/new")
    .content(objectMapper.writeValueAsString(request))
    .contentType(MediaType.APPLICATION_JSON)
    )
    .andDo(print())
    .andExpect(MockMvcResultMatchers.status().isOk());
    ```
    > 📌 여기선 ObjectMapper을 사용하여 json으로 직렬화 해주었다.
    >  
    > ObjectMapper는 json을 다시 역직렬화 할 때 기본생성자를 사용하기 때문에 request 클래스에 기본생성자가 필요하다!
    > > - 테스트해보니 `@RequestBody`만 적용해주었는데 작동 되었다. 
    > > - 반대로 `@RequestBody`없이 기본생성자만 입력했을 때는 컴파일 오류는 뜨지 않았지만
    > >   값을 제대로 세팅해주지는 못했다.(아래의 테스트는 통과했지만 값 세팅이 제대로 되지않음.)
    > > - `@RequestBody`와 기본생성자 모두 적용하지 않았을 때는 `org.springframework.web.bind.MethodArgumentNotValidException` 오류가 발생했다.
    
- **get**
  - 예제
    ```java
     mockMvc.perform(
            get("/api/v1/products/selling")
              // .queryParam("name","이름") // 처럼 query param을 넣을 수 있다.
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andExpect(jsonPath("$.data").isArray());
    ```

### ResultActions
- status(import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status)
  - `.andExpect(status().isOk());`
- jsonPath(import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath)
  - json path를 바로 검증할 수 있는 기능 제공 
  - `.andExpect(jsonPath("$.code").value("400"))` 


# mockito
- import org.springframework.boot.test.mock.mockito
- [공식문서](https://site.mockito.org/)
### dependency
```groovy
 repositories { mavenCentral() }
 dependencies { testImplementation "org.mockito:mockito-core:3.+" }
```
> spring-boot-starter-test에 포함되어 있다.
>  ```groovy
>  testImplementation 'org.springframework.boot:spring-boot-starter-test'
>  ```

## @mockBean
컨테이너에 mockito로 mock 객체를 넣어준다.
```java
@MockBean
private ProductService productService;
```
## stubbing
```java
when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
        .thenReturn(true);
```

