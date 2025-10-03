package br.com.fiap.postech.medsync.scheduling.domain.entities;


import br.com.fiap.postech.medsync.scheduling.domain.enums.HistoryEventType;
import br.com.fiap.postech.medsync.scheduling.domain.enums.NotificationEventType;

import java.time.LocalDateTime;
import java.util.UUID;

public class QueueEvent {
    private UUID id;
    private UUID appointmentId;
    private HistoryEventType historyEventType;
    private NotificationEventType notificationEventType;
    private String payload;
    private LocalDateTime createdAt;
    private String source;
    private boolean processed;
    private String routingKey;

    // Construtor padrão
    public QueueEvent() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.processed = false;
    }

    // Construtor para eventos de histórico
    public QueueEvent(UUID appointmentId, HistoryEventType historyEventType, String payload, String source) {
        this();
        this.appointmentId = appointmentId;
        this.historyEventType = historyEventType;
        this.payload = payload;
        this.source = source;
        this.routingKey = generateHistoryRoutingKey(historyEventType);
    }

    // Construtor para eventos de notificação
    public QueueEvent(UUID appointmentId, NotificationEventType notificationEventType, String payload, String source) {
        this();
        this.appointmentId = appointmentId;
        this.notificationEventType = notificationEventType;
        this.payload = payload;
        this.source = source;
        this.routingKey = generateNotificationRoutingKey(notificationEventType);
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    public HistoryEventType getHistoryEventType() {
        return historyEventType;
    }

    public void setHistoryEventType(HistoryEventType historyEventType) {
        this.historyEventType = historyEventType;
    }

    public NotificationEventType getNotificationEventType() {
        return notificationEventType;
    }

    public void setNotificationEventType(NotificationEventType notificationEventType) {
        this.notificationEventType = notificationEventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    // Métodos de negócio
    public void markAsProcessed() {
        this.processed = true;
    }

    public boolean isHistoryEvent() {
        return this.historyEventType != null;
    }

    public boolean isNotificationEvent() {
        return this.notificationEventType != null;
    }

    // Geração de routing keys baseada nos exemplos
    private String generateHistoryRoutingKey(HistoryEventType eventType) {
        switch (eventType) {
            case APPOINTMENT_CREATED:
                return "appointment.created";
            case APPOINTMENT_COMPLETED:
                return "appointment.completed";
            case MEDICAL_DATA_ADDED:
                return "appointment.medical.updated";
            case APPOINTMENT_CANCELLED:
                return "appointment.cancelled";
            case APPOINTMENT_UPDATED:
                return "appointment.updated";
            default:
                return "appointment.general";
        }
    }

    private String generateNotificationRoutingKey(NotificationEventType eventType) {
        switch (eventType) {
            case CREATED:
                return "notification.appointment.created";
            case UPDATED:
                return "notification.appointment.updated";
            case CANCELLED:
                return "notification.appointment.cancelled";
            default:
                return "notification.appointment.general";
        }
    }

    @Override
    public String toString() {
        return "QueueEvent{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                ", historyEventType=" + historyEventType +
                ", notificationEventType=" + notificationEventType +
                ", routingKey=" + routingKey +
                ", createdAt=" + createdAt +
                ", processed=" + processed +
                '}';
    }
}