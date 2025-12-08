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

    // Кэш для уже найденных формул по MP
    private final Set<Formula> alreadyGenerated = new HashSet<>();
    private final Map<Pair<Formula, Formula>, Formula> mpCache = new HashMap<>();

    Formula pattern = BinaryFormula.builder()
            .left(new Variable("A"))
            .connective(Connective.IMPLIES)
            .right(new Variable("B"))
            .build();

    public List<Formula> findModusPonens(List<Formula> premises) {
        List<Formula> newFormulas = new ArrayList<>();

        for (int i = 0; i < premises.size(); i++) {
            for (int j = 0; j < premises.size(); j++) {
                if (i != j) {
                    var A = premises.get(i);
                    var AimpliesB = premises.get(j);
                    tryApplyMP(A, AimpliesB).ifPresent(newFormulas::add);
                }
            }
        }
        return newFormulas;
    }

    public void clearCache() {
        alreadyGenerated.clear();
        mpCache.clear();
    }

    public void addToCache(Formula formula) {
        alreadyGenerated.add(formula);
    }


    public Optional<Formula> tryApplyMP(Formula A, Formula AimpliesB) {
        log.trace("Попытка найти соответствия для MP. Посылка 1: {}, Посылка 2 {}", A, AimpliesB);
        Pair<Formula, Formula> key = new Pair<>(A, AimpliesB);

        if (mpCache.containsKey(key)) {
            return Optional.of(mpCache.get(key));
        }

        var result = patternFinder.findPatternMatch(AimpliesB, pattern);
        if (result.isPresent()) {
            var patterns = result.get();
            if (patterns.get("A").equals(A)) {
                Formula B = patterns.get("B");
                mpCache.put(key, B);
                log.debug("Найдено соответствие для MP. Посылка 1: {}, Посылка с правилом вывода: {}, Новая посылка: {}", A, AimpliesB, B);
                return Optional.of(B);
            }
        }

        return Optional.empty();
    }
}

