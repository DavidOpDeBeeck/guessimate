dependencies {
    implementation(project(":guessimate-session:api"))
    implementation(project(":guessimate-lobby:api"))
    implementation("com.google.guava:guava")
    implementation("org.apache.commons:commons-collections4")
    implementation("app.dodb:smd-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.apache.tomcat.embed:tomcat-embed-websocket")
    implementation("io.micrometer:micrometer-registry-prometheus")

    testFixturesImplementation(testFixtures(project(":guessimate-session:api")))
    testFixturesImplementation(testFixtures(project(":guessimate-lobby:api")))

    testImplementation(testFixtures(project(":guessimate-session:api")))
    testImplementation(testFixtures(project(":guessimate-lobby:api")))

    integrationTestImplementation(testFixtures(project(":guessimate-session:api")))
    integrationTestImplementation(testFixtures(project(":guessimate-lobby:api")))
    integrationTestImplementation("org.testcontainers:postgresql")
    integrationTestImplementation("org.liquibase:liquibase-core")
    integrationTestRuntimeOnly("org.postgresql:postgresql")

    useCaseTestImplementation(testFixtures(project(":guessimate-session:api")))
    useCaseTestImplementation(testFixtures(project(":guessimate-lobby:api")))
    useCaseTestImplementation(testFixtures(project(":guessimate-lobby:application")))
}