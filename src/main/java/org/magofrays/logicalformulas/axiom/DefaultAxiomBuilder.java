package org.magofrays.logicalformulas.axiom;

import org.magofrays.logicalformulas.types.Axiom;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Connective;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultAxiomBuilder {
    public List<Axiom> buildAxioms() {
        List<Axiom> axioms = new ArrayList<>();

        // Переменные для удобства
        Variable A = new Variable("A");
        Variable B = new Variable("B");
        Variable C = new Variable("C");

        // Создаем отрицания
        Variable notA = new Variable("A");
        notA.setNegative(true);
        Variable notB = new Variable("B");
        notB.setNegative(true);

        // A1: (A→(B→A))
        var A1 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(A)
                                .connective(Connective.IMPLIES)
                                .right(
                                        BinaryFormula.builder()
                                                .left(B)
                                                .connective(Connective.IMPLIES)
                                                .right(A)
                                                .build()
                                )
                                .build()
                )
                .name("A1")
                .build();
        axioms.add(A1);

        // A2: ((A→(B→C))→((A→B)→(A→C)))
        var A2 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(
                                        BinaryFormula.builder()
                                                .left(A)
                                                .connective(Connective.IMPLIES)
                                                .right(
                                                        BinaryFormula.builder()
                                                                .left(B)
                                                                .connective(Connective.IMPLIES)
                                                                .right(C)
                                                                .build()
                                                )
                                                .build()
                                )
                                .connective(Connective.IMPLIES)
                                .right(
                                        BinaryFormula.builder()
                                                .left(
                                                        BinaryFormula.builder()
                                                                .left(A)
                                                                .connective(Connective.IMPLIES)
                                                                .right(B)
                                                                .build()
                                                )
                                                .connective(Connective.IMPLIES)
                                                .right(
                                                        BinaryFormula.builder()
                                                                .left(A)
                                                                .connective(Connective.IMPLIES)
                                                                .right(C)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .name("A2")
                .build();
        axioms.add(A2);

        // A3: ((¬B→¬A)→((¬B→A)→B))
        // Создаем ¬B→¬A
        BinaryFormula notBimpliesNotA = BinaryFormula.builder()
                .left(notB)
                .connective(Connective.IMPLIES)
                .right(notA)
                .build();

        // Создаем ¬B→A
        BinaryFormula notBimpliesA = BinaryFormula.builder()
                .left(notB)
                .connective(Connective.IMPLIES)
                .right(A)
                .build();

        // Создаем ((¬B→A)→B)
        BinaryFormula notBimpliesAimpliesB = BinaryFormula.builder()
                .left(notBimpliesA)
                .connective(Connective.IMPLIES)
                .right(B)
                .build();

        var A3 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(notBimpliesNotA)
                                .connective(Connective.IMPLIES)
                                .right(notBimpliesAimpliesB)
                                .build()
                )
                .name("A3")
                .build();
        axioms.add(A3);
        return axioms;
    }
}
