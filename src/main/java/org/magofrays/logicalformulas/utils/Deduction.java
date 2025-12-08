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
public class Deduction {
    private final PatternFinder patternFinder;
    private final CNFConverter cnfConverter;
    private final ImpliesConverter impliesConverter;
    Random random = new Random();

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
            log.trace("Попытка применить теорему дедукции к посылке: {}", premise);
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
                var newRight = BinaryFormula.builder()
                        .left(chosenFormula)
                        .connective(Connective.IMPLIES)
                        .right(right)
                        .build();
                Formula newPremise;
                if(!clauses.isEmpty()) {
                    var newLeft = impliesConverter.toImplies(
                            cnfConverter.fromClausesToFormula(clauses)
                    );


                    newPremise = BinaryFormula.builder()
                            .left(newLeft)
                            .connective(Connective.IMPLIES)
                            .right(newRight)
                            .build();

                }
                else{
                    newPremise = newRight;
                }
                if(!generatedResults.contains(newPremise)){
                    log.debug("Применена теорема дедукции для посылки: {}, Новая посылка: {}", premise, newPremise);
                    newPremises.add(newPremise);
                    generatedResults.add(newPremise);
                }
            }
        }
        return newPremises;
    }

    public void clearCache(){
        patternCache.clear();
        generatedResults.clear();
    }
}