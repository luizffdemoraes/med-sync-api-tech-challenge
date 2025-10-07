package br.com.fiap.postech.medsync.scheduling.application.dtos;


import java.time.LocalDateTime;
import java.util.Map;

public class HistoryEventDTO {
    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private Map<String, Object> appointment;
    private Long appointmentId;
    private Map<String, Object> clinicalData;
    private Long updatedBy;

    public HistoryEventDTO() {}

    // Construtor para APPOINTMENT_CREATED, APPOINTMENT_COMPLETED, APPOINTMENT_CANCELLED
    public HistoryEventDTO(String eventId, String eventType, LocalDateTime timestamp,
                            Map<String, Object> appointment, Long appointmentId) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.appointment = appointment;
        this.appointmentId = appointmentId;
    }

    // Construtor específico para MEDICAL_DATA_ADDED
    public HistoryEventDTO(String eventId, String eventType, LocalDateTime timestamp,
                           Long appointmentId, Map<String, Object> clinicalData, Long updatedBy) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.appointmentId = appointmentId;
        this.clinicalData = clinicalData;
        this.updatedBy = updatedBy;
    }

    // Getters e Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Map<String, Object> getAppointment() {
        return appointment;
    }

    public void setAppointment(Map<String, Object> appointment) {
        this.appointment = appointment;
    }

    public Map<String, Object> getClinicalData() {
        return clinicalData;
    }

    public void setClinicalData(Map<String, Object> clinicalData) {
        this.clinicalData = clinicalData;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}