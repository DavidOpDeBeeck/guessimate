dependencies {
    implementation("app.dodb:smd-api")
    implementation(project(":guessimate-session:api"))

    testFixturesImplementation(project(":guessimate-session:api"))
    testFixturesImplementation(testFixtures(project(":guessimate-session:api")))
}