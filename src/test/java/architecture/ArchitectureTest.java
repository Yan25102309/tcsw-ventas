package architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    private final JavaClasses clases = new ClassFileImporter().importPackages("domain", "application", "adapter");

    @Test
    void elDominioNoDebeDependerDeAdaptadoresConcretosNiDeInfraestructura() {
        ArchRule regla = noClasses()
                .that().resideInAPackage("..domain..")
                .and().haveNameNotMatching(".*Test")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..adapter..", "javax.swing..", "java.awt..");
        regla.check(clases);
    }

    @Test
    void laCapaDeAplicacionNoDebeDependerDeAdaptadoresConcretos() {
        ArchRule regla = noClasses()
                .that().resideInAPackage("..application..")
                .and().haveNameNotMatching(".*Test")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..adapter..", "javax.swing..");
        regla.check(clases);
    }
}