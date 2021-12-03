package com.julien.saevecke.learner.sul.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO: multiple queues? pros and cons? when to think about a new queue?
//TODO: what kind of exchange do I need?

@Configuration
public class RabbitMQConfig {
    public static final String SUL_ROUTING_KEY = "SULRoutingKey";
    public static final String SUL_TOPIC_EXCHANGE = "SULTopicExchange";
    public static final String SUL_QUEUE = "SULQueue";

    @Bean
    public Queue queue() {
        return new Queue(SUL_QUEUE);
    }

    //TODO: durable? autodelete? arguments?
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(SUL_TOPIC_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SUL_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
