import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.magofrays.logicalformulas.Main;
import org.magofrays.logicalformulas.algorithm.SimpleAlgorithm;

import org.magofrays.logicalformulas.axiom.DefaultAxiomBuilder;
import org.magofrays.logicalformulas.axiom.TestAxiomBuilder;
import org.magofrays.logicalformulas.types.Axiom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@SpringBootTest(classes = Main.class)
public class SimpleAlgorithmTest {
    @Autowired
    private SimpleAlgorithm simpleAlgorithm;

    @Autowired
    private DefaultAxiomBuilder defaultAxiomBuilder;

    private List<Axiom> proveAxioms = new TestAxiomBuilder().buildAxioms();


    @Test
    public void testAxiomsA4() {
        var axiom = proveAxioms.get(0);
        log.info("\nДоказываем аксиому {}: {}", axiom.getName(), axiom.getFormula());
        simpleAlgorithm.prove(axiom.getFormula(), defaultAxiomBuilder.buildAxioms());
    }

    @Test
    public void testAxiomsA5(){
        var axiom = proveAxioms.get(1);
        log.info("\nДоказываем аксиому {}: {}", axiom.getName(), axiom.getFormula());
        simpleAlgorithm.prove(axiom.getFormula(), defaultAxiomBuilder.buildAxioms());
    }

    @Test
    public void testAxiomsA6(){
        var axiom = proveAxioms.get(2);
        log.info("\nДоказываем аксиому {}: {}", axiom.getName(), axiom.getFormula());
        simpleAlgorithm.prove(axiom.getFormula(), defaultAxiomBuilder.buildAxioms());
    }

    @Test
    public void testAxiomsA7(){
        var axiom = proveAxioms.get(3);
        log.info("\nДоказываем аксиому {}: {}", axiom.getName(), axiom.getFormula());
        simpleAlgorithm.prove(axiom.getFormula(), defaultAxiomBuilder.buildAxioms());
    }
    @Test
    public void testAxiomsA8(){
        var axiom = proveAxioms.get(4);
        log.info("\nДоказываем аксиому {}: {}", axiom.getName(), axiom.getFormula());
        simpleAlgorithm.prove(axiom.getFormula(), defaultAxiomBuilder.buildAxioms());
    }
    @Test
    public void testAxiomsA9(){
        var axiom = proveAxioms.get(5);
        log.info("\nДоказываем аксиому {}: {}", axiom.getName(), axiom.getFormula());
        simpleAlgorithm.prove(axiom.getFormula(), defaultAxiomBuilder.buildAxioms());
    }
    @Test
    public void testAxiomsA10(){
        var axiom = proveAxioms.get(6);
        log.info("\nДоказываем аксиому {}: {}", axiom.getName(), axiom.getFormula());
        simpleAlgorithm.prove(axiom.getFormula(), defaultAxiomBuilder.buildAxioms());
    }
    @Test
    public void testAxiomsA11(){
        var axiom = proveAxioms.get(7);
        log.info("\nДоказываем аксиому {}: {}", axiom.getName(), axiom.getFormula());
        simpleAlgorithm.prove(axiom.getFormula(), defaultAxiomBuilder.buildAxioms());
    }
}
