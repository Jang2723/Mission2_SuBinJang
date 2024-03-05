# 쇼핑몰 구축
- 일반 사용자는 중고거래가 가능하며, 사업자는 인터넷 쇼핑몰을 운영할 수 있게 해주는 쇼핑몰 사이트를 만들어보자.
- 요구사항의 내용은 프론트엔드 없이, 백엔드만 개발한다. 
- 별도의 프론트엔드 클라이언트가 존재한다고 생각하고 서버를 만들고, Postman으로 테스트를 한다. 
- 단, CORS는 지금은 고려하지 않는다.
---
## 스택
- Spring Boot 3.2.3
- Spring Boot Data JPA
- SQLite
- JWT
- Spring Security

## 실행
1. 본 Repository를 clone 받는다.
2. Intellij IDEA를 이용해 clone 받은 폴더를 연다.
3. [MissionShopApplication.java](/src/main/java/com/example/Mission_shop/MissionShopApplication.java) 의 `main`을 실행한다.
    - 테스트 데이터를 사용하지 않기 때문에 한번 프로젝트를 실행하고 종료 (`ddl-auto` 옵션으로 테이블 자동 생성)
    - `spring.data.jpa.hibernate.ddl-auto`를 `update`로 수정
    - 이후 정상적으로 프로젝트 실행
4. Postman을 이용한 실행 방법 => **[바로가기](/md/ExecuteReadme.md)**
---
## 필수 기능 요구사항
- [1. 사용자 인증 및 권한 처리 ](/md/1.User-authentication&authorization-handling.md)
- [2. 중고거래 중개하기 ](/md/2.Used-trade.md)
- [3. 쇼핑몰 운영하기 ](/md/3.ShoppingMall.md)
