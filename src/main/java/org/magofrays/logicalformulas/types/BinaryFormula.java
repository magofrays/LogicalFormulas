package org.magofrays.logicalformulas.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BinaryFormula extends Formula{
    private Formula left;
    private Connective connective;
    private Formula right;
    @Override
    public String toString(){
        if(isNegative){
            return "¬(%s %s %s)".formatted(left, connective.value, right);
        }
        return "(%s %s %s)".formatted(left, connective.value, right);
    }
}
