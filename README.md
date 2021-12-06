# 목차
  * [Member](#member)
  * [과제 안내 내용](#과제-안내-내용)
  * [사용 기술 및 tools](#사용-기술-및-tools)
  * [모델링](#모델링)
  * [서버 구조 및 아키텍쳐](#서버-구조-및-아키텍쳐)
  * [상세 구현 기능 설명](#상세-구현-기능)
  * [서버 실행 방법](#서버-실행-방법)
  * [배포 정보](#배포-정보)
  * [API DOCS](#api-docs)


##  원티트X위코드 백엔드 프리온보딩 프로젝트 
해당 프로젝트는 원티드X위코드 프리온보딩 백엔드 코스에서 수행한 **카닥**의 기업 과제 입니다.

## Member
| 이름  | github                                   |
|-------|-----------------------------------------|
|박세원 |[sw-develop](https://github.com/sw-develop)| 


## 과제 안내 내용
<details>
<summary><b>과제내용 자세히 보기</b></summary>
<div markdown="1">

### **[필수 포함 사항]**
- READ.ME 작성
    - 프로젝트 빌드, 자세한 실행 방법 명시
    - 구현 방법과 이유에 대한 간략한 설명
    - **서버 구조 및 디자인 패턴에 대한 개략적인 설명**
    - 완료된 시스템이 배포된 서버의 주소
    - 해당 과제를 진행하면서 회고 내용 블로그 포스팅
- Swagger나 Postman을 이용하여 API 테스트 가능하도록 구현

### 1. 배경 및 공통 요구사항

<aside>
😁 **카닥에서 실제로 사용하는 프레임워크를 토대로 타이어 API를 설계 및 구현합니다.**

</aside>

- 데이터베이스 환경은 별도로 제공하지 않습니다.
  **RDB중 원하는 방식을 선택**하면 되며, sqlite3 같은 별도의 설치없이 이용 가능한 in-memory DB도 좋으며, 가능하다면 Docker로 준비하셔도 됩니다.
- 단, 결과 제출 시 README.md 파일에 실행 방법을 완벽히 서술하여 DB를 포함하여 전체적인 서버를 구동하는데 문제없도록 해야합니다.
- 데이터베이스 관련처리는 raw query가 아닌 **ORM을 이용하여 구현**합니다.
- Response Codes API를 성공적으로 호출할 경우 200번 코드를 반환하고, 그 외의 경우에는 아래의 코드로 반환합니다.

| Response Code  | Description                     |
|-------|------------------------------------------|
|200 OK	|성공
|400 Bad Request	|Parameter가 잘못된 (범위, 값 등)|
|401 Unauthorized	|인증을 위한 Header가 잘못됨|
|500 Internal Server Error	|기타 서버 에러|

---

### 2. 사용자 생성 API

🎁 **요구사항**

- ID/Password로 사용자를 생성하는 API.
- 인증 토큰을 발급하고 이후의 API는 인증된 사용자만 호출할 수 있다.

```jsx
/* Request Body 예제 */

 { "id": "candycandy", "password": "ASdfdsf3232@" }
```

---

### 3. 사용자가 소유한 타이어 정보를 저장하는 API

🎁 **요구사항**

- 자동차 차종 ID(trimID)를 이용하여 사용자가 소유한 자동차 정보를 저장한다.
- 한 번에 최대 5명까지의 사용자에 대한 요청을 받을 수 있도록 해야한다. 즉 사용자 정보와 trimId 5쌍을 요청데이터로 하여금 API를 호출할 수 있다는 의미이다.

```jsx
/* Request Body 예제 */
[
  {
    "id": "candycandy",
    "trimId": 5000
  },
  {
    "id": "mylovewolkswagen",
    "trimId": 9000
  },
  {
    "id": "bmwwow",
    "trimId": 11000
  },
  {
    "id": "dreamcar",
    "trimId": 15000
  }
]
```

🔍 **상세구현 가이드**

- 자동차 정보 조회 API의 사용은 아래와 같이 5000, 9000부분에 trimId를 넘겨서 조회할 수 있다.
  **자동차 정보 조회 API 사용 예제**

📄 [https://dev.mycar.cardoc.co.kr/v1/trim/5000](https://dev.mycar.cardoc.co.kr/v1/trim/5000)

📄 [https://dev.mycar.cardoc.co.kr/v1/trim/9000](https://dev.mycar.cardoc.co.kr/v1/trim/9000)

📄 [https://dev.mycar.cardoc.co.kr/v1/trim/11000](https://dev.mycar.cardoc.co.kr/v1/trim/11000)

📄 [https://dev.mycar.cardoc.co.kr/v1/trim/15000](https://dev.mycar.cardoc.co.kr/v1/trim/15000)


- 조회된 정보에서 타이어 정보는 spec → driving → frontTire/rearTire 에서 찾을 수 있다.
- 타이어 정보는 205/75R18의 포맷이 정상이다. 205는 타이어 폭을 의미하고 75R은 편평비, 그리고 마지막 18은 휠사이즈로써 {폭}/{편평비}R{18}과 같은 구조이다.
  위와 같은 형식의 데이터일 경우만 DB에 항목별로 나누어 서로다른 Column에 저장하도록 한다.


### 4. 사용자가 소유한 타이어 정보 조회 API

🎁 **요구사항**

- 사용자 ID를 통해서 2번 API에서 저장한 타이어 정보를 조회할 수 있어야 한다.

</div>
</details>


## 사용 기술 및 tools
- Back-End :  <img src="https://img.shields.io/badge/Java 11-007396?style=for-the-badge&logo=Java&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Spring Boot 2.5.7-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/MySQL 8-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>&nbsp;
- Deploy : <img src="https://img.shields.io/badge/AWS_EC2-232F3E?style=for-the-badge&logo=Amazon&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Docker-0052CC?style=for-the-badge&logo=Docker&logoColor=white"/>
- ETC :  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>&nbsp;


## 모델링
![image](https://user-images.githubusercontent.com/69254943/144006790-76db2dd4-723d-42fd-9171-4af04597532f.png)
- 구성 이유   
  중복된 값을 가진(폭, 편평비, 휠사이즈) 동일한 타이어 대해서는 1개의 객체만 생성 가능 합니다.   
  해당 모델링의 경우 추후 다양한 API 제공에 유리합니다. (ex. 가장 많이 소유한 타이어의 종류)
  

## 서버 구조 및 아키텍쳐
![image](https://user-images.githubusercontent.com/69254943/144007049-1de9d20a-8d81-4649-96a2-f4605087130c.png)


### 어플리케이션 내부 (디자인 패턴)
![image](https://user-images.githubusercontent.com/69254943/144007195-f62f3ab5-93b2-4f8c-bfb3-9198ad24ee7b.png)
→ Spring MVC 패턴과 JPA 사용을 위해 크게 Controller, Service, Repository로 나누어 코드를 작성하였습니다.   
→ UserController에서는 사용자 회원가입/로그인 과 사용자가 소유한 타이어 정보 조회를 처리하고, TireController에서는 사용자가 소유한 타이어 정보 저장을 처리합니다.


## 상세 구현 기능
<details>
<summary><b>사용자 생성 API</b></summary>
<div markdown="1">

**POST /users/signup (회원가입)**
**POST /users/login   (로그인)**

## 회원가입   
-  id와 password를 입력하여 회원가입을 할 수 있습니다.   
-  중복된 id는 사용할 수 없습니다.   

## 로그인 및 인증
- JWT 기반 인증 구현을 위해 로그인 시 토큰을 발급해주고 특정 API 요청 시 Header에 "X-AUTH-TOKEN" : "TOKEN값" 을 설정하여 인증을 수행하도록 하였습니다.   
- 인증과 권한 등 보안에 관한 기능을 제공하는 **Spring Security**를 사용하였습니다.   
-  WebSecurityConfigurerAdapter를 상속한 WebSecurityConfig 클래스를 생성하여 요청에 대한 사용 권한 체크 및 JWTFilter를 등록하였습니다.   
-  JWT   
   토큰 발행 및 검증 모듈 : io.jsonwebtoken.jjwt 라이브러리를 사용하였습니다.     
   JWTFilter를 구성하여 검증이 끝난 JWT 로부터 사용자 정보를 받아와 UsernamePasswordAuthenticationFilter로 전달하도록 하였습니다.   
   토큰에 저장한 정보 및 유효시간 : User의 name & role, 30분   
   
## Response Code
| 종류 | 상황 | 응답 코드 |
| --- | --- | --- |
| 회원가입 | 회원가입 성공 | 200 OK |
|  | 중복된 id | 400 Bad Request |
| 로그인 | 로그인 성공 | 200 OK |
|  | 존재하지 않는 id | 404 Not Found |
|  | 올바르지 않은 id, password | 400 Bad Request |

</div>
</details>

<details>
<summary><b>사용자가 소유한 타이어 정보 저장 API</b></summary>
<div markdown="1">

**POST /tires (사용자가 소유한 타이어 정보 저장)**

## 로직 구현을 위한 세부 조건
코드 작성 전 세부 조건을 명확히 정하는 것이 중요하다고 생각하여 관련 세부 조건을 자체적으로 정하였습니다.

- 해당 API 호출 가능한 사람    
  - 인증된 사용자만 (다른 사용자가 소유한 타이어 정보도 저장이 가능함)
  

- 최대 5개의 요청 데이터에 대해 1개라도 올바르지 않은 데이터가 있을 때의 처리    
  - 모든 요청 데이터가 올바른 경우에만(예외를 발생시키지 않는 경우) 정상 요청으로 판단   
  - 1개라도 올바르지 않은 데이터(예외를 발생시키는 경우)가 있는 경우 Exception 반환   


- 올바르지 않은 데이터(예외 발생)의 조건    
  - 요청 데이터의 개수가 1~5개가 아닌 경우 (400 Bad Request)   
  - 해당 id 값을 가진 사용자가 존재하지 않는 경우 (400 Not Found)      
  - 자동차 정보 조회 외부 API 응답 상태코드가 200이 아닌 경우 (400 Bad Request)      
  - 자동차 정보 조회 외부 API 응답 상태코드가 200이지만, 응답의 spec → driving → frontTire/rearTire 에서 타이어 정보를 찾을 수 없는 경우 (400 Bad Request)      


- 올바른 타이어 정보 포맷인지 확인 과정
  - 공백(`\t, \n, \x0B, \f, \r`) 제거
  - `{차량종류}{폭}/{편평비}R{휠사이즈}` 패턴에 맞는지 확인   
  - 올바른 포맷인 경우   
    frontTire, rearTire 중 올바른 포맷의 타이어만 저장   
    frontTire와 rearTire가 동일한 타이어 정보인 경우 Tire 객체 1개 생성, 사용자 소유 타이어 1개 생성   
  - 올바르지 않은 포맷인 경우   
    예외 처리는 되지 않고 사용자 소유 타이어 정보가 저장되지 않음   
    (외부 API의 응답값의 포맷이 달라서 발생한 경우이므로 예외라고 보지 않았음)


- 사용자 소유 타이어 정보 저장 순서    
  1. (사용자id, trimID)를 한 쌍으로 데이터 유효성 검사 수행   
   사용자 정보 존재 확인   
   trimID에 대한 외부 API 응답 코드 & 응답에서 타이어 정보 찾을 수 있는지 확인   
   올바른 타이어 포맷인 경우 타이어 정보를 데이터베이스로부터 가져오거나 새로 생성
  2. 1의 검사 통과했고, 해당 (사용자, 타이어) 정보가 데이터베이스에 아직 없는 경우에 UserTire 객체 생성하여 List<>에 저장해둠   
  3. 모든 요청 값에 대해 1,2 과정 수행 후 List<>에 있는 UserTire 객체를 데이터베이스에 저장
     

*위의 해당 저장 순서 선택 이유   
올바르지 않은 데이터가 존재해 중간에 예외가 발생한 경우 데이터베이스 rollback을 수행하지 않아도 됨   
새로운 타이어 정보는 데이터베이스에 무조건 저장됨(타이어 정보는 요청 시 마다 새로운 값일 경우 데이터베이스에 새로 생성해둬도 문제가 없음)   

## 기능 구현 자세한 설명
- 자동차 정보 조회 외부 API 호출   
  - RestTemplate 사용하여 HTTP Client로 Cardoc의 REST API를 호출하였습니다.
  - RestTemplate Bean 등록을 위해 RestTemplateConfig 클래스를 구성하였습니다.
  - Connection Pooling을 사용하여 커넥션을 재사용하고 제한하기 위해 apache에서 제공하는 HttpClient를 사용하였습니다.


- 외부 API에 대한 응답 String to Json 
  - `com.googlecode.json-simple` 라이브러로 RestTemplate으로 호출 시 반환된 응답의 body를 Parsing하고 JSONObject로 변환시켰습니다.
  - 찾고자 하는 타이어 정보는 spec → driving → frontTire/rearTire 이므로, 데이터의 key로 해당 값을 접근하기 위해 JSONObject로 변환하여 쉽게 접근하도록 하였습니다.


- 올바른 타이어 정보 포맷인지 확인 
  - 올바른 데이터 형식을 `{차량종류}{폭}/{편평비}R{휠사이즈}` 로 정해두고, 해당 형식에 해당하는 `[a-zA-Z]?[0-9]+/[0-9]+R[0-9]+` 정규식 표현과 주어진 데이터 값이 매칭되는지 비교하였습니다.  

## Response Code
| 상황 | 응답 코드 |
| --- | --- |
| 사용자가 소유한 타이어 정보 저장 성공 | 200 OK |
| 요청 데이터 개수가 0개 or 5개 초과 | 400 Bad Request |
| 자동차 정보 조회 외부 API 응답 상태코드 200이 아님 | 400 Bad Request |
| 자동차 정보 조회 외부 API 응답 상태코드가 200이지만, 응답의 spec → driving → frontTire/rearTire 에서 타이어 정보를 찾을 수 없음 | 400 Bad Request |
| Header에 Token이 없음 | 401 Unauthorized |

</div>
</details>

<details>
<summary><b>사용자가 소유한 타이어 정보 조회 API</b></summary>
<div markdown="1">

**GET /{userName}/tires**

## 기능 구현 자세한 설명
JpaRepository<T, ID> 인터페이스를 구현한 UserRepository와 UserTireRepository 를 사용해 PathVariable로 들어온 userName에 해당하는 사용자가 존재하는지 확인 후 해당 사용자가 소유한 타이어 정보를 찾아 반환하도록 하였습니다.

## Response Code
| 상황 | 응답 코드 |
| --- | --- |
| 사용자가 소유한 타이어 정보 조회 성공 | 200 OK |
| 해당 사용자가 존재하지 않음 | 404 Not Found |
| Header에 Token이 없음 | 401 Unauthorized |

</div>
</details>

<details>
<summary><b>Exception Handling</b></summary>
<div markdown="1">

- `@RestControllerAdvice`와 `@ExceptionHandler` 을 사용해 가장 많이 사용하는 BadRequest와 ResourceNotFound에 대한 Custom 예외 처리 구성하였습니다.
- DefaultResponse 클래스로 응답 기본 형태를 구성해두고, 응답시 사용할 Response Code와 Response Message를 구체적으로 작성해두었습니다.   
```java
// Response Message 
public static final String INTERNAL_SERVER_ERROR    = "서버 내부 에러";

public static final String SUCCESS_USER_SIGNUP      = "유저 회원 가입 성공";
public static final String SUCCESS_USER_LOGIN       = "유저 로그인 성공";

public static final String FAIL_USER_SIGNUP_DUPLICATE_USER_ID = "중복된 ID 입니다";
public static final String FAIL_USER_LOGIN_WRONG_PASSWORD = "잘못된 비밀번호 입니다.";

public static final String SUCCESS_SAVE_TIRE_INFO   = "사용자가 소유한 타이어 정보 저장 성공";

public static final String SUCCESS_GET_TIRE_LIST    = "사용자가 소유한 타이어 정보 조회 성공";

public static final String FAIL_USERTIRE_NOT_VALID_NUMBER_OF_DATA_REQUESTS = "요청 값의 개수는 1개 이상 5개 이하여야 합니다.";
public static final String FAIL_USERTIRE_CANNOT_FIND_CAR_INFO = " 해당 자동차 정보를 조회할 수 없습니다";
public static final String FAIL_USERTIRE_CANNOT_FIND_TIRE_INFO = "해당 타이어 정보를 조회할 수 없습니다";
```
→ 통일된 형식으로 구체적인 응답메시지와 함께 응답 확인이 가능합니다.   

</div>
</details>


## 서버 실행 방법
### 로컬개발/배포 환경 분리를 위한 파일 구성
- application.yml   
  src/main/resources/application.yml : 공통 설정   
  src/main/resources/application-dev.yml : 로컬 개발용 설정   
  src/main/resources/application-real.yml : 배포용 설정   


- docker-compose.yml   
  docker-compose-dev.yml : 로컬 개발용 (application, database 포함)   
  docker-compose-real.yml : 배포용 (application, database, nginx 포함)   
  
  
### Requirements
Docker, Docker Compose   


### 실행 방법
<details>
<summary><b>로컬 개발 및 테스트용</b></summary>
<div markdown="1">

1. 해당 프로젝트 git clone 후 프로젝트 폴더로 이동

    ```bash
    $ git clone https://github.com/Wanted-Preonboarding-Backend-1st-G5/Assignment7-SW.git
    $ cd Assignment7-SW
    ```
2. 어플리케이션, MySQL 컨테이너 생성 및 실행

    ```bash
    $ docker-compose -f docker-compose-dev.yml up -d
    ```

   → -d : 백그라운드 실행   
   → Dockerfile에서 `gradle build` 를 수행하므로 cardoc_api container 생성 시 시간이 소요될 수 있습니다.   

</div>
</details>

<details>
<summary><b>배포용</b></summary>
<div markdown="1">

1. 해당 프로젝트 git clone 후 프로젝트 폴더로 이동

    ```bash
    $ git clone https://github.com/Wanted-Preonboarding-Backend-1st-G5/Assignment7-SW.git
    $ cd Assignment7-SW
    ```
2. 어플리케이션, MySQL, Nginx 컨테이너 생성 및 실행

    ```bash
    $ docker-compose -f docker-compose-real.yml up -d
    ```

   → Dockerfile에서 `gradle build` 를 수행하므로 cardoc_api container 생성 시 시간이 소요될 수 있습니다.   

</div>
</details>


## 배포 정보
| 배포 플랫폼 | AWS EC2 |
| --- | --- |
| 배포 주소 | http://ec2-13-209-88-119.ap-northeast-2.compute.amazonaws.com/ |


## API DOCS
### [Postman Docs](https://documenter.getpostman.com/view/12950398/UVJhEFaS)
### [API 상세 문서](https://github.com/Wanted-Preonboarding-Backend-1st-G5/Assignment7-SW/wiki/API-문서)
