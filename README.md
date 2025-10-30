# 웹 프로그래밍 기초 실습 4: Spring MVC TODO 서비스 (상세 분석)

이 프로젝트는 **Spring MVC** 프레임워크와 **MyBatis**를 사용하여 **TODO(할 일 목록) 웹 애플리케이션**의 **CRUD(Create, Read, Update, Delete)** 기능을 구현하는 연습 예제입니다.

\*\*계층형 아키텍처(Controller-Service-Repository)\*\*를 기반으로 구축되었으며, Spring MVC의 핵심 기능(DI, 요청 처리, 데이터 바인딩, 예외 처리, Formatter 등)과 MyBatis를 이용한 영속성(Persistence) 처리, 그리고 계층 간의 효과적인 데이터 전송(DTO/VO 분리, ModelMapper) 방식을 심도 있게 학습하는 것을 목표로 합니다.

## 🌟 주요 기능 상세

이 애플리케이션은 다음과 같은 핵심 기능을 제공합니다.

1.  **TODO 등록 (Create)**:
      * **요청 경로**: `GET /todo/register` (폼 요청), `POST /todo/register` (등록 처리)
      * **기능**: 사용자가 할 일의 제목, 마감일, 작성자를 입력하여 새로운 TODO 항목을 생성합니다.
      * **처리 흐름**: Controller가 `TodoDTO`로 데이터를 받아 Service에 전달 -\> Service가 DTO를 VO로 변환하여 DAO/Mapper 호출 -\> Mapper가 DB에 INSERT 수행.
2.  **TODO 목록 조회 (Read - List)**:
      * **요청 경로**: `GET /todo/list`
      * **기능**: 데이터베이스에 저장된 모든 TODO 항목들을 조회하여 목록 형태로 보여줍니다.
      * **처리 흐름**: Controller가 Service 호출 -\> Service가 DAO/Mapper 호출하여 `List<TodoVO>` 조회 -\> Service가 `List<TodoDTO>`로 변환하여 Controller 반환 -\> Controller가 Model에 담아 View 전달.
3.  **TODO 상세 조회 (Read - Detail)**:
      * **요청 경로**: `GET /todo/read` (파라미터: `tno`)
      * **기능**: 목록에서 특정 항목을 선택했을 때, 해당 항목의 고유 번호(`tno`)를 이용하여 상세 정보를 보여줍니다.
      * **처리 흐름**: Controller가 `tno` 파라미터를 받아 Service 호출 -\> Service가 DAO/Mapper 호출하여 `TodoVO` 조회 -\> Service가 `TodoDTO`로 변환하여 Controller 반환 -\> Controller가 Model에 담아 View 전달.
4.  **TODO 수정 (Update)**:
      * **요청 경로**: `GET /todo/modify` (수정 폼 요청, 파라미터: `tno`), `POST /todo/modify` (수정 처리)
      * **기능**: 기존 TODO 항목의 제목, 마감일, 완료 여부를 수정합니다.
      * **처리 흐름 (POST)**: Controller가 `TodoDTO`로 데이터를 받아 Service 호출 -\> Service가 DTO를 VO로 변환하여 DAO/Mapper 호출 -\> Mapper가 DB UPDATE 수행.
5.  **TODO 삭제 (Delete)**:
      * **요청 경로**: `POST /todo/remove` (파라미터: `tno`)
      * **기능**: 특정 `tno`에 해당하는 TODO 항목을 데이터베이스에서 삭제합니다.
      * **처리 흐름**: Controller가 `tno` 파라미터를 받아 Service 호출 -\> Service가 DAO/Mapper 호출 -\> Mapper가 DB DELETE 수행.

