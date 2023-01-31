# Grocery API
## 프로젝트에 사용된 기술 스택 및 버전 정보
- Gradle 7.6
- Java 17.0.6 
- Spring Boot 3.0.2


## 프로젝트에 사용된 의존성 
- spring boot starter web
  - WebClient 동기 호출을 사용하기 위해 적용하였습니다. 
- spring boot starter webflux
  - WebClient를 사용하기 위해 적용하였습니다.
- netty-resolver-dns-native-macos
  - M1 chip을 사용하는 Mac OS 상에서 netty와 충돌이 있습니다. 요청 시마다 지속적으로 예외가 발생하여 적용하였습니다.
- spring boot starter thymeleaf
  - 사용자에게 결과 화면을 보여주기 위해 적용하였습니다.
- spring boot starter test
  - 테스트 코드 작성을 위해 적용하였습니다.
- lombok
  - `@Slf4j`, `@RequiredArgsConstructor` 등 편의 애노테이션을 사용하기 위해 적용하였습니다.
- okhttp3 mock web server
  - WebClient 테스트 시, WebServer Mocking을 위해 적용하였습니다.


## 프로젝트 전체 구조 
```text
.
└── groceryapi
    ├── GroceryapiApplication.java
    ├── config
    │   └── GroceryWebClientConfig.java
    ├── exception
    │   └── presentation
    │       ├── BusinessExceptionController.java
    │       ├── WebClientExceptionController.java
    │       └── dto
    │           └── ExceptionResult.java
    ├── home
    │   └── presentation
    │       └── HomeController.java
    └── product
        ├── application
        │   ├── ProductService.java
        │   └── dto
        │       ├── ProductPriceResponse.java
        │       └── ProductsResponse.java
        ├── domain
        │   ├── AccessToken.java
        │   ├── GroceryWebClientMapper.java
        │   ├── Product.java
        │   ├── ProductType.java
        │   ├── Products.java
        │   └── web_client
        │       ├── FruitWebClient.java
        │       ├── GroceryWebClient.java
        │       └── VegetableWebClient.java
        └── presentation
            ├── ProductController.java
            └── dto
                ├── ProductPriceRequest.java
                └── ProductsSearchRequest.java
``` 
### 대분류 
- `config`
  - `@Configuration`, `@Bean` 애노테이션을 통해 빈을 직접 등록하는 패키지 입니다.
- `exception`
  - 발생하는 예외에 대해 처리하는 패키지 입니다.
- `home`
  - 홈 화면을 위한 패키지 입니다.
- `product`
  - 청과물 조회를 위한 주 로직이 들어있는 패키지 입니다.

### 소분류
역할에 따라 아래의 3계층으로 나누었습니다. 
- `presentation`
  - `@Controller`가 위치하며, View에 데이터를 전달 해주거나, 전달 받는 역할을 합니다.
- `application`
  - `@Service`가 위치하며, `presentation` 계층에서 받은 데이터를 `domain`에 전달 해주거나, `domain`에서 전달 받은 데이터를 `presentation`에 전달 해주는 역할을 합니다. 
- `domain`
  - 주 비즈니스 로직이 위치하며, `application` 계층에 데이터를 전달 하며, 외부 API 서버와의 통신을 하기도 합니다.


## 특이 사항
### 홈 화면
- http://localhost:8080

### profile
- `application.yml` 파일에는 gitHub에 올라가지 않은 `dev` profile이 설정 되어있습니다.
  - 이는 `test/resources`에도 적용 되어있습니다.
  - 아래의 내용을 설정 해주신 후, profile을 `real`로 설정해 주세요.
- `application-real.yml`에 `dev` profile과 같은 스타일의 `property`가 적혀있으며, 아래와 같이 표기 해두었습니다.
  - "과일 가게 URI"
  - "채소 가게 URI"



