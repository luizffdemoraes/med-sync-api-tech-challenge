package br.com.fiap.postech.medsync.notification.domain.entities;

import java.time.LocalDateTime;

public class Notification {
    private Long id;
    private Long patientId;
    private String patientEmail;
    private LocalDateTime appointmentDate;
    private String eventType;
    private String status;
    private String message;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;

    public static Notification create(Long patientId, String patientEmail,
                                      LocalDateTime appointmentDate, String eventType,
                                      String message) {
        Notification notification = new Notification();
        notification.patientId = patientId;
        notification.patientEmail = patientEmail;
        notification.appointmentDate = appointmentDate;
        notification.eventType = eventType;
        notification.status = "PENDING";
        notification.message = message;
        notification.createdAt = LocalDateTime.now();
        return notification;
    }

    public void markAsSent() {
        this.status = "SENT";
        this.sentAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = "FAILED";
        this.sentAt = LocalDateTime.now();
    }

    // Apenas getters
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getPatientEmail() { return patientEmail; }
    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public String getEventType() { return eventType; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getSentAt() { return sentAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
