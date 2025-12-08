package org.magofrays.logicalformulas.axiom;

import org.magofrays.logicalformulas.types.Axiom;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Connective;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestAxiomBuilder {
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

        // A4: A∧B→A
        var A4 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(
                                        BinaryFormula.builder()
                                                .left(A)
                                                .connective(Connective.AND)
                                                .right(B)
                                                .build()
                                )
                                .connective(Connective.IMPLIES)
                                .right(A)
                                .build()
                )
                .name("A4")
                .build();
        axioms.add(A4);

        // A5: A∧B→B
        var A5 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(
                                        BinaryFormula.builder()
                                                .left(A)
                                                .connective(Connective.AND)
                                                .right(B)
                                                .build()
                                )
                                .connective(Connective.IMPLIES)
                                .right(B)
                                .build()
                )
                .name("A5")
                .build();
        axioms.add(A5);

        // A6: A→(B→(A∧B))
        var A6 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(A)
                                .connective(Connective.IMPLIES)
                                .right(
                                        BinaryFormula.builder()
                                                .left(B)
                                                .connective(Connective.IMPLIES)
                                                .right(
                                                        BinaryFormula.builder()
                                                                .left(A)
                                                                .connective(Connective.AND)
                                                                .right(B)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .name("A6")
                .build();
        axioms.add(A6);

        // A7: A→(A∨B)
        var A7 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(A)
                                .connective(Connective.IMPLIES)
                                .right(
                                        BinaryFormula.builder()
                                                .left(A)
                                                .connective(Connective.OR)
                                                .right(B)
                                                .build()
                                )
                                .build()
                )
                .name("A7")
                .build();
        axioms.add(A7);

        // A8: B→(A∨B)
        var A8 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(B)
                                .connective(Connective.IMPLIES)
                                .right(
                                        BinaryFormula.builder()
                                                .left(A)
                                                .connective(Connective.OR)
                                                .right(B)
                                                .build()
                                )
                                .build()
                )
                .name("A8")
                .build();
        axioms.add(A8);

        // A9: (A→C)→((B→C)→((A∨B)→C))
        // Создаем (A→C)
        BinaryFormula AimpliesC = BinaryFormula.builder()
                .left(A)
                .connective(Connective.IMPLIES)
                .right(C)
                .build();

        // Создаем (B→C)
        BinaryFormula BimpliesC = BinaryFormula.builder()
                .left(B)
                .connective(Connective.IMPLIES)
                .right(C)
                .build();

        // Создаем (A∨B)
        BinaryFormula AorB = BinaryFormula.builder()
                .left(A)
                .connective(Connective.OR)
                .right(B)
                .build();

        // Создаем ((A∨B)→C)
        BinaryFormula AorBimpliesC = BinaryFormula.builder()
                .left(AorB)
                .connective(Connective.IMPLIES)
                .right(C)
                .build();

        // Создаем ((B→C)→((A∨B)→C))
        BinaryFormula BimpliesCimpliesAorBimpliesC = BinaryFormula.builder()
                .left(BimpliesC)
                .connective(Connective.IMPLIES)
                .right(AorBimpliesC)
                .build();

        var A9 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(AimpliesC)
                                .connective(Connective.IMPLIES)
                                .right(BimpliesCimpliesAorBimpliesC)
                                .build()
                )
                .name("A9")
                .build();
        axioms.add(A9);

        // A10: ¬A→(A→B)
        var A10 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(notA)
                                .connective(Connective.IMPLIES)
                                .right(
                                        BinaryFormula.builder()
                                                .left(A)
                                                .connective(Connective.IMPLIES)
                                                .right(B)
                                                .build()
                                )
                                .build()
                )
                .name("A10")
                .build();
        axioms.add(A10);

        // A11: A∨¬A
        var A11 = Axiom.builder()
                .formula(
                        BinaryFormula.builder()
                                .left(A)
                                .connective(Connective.OR)
                                .right(notA)
                                .build()
                )
                .name("A11")
                .build();
        axioms.add(A11);

        return axioms;
    }
}
