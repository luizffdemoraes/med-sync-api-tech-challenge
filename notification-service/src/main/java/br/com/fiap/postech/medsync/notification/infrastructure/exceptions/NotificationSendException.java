package br.com.fiap.postech.medsync.notification.infrastructure.exceptions;

public class NotificationSendException extends RuntimeException {
    public NotificationSendException(String message) {
        super(message);
    }
}