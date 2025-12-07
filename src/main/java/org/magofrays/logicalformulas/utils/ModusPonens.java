package org.magofrays.logicalformulas.utils;

import lombok.RequiredArgsConstructor;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Connective;
import org.magofrays.logicalformulas.types.Formula;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.stereotype.Component;

import java.util.*;

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

    /**
     * Находит новые формулы по MP, избегая повторов
     */
    public List<Formula> findModusPonens(List<Formula> premises) {
        List<Formula> newFormulas = new ArrayList<>();

        for (int i = 0; i < premises.size(); i++) {
            for (int j = 0; j < premises.size(); j++) {
                if (i != j) {
                    var A = premises.get(i);
                    var AimpliesB = premises.get(j);

                    // Проверяем кэш
                    Pair<Formula, Formula> key = new Pair<>(A, AimpliesB);
                    if (mpCache.containsKey(key)) {
                        Formula cached = mpCache.get(key);
                        if (!alreadyGenerated.contains(cached)) {
                            newFormulas.add(cached);
                            alreadyGenerated.add(cached);
                        }
                        continue;
                    }

                    var result = patternFinder.findPatternMatch(AimpliesB, pattern);
                    result.ifPresent(patterns -> {
                        var B = patterns.get("B");

                        // Проверяем, что A совпадает с левой частью импликации
                        if (patterns.get("A").equals(A)) {
                            // Проверяем, не генерировали ли уже эту формулу
                            if (!alreadyGenerated.contains(B)) {
                                newFormulas.add(B);
                                alreadyGenerated.add(B);
                                mpCache.put(key, B);
                            }
                        }
                    });
                }
            }
        }
        return newFormulas;
    }

    /**
     * Очищает кэш (если начали новое доказательство)
     */
    public void clearCache() {
        alreadyGenerated.clear();
        mpCache.clear();
    }

    /**
     * Добавляет формулы в кэш, чтобы не генерировать их снова
     */
    public void addToCache(Formula formula) {
        alreadyGenerated.add(formula);
    }

    /**
     * Проверяет, можно ли применить MP к двум формулам
     * без дублирования вычислений
     */
    public Optional<Formula> tryApplyMP(Formula A, Formula AimpliesB) {
        Pair<Formula, Formula> key = new Pair<>(A, AimpliesB);

        // Проверяем кэш
        if (mpCache.containsKey(key)) {
            return Optional.of(mpCache.get(key));
        }

        // Вычисляем
        var result = patternFinder.findPatternMatch(AimpliesB, pattern);
        if (result.isPresent()) {
            var patterns = result.get();
            if (patterns.get("A").equals(A)) {
                Formula B = patterns.get("B");
                mpCache.put(key, B);
                return Optional.of(B);
            }
        }

        return Optional.empty();
    }
}

