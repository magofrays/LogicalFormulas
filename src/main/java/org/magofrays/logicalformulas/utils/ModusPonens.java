package org.magofrays.logicalformulas.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Connective;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

import java.util.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class ModusPonens {
    private final PatternFinder patternFinder;
    private final Set<Formula> alreadyGenerated = new HashSet<>();

    Formula pattern = BinaryFormula.builder()
            .left(new Variable("A"))
            .connective(Connective.IMPLIES)
            .right(new Variable("B"))
            .build();

    public List<Formula> findModusPonens(List<Formula> premises) {
        List<Formula> newFormulas = new ArrayList<>();
        for (int i = 0; i < premises.size(); i++) {
            var AimpliesB = premises.get(i);
            var result = patternFinder.findPatternMatch(AimpliesB, pattern);
            if (!result.isPresent()) {
                continue;
            }
            if(alreadyGenerated.contains(result.get().get("B"))){
                continue;
            }
//            for (int j = 0; j < premises.size(); j++) {
//                if (i != j) {
            log.trace("Попытка найти соответствия для MP. Посылка : {}", AimpliesB);
            var patterns = result.get();
            if (alreadyGenerated.contains(patterns.get("A"))) {
                Formula B = patterns.get("B");
                alreadyGenerated.add(B);
                log.debug("Найдено соответствие для MP. Посылка 1: {}, Посылка с правилом вывода: {}, Новая посылка: {}", patterns.get("A"), AimpliesB, B);
                newFormulas.add(B);
            }
//                }
//            }
        }
        return newFormulas;
    }

    public void addAlreadyGenerated(Formula a){
        alreadyGenerated.add(a);
    }

    public void clearCache() {
        alreadyGenerated.clear();
    }
}

