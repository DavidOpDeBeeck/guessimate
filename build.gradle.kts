import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    `java-library`
}

group = "app.dodb"
version = "1.0"

subprojects {
    val libs = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")

    java.sourceCompatibility = JavaVersion.VERSION_25

    // https://github.com/gradle/gradle/issues/847
    group = project.path.substring(1).replace(':', '-')
    version = rootProject.version

    tasks.jar {
        archiveBaseName.set("${project.group}")
        archiveClassifier.set("plain")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-parameters")
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        implementation(platform(libs.findLibrary("spring-boot-dependencies").get()))
        implementation(libs.findLibrary("jakarta-inject-api").get())

        testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())
        testImplementation(libs.findLibrary("smd-spring-boot-starter-test").get())
        testImplementation(libs.findLibrary("spring-boot-starter-test").get())
        testImplementation(platform(libs.findLibrary("junit-bom").get()))
        testImplementation(libs.findLibrary("junit-jupiter").get())
        testImplementation(libs.findLibrary("archunit-junit5").get())
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    val integrationTest by sourceSets.creating {
        java.srcDirs(file("src/integrationTest/java"))
        resources.setSrcDirs(listOf("src/integrationTest/resources"))
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += output + compileClasspath
    }

    val acceptanceTest by sourceSets.creating {
        java.srcDirs(file("src/acceptanceTest/java"))
        resources.setSrcDirs(listOf("src/acceptanceTest/resources"))
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += output + compileClasspath
    }

    val useCaseTest by sourceSets.creating {
        java.srcDirs(file("src/useCaseTest/java"))
        resources.setSrcDirs(listOf("src/useCaseTest/resources"))
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += output + compileClasspath
    }

    configurations[integrationTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
    configurations[integrationTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

    configurations[acceptanceTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
    configurations[acceptanceTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

    configurations[useCaseTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
    configurations[useCaseTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

    var integrationTestTask = tasks.register<Test>("integrationTest") {
        description = "Runs integration tests"
        group = "verification"
        testClassesDirs = integrationTest.output.classesDirs
        classpath = integrationTest.runtimeClasspath
        shouldRunAfter(tasks.test)
    }

    var acceptanceTestTask = tasks.register<Test>("acceptanceTest") {
        description = "Runs acceptance tests"
        group = "verification"
        testClassesDirs = acceptanceTest.output.classesDirs
        classpath = acceptanceTest.runtimeClasspath
        shouldRunAfter(tasks.test)
    }

    var useCaseTestTask = tasks.register<Test>("useCaseTest") {
        description = "Runs use case tests"
        group = "verification"
        testClassesDirs = useCaseTest.output.classesDirs
        classpath = useCaseTest.runtimeClasspath
        shouldRunAfter(tasks.test)
    }

    tasks.named("check") {
        dependsOn(integrationTestTask)
        dependsOn(acceptanceTestTask)
        dependsOn(useCaseTestTask)
    }

    configurations {
        named("acceptanceTestRuntimeClasspath") {
            exclude(
                group = project.group.toString(),
                module = project.name
            )
        }
        named("integrationTestRuntimeClasspath") {
            exclude(
                group = project.group.toString(),
                module = project.name
            )
        }
        named("useCaseTestRuntimeClasspath") {
            exclude(
                group = project.group.toString(),
                module = project.name
            )
        }
    }
}
