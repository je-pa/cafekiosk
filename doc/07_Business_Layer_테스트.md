# Business Layer 테스트
- 비지니스 로직을 구현하는 역할
- Persistence Layer와의 상호작용(Data를 읽고 쓰는 행위)을 통해 비즈니스 로직을 전개시킨다.
- **트랜잭션**을 보장해야 한다.
> Business Layer는 Persistence Layer와 통합한 두 레이어를 한꺼번에 테스트 하는 느낌으로 작성한다.
