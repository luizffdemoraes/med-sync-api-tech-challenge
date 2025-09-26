package br.com.fiap.postech.medsync.notification.domain.entities;

import java.time.LocalDateTime;

public class Notification {
    private Long id;
    private Long patientId;
    private String patientEmail;
    private LocalDateTime appointmentDate;
    private String eventType;
    private NotificationStatus status;
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
        notification.status = NotificationStatus.PENDING;
        notification.message = message;
        notification.createdAt = LocalDateTime.now();
        return notification;
    }

    // Apenas getters
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getPatientEmail() { return patientEmail; }
    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public String getEventType() { return eventType; }
    public NotificationStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getSentAt() { return sentAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static Notification of(Long id, Long patientId, String patientEmail,
                                  LocalDateTime appointmentDate, String eventType,
                                  NotificationStatus status, String message,
                                  LocalDateTime sentAt, LocalDateTime createdAt) {
        Notification notification = new Notification();
        notification.id = id;
        notification.patientId = patientId;
        notification.patientEmail = patientEmail;
        notification.appointmentDate = appointmentDate;
        notification.eventType = eventType;
        notification.status = status;
        notification.message = message;
        notification.sentAt = sentAt;
        notification.createdAt = createdAt;
        return notification;
    }
}
