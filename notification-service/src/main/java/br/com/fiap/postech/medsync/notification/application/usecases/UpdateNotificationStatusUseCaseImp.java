package br.com.fiap.postech.medsync.notification.application.usecases;

import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;
import br.com.fiap.postech.medsync.notification.domain.gateways.NotificationGateway;

public class UpdateNotificationStatusUseCaseImp implements UpdateNotificationStatusUseCase {

    private final NotificationGateway notificationGateway;

    public UpdateNotificationStatusUseCaseImp(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    @Override
    public void updateStatus(Long notificationId, NotificationStatus status) {
        notificationGateway.updateStatus(notificationId, status);
    }
}

