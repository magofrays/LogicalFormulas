package org.magofrays.logicalformulas.utils;

import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Connective;
import org.magofrays.logicalformulas.types.Formula;
import org.springframework.stereotype.Component;

@Component
public class ImpliesConverter {
    public Formula toImplies(Formula formula){
        if(formula instanceof BinaryFormula bin){
            Formula leftConv = toImplies(bin.getLeft());
            Formula rightConv = toImplies(bin.getRight());
            switch (bin.getConnective()) {
                case OR:
                    // A ∨ B ≡ ¬A → B
                    leftConv.setNegative(!leftConv.isNegative());
                    BinaryFormula orImpl = BinaryFormula.builder()
                            .left(leftConv)
                            .connective(Connective.IMPLIES)
                            .right(rightConv)
                            .build();
                    orImpl.setNegative(bin.isNegative());
                    return orImpl;

                case AND:
                    // A ∧ B ≡ ¬(A → ¬B)
                    rightConv.setNegative(!rightConv.isNegative());
                    BinaryFormula impl = BinaryFormula.builder()
                            .left(leftConv)
                            .connective(Connective.IMPLIES)
                            .right(rightConv)
                            .build();
                    impl.setNegative(true);
                    if (bin.isNegative()) {
                        impl.setNegative(!impl.isNegative());
                    }
                    return impl;
            }
        }
        return formula;
    }

}
