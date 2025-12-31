![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-EE4C2C?style=for-the-badge&logo=mapstruct&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![GraphQL](https://img.shields.io/badge/GraphQL-E10098?style=for-the-badge&logo=graphql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)


---
# Genie_back
---

### 📅 251209(화)
* 요구사항 정의서(Member, Admin, Order, Subscription, Pay) 작성 

### 📅 251210(수)
* 요구사항 정의서 초안 작성 완료
* 테이블 정의서 작성중 (현재 16개)
* Spring Security 포함 백엔드 서버 구상

### 📅 251211(목)
* 오라클 클라우드 서버 신규 인스턴스 1개 추가
* 서버 내 배포 디렉토리 설정 및 docker-infra 설정 및 컨테이너 생성(PostgreSQL, Redis)
* docker-container genie_tune db 생성 (table 18개)
* Java Spring Boot(Java 17, Spring Boot 3.5.8) 생성 및 db 정상 연결 확인
* 
### 📅 251212(금)
* DDD 구조 토대 패키지 생성중
* 금칙어 목록 DB ubuntu docker db(postgres) -> insert 시도 -> 데이터 형태 불완전 확인 및 중지 -> 데이터 재 조사 및 정리 요청(-> 인숙님)

### 📅 251213(토)
* Security Config 초안 작성 중 (커스텀 Filter 미등록)
* JWT Util AccessToken 생성 로직 구현 (Test 실패 및 에러 수정 필요)
* RefreshToken 관리 주체 설정 및 로직 구상 중

### 📅 251214(일)
* JWTToken 기반 Security 초안(Security Config, JWTAuthenticationFilter, JWTUtil, CookieUtil,JWTPrincipal, JWTService 등) 작성 완료 (RefreshToken 미구현 -> 보안 비즈니스 로직 구상 중, 추후 Redis와 결합하여 로직 대규모 추가 예정)
* JWTToken, HttpOnly 기반 ResponseCookie 생성 SpringBootTest 기반 코드로 성공 확인
* Member Entity, Register 서비스 로직, 관련 DTO 2개(Request,Response), Controller, schema, MemberMapper(MapStruct 기반), PasswordEncoderConfig
* GlobalExceptionHandler, ErrorCode, GlobalException 등 공통 사항 구현

### 📅 251215(월)
* Register 기능 구현 및 GraphiQL 기반 Test 완료 (Global Exception check 병행)
* JWT Service (JWT 토큰 발급 및 Cookie 생성 Cookie Header에 JWT Token 심기) 로직 완성
* Login 서비스 로직, Controller 구현 및 Mutation Schema 작성 기반 Junit Test 완료 (AuthService로 빼서 로그인 성공에 한해서 -> JWTToken 발급 및 이를 토대로 ResponseCookie 생성 -> Response Header에 add로 추가)
* GraphQL 기반 Controller에서 Respons를 catch 할 수 있도록 설정해주는 WebGraphQlInterceptor를 구현한 custom Interceptor 작성

### 📅 251216(화)
* GraphQLInterCeptor 구현(API 요청시 Response, Request catch 하는 역할 -> JWTToken를 담은 쿠키 발급용)
* Member Login, Logout 백단 로직 구현 -> 프론트 Test용 React/TypeScript 프로젝트 생성 -> 정상 구현 확인
* 국세청 사업자등록증 상태 조회 외부 API 연동 준비

### 📅 251217(수)
* WebClient 기반, 국세청 공공 API 연동 준비(WebClient Config, 관련 DTO(Request, Response 확인)
* 국세청 공공 API 사업자등록정보 상태조회 Junit Test 완료
* 비즈니스 로직- 사업자등록정보 상태조회 -> 정상영업시 회원가입창 전환, -> table 추가 생성
* 프론트 - dto 1(프론트 -> 백) - dto 2 (백 -> 공공API요청용 DTO) - dto 3(공공API 응답용 dto) -> dto 4(공공API 응답용 dto -> Entity 등록용) -> dto 5(Entity-> 프론트 응답용 dto) 구조 설립
* 관련하여 dto <-> entity 맵핑 issue, 필드명 issue 등 발생, 에러 수정하여 프론트 요청 및 연결 확인 완료

### 📅 251218(목)
* 회원가입 비즈니스 로직 변경 (회원가입창 -> 사업자등록정보 진위확인 연동 (추가 테이블 컬럼추가)) -> 백단 로직 완료 (DTO, Service, Controller, schema 등 초안 작성)
* Junit Test로 직접 test 완료
* (프론트 <-> 백) Register Test 및 사업자등록정보 진위확인 연동 Test 완료
* Redis 관련 Config 및 Util 작성

### 📅 251219(금)
* 회원가입 내 Email 인증 로직 구현 (인증메일 발송 요청 -> Spring 단에서 Redis의 key:value 형태로 6자리 코드 발급 -> JavaMailSender 활용 인증코드 메일 발송 -> 인증코드 확인 및 프론트에 코드 입력 -> 
코드 인증 요청 버튼 -> Spring단에서 RequestDTO의 Code 값과 Redis의 저장된 Code 값을 비교해서 일치하면 true 반환)
* Junit Test로 메일 발송 완료, 프론트 단 연동 테스트 완료
* Member login 및 get 재확인(백단 <-> 프론트)
* Redis 기반 RefreshToken, RefreshCookie 발급 로직 추가, JWTAuthenticationFilter 내 AccessToken 만료시 재발급 로직 미완성

### 📅 251222(월)
* RefreshToken 발급 -> ResponseCookie 발급 로직 구현, 프론트 Test 코드로 accessCookie 삭제시 RefreshCookie 보유-> 재발급 로직 check 완료
* Admin 페이지 내 등록요청 회원 목록 조회 백단 로직 구현 완료(Pageable & keyword 검색어 처리 포함)

### 📅 251223(화)
* Admin 페이지 내 회원가입 요청 회원 목록 조회(검색, 정렬, 페이지) 프론트 연결 확인
* Admin 페이지 회원 목록 내 개별 item 클릭시 멤버 정보 조회 기능 프론트 연결 확인
* back단 파트 발표 자료 준비

### 📅 251224(수)
* 프로젝트 중간 점검 발표, 기존 구현 기능 재점검
* Member update 로직 구상 중

### 📅 251225(목)
* ReadMe 커밋 이후 오류로 인한 소실 복구
* Member Update 로직(스키마 이외 구현 완료 -> 이후 프론트 연동 체크 예정)
* JWT 기반 Access Token 내 claim 회원 상태, 회원 가입 요청 상태 2가지 추가
* Security PreAuthorize 관련 Custom Bean, Custom Annotation 생성

### 📅 251226(금)
* Member Update 기능 (대표자명, 담당자명) 비밀번호 확인 및 변경 기능 추가, 프론트 체크 완료

### 📅 251227(토)
* 이메일 찾기, 비밀번호 찾기(초기화) 백단 로직 구현(프론트 체크 필요)
* Member Table Column 2개 추가(isTempPassword(boolean), passwordUpdatedAt(LocalDateTime)) 및 Entity, Mapper, DTO 수정

### 📅 251229(월)
* 이메일 찾기, 비밀번호 찾기 프론트 연결 체크 완료
* 회원 탈퇴 로직 추가 및 프론트 연결 체크 완료
* 회원가입 로직 보완(이메일 중복체크 기능 추가, 약관 추가 및 약관-회원 관계 테이블 관련 Entity, DTO, Mapper 및  memberService 내 회원가입 로직 보완, 프론트 체크 필요)
* ChatGPT Open API 연동 준비(로직 구상 중)
* register시 약관 및 약관 연동 프론트 확인 완료

### 📅 251230(화)
* ChatGPT Open API 연동을 위한 사전 세팅(DTO 2개, Config 설정)
* ChatGPT 서비스 로직 작성 및 테스트에 필요한 테이블 점검 및 수정 -> 반영하여 JAVA Entity 등록 완료 (Order, Pay, PayMethod, Product, Prompt, Service_Access, Subscription)

### 📅 251231(수)
* WebClientConfig 공통 추출 이후 나타난 사업자등록증 API 호출 error 해결
* service_access_id key 접근 Security 설정을 위한 사전 준비 (security 관리 로직 구상 -> table 수정, AES Util 생성)
* 상품 관련 서비스 로직 구상 중
* PreAuthorize를 위한 Custom 어노테이션 추가 생성(관리자 전용), 컨트롤러 메서드 개별 권한 점검
