package com.julien.saevecke.learner.sul.producer;

import com.julien.saevecke.learner.sul.config.RabbitMQConfig;
import com.julien.saevecke.learner.sul.messages.Response;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/response")
public class ResponseProducer {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/{output}")
    public Response produce(@PathVariable String output) {
        Response response = new Response(output);
        template.convertAndSend(RabbitMQConfig.SUL_TOPIC_EXCHANGE, RabbitMQConfig.SUL_ROUTING_KEY, response);
        return response;
    }
}
