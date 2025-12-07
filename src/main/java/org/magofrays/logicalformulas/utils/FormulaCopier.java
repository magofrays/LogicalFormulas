package org.magofrays.logicalformulas.utils;

import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

@Component
public class FormulaCopier {
    public Formula deepCopy(Formula formula) {
        if (formula == null) {
            return null;
        }

        if (formula instanceof Variable) {
            Variable var = (Variable) formula;
            return Variable.builder()
                    .value(var.getValue())
                    .isNegative(var.isNegative())
                    .build();
        }
        else if (formula instanceof BinaryFormula) {
            BinaryFormula bin = (BinaryFormula) formula;

            // Рекурсивно копируем левую и правую части
            Formula leftCopy = deepCopy(bin.getLeft());
            Formula rightCopy = deepCopy(bin.getRight());

            return BinaryFormula.builder()
                    .left(leftCopy)
                    .connective(bin.getConnective())
                    .right(rightCopy)
                    .isNegative(bin.isNegative())
                    .build();
        }

        throw new IllegalArgumentException("Unknown formula type: " + formula.getClass().getName());
    }
}