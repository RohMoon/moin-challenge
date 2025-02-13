# 🏦 Moin Backend Challenge

> **모인 해외 송금 서비스 API**  
> 본 프로젝트는 모인(Moin)의 백엔드 개발 과제를 수행하기 위한 API 서버입니다.

---

## 📋 **프로젝트 개요**
본 프로젝트는 사용자가 미국(USD) 및 일본(JPY)으로 송금할 수 있는 기능을 제공합니다.  
주요 기능은 다음과 같습니다.

- **회원 가입**
- **로그인** (JWT 인증)
- **송금 견적서 발행**
- **송금 접수 요청**
- **사용자의 거래 이력 조회**

---

## ⚙ **기술 스택**
- **언어**: Java 17
- **프레임워크**: Spring Boot 3.3.2
- **빌드 도구**: Gradle 8.12.1
- **데이터베이스**: H2 (인메모리)
- **인증 방식**: JWT (JSON Web Token)
- **테스트**: JUnit 5

---

## 🚀 **실행 방법**
### 📌 **1. 프로젝트 클론**
bash
git clone https://github.com/your-repo/moin-challenge.git
cd moin-challenge


###  2️⃣ **환경 변수 설정**
yml
jwt:
secretKey: "QyNvbmc4aW9ucyYteW91LXdFcmUtYWxsLTEhbDBuMiE"
expiration: 1800000 # 30분 (단위: 밀리초)


###  3️⃣ **빌드 및 실행**
bash
./gradlew build
./gradlew bootRun

### 4️⃣ **Swagger API 문서 확인**
- API 문서는 실행 후 아래 URL에서 확인 가능합니다.

<a href="http://localhost:8080/swagger-ui/index.html" target="_blank">
    <button>Swagger UI 열기</button>
</a>

--- 
## 🛠  **환경 변수 설정**
yml
spring:
application:
name: moin-challenge
datasource:
url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
driver-class-name: org.h2.Driver
username: sangMoon
password: 1234

jpa:
hibernate:
ddl-auto: update
show-sql: true

# H2 콘솔 활성화
h2:
console:
enabled: true
path: /h2-console

security:
user:
name: user
password: "$2a$10$l49YJqdf5NVX7IeBYWNS6eYTfaLPGd7V1m.YLa1cZzghBPqlOQPjS"

springdoc:
api-docs:
path: /api-docs
enabled : true

swagger-ui:
enabled: true
path: swagger-ui
operations-sorter: method
tags-sorter: alpha
disable-swagger-default-url: true

jwt:
secretKey: "QyNvbmc4aW9ucyYteW91LXdFcmUtYWxsLTEhbDBuMiE="
expiration: 1800000

encryption:
secretKey : "LSNCZXlvbmQtdGhlLW1vb24jMDchMjJCYUNFU1NLRVk="


---
## 🔑 **API 명세**
-----------

|     기능     | 	HTTP Method  |        	URL	        | 인증 필요  |
|:----------:|:-------------:|:-------------------:|:------:|
|   회원 가입    |     	POST     |    	/user/signup    |   	❌   |
|    로그인     |     	POST     |    	/user/login     |   	❌   |
| 송금 견적서 발행  |     	POST     |  	/transfer/quote   |   	✅   |
|  송금 접수 요청  |     	POST     | 	/transfer/request  |   	✅   |
|  거래 내역 조회  |     	GET	     |   /transfer/list    |   	✅   |

### 📌 **1. 회원 가입**
- Endpoint: POST /user/signup
- 요청 예시
  json
  {
  "userId": "sample@gmail.com",
  "password": "Qq09iu!@1238798",
  "name": "테스트 유저",
  "idType": "REG_NO",
  "idValue": "001123-3111111"
  }

- 응답 예시
  json
  {
  "resultCode": 200,
  "resultMsg": "OK"
  }


### 📌 **2. 로그인**
- Endpoint: POST /user/login
- 요청 예시
  json
  {
  "userId": "sample@gmail.com",
  "password": "Qq09iu!@1238798"
  }

- 응답 예시
  json
  {
  "resultCode": 200,
  "resultMsg": "OK",
  "token": "tokenValue"
  }


### 📌 **3. 송금 견적서 발행**
- Endpoint:POST /transfer/quote
- 요청 예시
  json
  {
  "amount": 10000,
  "targetCurrency": "JPY"
  }

- 응답 예시
  json
  {
  "resultCode": 200,
  "resultMsg": "OK",
  "quote": {
  "quoteId": 1,
  "exchangeRate": 9.013,
  "expireTime": "2024-02-16 08:21:09",
  "targetAmount": 630.91
  }
  }


### 📌 **4. 송금 접수 요청**
- Endpoint:POST /transfer/request
- 요청 예시
  json
  {
  "quoteId": 1
  }

- 응답 예시
  json
  {
  "resultCode": 200,
  "resultMsg": "OK"
  }