## 🛠️ 기술 스택 및 라이브러리

  * **Java**: 11 (build.gradle 기준)
  * **Spring Framework**: 5.3.x 버전 (MVC, Test 등)
  * **Servlet API**: 4.0.1 (Tomcat 9 기준)
  * **JSP API**: 2.3.3
  * **JSTL**: 1.2
  * **MyBatis**: 3.5.x, `mybatis-spring` 2.0.x
  * **Database Driver**: MySQL Connector/J 8.0.x
  * **Connection Pool**: HikariCP 5.0.x
  * **Lombok**: 1.18.x (코드 간결화)
  * **ModelMapper**: 3.0.x (객체 매핑)
  * **Logging**: Log4j2 (SLF4j 바인딩 포함)
  * **Testing**: JUnit 5, Spring Test
  * **Build Tool**: Gradle
  * **WAS**: Apache Tomcat (추정)

## 🏗️ 아키텍처 및 설계

### 1\. 계층형 구조 (3-Tier Architecture)

  * **Presentation Layer (Controller)**: `com.ssg.todoservice.controller` 패키지.
      * HTTP 요청 수신, 요청 파라미터 처리 및 유효성 검증 (Formatter 활용).
      * Service 계층 호출하여 비즈니스 로직 위임.
      * Service로부터 받은 결과(DTO)를 Model 객체에 담아 View(JSP)로 전달하거나 리다이렉트 처리.
      * 전역 예외 처리(`CommonExceptionAdvice`).
  * **Business Logic Layer (Service)**: `com.ssg.todoservice.service` 패키지.
      * `TodoService` 인터페이스와 `TodoServiceImpl` 구현체로 구성.
      * 핵심 비즈니스 로직(예: TODO 등록, 수정 시 유효성 검사 - 현재는 미구현) 수행.
      * Controller로부터 DTO를 받아 필요시 VO로 변환하여 Repository/DAO 호출.
      * Repository/DAO로부터 VO를 받아 필요시 DTO로 변환하여 Controller에 반환.
      * 트랜잭션 관리 (현재 프로젝트에서는 명시적인 트랜잭션 설정은 없으나, 필요시 `@Transactional` 추가 가능).
      * ModelMapper를 사용하여 DTO \<-\> VO 변환 로직 처리.
  * **Data Access Layer (Repository/DAO & Mapper)**: `com.ssg.todoservice.repository`, `com.ssg.todoservice.mapper` 패키지.
      * `TodoDAO` 인터페이스와 `TodoDAOImpl` 구현체: 데이터베이스 접근 로직 추상화. `TodoMapper`를 호출하여 실제 SQL 실행 위임.
      * `TodoMapper` 인터페이스: MyBatis가 구현체를 동적으로 생성. 각 메서드는 `TodoMapper.xml`의 SQL ID와 매핑됨.
      * `TodoMapper.xml`: 실제 실행될 SQL 쿼리(INSERT, SELECT, UPDATE, DELETE) 정의. 파라미터 타입(`TodoVO`)과 결과 타입(`TodoVO` 또는 `List<TodoVO>`) 명시.

