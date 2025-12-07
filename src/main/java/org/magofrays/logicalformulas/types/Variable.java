package org.magofrays.logicalformulas.types;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
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

    public String negValue(){
        return "¬" + value;
    }
}
