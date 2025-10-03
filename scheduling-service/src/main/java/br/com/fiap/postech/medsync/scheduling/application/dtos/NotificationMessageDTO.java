package br.com.fiap.postech.medsync.scheduling.application.dtos;

import java.time.LocalDateTime;

public class NotificationMessageDTO {
    private Long patientId;
    private String patientEmail;
    private LocalDateTime appointmentDate;
    private String eventType;
    private String message;

    public NotificationMessageDTO() {}

    public NotificationMessageDTO(Long patientId, String patientEmail, LocalDateTime appointmentDate,
                                  String eventType, String message) {
        this.patientId = patientId;
        this.patientEmail = patientEmail;
        this.appointmentDate = appointmentDate;
        this.eventType = eventType;
        this.message = message;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}