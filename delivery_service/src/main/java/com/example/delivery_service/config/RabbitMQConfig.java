//package com.example.delivery_service.configuration;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//
//    @Bean
//    public Queue myQueue() {
//        return new Queue("order.queue", false);
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange("order.exchange");
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("routing.key");
//    }
//}
