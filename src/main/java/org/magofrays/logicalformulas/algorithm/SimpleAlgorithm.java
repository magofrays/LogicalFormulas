package org.magofrays.logicalformulas.algorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.magofrays.logicalformulas.axiom.DefaultAxiomBuilder;
import org.magofrays.logicalformulas.types.Axiom;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.utils.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleAlgorithm {
    private final Deduction deduction;
    private final ModusPonens modusPonens;
    private final SubstitutionGenerator substitutionGenerator;
    private final AxiomSubstituter axiomSubstituter;
    private final ImpliesConverter impliesConverter;

    public void prove(Formula formula, List<Axiom> axioms){
        log.info("============Начало доказательства==============");
        modusPonens.clearCache();
        deduction.clearCache();
        Set<Formula> allPremises = new HashSet<>();
        formula = impliesConverter.toImplies(formula);
        log.debug("Преобразованная формула: {}", formula);
        for(var axiom : axioms) {
            var subs = substitutionGenerator.createSubstitutionsForAxiom(formula, axiom, 100); // ограничим
            for(var sub: subs){
                allPremises.add(axiomSubstituter.applySubstitution(axiom, sub));
            }
        }
        for(var premise : allPremises){
            modusPonens.addAlreadyGenerated(premise);
            deduction.addAlreadyGenerated(premise);
        }
        if(findFormula(allPremises, formula)){
            return;
        }
        int maxSteps = 100;
        for(int step = 0; step < maxSteps; step++){

            List<Formula> currentList = new ArrayList<>(allPremises);
            log.info("Используем правило вывода");
            var mpResults = modusPonens.findModusPonens(currentList);
            if(findFormula(mpResults, formula)){
                return;
            }
            allPremises.addAll(mpResults);
            if(step % 3 == 0){
                log.info("Используем теорему дедукции");
                var deductionResults = deduction.randomDeduct(currentList);
                if(findFormula(deductionResults, formula)){
                    return;
                }
                allPremises.addAll(deductionResults);

            }

            log.info("Step {}: {} formulas", step, allPremises.size());

            if(allPremises.size() > 10000){
                log.warn("Too many formulas, stopping");
                break;
            }
        }
        log.warn("Failed to prove {}", formula);
    }

    public boolean findFormula(Collection<Formula> premises, Formula formula){
        if(premises.contains(formula)){
            log.info("===================Алгоритм доказал формулу=====================");
            log.info("Формула была выведена: {}", formula);
            return true;
        }
        return false;
    }
}