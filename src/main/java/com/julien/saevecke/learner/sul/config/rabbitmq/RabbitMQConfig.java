package com.julien.saevecke.learner.sul.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO: multiple queues? pros and cons? when to think about a new queue?
//TODO: what kind of exchange do I need?
//NOTE: best practices for high performance: https://www.cloudamqp.com/blog/part2-rabbitmq-best-practice-for-high-performance.html#:~:text=Use%20multiple%20queues%20and%20consumers&text=You%20will%20achieve%20better%20throughput,every%20queue%20in%20the%20cluster.
//NOTE: naming conventions: https://eng.revinate.com/2015/12/01/rabbitmq-naming-conventions.html
//NOTE: tutorial https://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html

@Configuration
public class RabbitMQConfig {
    public static final String SUL_INPUT_ROUTING_KEY = "sul.input";
    public static final String SUL_OUTPUT_ROUTING_KEY = "sul.output";
    public static final String SUL_DIRECT_EXCHANGE = "sul_dx";
    public static final String SUL_INPUT_QUEUE = "sul_input_q";
    public static final String SUL_OUTPUT_QUEUE = "sul_output_q";

    @Bean
    public Queue sulInputQueue() {
        return new Queue(SUL_INPUT_QUEUE);
    }
    @Bean
    public Queue sulOutputQueue() { return new Queue(SUL_OUTPUT_QUEUE); }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(SUL_DIRECT_EXCHANGE);
    }

    @Bean
    public Binding bindingSULQueue(DirectExchange exchange) {
        return BindingBuilder.bind(sulInputQueue()).to(exchange).with(SUL_INPUT_ROUTING_KEY);
    }

    @Bean
    public Binding bindingMOQueue(DirectExchange exchange) {
        return BindingBuilder.bind(sulOutputQueue()).to(exchange).with(SUL_OUTPUT_ROUTING_KEY);
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
