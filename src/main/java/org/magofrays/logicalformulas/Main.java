package org.magofrays.logicalformulas;

import lombok.extern.slf4j.Slf4j;
import org.magofrays.logicalformulas.algorithm.SimpleAlgorithm;
import org.magofrays.logicalformulas.types.BinaryFormula;
import org.magofrays.logicalformulas.types.Connective;
import org.magofrays.logicalformulas.types.Variable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner run(SimpleAlgorithm algorithm) {
        return args -> {
            log.info("🚀 Запуск доказательства формулы P → P");

            Variable p = Variable.builder().value("P").build();
            BinaryFormula formula = BinaryFormula.builder()
                    .left(p)
                    .connective(Connective.IMPLIES)
                    .right(p)
                    .build();

            log.info("Цель: " + formula);
            log.info("=".repeat(50));

            algorithm.prove(formula);
        };
    }
}