package br.com.fiap.postech.medsync.notification.application.usecases;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.gateways.EmailNotificationGateway;

public class SendNotificationUseCaseImp implements SendNotificationUseCase {

    private final EmailNotificationGateway emailNotificationGateway;

    public SendNotificationUseCaseImp(EmailNotificationGateway emailNotificationGateway) {
        this.emailNotificationGateway = emailNotificationGateway;
    }

    @Override
    public void send(Notification notification) throws Exception {
        emailNotificationGateway.send(notification);
    }
}

