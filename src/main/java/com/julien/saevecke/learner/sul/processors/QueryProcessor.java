package com.julien.saevecke.learner.sul.processors;

import com.julien.saevecke.learner.sul.config.mealymachines.coffee.Input;
import com.julien.saevecke.learner.sul.config.rabbitmq.RabbitMQConfig;
import com.julien.saevecke.learner.sul.messages.MembershipQuery;

import de.learnlib.api.SUL;

import net.automatalib.words.WordBuilder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryProcessor {
    @Autowired
    SUL<String, String> sul;
    @Autowired
    private AmqpTemplate template;

    @RabbitListener(queues = RabbitMQConfig.SUL_INPUT_QUEUE)
    public void consume(MembershipQuery membershipQuery) {
        sul.pre();

        System.out.println("Message received from queue: " + membershipQuery.toString());

        var query = membershipQuery.getQuery();
        var suffix = query.getSuffix(); // with output
        var prefix = query.getPrefix(); // without output

        try {
            for (var input : prefix) {
                sul.step(input);
            }

            // Suffix: Execute symbols, outputs constitute output word
            var wb = new WordBuilder<String>(suffix.size());
            for (var input : suffix) {
                wb.add(sul.step(input));
            }

            query.setOutput(wb.toWord().asList());

            var response = new MembershipQuery(membershipQuery.getUuid(), membershipQuery.getQuery());

            System.out.println("UUID: " + response.getUuid() + " output: " + wb.toWord());

            template.convertAndSend(
                    RabbitMQConfig.SUL_DIRECT_EXCHANGE,
                    RabbitMQConfig.SUL_OUTPUT_ROUTING_KEY,
                    response
            );

            System.out.println("Message send: " + response);
        } finally {
            sul.post();
        }
    }
}
