package br.com.fiap.postech.medsync.notification.application.usecases;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;

public interface SendNotificationUseCase {
    void send(Notification notification) throws Exception;
}