### 2\. Spring MVC 설정 (`web.xml`, `servlet-context.xml`, `root-context.xml`)

  * **`web.xml` (배포 서술자)**:
      * 애플리케이션 시작 시 `ContextLoaderListener`를 통해 `root-context.xml` 로딩 (Service, DAO, DB 설정 등).
      * `DispatcherServlet` (Spring MVC의 프론트 컨트롤러) 등록 및 URL 매핑 (`/`).
      * `DispatcherServlet`이 사용할 설정 파일로 `servlet-context.xml` 지정.
      * 요청 파라미터 인코딩 처리를 위한 `CharacterEncodingFilter` 설정.
  * **`root-context.xml` (애플리케이션 컨텍스트)**:
      * `component-scan`: Service(`@Service`), Repository(`@Repository`), Config(`@Configuration`) 빈 탐색 및 등록 (Controller 제외).
      * `hikariConfig`, `dataSource`: HikariCP를 사용한 데이터베이스 커넥션 풀 설정.
      * `sqlSessionFactory`: MyBatis 핵심 객체 설정. `dataSource` 참조, Mapper XML 파일 위치 지정.
      * `mapper-locations`: `com.ssg.todoservice.mapper` 패키지의 Mapper 인터페이스 스캔 설정.
      * `modelMapper`: ModelMapper 빈 등록 (`ModelMapperConfig` 클래스 참조).
  * **`servlet-context.xml` (웹 컨텍스트)**:
      * `<mvc:annotation-driven>`: `@Controller`, `@RequestMapping` 등 어노테이션 기반 MVC 기능 활성화 및 Formatter 등록.
      * `component-scan`: Controller(`@Controller`), ControllerAdvice(`@ControllerAdvice`) 빈 탐색 및 등록.
      * `InternalResourceViewResolver`: View 이름을 실제 JSP 경로로 변환 (`prefix="/WEB-INF/views/"`, `suffix=".jsp"`).
      * `<mvc:resources>`: `/resources/**` 경로 요청을 `/webapp/resources/` 디렉토리의 정적 자원으로 매핑.

### 3\. 데이터 흐름

1.  **사용자 요청**: 브라우저에서 특정 URL 요청 (예: `/todo/list`).
2.  **`DispatcherServlet` 수신**: `web.xml` 설정에 따라 모든 요청을 받음.
3.  **`HandlerMapping`**: 요청 URL에 맞는 `TodoController`의 `list()` 메서드를 찾아 매핑.
4.  **`Controller` 실행**: `list()` 메서드 실행. `TodoService` 인터페이스의 `getAll()` 메서드 호출.
5.  **`Service` 실행**: `TodoServiceImpl`의 `getAll()` 실행. `TodoDAO` 인터페이스의 `selectAll()` 호출.
6.  **`DAO` 실행**: `TodoDAOImpl`의 `selectAll()` 실행. `TodoMapper` 인터페이스의 `selectAll()` 호출.
7.  **`MyBatis` 실행**: `TodoMapper` 인터페이스와 연결된 `TodoMapper.xml`의 `selectAll` SQL 실행. DB에서 `List<TodoVO>` 조회.
8.  **결과 반환 (역순)**: `List<TodoVO>`가 DAO -\> Service로 반환됨.
9.  **데이터 변환 (Service)**: Service에서 `List<TodoVO>`를 `List<TodoDTO>`로 변환 (ModelMapper 사용).
10. **결과 반환 (Service -\> Controller)**: `List<TodoDTO>`가 Controller로 반환됨.
11. **Model 저장 및 View 이름 반환**: Controller는 `List<TodoDTO>`를 `Model` 객체에 "dtoList"라는 이름으로 저장하고, 뷰 이름 "todo/list"를 `DispatcherServlet`에 반환.
12. **`ViewResolver`**: 뷰 이름 "todo/list"를 실제 JSP 경로 `/WEB-INF/views/todo/list.jsp`로 변환.
13. **View 렌더링**: `list.jsp` 실행. Model에 담긴 "dtoList" 데이터를 JSTL을 사용하여 HTML로 렌더링.
14. **`DispatcherServlet` 응답**: 생성된 HTML 응답을 브라우저로 전송.
15. **브라우저 표시**: 사용자는 최종 HTML 화면(TODO 목록)을 보게 됨.

