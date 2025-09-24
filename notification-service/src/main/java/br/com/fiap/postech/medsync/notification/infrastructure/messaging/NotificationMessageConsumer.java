package br.com.fiap.postech.medsync.notification.infrastructure.messaging;

import br.com.fiap.postech.medsync.notification.application.usecases.CreateNotificationUseCase;
import br.com.fiap.postech.medsync.notification.application.usecases.SendNotificationUseCase;
import br.com.fiap.postech.medsync.notification.application.usecases.UpdateNotificationStatusUseCase;
import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageConsumer {

    @Autowired
    private CreateNotificationUseCase createNotificationUseCase;

    @Autowired
    private SendNotificationUseCase sendNotificationUseCase;

    @Autowired
    private UpdateNotificationStatusUseCase updateNotificationStatusUseCase;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void receiveMessage(NotificationMessageDTO dto) {
        var notification = createNotificationUseCase.create(dto.toDomain());
        try {
            sendNotificationUseCase.send(notification);
            updateNotificationStatusUseCase.updateStatus(notification.getId(), NotificationStatus.valueOf("SENT"));
        } catch(Exception e) {
            updateNotificationStatusUseCase.updateStatus(notification.getId(), NotificationStatus.valueOf("FAILED"));
        }
    }
}

