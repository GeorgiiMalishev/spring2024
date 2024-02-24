package my.spring2024;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;


@AnalyzeClasses(packages = "my.spring2024")
public class ArchUnitTest {
    private static final String PACKAGE = "my.spring2024";
    private static final JavaClasses CLASSES = new ClassFileImporter().importPackages(PACKAGE);

    @Test void testLayeredArchitecture(){
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain").definedBy("..domain..")
                .layer("app").definedBy("..app..")
                .layer("extern").definedBy("..api..", "..infrastructure..")
                .whereLayer("app").mayOnlyBeAccessedByLayers("app", "extern")
                .whereLayer("extern").mayOnlyBeAccessedByLayers("extern")
                .check(CLASSES);
    }

    @Test
    void testApiLayerDoesNotContainRepository() {
        noClasses().that().resideInAPackage("..api..")
                .should().accessClassesThat().resideInAPackage("..infrastructure..")
                .check(CLASSES);
    }
}
