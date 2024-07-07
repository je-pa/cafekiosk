# BDDMockito

기존의 `org.mockito.stubbing;`를 사용하면 어색한 부분이 있다.
```java
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

  @Mock
  private MailSendClient mailSendClient;

  @Mock
  private MailSendHistoryRepository mailSendHistoryRepository;

  @InjectMocks
  private MailService mailService;

  @DisplayName("메일 전송 테스트")
  @Test
  void sendMail() {

    // given
    Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(true);

    // when
    boolean result = mailService.sendMail("", "", "", "");

    // then
    assertThat(result).isTrue();
    verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
  }
}
```
바로 given에 when 함수가 들어가게 된다는 것

given when then으로 작성하고자 만든 것이 BDDMockito다.

```java
  @DisplayName("메일 전송 테스트")
  @Test
  void sendMail() {

    // given
    BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
      .willReturn(true);
    // when
    boolean result = mailService.sendMail("", "", "", "");

    // then
    assertThat(result).isTrue();
    verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
  }
```

## BDDMocktio는 Mockito를 감싼 것이다.
Mockito와 모든 동작이 동일하지만 이름만 바뀌었다.

```java
package org.mockito;

import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

public class BDDMockito extends Mockito {
  public BDDMockito() {
  }

  public static <T> BDDMyOngoingStubbing<T> given(T methodCall) {
    return new BDDOngoingStubbingImpl(Mockito.when(methodCall));
  }
  ...
```