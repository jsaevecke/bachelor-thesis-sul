package com.julien.saevecke.learner.sul.config.mealymachines.coffee;

import de.learnlib.api.ObservableSUL;
import de.learnlib.api.SUL;
import de.learnlib.driver.util.MealySimulatorSUL;
import de.learnlib.driver.util.ObservableMealySimulatorSUL;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.util.automata.builders.AutomatonBuilders;
import net.automatalib.words.impl.Alphabets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Machine {
    public static final String OUT_OK = "ok";
    public static final String OUT_ERROR = "error";
    public static final String OUT_COFFEE = "coffee!";

    @Bean
    public SUL<Input, String> sul() {
        return new MealySimulatorSUL<>(coffeeMachine());
    }

    private CompactMealy<Input, String> coffeeMachine() {
        var alphabet = Alphabets.fromEnum(Input.class);
        var automaton = new CompactMealy<Input, String>(alphabet);

        // TODO: put states and output in separate classes
        // @formatter:off
        return AutomatonBuilders.forMealy(automaton)
                .withInitial("a")
                .from("a")
                .on(Input.WATER).withOutput(OUT_OK).to("c")
                .on(Input.POD).withOutput(OUT_OK).to("b")
                .on(Input.BUTTON).withOutput(OUT_ERROR).to("f")
                .on(Input.CLEAN).withOutput(OUT_OK).loop()
                .from("b")
                .on(Input.WATER).withOutput(OUT_OK).to("d")
                .on(Input.POD).withOutput(OUT_OK).loop()
                .on(Input.BUTTON).withOutput(OUT_ERROR).to("f")
                .on(Input.CLEAN).withOutput(OUT_OK).to("a")
                .from("c")
                .on(Input.WATER).withOutput(OUT_OK).loop()
                .on(Input.POD).withOutput(OUT_OK).to("d")
                .on(Input.BUTTON).withOutput(OUT_ERROR).to("f")
                .on(Input.CLEAN).withOutput(OUT_OK).to("a")
                .from("d")
                .on(Input.WATER, Input.POD).withOutput(OUT_OK).loop()
                .on(Input.BUTTON).withOutput(OUT_COFFEE).to("e")
                .on(Input.CLEAN).withOutput(OUT_OK).to("a")
                .from("e")
                .on(Input.WATER, Input.POD, Input.BUTTON).withOutput(OUT_ERROR).to("f")
                .on(Input.CLEAN).withOutput(OUT_OK).to("a")
                .from("f")
                .on(Input.WATER, Input.POD, Input.BUTTON, Input.CLEAN).withOutput(OUT_ERROR).loop()
                .create();
        // @formatter:on
    }
}
