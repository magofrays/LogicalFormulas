package org.magofrays.logicalformulas.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Axiom {
    private String name;
    private Formula formula;
}
