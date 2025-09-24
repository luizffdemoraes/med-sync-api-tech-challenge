package br.com.fiap.postech.medsync.notification.domain.gateways;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;

public interface NotificationGateway {
    Notification save(Notification notification);
    void updateStatus(Long notificationId, NotificationStatus status);
}
