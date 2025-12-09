package org.magofrays.logicalformulas.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.magofrays.logicalformulas.types.Axiom;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class AxiomSubstituter {
    private final FormulaCopier copier;

    public Formula applySubstitution(Axiom axiom, Map<String, Formula> substitution){
        var substitutionResult = substituteFormula(axiom.getFormula(), substitution);
        log.debug("Подставляем в аксиому {} подстановку {}. Результат: {}", axiom.getFormula(), substitution, substitutionResult);
        return substitutionResult;
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
            return copier.deepCopy(substitution.get(varPattern.getValue()));
        }
        throw new RuntimeException("Impossible magic");
    }
}
