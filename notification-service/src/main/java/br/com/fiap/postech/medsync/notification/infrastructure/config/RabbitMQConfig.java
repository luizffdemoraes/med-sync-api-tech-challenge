package br.com.fiap.postech.medsync.notification.infrastructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue}")
    private String queueName;

    @Bean
    public Queue notificationQueue() {
        // Cria a fila para consumir as mensagens
        return new Queue(queueName, true);
    }
}