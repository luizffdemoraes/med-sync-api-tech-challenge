package br.com.fiap.postech.medsync.notification.application.usecases;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.gateways.NotificationGateway;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateNotificationUseCaseImp implements CreateNotificationUseCase {

    private final NotificationGateway notificationGateway;

    @Autowired
    public CreateNotificationUseCaseImp(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    @Override
    public Notification create(Notification notification) {
        return notificationGateway.save(notification);
    }
}

