dependencies {
    runtimeOnly("org.postgresql:postgresql")
    implementation(project(":guessimate-session:api"))
    implementation("com.google.guava:guava")
    implementation("app.dodb:smd-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testFixturesImplementation(testFixtures(project(":guessimate-session:api")))

    testImplementation(testFixtures(project(":guessimate-session:api")))

    integrationTestImplementation("org.testcontainers:postgresql")

    acceptanceTestImplementation("org.testcontainers:postgresql")
}