package com.julien.saevecke.learner.sul.processors;

import com.julien.saevecke.learner.sul.config.rabbitmq.RabbitMQConfig;
import com.julien.saevecke.learner.sul.messages.MembershipQuery;

import de.learnlib.api.SUL;

import net.automatalib.words.WordBuilder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class QueryProcessor {
    @Autowired
    SUL<String, String> sul;
    @Autowired
    private AmqpTemplate template;

    @Value("${MY_POD_NAME}")
    private String hostname;

    @RabbitListener(queues = RabbitMQConfig.SUL_INPUT_QUEUE)
    public void consume(MembershipQuery membershipQuery) {
        sul.pre();

        System.out.println(hostname + " received message with uuid: " + membershipQuery.getUuid());

        var query = membershipQuery.getQuery();
        var suffix = query.getSuffix(); // with output
        var prefix = query.getPrefix(); // without output

        // artificial delay for testing purposes - kubernetes autoscaling
        var delayInSeconds = membershipQuery.getDelayInSeconds();
        if(delayInSeconds >= 1) {
            try {
                TimeUnit.SECONDS.sleep(delayInSeconds);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

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

            var response = new MembershipQuery(membershipQuery.getUuid(), hostname, membershipQuery.getDelayInSeconds(), membershipQuery.getQuery());

            template.convertAndSend(
                    RabbitMQConfig.SUL_DIRECT_EXCHANGE,
                    RabbitMQConfig.SUL_OUTPUT_ROUTING_KEY,
                    response
            );
        } finally {
            sul.post();
        }

        System.exit(0);
    }
}
