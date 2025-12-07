import org.junit.jupiter.api.Test;
import org.magofrays.logicalformulas.types.*;
import org.magofrays.logicalformulas.utils.AxiomSubstituter;
import org.magofrays.logicalformulas.axiom.DefaultAxiomBuilder;
import org.magofrays.logicalformulas.utils.SubstitutionGenerator;

import java.util.List;

public class SubstitutionGeneratorTest {
    SubstitutionGenerator generator = new SubstitutionGenerator();
    AxiomSubstituter axiomSubstitute = new AxiomSubstituter();
    @Test
    public void substitutionGeneratorAndSubstituteTester(){
        List<Axiom> axiomList = new DefaultAxiomBuilder().buildAxioms();
        Formula formula = BinaryFormula.builder()
                .left(new Variable("A"))
                .connective(Connective.IMPLIES)
                .right(new Variable("A"))
                .build();
        var subs = generator.createSubstitutionsForAxiom(formula, axiomList.get(0),null);
        for(var sub : subs){
//            System.out.println(axiomList.get(0));
            System.out.println(sub);
//            System.out.println(
//                    axiomSubstitute.substituteAxiom(axiomList.get(0), sub)
//            );
        }
    }
}
