plugins {
    `java-library`
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.12.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.retry:spring-retry")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("io.swagger.core.v3:swagger-annotations:2.2.20")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    api(project(":service"))
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/rest/src/main/resources/openapi/openapi.yaml")
    outputDir.set("${buildDir}/generated")
    apiPackage.set("com.oliver.eshop.rest.api")
    modelPackage.set("com.oliver.eshop.rest.model")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "dateLibrary" to "java8",
            "useSpringBoot3" to "true"
        )
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("bootJar") {
    enabled = false
}
sourceSets["main"].java.srcDir("${buildDir}/generated/src/main/java")

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}
