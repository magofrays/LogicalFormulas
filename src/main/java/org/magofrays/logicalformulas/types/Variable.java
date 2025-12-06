package org.magofrays.logicalformulas.types;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
public class Variable extends Formula{
    private String value;

    @Override
    public String toString() {
        if(isNegative){
            return "¬" + value;
        }
        return value;
    }
}
