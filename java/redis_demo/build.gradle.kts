plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation ("org.springframework.boot:spring-boot-starter")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.data:spring-data-redis")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation ("io.lettuce:lettuce-core") // For Redis connection during tests
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation ("org.mockito:mockito-core")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
