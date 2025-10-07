package br.com.fiap.postech.medsync.scheduling.infrastructure.messaging;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.HistoryEventDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.MedicalDataRequestDTO;
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

    public void publishAppointmentCreated(AppointmentDTO appointment) {
        Map<String, Object> appointmentDetail = createAppointmentDetail(appointment);

        HistoryEventDTO event = new HistoryEventDTO(
                UUID.randomUUID().toString(),
                "APPOINTMENT_CREATED",
                LocalDateTime.now(),
                appointment.getId(),
                appointmentDetail
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

        HistoryEventDTO event = new HistoryEventDTO(
                UUID.randomUUID().toString(),
                "APPOINTMENT_COMPLETED",
                LocalDateTime.now(),
                appointment.getId(),
                appointmentUpdate
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

        HistoryEventDTO event = new HistoryEventDTO(
                UUID.randomUUID().toString(),
                "APPOINTMENT_CANCELLED",
                LocalDateTime.now(),
                appointment.getId(),
                appointmentUpdate
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.HISTORY_EXCHANGE,
                RabbitMQConfig.APPOINTMENT_CANCELLED_KEY,
                event
        );
    }

    public void publishMedicalDataAdded(Long appointmentId, MedicalDataRequestDTO medicalData) {
        Map<String, Object> clinicalData = Map.of(
                "chiefComplaint", medicalData.getChiefComplaint(),
                "diagnosis", medicalData.getDiagnosis(),
                "prescription", medicalData.getPrescription(),
                "notes", medicalData.getClinicalNotes()
        );

        HistoryEventDTO event = new HistoryEventDTO(
                UUID.randomUUID().toString(),
                "MEDICAL_DATA_ADDED",
                LocalDateTime.now(),
                appointmentId,
                clinicalData,
                medicalData.getUpdatedBy()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.HISTORY_EXCHANGE,
                RabbitMQConfig.MEDICAL_DATA_ADDED_KEY,
                event
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
