plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.hdnguyen"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

tasks.bootJar {
	manifest {
		attributes(
			"Implementation-Version" to project.version
		)
	}
}

tasks.register("runJar", Exec::class.java) {
	dependsOn(tasks.bootJar) // Ensure the JAR is built before running it
	group = "application"
	description = "Runs the Spring Boot application using the packaged JAR."

	// Use project name and version to construct the JAR file path
	val jarFileName = "${project.name}-${project.version}.jar"
	val jarFilePath = "build/libs/$jarFileName"
	println("Jar file path: $jarFilePath")

	// Command to run the JAR
	commandLine("java", "-jar", jarFilePath)
}


repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")

	implementation("org.slf4j:slf4j-api:2.0.0") // SLF4J API
	implementation("ch.qos.logback:logback-classic:1.4.5") // Logback implementation

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
