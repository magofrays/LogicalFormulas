package org.magofrays.logicalformulas.types;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public String getValue(){
        return "(%s %s %s)".formatted(left, connective.value, right);
    }

}
