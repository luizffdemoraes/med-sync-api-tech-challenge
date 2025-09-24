package br.com.fiap.postech.medsync.notification.application.usecases;

public interface UpdateNotificationStatusUseCase {
    void updateStatus(Long notificationId, NotificationStatus status);
}

