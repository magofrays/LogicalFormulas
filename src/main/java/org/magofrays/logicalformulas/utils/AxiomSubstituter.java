package org.magofrays.logicalformulas.utils;

import org.magofrays.logicalformulas.types.Axiom;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AxiomSubstituter {

    public Formula applySubstitution(Axiom axiom, Map<String, Formula> substitution){
        return substituteFormula(axiom.getFormula(), substitution);
    }

    private Formula substituteFormula(Formula pattern, Map<String, Formula> substitution){
        if(pattern instanceof BinaryFormula binPattern){
            return BinaryFormula.builder()
                    .left(substituteFormula(binPattern.getLeft(), substitution))
                    .connective(binPattern.getConnective())
                    .right(substituteFormula(binPattern.getRight(), substitution))
                    .build();
        }
        else if(pattern instanceof Variable varPattern){
            return substitution.get(varPattern.getValue());
        }
        throw new RuntimeException("Impossible magic");
    }
}
