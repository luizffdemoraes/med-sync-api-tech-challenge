package br.com.fiap.postech.medsync.scheduling.infrastructure.messaging;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.NotificationMessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventProducer {

    @Value("${app.rabbitmq.queue.notification}")
    private String notificationQueue;

    private final RabbitTemplate rabbitTemplate;

    public NotificationEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAppointmentEvent(AppointmentDTO appointment, String eventType) {
        String message = buildMessage(eventType);

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


    private String buildMessage(String eventType) {
        return switch (eventType) {
            case "CREATED" -> "Sua consulta foi agendada para %s. Chegue 15 minutos antes.";
            case "UPDATED" -> "Sua consulta foi reagendada para %s. Confirme sua disponibilidade.";
            case "CANCELLED" -> "Sua consulta para %s foi cancelada. Entre em contato para mais informações.";
            default -> "Atualização sobre sua consulta: %s";
        };
    }
}