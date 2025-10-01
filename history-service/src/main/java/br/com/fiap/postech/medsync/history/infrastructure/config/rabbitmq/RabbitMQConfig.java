package br.com.fiap.postech.medsync.history.infrastructure.config.rabbitmq;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue}")
    private String appointmentQueueName;
    public static final String APPOINTMENT_EXCHANGE = "appointment.exchange";

    // Routing keys específicas que queremos
    public static final String APPOINTMENT_CREATED_KEY = "appointment.created";
    public static final String APPOINTMENT_COMPLETED_KEY = "appointment.completed";
    public static final String APPOINTMENT_CANCELLED_KEY = "appointment.cancelled";
    public static final String MEDICAL_DATA_ADDED_KEY = "appointment.medical.updated";

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(APPOINTMENT_EXCHANGE);
    }

    @Bean
    public Queue appointmentQueue() {
        return QueueBuilder.durable(appointmentQueueName).build();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Permite comentários no JSON
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    // Bindings específicos para cada evento que queremos consumir
    @Bean
    public Binding appointmentCreatedBinding(Queue appointmentQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentQueue)
                .to(appointmentExchange)
                .with(APPOINTMENT_CREATED_KEY);
    }

    @Bean
    public Binding appointmentCompletedBinding(Queue appointmentQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentQueue)
                .to(appointmentExchange)
                .with(APPOINTMENT_COMPLETED_KEY);
    }

    @Bean
    public Binding appointmentCancelledBinding(Queue appointmentQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentQueue)
                .to(appointmentExchange)
                .with(APPOINTMENT_CANCELLED_KEY);
    }

    @Bean
    public Binding medicalDataAddedBinding(Queue appointmentQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentQueue)
                .to(appointmentExchange)
                .with(MEDICAL_DATA_ADDED_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}