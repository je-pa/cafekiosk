# 테스트는 문서다.
- 프로덕션 기능을 설명하는 테스트 코드 문서
- 다양한 테스트 케이스를 통해 프로덕션 코드를 이해하는 시각과 관점을 보완
  - 해피케이스, 예외케이스
- 어느 한 사람이 과거에 경험했는 고민의 결과물을 팀 차원으로 승격시켜 모두의 자산으로 공유

# DisplayName
5버전부터 지원되는 어노테이션으로서 테스트의 이름을 지정할 수 있다.

## DisplayName 섬세하게
### 명사 나열보다 문장
- A이면 B이다
- A이면 B가 아니고 C다.

- 예시
    > 음료 1개 추가 테스트(지양할 것)
    > 음료를 1개 추가할 수 있다.

### 테스트 행위에 대한 결과까지 기술
- 예시
  > 음료를 1개 추가할 수 있다.
  > 음료를 1개 추가하면 주문 목록에 담긴다.

### 도메인 용어를 사용해 한층 추상화된 내용 기술
- 메서드 자체 관점보다 도메인 정책 관점으로
- 테스트의 현상을 중점으로 기술하지 말 것
- 예시
  > 특정 시간 이전에 주문을 생성하면 실패한다.
  > 영업 시작 시간 이전에는 주문을 생성할 수 없다.
