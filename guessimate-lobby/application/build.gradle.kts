dependencies {
    implementation(project(":guessimate-session:api"))
    implementation(project(":guessimate-lobby:api"))
    implementation(libs.guava)
    implementation(libs.commons.collections4)
    implementation(libs.smd.spring.boot.starter)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.liquibase)
    implementation(libs.spring.boot.starter.websocket)
    implementation(libs.tomcat.embed.websocket)
    implementation(libs.micrometer.registry.prometheus)

    testFixturesImplementation(testFixtures(project(":guessimate-session:api")))
    testFixturesImplementation(testFixtures(project(":guessimate-lobby:api")))

    testImplementation(testFixtures(project(":guessimate-session:api")))
    testImplementation(testFixtures(project(":guessimate-lobby:api")))

    integrationTestImplementation(testFixtures(project(":guessimate-session:api")))
    integrationTestImplementation(testFixtures(project(":guessimate-lobby:api")))
    integrationTestImplementation(libs.testcontainers.postgresql)
    integrationTestImplementation(libs.liquibase.core)
    integrationTestRuntimeOnly(libs.postgresql)

    useCaseTestImplementation(testFixtures(project(":guessimate-session:api")))
    useCaseTestImplementation(testFixtures(project(":guessimate-lobby:api")))
    useCaseTestImplementation(testFixtures(project(":guessimate-lobby:application")))
}
