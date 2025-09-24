package br.com.fiap.postech.medsync.notification.application.usecases;

import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;

public interface UpdateNotificationStatusUseCase {
    void updateStatus(Long notificationId, NotificationStatus status);
}

