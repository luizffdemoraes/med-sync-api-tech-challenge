package br.com.fiap.postech.medsync.notification.infrastructure.messaging;

import br.com.fiap.postech.medsync.notification.application.usecases.CreateNotificationUseCase;
import br.com.fiap.postech.medsync.notification.application.usecases.SendNotificationUseCase;
import br.com.fiap.postech.medsync.notification.application.usecases.UpdateNotificationStatusUseCase;
import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;


@Component
public class NotificationMessageConsumer {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final SendNotificationUseCase sendNotificationUseCase;
    private final UpdateNotificationStatusUseCase updateNotificationStatusUseCase;
    private static final Logger logger = LoggerFactory.getLogger(NotificationMessageConsumer.class);

    public NotificationMessageConsumer(CreateNotificationUseCase createNotificationUseCase,
                                       SendNotificationUseCase sendNotificationUseCase,
                                       UpdateNotificationStatusUseCase updateNotificationStatusUseCase) {
        this.createNotificationUseCase = createNotificationUseCase;
        this.sendNotificationUseCase = sendNotificationUseCase;
        this.updateNotificationStatusUseCase = updateNotificationStatusUseCase;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void receiveMessage(NotificationMessageDTO dto) {
        logger.info("Mensagem recebida do RabbitMQ: {}", dto.toString());

        var notification = createNotificationUseCase.create(dto.toDomain());
        logger.info("Notificação criada no banco com ID: {}", notification.getId());

        try {
            logger.info("Tentando enviar notificação para: {}", notification.getPatientEmail());
            sendNotificationUseCase.send(notification);
            logger.info("Notificação enviada com sucesso");

            updateNotificationStatusUseCase.updateStatus(notification.getId(), NotificationStatus.SENT);
            logger.info("Status atualizado para SENT");

        } catch(Exception e) {
            logger.error("ERRO ao enviar notificação: {}", e.getMessage(), e);
            updateNotificationStatusUseCase.updateStatus(notification.getId(), NotificationStatus.FAILED);
            logger.info("Status atualizado para FAILED");
        }
    }
}

