package br.com.fiap.postech.medsync.scheduling.infrastructure.messaging;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.NotificationMessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// infrastructure/messaging/producers/NotificationEventProducer.java
@Component
public class NotificationEventProducer {

    @Value("${app.rabbitmq.queue.notification}")
    private String notificationQueue;

    private final RabbitTemplate rabbitTemplate;

    public NotificationEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAppointmentEvent(AppointmentDTO appointment, String eventType) {
        String message = String.format("Olá! Você tem uma consulta agendada para %s",
                appointment.getAppointmentDate());

        NotificationMessageDTO notification = new NotificationMessageDTO(
                appointment.getPatientUserId(),
                appointment.getPatientEmail(),
                appointment.getAppointmentDate(),
                eventType, // CREATED, UPDATED, CANCELLED
                message
        );

        // Envia DIRECT para a queue de notification
        rabbitTemplate.convertAndSend(notificationQueue, notification);
    }
}