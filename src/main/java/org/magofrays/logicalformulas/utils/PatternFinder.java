package org.magofrays.logicalformulas.utils;

import lombok.RequiredArgsConstructor;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PatternFinder {
    private final FormulaCopier formulaCopier;

    public Optional<Map<String, Formula>> findPatternMatch(Formula formula, Formula pattern){
        Map<String, Formula> currentSubstitution = new HashMap<>();
        if(findMatch(formula, pattern, currentSubstitution)){
            return Optional.of(currentSubstitution);
        }
        return Optional.empty();
    }

    public boolean findMatch(Formula formula, Formula pattern, Map<String, Formula> currentSubstitution){
        if(formula instanceof BinaryFormula binFormula && pattern instanceof BinaryFormula binPattern){
            if(!binPattern.getConnective().equals(binFormula.getConnective())){
                return false;
            }
            return findMatch(binFormula.getLeft(), binPattern.getLeft(), currentSubstitution) && findMatch(binFormula.getRight(), binPattern.getRight(), currentSubstitution);
        }
        else if(pattern instanceof Variable variable){
            var negFormula = currentSubstitution.get(variable.negValue());
            var posFormula = currentSubstitution.get(variable.getValue());
            if(posFormula == null && negFormula == null){
                currentSubstitution.put(variable.toString(), formulaCopier.deepCopy(formula));
                return true;
            }
            if(posFormula != null){
                return posFormula.getValue().equals(formula.getValue());
            }
            return negFormula.getValue().equals(formula.getValue());
        }
        return false;
    }

}
