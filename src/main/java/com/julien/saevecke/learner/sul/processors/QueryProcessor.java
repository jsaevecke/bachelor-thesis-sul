package com.julien.saevecke.learner.sul.processors;

import com.julien.saevecke.learner.sul.config.mealymachines.coffee.Input;
import com.julien.saevecke.learner.sul.config.rabbitmq.RabbitMQConfig;
import com.julien.saevecke.learner.sul.messages.Query;

import com.julien.saevecke.learner.sul.messages.Response;
import de.learnlib.api.SUL;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class QueryProcessor {
    @Autowired
    SUL<Input, String> sul;
    @Autowired
    private AmqpTemplate template;

    @RabbitListener(queues = RabbitMQConfig.SUL_QUEUE)
    public void consume(Query query) {
        sul.pre();

        System.out.println("Message received from queue: " + query);

        var sequence = query.getSequence();
        var outputSequence = new String[sequence.length];

        int index = 0;
        for(Input input : sequence) {
            var output = sul.step(input);
            outputSequence[index] = output;
            index++;
        }

        System.out.println("output: " + Arrays.toString(outputSequence));

        template.convertAndSend(RabbitMQConfig.SUL_QUEUE, new Response(
            query,
            outputSequence
        ));
    }
}