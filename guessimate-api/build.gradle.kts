plugins {
    id("org.springframework.boot") version "3.4.12"
}

springBoot {
    mainClass.set("app.dodb.guessimate.application.GuessimateApplication")
}

dependencies {
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    implementation(project(":guessimate-session:application"))
    implementation(project(":guessimate-lobby:application"))

    implementation("app.dodb:smd-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
}