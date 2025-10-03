package br.com.fiap.postech.medsync.scheduling.infrastructure.config.rabbitmq;

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

    // Configuração para NOTIFICATION
    @Value("${app.rabbitmq.queue.notification}")
    private String notificationQueueName;

    // Configuração para HISTORY (com exchange e routing keys)
    @Value("${app.rabbitmq.queue.history}")
    private String historyQueueName;

    public static final String HISTORY_EXCHANGE = "appointment.exchange";

    // Routing keys para history (compatível com o history-service)
    public static final String APPOINTMENT_CREATED_KEY = "appointment.created";
    public static final String APPOINTMENT_COMPLETED_KEY = "appointment.completed";
    public static final String APPOINTMENT_CANCELLED_KEY = "appointment.cancelled";
    public static final String MEDICAL_DATA_ADDED_KEY = "appointment.medical.updated";

    // 1. EXCHANGE para History (Topic Exchange)
    @Bean
    public TopicExchange historyExchange() {
        return new TopicExchange(HISTORY_EXCHANGE);
    }

    // 2. QUEUE para Notification (Direct)
    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueueName, true); // durable
    }

    // 3. QUEUE para History
    @Bean
    public Queue historyQueue() {
        return QueueBuilder.durable(historyQueueName).build();
    }

    // 4. BINDINGS para History (compatível com history-service)
    @Bean
    public Binding appointmentCreatedBinding() {
        return BindingBuilder.bind(historyQueue())
                .to(historyExchange())
                .with(APPOINTMENT_CREATED_KEY);
    }

    @Bean
    public Binding appointmentCompletedBinding() {
        return BindingBuilder.bind(historyQueue())
                .to(historyExchange())
                .with(APPOINTMENT_COMPLETED_KEY);
    }

    @Bean
    public Binding appointmentCancelledBinding() {
        return BindingBuilder.bind(historyQueue())
                .to(historyExchange())
                .with(APPOINTMENT_CANCELLED_KEY);
    }

    @Bean
    public Binding medicalDataAddedBinding() {
        return BindingBuilder.bind(historyQueue())
                .to(historyExchange())
                .with(MEDICAL_DATA_ADDED_KEY);
    }

    // 5. Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 6. Rabbit Template
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}