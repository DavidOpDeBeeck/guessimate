dependencies {
    runtimeOnly(libs.postgresql)
    implementation(project(":guessimate-session:api"))
    implementation(libs.guava)
    implementation(libs.smd.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.liquibase)
    implementation(libs.spring.boot.starter.data.jpa)

    testFixturesImplementation(testFixtures(project(":guessimate-session:api")))

    testImplementation(testFixtures(project(":guessimate-session:api")))

    integrationTestImplementation(libs.testcontainers.postgresql)

    acceptanceTestImplementation(libs.testcontainers.postgresql)
}
