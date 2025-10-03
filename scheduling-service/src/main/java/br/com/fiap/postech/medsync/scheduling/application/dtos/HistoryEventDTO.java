package br.com.fiap.postech.medsync.scheduling.application.dtos;

import java.time.LocalDateTime;

public class HistoryEventDTO {
    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private Long appointmentId;
    private Object appointment;

    public HistoryEventDTO() {}

    public HistoryEventDTO(String eventId, String eventType, LocalDateTime timestamp,
                           Long appointmentId, Object appointment) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.appointmentId = appointmentId;
        this.appointment = appointment;
    }

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

    public Object getAppointment() {
        return appointment;
    }

    public void setAppointment(Object appointment) {
        this.appointment = appointment;
    }
}
