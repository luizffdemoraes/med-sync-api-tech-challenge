package br.com.fiap.postech.medsync.scheduling.domain.entities;


import java.time.LocalDateTime;

public class QueueEvent {
    private Long id;
    private String eventType; //  "HISTORY" ou "NOTIFICATION"
    private String queueName;
    private String routingKey;
    private String messageBody; // JSON como string
    private String status; //  "PENDING", "SENT", "FAILED"
    private Integer retryCount;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private String errorMessage;

    public QueueEvent() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
        this.retryCount = 0;
    }

    // Construtor para eventos
    public QueueEvent(String eventType, String queueName, String routingKey, String messageBody) {
        this();
        this.eventType = eventType;
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.messageBody = messageBody;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getQueueName() { return queueName; }
    public void setQueueName(String queueName) { this.queueName = queueName; }

    public String getRoutingKey() { return routingKey; }
    public void setRoutingKey(String routingKey) { this.routingKey = routingKey; }

    public String getMessageBody() { return messageBody; }
    public void setMessageBody(String messageBody) { this.messageBody = messageBody; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    // Métodos de negócio
    public void markAsSent() {
        this.status = "SENT";
        this.sentAt = LocalDateTime.now();
    }

    public void markAsFailed(String error) {
        this.status = "FAILED";
        this.errorMessage = error;
        this.retryCount++;
    }

    public boolean canRetry() {
        return "FAILED".equals(this.status) && this.retryCount < 3;
    }
}