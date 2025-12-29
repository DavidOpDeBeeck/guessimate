package app.dodb.guessimate.application;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    @Test
    void architectureShouldBeRespected() {
        JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("app.dodb.guessimate");

        Architectures.layeredArchitecture()
            .consideringAllDependencies()
            .layer("Api").definedBy("..api..")
            .layer("Application").definedBy("..application..")
            .layer("Domain").definedBy("..domain..")
            .layer("Driving Adapter").definedBy("..drivingadapter..")
            .layer("Driven Adapter").definedBy("..drivenadapter..")
            .layer("Port").definedBy("..port..")
            .layer("UseCase").definedBy("..usecase..")
            .whereLayer("Api").mayOnlyBeAccessedByLayers("Domain", "Driving Adapter", "Driven Adapter", "Port", "UseCase")
            .whereLayer("Application").mayNotBeAccessedByAnyLayer()
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Driven Adapter", "Port", "UseCase")
            .whereLayer("Driving Adapter").mayOnlyBeAccessedByLayers("Application", "Driven Adapter")
            .whereLayer("Driven Adapter").mayOnlyBeAccessedByLayers("Application")
            .whereLayer("Port").mayOnlyBeAccessedByLayers("Driven Adapter", "UseCase", "Domain")
            .whereLayer("UseCase").mayOnlyBeAccessedByLayers("Application")
            .check(importedClasses);
    }
}