### **📌 5. 거래 내역 조회**
- Endpoint:GET /transfer/list
- 응답 예시
  json
  {
  "resultCode": 200,
  "resultMsg": "OK",
  "userId": "moin@themoin.com",
  "name": "모인주식회사",
  "todayTransferCount": 1,
  "todayTransferUsdAmount": 457.10,
  "history": [
  {
  "sourceAmount": 400000,
  "fee": 3000,
  "usdExchangeRate": 1301.01,
  "usdAmount": 305.14,
  "targetCurrency": "USD",
  "exchangeRate": 1301.01,
  "targetAmount": 305.14,
  "requestedDate": "2023-12-01 10:30:21"
  }
  ]
  }

-----
## ✅ 구현 내용
### 1. 회원 인증 (JWT)
- Spring Security + JWT 를 활용한 인증 및 권한 관리 구현
  로그인 성공 시 JWT 발급 및 요청 시 검증 수행
### 2. 송금 견적서 발행
- API를 통해 실시간 환율 조회 (https://crix-api-cdn.upbit.com/v1/forex/recent)
  통화별 수수료 적용 및 받는 금액 계산
### 3. 송금 요청 제한
- 개인회원(REG_NO) 하루 송금 한도: $1000
- 법인회원(BUSINESS_NO) 하루 송금 한도: $5000
- 초과 시 LIMIT_EXCESS 에러 반환
### 4. 거래 내역 조회
사용자별 송금 이력 관리 및 페이징 적용 가능

## 🔍 테스트 ##
- 단위 테스트: JUnit 5
  bash
  ./gradlew test


## 📌 회고 ##
- 설계 방식
  계층 분리 (Controller → Service → Repository)
    - 예외 처리를 위한 GlobalExceptionHandler 추가
    - H2 인메모리 DB 사용 (로컬 개발 환경 최적화)


- 보완할 점
    - Redis를 활용한 Quote 저장을 고려
    - 캐싱을 이용한 환율 API 호출 최적화
    - 로깅 및 트랜잭션 모니터링 추가
    - aggregate 등 레이어를 좀 더 작게 분리
    - 일관화 된 응답/반응
    - docker 설정
    - userId와 userPK 필드 사용을 풀어내지 못한점
    - 받는 돈이라는 문장이 요구사항에 있었지만 풀어내지 못한 점
    - 네이밍 및 메서드 이름의 심플화 

- ## ⚠️ 주요 예외 처리

본 프로젝트에서는 다양한 예외를 글로벌 예외 처리기로 관리합니다.  
아래는 `GlobalExceptionHandler`를 통해 처리하는 주요 예외 목록과 응답 예시입니다.

### 1️⃣ 잘못된 파라미터 예외 (`MethodArgumentNotValidException`)
- **설명**: 요청 데이터의 검증에 실패했을 경우 발생합니다.
- **HTTP 상태 코드**: `400 Bad Request`
- **응답 예시**
```json
{
  "resultCode": 400,
  "resultMsg": "잘못된 파라미터 입니다."
}
```
### 2️⃣ 잘못된 요청 예외 (`IllegalArgumentException`)
- **설명**: 잘못된 입력값이 들어온 경우 발생합니다.
- **HTTP 상태 코드**: `400 Bad Request`
- **응답 예시**
```json
{
  "resultCode": 400,
  "resultMsg": "잘못된 요청입니다."
}
```

### 3️⃣ 음수 금액 예외 (NegativeNumberException)
- **설명**: 송금 금액이 음수일 경우 발생합니다.
- **HTTP 상태 코드**: `400 Bad Request`
- **응답 예시**
```json
{
  "resultCode": 400,
  "resultMsg": "송금액은 음수가 될 수 없습니다."
}

```

### 4️⃣ 송금 한도 초과 예외 (LimitExcessException)
- **설명**: 개인회원(₩1,000) 또는 법인회원(₩5,000) 한도를 초과하는 경우 발생합니다.
- **HTTP 상태 코드**: `400 Bad Request`
- **응답 예시**
```json
{
  "resultCode": 400,
  "resultMsg": "오늘 송금 한도 초과 입니다."
}

```


### 5️⃣ 서버 내부 오류 (Exception)
- **설명**: 예상치 못한 예외가 발생할 경우 서버 내부 오류로 처리됩니다.
- **HTTP 상태 코드**: `500 Internal Server Error`
- **응답 예시**
```json
{
  "resultCode": 500,
  "resultMsg": "알 수 없는 에러 입니다."
}

```

### 6️⃣ 인증 실패 예외 (`AuthenticationException`)
- **설명**: 유효하지 않은 인증 정보(JWT 토큰 만료 또는 비정상적 접근)로 인해 접근이 차단됩니다.
- **HTTP 상태 코드**: `401 Unauthorized`
- **응답 예시**
```json
{
  "resultCode": 401,
  "resultMsg": "사용할 수 없는 토큰입니다."
}
```