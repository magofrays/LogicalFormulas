//package org.magofrays.logicalformulas.utils;
//
//
//import org.magofrays.logicalformulas.types.Axiom;
//import org.magofrays.logicalformulas.types.BinaryFormula;
//import org.magofrays.logicalformulas.types.Formula;
//import org.magofrays.logicalformulas.types.Variable;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AxiomMatchFinder {
//    private List<Axiom> axiomList;
//    private Map<String, Formula> currentSubstitution;
//
//    public AxiomMatchFinder(List<Axiom> axiomList){
//        this.axiomList = axiomList;
//    }
//
//    public boolean findMatch(Formula formula, Formula pattern){
//        if(formula instanceof BinaryFormula binFormula && pattern instanceof BinaryFormula binPattern){
//            if(!binPattern.getConnective().equals(binFormula.getConnective())){
//                return false;
//            }
//            return findMatch(binFormula.getLeft(), binPattern.getLeft()) && findMatch(binFormula.getRight(), binPattern.getRight());
//        }
//        else if(pattern instanceof Variable variable){
//            var strVar = variable.toString();
//            variable.setNegative(!variable.isNegative());
//            var negStrVar = variable.toString();
//            variable.setNegative(!variable.isNegative());
//            if(currentSubstitution.containsKey(strVar) || currentSubstitution.containsKey(negStrVar)){
//                Formula substitute;
//                boolean isNegative = false;
//                if(currentSubstitution.containsKey(strVar)){
//                    substitute = currentSubstitution.get(strVar);
//                }
//                else{
//                    isNegative = true;
//                    substitute = currentSubstitution.get(negStrVar);
//                }
//                boolean flagEquals = false;
//                if(isNegative){
//                    substitute.setNegative(!substitute.isNegative());
//                }
//                if(substitute.equals(formula)){
//                    flagEquals = true;
//                }
//                if(isNegative){
//                    substitute.setNegative(!substitute.isNegative());
//                }
//                return flagEquals;
//            }
//            currentSubstitution.put(strVar, formula);
//        }
//        return false;
//    }
//
//    public Map<String, Formula> findMatches(Formula formula){
//        Map<String, Formula> matches = new HashMap<>();
//        for(var axiom : axiomList){
//            if(findMatch(formula, axiom.getFormula())){
//                            }
//        }
//    }
//}
