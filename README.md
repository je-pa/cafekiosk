# 간단한 카페 키오스크

## spring x 요구사항
- 주문 목록에 음료 추가/삭제 기능
  - 한 종류의 음료 여러 잔을 한 번에 담는 기능
- 주문 목록 전체 지우기
- 주문 목록 총 금액 계산하기
- 주문 생성하기 
- 가게 운영 시간(10:00~22:00) 외 주문 생성 불가
- 키오스크 주문을 위한 상품 후보 리스트 조회하기

## spring o 요구사항
- 원하는 판매상태를 가진 상품들을 조회한다.
  - 상품의 판매 상태: 판매중, 판매보류, 판매중지
    - 판매중, 판매보류인 상태의 상품을 화면에 보여준다.
- 화면에 필요한 정보: id, 상품 번호, 상품 타입, 판매 상태, 상품 이름, 가격
- 주문 생성: 메뉴 선택 후 가격을 보고 주문하기를 누르면 주문 엔티티 생성
  - 상품 번호(product number) 리스트를 받아 주문 생성하기
  - 주문은 주문 상태, 주문 등록 시간을 가진다.
  - 주문의 총 금액을 계산할 수 있어야 한다.
  - 생성 시 재고 확인 및 개수 차감 후 생성하기
    - 재고는 상품번호를 가진다.
    - 재고와 관련 있는 상품 타입은 병 음료, 베이커리이다.(핸드메이드는 관련 없다.)