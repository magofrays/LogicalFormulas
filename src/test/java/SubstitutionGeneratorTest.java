import org.junit.jupiter.api.Test;
import org.magofrays.logicalformulas.types.*;
import org.magofrays.logicalformulas.utils.DefaultAxiomBuilder;
import org.magofrays.logicalformulas.utils.SubstitutionGenerator;

import java.util.List;

public class SubstitutionGeneratorTest {
    SubstitutionGenerator generator = new SubstitutionGenerator();
    @Test
    public void substitutionGenerator(){
        List<Axiom> axiomList = new DefaultAxiomBuilder().buildAxioms();
        Formula formula = BinaryFormula.builder()
                .left(new Variable("A"))
                .connective(Connective.IMPLIES)
                .right(new Variable("A"))
                .build();
        var subs = generator.createSubstitutionsForAxiom(formula, axiomList.get(0),null);
        for(var sub : subs){
            System.out.println(sub);
        }
    }
}
