package com.julien.saevecke.learner.sul.consumer;

import com.julien.saevecke.learner.sul.config.mealymachines.coffee.Input;
import com.julien.saevecke.learner.sul.config.rabbitmq.RabbitMQConfig;
import com.julien.saevecke.learner.sul.messages.Query;

import de.learnlib.api.ObservableSUL;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryConsumer {
    @Autowired
    CompactMealy<Input, String> coffeeMachine;
    @Autowired
    ObservableSUL<Integer, Input, String> sul;

    @RabbitListener(queues = RabbitMQConfig.SUL_QUEUE)
    public void consume(Query query) {
        System.out.println("Message received from queue: " + query);

        var previousState = sul.getState();
        var output = sul.step(query.getInput());
        var currentState = sul.getState();

        System.out.println("prevState: " + previousState + " output: " + output + " curState: " + currentState);
    }
}
