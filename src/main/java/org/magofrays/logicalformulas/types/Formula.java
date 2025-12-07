package org.magofrays.logicalformulas.types;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class Formula {
    @Builder.Default
    boolean isNegative = false;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Formula other)) return false;
        return this.toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    public abstract String getValue();
}