### 4\. 주요 어노테이션

  * `@Controller`: 해당 클래스가 웹 요청을 처리하는 컨트롤러임을 명시.
  * `@Service`: 해당 클래스가 비즈니스 로직을 처리하는 서비스 계층의 컴포넌트임을 명시.
  * `@Repository`: 해당 클래스가 데이터 접근 계층(DAO)의 컴포넌트임을 명시 (DB 예외 변환 기능 포함).
  * `@Component`: 일반적인 Spring 관리 빈임을 명시 (Config 등).
  * `@Autowired`: 의존성 자동 주입 (타입 기반).
  * `@RequiredArgsConstructor` (Lombok): `final` 또는 `@NonNull` 필드에 대한 생성자를 자동으로 만들고, 이를 통해 의존성 주입 (생성자 주입 방식).
  * `@RequestMapping`, `@GetMapping`, `@PostMapping`: 특정 URL 요청과 Controller 메서드를 매핑.
  * `@RequestParam`: 요청 파라미터를 메서드 파라미터에 바인딩.
  * `@Log4j2` (Lombok): Log4j2 로거 객체 자동 생성.
  * `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` (Lombok): DTO/VO 클래스의 boilerplate 코드(getter, setter, 생성자 등) 자동 생성.
  * `@ControllerAdvice`: 전역 예외 처리 등 Controller 공통 로직을 담는 클래스임을 명시.
  * `@ExceptionHandler`: 특정 예외 발생 시 실행될 메서드 지정.
  * `@Configuration`: 해당 클래스가 Spring 설정 정보를 담고 있음을 명시.
  * `@Bean`: 메서드가 반환하는 객체를 Spring 빈으로 등록.

## 🧪 테스트 (`src/test/java/TodoTests.java`)

JUnit 5와 Spring Test Context Framework를 사용하여 Service, DAO, Mapper 계층의 주요 기능들을 단위 테스트합니다.

  * `@ExtendWith(SpringExtension.class)`: JUnit 5에서 Spring 테스트 기능을 사용하도록 설정.
  * `@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/root-context.xml")`: 테스트에 사용할 Spring 설정 파일(`root-context.xml`) 로딩. 이를 통해 테스트 코드 내에서 `@Autowired`로 Service, DAO, Mapper 빈을 주입받아 사용 가능.
  * 각 테스트 메서드(`@Test`)는 특정 기능(등록, 조회, 수정, 삭제 등)을 실행하고 결과를 검증합니다 (`Assertions.assertNotNull`, `Assertions.assertEquals` 등).

## 🚀 실행 전 준비 및 실행 방법

1.  **데이터베이스 설정**:
      * 사용할 데이터베이스(예: MySQL)를 준비합니다.
      * `src/main/webapp/WEB-INF/spring/root-context.xml` 파일의 `hikariConfig` 빈 설정을 자신의 DB 환경에 맞게 수정합니다 (JDBC URL, 사용자 이름, 비밀번호).
2.  **테이블 생성**:
      * 아래 SQL을 실행하여 `tbl_todo` 테이블을 생성합니다. (기본 구조는 `TodoVO` 클래스 및 `TodoMapper.xml` 참고)

        ```sql
        CREATE TABLE todo (
            tno INT AUTO_INCREMENT PRIMARY KEY COMMENT 'TODO 번호',
            title VARCHAR(100) NOT NULL COMMENT '제목',
            dueDate DATE NOT NULL COMMENT '마감일',
            writer VARCHAR(50) NOT NULL COMMENT '작성자',
            finished BOOLEAN DEFAULT FALSE COMMENT '완료 여부'
        );
        ```
3.  **빌드**:
      * 프로젝트 루트 디렉토리에서 Gradle Wrapper를 사용하여 빌드합니다.
        ```bash
        ./gradlew build
        ```
      * 빌드가 성공하면 `build/libs/` 디렉토리에 `.war` 파일(예: `ROOT.war` 또는 `web_making_basic_practice4-1.0-SNAPSHOT.war`)이 생성됩니다.
4.  **배포 및 실행**:
      * 생성된 `.war` 파일을 Apache Tomcat 등 WAS(Web Application Server)의 `webapps` 디렉토리에 복사합니다.
      * Tomcat 서버를 시작합니다.
      * 웹 브라우저에서 `http://<서버 주소>:<포트 번호>/todo/list` (또는 배포된 컨텍스트 경로에 맞게) 로 접속하여 애플리케이션을 확인합니다.

-----
