package br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.entity;

import br.com.fiap.postech.medsync.scheduling.domain.entities.QueueEvent;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue_events", schema = "scheduling")
public class QueueEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "queue_name", nullable = false)
    private String queueName;

    @Column(name = "routing_key", nullable = false)
    private String routingKey;

    @Column(name = "message_body", columnDefinition = "jsonb", nullable = false)
    private String messageBody;

    @Column(name = "status")
    private String status;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "error_message")
    private String errorMessage;

    public QueueEventEntity() {}

    public QueueEventEntity(String eventType, String queueName, String routingKey, String messageBody) {
        this.eventType = eventType;
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.messageBody = messageBody;
        this.createdAt = LocalDateTime.now();
    }

    public static QueueEventEntity toEntity(QueueEvent queueEvent) {
        QueueEventEntity entity = new QueueEventEntity();
        entity.setEventType(queueEvent.getEventType());
        entity.setQueueName(queueEvent.getSource());
        entity.setRoutingKey(queueEvent.getRoutingKey());
        entity.setMessageBody(queueEvent.getPayload());
        entity.setStatus(queueEvent.getStatus());
        entity.setErrorMessage(queueEvent.getError());
        entity.setCreatedAt(queueEvent.getCreatedAt());
        return entity;
    }

    public QueueEvent toDomain() {
        QueueEvent queueEvent = new QueueEvent();
        queueEvent.setEventType(this.getEventType());
        queueEvent.setEventSubType(this.getRoutingKey());
        queueEvent.setPayload(this.getMessageBody());
        queueEvent.setStatus(this.getStatus());
        queueEvent.setError(this.getErrorMessage());
        queueEvent.setCreatedAt(this.getCreatedAt());
        queueEvent.setRoutingKey(this.getRoutingKey());
        queueEvent.setSource(this.getQueueName());
        return queueEvent;
    }

    // Getters e setters
    // (gere com sua IDE ou peça exemplos específicos se quiser)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}