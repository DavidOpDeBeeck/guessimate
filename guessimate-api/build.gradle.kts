plugins {
    alias(libs.plugins.spring.boot)
}

springBoot {
    mainClass.set("app.dodb.guessimate.application.GuessimateApplication")
}

dependencies {
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.micrometer.registry.prometheus)

    implementation(project(":guessimate-session:application"))
    implementation(project(":guessimate-lobby:application"))

    implementation(libs.smd.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)

    implementation(libs.micrometer.tracing.bridge.otel)
    implementation(libs.opentelemetry.exporter.otlp)

    acceptanceTestImplementation(project(":guessimate-session:api"))
    acceptanceTestImplementation(project(":guessimate-session:application"))
    acceptanceTestImplementation(libs.spring.boot.starter.data.jpa)
    acceptanceTestImplementation(libs.testcontainers.postgresql)
}
