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
    private final Set<Formula> patternCache = new HashSet<>();
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
            if(patternCache.contains(premise)){
                continue;
            }
            patternCache.add(premise);
            var result = patternFinder.findPatternMatch(premise, pattern);
            if(result.isPresent()){
                var patterns = result.get();
                var left = patterns.get("A");
                var clauses = cnfConverter.fromImpliesToClauses(left);
                int randomClauseIndex = random.nextInt(clauses.size());
                var chosenFormula = clauses.get(randomClauseIndex);
                clauses.remove(randomClauseIndex);
                var right = patterns.get("B");
                var newRight = BinaryFormula.builder()
                        .left(impliesConverter.toImplies(chosenFormula))
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

    public void addAlreadyGenerated(Formula a){
        generatedResults.add(a);
    }

    public void clearCache(){
        patternCache.clear();
        generatedResults.clear();
    }
}