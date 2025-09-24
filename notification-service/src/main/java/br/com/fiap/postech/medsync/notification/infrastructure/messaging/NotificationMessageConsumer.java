package br.com.fiap.postech.medsync.notification.infrastructure.messaging;

import br.com.fiap.postech.medsync.notification.application.usecases.CreateNotificationUseCase;
import br.com.fiap.postech.medsync.notification.application.usecases.SendNotificationUseCase;
import br.com.fiap.postech.medsync.notification.application.usecases.UpdateNotificationStatusUseCase;
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
        var notification = createNotificationUseCase.create(dto); // status PENDING
        try {
            sendNotificationUseCase.send(notification);
            updateNotificationStatusUseCase.update(notification.getId(), "SENT");
        } catch(Exception e) {
            updateNotificationStatusUseCase.update(notification.getId(), "FAILED");
        }
    }
}

