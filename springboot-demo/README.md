# springboot-demo

簡單的 Spring Boot 範本，包含：

- Swagger (springdoc OpenAPI)
- 版本號設定
- 測試 API 範例 (/api/hello, /api/version)
- 伺服器狀態監控 (/api/server-status)

## 系統需求

- Java 17 或更高版本
- Gradle 8.5 (使用 Gradle Wrapper，無需預先安裝)

## 快速開始

1. 進入專案資料夾：

```bash
cd springboot-demo
```

2. 啟動應用程式：

```bash
./gradlew bootRun
```

或者先建置，然後執行 JAR：

```bash
./gradlew build
java -jar build/libs/springboot-demo-0.1.0.jar
```

3. 查看 Swagger UI：

http://localhost:8080/swagger-ui/index.html

## 執行測試

```bash
./gradlew test
```

## API 範例

- GET /api/hello -> { "message": "Hello from Spring Boot!" }
- GET /api/version -> { "version": "0.1.0" }
- GET /api/server-status -> 伺服器狀態和記憶體資訊
