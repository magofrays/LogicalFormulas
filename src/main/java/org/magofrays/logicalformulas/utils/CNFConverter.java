package org.magofrays.logicalformulas.utils;

import org.magofrays.logicalformulas.types.*;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class CNFConverter {

    /**
     * Преобразует формулу только с IMPLIES в КНФ (список клауз)
     * Каждая клауза - Formula (содержащая только AND, OR и отрицания)
     */
    public List<Formula> fromImpliesToClauses(Formula formula) {
        // Шаг 1: Устранить импликации (преобразовать в OR)
        Formula withoutImpl = eliminateImplications(formula);

        // Шаг 2: Привести к ННФ (отрицания только перед переменными)
        Formula nnf = toNNF(withoutImpl);

        // Шаг 3: Преобразовать в КНФ
        Formula cnf = toCNF(nnf);

        // Шаг 4: Разделить на клаузы (по AND)
        return splitIntoClauses(cnf);
    }

    public Formula fromClausesToFormula(List<Formula> clauses) {

        if(clauses.size() == 0){
            return null;
        }
        if (clauses.size() == 1) {
            return clauses.get(0);
        }

        Formula result = clauses.get(0);

        for (int i = 1; i < clauses.size(); i++) {
            result = BinaryFormula.builder()
                    .left(result)
                    .connective(Connective.AND)
                    .right(clauses.get(i))
                    .build();
        }

        return result;
    }



    private Formula eliminateImplications(Formula formula) {
        if (formula instanceof Variable) {
            return formula;
        } else if (formula instanceof BinaryFormula) {
            BinaryFormula bin = (BinaryFormula) formula;
            Formula left = eliminateImplications(bin.getLeft());
            Formula right = eliminateImplications(bin.getRight());

            if (bin.getConnective() == Connective.IMPLIES) {
                // A → B ≡ ¬A ∨ B
                Formula negLeft = negate(left);
                // Создаем OR через отрицание и IMPLIES (временное представление)
                BinaryFormula orFormula = BinaryFormula.builder()
                        .left(negLeft)
                        .connective(Connective.IMPLIES)
                        .right(right)
                        .build();
                orFormula.setNegative(false); // Это будет OR
                return orFormula;
            } else {
                // Сохраняем другие связки
                return BinaryFormula.builder()
                        .left(left)
                        .connective(bin.getConnective())
                        .right(right)
                        .isNegative(bin.isNegative())
                        .build();
            }
        }
        throw new IllegalArgumentException("Unknown formula type");
    }

    private Formula toNNF(Formula formula) {
        if (formula instanceof Variable) {
            return formula;
        } else if (formula instanceof BinaryFormula) {
            BinaryFormula bin = (BinaryFormula) formula;

            if (bin.isNegative()) {
                // Отрицание над бинарной формулой
                return applyDeMorgan(bin);
            }

            // Рекурсивно нормализуем подформулы
            Formula left = toNNF(bin.getLeft());
            Formula right = toNNF(bin.getRight());

            return BinaryFormula.builder()
                    .left(left)
                    .connective(bin.getConnective())
                    .right(right)
                    .isNegative(false)
                    .build();
        }
        throw new IllegalArgumentException("Unknown formula type");
    }

    private Formula applyDeMorgan(BinaryFormula formula) {
        Formula left = formula.getLeft();
        Formula right = formula.getRight();
        Connective connective = formula.getConnective();

        // ¬(A ∧ B) ≡ ¬A ∨ ¬B
        // ¬(A ∨ B) ≡ ¬A ∧ ¬B
        if (connective == Connective.IMPLIES) {
            // Для IMPLIES: ¬(¬A → B) ≡ ¬(A ∨ B) ≡ ¬A ∧ ¬B
            Formula negLeft = negate(left);
            Formula negRight = negate(right);

            // Создаем AND
            return BinaryFormula.builder()
                    .left(negLeft)
                    .connective(Connective.AND)
                    .right(negRight)
                    .isNegative(false)
                    .build();
        } else if (connective == Connective.AND) {
            // ¬(A ∧ B) ≡ ¬A ∨ ¬B
            Formula negLeft = negate(left);
            Formula negRight = negate(right);

            return BinaryFormula.builder()
                    .left(negLeft)
                    .connective(Connective.IMPLIES) // OR через IMPLIES
                    .right(negRight)
                    .isNegative(false)
                    .build();
        } else if (connective == Connective.OR) {
            // ¬(A ∨ B) ≡ ¬A ∧ ¬B
            Formula negLeft = negate(left);
            Formula negRight = negate(right);

            return BinaryFormula.builder()
                    .left(negLeft)
                    .connective(Connective.AND)
                    .right(negRight)
                    .isNegative(false)
                    .build();
        }

        throw new IllegalArgumentException("Unsupported connective");
    }

    private Formula toCNF(Formula formula) {
        if (formula instanceof Variable) {
            return formula;
        } else if (formula instanceof BinaryFormula) {
            BinaryFormula bin = (BinaryFormula) formula;
            Formula leftCNF = toCNF(bin.getLeft());
            Formula rightCNF = toCNF(bin.getRight());

            if (bin.getConnective() == Connective.AND) {
                // A ∧ B уже в КНФ форме
                return BinaryFormula.builder()
                        .left(leftCNF)
                        .connective(Connective.AND)
                        .right(rightCNF)
                        .build();
            } else if (bin.getConnective() == Connective.IMPLIES) { // Это OR
                // (A ∨ B) нужно проверить дистрибутивность
                return distributeOR(leftCNF, rightCNF);
            }
        }
        throw new IllegalArgumentException("Formula should be in NNF");
    }

    private Formula distributeOR(Formula A, Formula B) {
        // Если A = C ∧ D
        if (A instanceof BinaryFormula && ((BinaryFormula) A).getConnective() == Connective.AND) {
            BinaryFormula andA = (BinaryFormula) A;
            // (C ∧ D) ∨ B ≡ (C ∨ B) ∧ (D ∨ B)
            Formula leftOR = distributeOR(andA.getLeft(), B);
            Formula rightOR = distributeOR(andA.getRight(), B);
            return BinaryFormula.builder()
                    .left(leftOR)
                    .connective(Connective.AND)
                    .right(rightOR)
                    .build();
        }
        // Если B = C ∧ D
        else if (B instanceof BinaryFormula && ((BinaryFormula) B).getConnective() == Connective.AND) {
            BinaryFormula andB = (BinaryFormula) B;
            // A ∨ (C ∧ D) ≡ (A ∨ C) ∧ (A ∨ D)
            Formula leftOR = distributeOR(A, andB.getLeft());
            Formula rightOR = distributeOR(A, andB.getRight());
            return BinaryFormula.builder()
                    .left(leftOR)
                    .connective(Connective.AND)
                    .right(rightOR)
                    .build();
        }
        // Ни A, ни B не являются AND
        else {
            // Просто A ∨ B
            return BinaryFormula.builder()
                    .left(A)
                    .connective(Connective.IMPLIES) // OR через IMPLIES
                    .right(B)
                    .build();
        }
    }

    private List<Formula> splitIntoClauses(Formula formula) {
        List<Formula> clauses = new ArrayList<>();

        if (formula instanceof BinaryFormula) {
            BinaryFormula bin = (BinaryFormula) formula;

            if (bin.getConnective() == Connective.AND) {
                // Разделяем по AND
                clauses.addAll(splitIntoClauses(bin.getLeft()));
                clauses.addAll(splitIntoClauses(bin.getRight()));
            } else {
                // Это клауза (дизъюнкция литералов)
                clauses.add(formula);
            }
        } else {
            // Одиночная переменная - тоже клауза
            clauses.add(formula);
        }

        return clauses;
    }

    private Formula negate(Formula formula) {
        if (formula instanceof Variable) {
            Variable var = (Variable) formula;
            return Variable.builder()
                    .value(var.getValue())
                    .isNegative(!var.isNegative())
                    .build();
        } else if (formula instanceof BinaryFormula) {
            BinaryFormula bin = (BinaryFormula) formula;
            return BinaryFormula.builder()
                    .left(bin.getLeft())
                    .connective(bin.getConnective())
                    .right(bin.getRight())
                    .isNegative(!bin.isNegative())
                    .build();
        }
        throw new IllegalArgumentException("Cannot negate formula");
    }
}