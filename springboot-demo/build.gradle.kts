plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.1.0"
java {
    sourceCompatibility = JavaVersion.VERSION_23
    targetCompatibility = JavaVersion.VERSION_23
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // OpenAPI/Swagger Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    // Spring Boot Test (includes JUnit 5, Mockito, AssertJ, etc.)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

springBoot {
    mainClass.set("com.example.demo.DemoApplication")
}
