package br.com.fiap.postech.medsync.scheduling.infrastructure.messaging;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.HistoryEventDTO;
import br.com.fiap.postech.medsync.scheduling.infrastructure.config.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class HistoryEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public HistoryEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendHistoryEvent(HistoryEventDTO event) {
        String routingKey = switch(event.getEventType()) {
            case "APPOINTMENT_CREATED" -> RabbitMQConfig.APPOINTMENT_CREATED_KEY;
            case "APPOINTMENT_COMPLETED" -> RabbitMQConfig.APPOINTMENT_COMPLETED_KEY;
            case "APPOINTMENT_CANCELLED" -> RabbitMQConfig.APPOINTMENT_CANCELLED_KEY;
            case "MEDICAL_DATA_ADDED" -> RabbitMQConfig.MEDICAL_DATA_ADDED_KEY;
            default -> "appointment.unknown";
        };
        rabbitTemplate.convertAndSend(RabbitMQConfig.HISTORY_EXCHANGE, routingKey, event);
    }

    public void publishAppointmentCreated(AppointmentDTO appointment) {
        HistoryEventDTO event = createHistoryEvent(
                "APPOINTMENT_CREATED",
                appointment.getId(),
                createAppointmentDetail(appointment),
                RabbitMQConfig.APPOINTMENT_CREATED_KEY
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.HISTORY_EXCHANGE,
                RabbitMQConfig.APPOINTMENT_CREATED_KEY,
                event
        );
    }

    public void publishAppointmentCompleted(AppointmentDTO appointment) {
        Map<String, Object> appointmentUpdate = Map.of(
                "id", appointment.getId(),
                "status", "COMPLETED"
        );

        HistoryEventDTO event = createHistoryEvent(
                "APPOINTMENT_COMPLETED",
                appointment.getId(),
                appointmentUpdate,
                RabbitMQConfig.APPOINTMENT_COMPLETED_KEY
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.HISTORY_EXCHANGE,
                RabbitMQConfig.APPOINTMENT_COMPLETED_KEY,
                event
        );
    }

    public void publishAppointmentCancelled(AppointmentDTO appointment, String reason) {
        Map<String, Object> appointmentUpdate = Map.of(
                "id", appointment.getId(),
                "status", "CANCELLED",
                "cancellationReason", reason
        );

        HistoryEventDTO event = createHistoryEvent(
                "APPOINTMENT_CANCELLED",
                appointment.getId(),
                appointmentUpdate,
                RabbitMQConfig.APPOINTMENT_CANCELLED_KEY
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.HISTORY_EXCHANGE,
                RabbitMQConfig.APPOINTMENT_CANCELLED_KEY,
                event
        );
    }

    public void publishMedicalDataAdded(AppointmentDTO appointment) {
        Map<String, Object> clinicalData = Map.of(
                "chiefComplaint", appointment.getChiefComplaint(),
                "diagnosis", appointment.getDiagnosis(),
                "prescription", appointment.getPrescription(),
                "notes", appointment.getNotes(),
                "updatedBy", appointment.getDoctorUserId()
        );

        HistoryEventDTO event = createHistoryEvent(
                "MEDICAL_DATA_ADDED",
                appointment.getId(),
                clinicalData,
                RabbitMQConfig.MEDICAL_DATA_ADDED_KEY
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.HISTORY_EXCHANGE,
                RabbitMQConfig.MEDICAL_DATA_ADDED_KEY,
                event
        );
    }

    private HistoryEventDTO createHistoryEvent(String eventType, Long appointmentId,
                                               Object data, String routingKey) {
        return new HistoryEventDTO(
                UUID.randomUUID().toString(),
                eventType,
                LocalDateTime.now(),
                appointmentId,
                data
        );
    }

    private Map<String, Object> createAppointmentDetail(AppointmentDTO appointment) {
        return Map.of(
                "id", appointment.getId(),
                "patientUserId", appointment.getPatientUserId(),
                "doctorUserId", appointment.getDoctorUserId(),
                "appointmentDate", appointment.getAppointmentDate(),
                "status", appointment.getStatus().name(),
                "type", appointment.getType().name(),
                "durationMinutes", appointment.getDurationMinutes(),
                "notes", appointment.getNotes()
        );
    }
}
