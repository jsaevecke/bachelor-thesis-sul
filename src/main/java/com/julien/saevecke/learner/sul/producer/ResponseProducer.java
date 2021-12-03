package com.julien.saevecke.learner.sul.producer;

import com.julien.saevecke.learner.sul.config.RabbitMQConfig;
import com.julien.saevecke.learner.sul.messages.Response;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/response")
public class ResponseProducer {

    @Autowired
    private AmqpTemplate template;

    @GetMapping("/{output}")
    public Response produce(@PathVariable String output) {
        Response response = new Response(output);
        template.convertAndSend(RabbitMQConfig.SUL_QUEUE, response);
        return response;
    }
}
