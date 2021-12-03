package com.julien.saevecke.learner.sul.consumer;

import com.julien.saevecke.learner.sul.config.RabbitMQConfig;
import com.julien.saevecke.learner.sul.messages.Query;
import com.julien.saevecke.learner.sul.messages.Response;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class QueryConsumer {
    @RabbitListener(queues = RabbitMQConfig.SUL_QUEUE)
    public void consume(Response query) {
        System.out.println("Message received from queue: " + query);
    }
}
