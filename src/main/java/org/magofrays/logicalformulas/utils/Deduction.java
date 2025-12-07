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
public class Deduction {
    private final PatternFinder patternFinder;
    private final CNFConverter cnfConverter;
    private final ImpliesConverter impliesConverter;
    Random random = new Random();

    // ПРОСТО ДВА КЭША
    private final Map<Formula, Optional<Map<String, Formula>>> patternCache = new HashMap<>();
    private final Set<Formula> generatedResults = new HashSet<>();

    Formula pattern = BinaryFormula.builder()
            .left(new Variable("A"))
            .connective(Connective.IMPLIES)
            .right(new Variable("B"))
            .build();

    public List<Formula> randomDeduct(List<Formula> premises){
        List<Formula> newPremises = new ArrayList<>();

        for(var premise: premises){
            // КЭШ 1: паттерн-матч
            Optional<Map<String, Formula>> result = patternCache.get(premise);
            if(result == null){
                result = patternFinder.findPatternMatch(premise, pattern);
                patternCache.put(premise, result);
            }

            if(result.isPresent()){
                var patterns = result.get();
                var left = patterns.get("A");
                var clauses = cnfConverter.fromImpliesToClauses(left);
                if(clauses.isEmpty()) continue;

                int randomClauseIndex = random.nextInt(clauses.size());
                var chosenFormula = clauses.get(randomClauseIndex);
                clauses.remove(randomClauseIndex);
                var right = patterns.get("B");
                var newLeft = impliesConverter.toImplies(
                        cnfConverter.fromClausesToFormula(clauses)
                );
                var newRight = BinaryFormula.builder()
                        .left(chosenFormula)
                        .connective(Connective.IMPLIES)
                        .right(right)
                        .build();
                var newPremise = BinaryFormula.builder()
                        .left(newLeft)
                        .connective(Connective.IMPLIES)
                        .right(newRight)
                        .build();

                // КЭШ 2: проверка дубликатов
                if(!generatedResults.contains(newPremise)){
                    newPremises.add(newPremise);
                    generatedResults.add(newPremise);
                }
            }
        }
        return newPremises;
    }

    // Для очистки при новом доказательстве
    public void clearCache(){
        patternCache.clear();
        generatedResults.clear();
    }
}