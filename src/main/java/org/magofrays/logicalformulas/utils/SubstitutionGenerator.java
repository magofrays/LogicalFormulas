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
        Collections.reverse(allParts);
        return createSubstitutions(allParts, axiomVariables, num);
    }

    public List<Map<String, Formula>> createSubstitutions(List<Formula> parts, Set<String> variables, Integer num) {
        List<Map<String, Formula>> substitutions = new ArrayList<>();
        String[] vars = variables.toArray(new String[0]);
        boolean generateAll = (num == null);
        if (vars.length == 0 || parts.isEmpty() || !generateAll && num == 0) {
            return substitutions;
        }
        int[] currentIndices = new int[vars.length];
        while (generateAll || substitutions.size() < num) {
            Map<String, Formula> substitution = new HashMap<>();
            for (int i = 0; i < vars.length; i++) {
                substitution.put(vars[i], parts.get(currentIndices[i]));
            }
            substitutions.add(substitution);

            int carry = 1;
            for (int i = vars.length - 1; i >= 0; i--) {
                currentIndices[i] += carry;
                if (currentIndices[i] < parts.size()) {
                    carry = 0;
                    break;
                }
                currentIndices[i] = 0;
                carry = 1;
            }

            if (carry == 1) {
                break;
            }
        }
        return substitutions;
    }

    public List<Formula> excludeParts(Formula formula){
        Queue<Formula> stack = new LinkedList<>();
        List<Formula> parts = new ArrayList<>();
        Set<Formula> uniqueParts = new HashSet<>();
        stack.add(formula);
        while(!stack.isEmpty()){
            var curFormula = stack.poll();
            if(!uniqueParts.contains(curFormula)){
                uniqueParts.add(curFormula);
                parts.add(curFormula);
            }
            if(curFormula instanceof BinaryFormula binaryFormula){
                stack.add(binaryFormula.getLeft());
                stack.add(binaryFormula.getRight());
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

