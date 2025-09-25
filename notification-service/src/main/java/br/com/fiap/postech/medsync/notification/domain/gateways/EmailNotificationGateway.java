package br.com.fiap.postech.medsync.notification.domain.gateways;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.infrastructure.exceptions.NotificationSendException;

public interface EmailNotificationGateway {
    void send(Notification notification) throws Exception, NotificationSendException;
}

