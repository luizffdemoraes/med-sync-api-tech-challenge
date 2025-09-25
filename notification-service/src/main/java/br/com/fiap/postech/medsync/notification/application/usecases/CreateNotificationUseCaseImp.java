package br.com.fiap.postech.medsync.notification.application.usecases;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.gateways.NotificationGateway;


public class CreateNotificationUseCaseImp implements CreateNotificationUseCase {

    private final NotificationGateway notificationGateway;

    public CreateNotificationUseCaseImp(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    @Override
    public Notification create(Notification notification) {
        return notificationGateway.save(notification);
    }
}

