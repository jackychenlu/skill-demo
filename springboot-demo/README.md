# springboot-demo

簡單的 Spring Boot 範本，包含：

- Swagger (springdoc OpenAPI)
- 版本號設定
- 測試 API 範例 (/api/hello, /api/version)

快速開始

1. 進入專案資料夾：

```bash
cd springboot-demo
```

2. 啟動應用程式：

```bash
.\\gradlew.bat bootRun
```

3. 查看 Swagger UI：

http://localhost:8080/swagger-ui/index.html

API 範例

- GET /api/hello -> { "message": "Hello from Spring Boot!" }
- GET /api/version -> { "version": "0.1.0" }
