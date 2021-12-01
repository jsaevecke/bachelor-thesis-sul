package com.julien.saevecke.learner.sul.consumer;

import com.julien.saevecke.learner.sul.config.RabbitMQConfig;
import com.julien.saevecke.learner.sul.messages.Query;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


//TODO: does not work... ??
public class QueryConsumer {
    @RabbitListener(queues = RabbitMQConfig.SUL_QUEUE)
    public void consume(String query) {
        System.out.println("Message received from queue: " + query);
    }
}
