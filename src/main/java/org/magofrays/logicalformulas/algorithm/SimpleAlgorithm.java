package org.magofrays.logicalformulas.algorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.magofrays.logicalformulas.axiom.DefaultAxiomBuilder;
import org.magofrays.logicalformulas.types.Axiom;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.utils.AxiomSubstituter;
import org.magofrays.logicalformulas.utils.Deduction;
import org.magofrays.logicalformulas.utils.ModusPonens;
import org.magofrays.logicalformulas.utils.SubstitutionGenerator;
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



    public void prove(Formula formula){
        List<Axiom> axioms = new DefaultAxiomBuilder().buildAxioms();
        Set<Formula> allPremises = new HashSet<>();

        for(var axiom : axioms) {
            var subs = substitutionGenerator.createSubstitutionsForAxiom(formula, axiom, 100); // ограничим
            for(var sub: subs){
                allPremises.add(axiomSubstituter.applySubstitution(axiom, sub));
            }
        }

        int maxSteps = 1000; // защита от бесконечного цикла
        for(int step = 0; step < maxSteps; step++){
            if(findFormula(allPremises, formula)){
                log.info("Формула была найдена: {}", formula);
                return;
            }

            List<Formula> currentList = new ArrayList<>(allPremises);

            // Применяем MP
            var mpResults = modusPonens.findModusPonens(currentList);
            allPremises.addAll(mpResults);

            // Применяем дедукцию (не каждый шаг, чтобы не плодить)
            if(step % 3 == 0){
                var deductionResults = deduction.randomDeduct(currentList);
                allPremises.addAll(deductionResults);
            }

            log.debug("Step {}: {} formulas", step, allPremises.size());

            if(allPremises.size() > 10000){ // защита от памяти
                log.warn("Too many formulas, stopping");
                break;
            }
        }

        log.warn("Failed to prove {}", formula);
    }

    public boolean findFormula(Collection<Formula> premises, Formula formula){
        return premises.contains(formula);
    }
}