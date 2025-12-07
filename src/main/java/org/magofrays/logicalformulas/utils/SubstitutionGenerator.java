package org.magofrays.logicalformulas.utils;


import org.magofrays.logicalformulas.types.Axiom;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SubstitutionGenerator {

    public List<Map<String, Formula>> createSubstitutionsForAxiom(Formula formula, Axiom axiom, Integer num){
        var axiomVariables = excludeVariables(axiom.getFormula());
        var allParts = excludeParts(formula);
        return createSubstitutions(allParts, axiomVariables, num);
    }

    public List<Map<String, Formula>> createSubstitutions(List<Formula> parts, Set<String> variables, Integer num) {
        List<Map<String, Formula>> substitutions = new ArrayList<>();
        var variArr = variables.toArray();

        String[] vars = new String[variArr.length];
        for (int i = 0; i < variArr.length; i++) {
            vars[i] = (String) variArr[i];
        }
        boolean generateAll = (num == null);
        if (vars.length == 0 || parts.isEmpty() || !generateAll && num == 0) {
            return substitutions;
        }



        int[] currentIndices = new int[vars.length];

        while (generateAll || substitutions.size() < num) {
            Map<String, Formula> substitution = new HashMap<>();
            boolean valid = true;

            for (int j = 0; j < vars.length; j++) {
                if (currentIndices[j] >= parts.size()) {
                    valid = false;
                    break;
                }
                substitution.put(vars[j], parts.get(currentIndices[j]));
            }

            if (valid) {
                substitutions.add(substitution);
            }

            int carry = 1;
            for (int j = vars.length - 1; j >= 0; j--) {
                currentIndices[j] += carry;
                if (currentIndices[j] < parts.size()) {
                    carry = 0;
                    break;
                }

                currentIndices[j] = 0;
                carry = 1;
            }

            if (carry == 1) {
                break;
            }
        }

        if (num != null && substitutions.size() > num) {
            return substitutions.subList(0, num);
        }

        return substitutions;
    }

    public List<Formula> excludeParts(Formula formula){
        Stack<Formula> stack = new Stack<>();
        List<Formula> parts = new ArrayList<>();
        Set<Formula> uniqueParts = new HashSet<>();
        stack.push(formula);
        while(!stack.empty()){
            var curFormula = stack.pop();
            if(!uniqueParts.contains(curFormula)){
                uniqueParts.add(curFormula);
                parts.add(curFormula);
            }
            if(curFormula instanceof BinaryFormula binaryFormula){
                stack.push(binaryFormula.getLeft());
                stack.push(binaryFormula.getRight());
            }

        }
        return parts;
    }


    public Set<String> excludeVariables(Formula formula){
        Stack<Formula> stack = new Stack<>();
        Set<String> variables = new HashSet<>();
        stack.push(formula);
        while(!stack.empty()){
            var curFormula = stack.pop();
            if(curFormula instanceof BinaryFormula binaryFormula){
                stack.push(binaryFormula.getLeft());
                stack.push(binaryFormula.getRight());
            }
            else if(curFormula instanceof Variable variable){
                variables.add(variable.getValue());
            }
        }
        return variables;
    }
}

