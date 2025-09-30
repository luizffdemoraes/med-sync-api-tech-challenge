package br.com.fiap.postech.medsync.history.infrastructure.messaging;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCancelledEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCompletedEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCreatedEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.MedicalDataAddedEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Map;

public class AppointmentMessageDTO {
    private String eventId;
    private String eventType;
    private String timestamp;
    private Map<String, Object> appointment;
    private Long appointmentId;
    private Map<String, Object> clinicalData;
    private Long updatedBy;

    public AppointmentMessageDTO() {}

    @JsonCreator
    public AppointmentMessageDTO(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("timestamp") String timestamp,
            @JsonProperty("appointment") Map<String, Object> appointment,
            @JsonProperty("appointmentId") Long appointmentId,
            @JsonProperty("clinicalData") Map<String, Object> clinicalData,
            @JsonProperty("updatedBy") Long updatedBy) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.appointment = appointment;
        this.appointmentId = appointmentId;
        this.clinicalData = clinicalData;
        this.updatedBy = updatedBy;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public String getTimestamp() { return timestamp; }
    public Map<String, Object> getAppointment() { return appointment; }
    public Long getAppointmentId() { return appointmentId; }
    public Map<String, Object> getClinicalData() { return clinicalData; }
    public Long getUpdatedBy() { return updatedBy; }

    // Métodos auxiliares para conversão para DTOs de Messaging (toAppointmentCreatedEvent etc) podem ser adicionados aqui
    public AppointmentCreatedEvent toAppointmentCreatedEvent() {
        if (appointment == null) return null;
        return new AppointmentCreatedEvent(
                eventId, eventType, timestamp,
                new AppointmentCreatedEvent.Appointment(
                        ((Number) appointment.get("id")).longValue(),
                        ((Number) appointment.get("patientUserId")).longValue(),
                        ((Number) appointment.get("doctorUserId")).longValue(),
                        LocalDateTime.parse((String) appointment.get("appointmentDate")),
                        (String) appointment.get("status"),
                        (String) appointment.get("type"),
                        appointment.get("durationMinutes") != null ? ((Number) appointment.get("durationMinutes")).intValue() : null,
                        (String) appointment.get("notes")
                )
        );
    }

    public AppointmentCompletedEvent toAppointmentCompletedEvent() {
        if (appointment == null) return null;
        return new AppointmentCompletedEvent(
                eventId, eventType, timestamp,
                new AppointmentCompletedEvent.Appointment(
                        ((Number) appointment.get("id")).longValue(),
                        (String) appointment.get("status")
                )
        );
    }

    public MedicalDataAddedEvent toMedicalDataAddedEvent() {
        if (clinicalData == null) return null;
        return new MedicalDataAddedEvent(
                eventId, eventType, timestamp, appointmentId,
                new MedicalDataAddedEvent.ClinicalData(
                        (String) clinicalData.get("chiefComplaint"),
                        (String) clinicalData.get("diagnosis"),
                        (String) clinicalData.get("prescription"),
                        (String) clinicalData.get("notes")
                ),
                updatedBy
        );
    }

    public AppointmentCancelledEvent toAppointmentCancelledEvent() {
        if (appointment == null) return null;
        return new AppointmentCancelledEvent(
                eventId, eventType, timestamp,
                new AppointmentCancelledEvent.Appointment(
                        ((Number) appointment.get("id")).longValue(),
                        (String) appointment.get("status"),
                        (String) appointment.get("cancellationReason")
                )
        );
    }
}