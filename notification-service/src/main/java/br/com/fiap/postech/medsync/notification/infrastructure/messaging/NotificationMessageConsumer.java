package br.com.fiap.postech.medsync.notification.infrastructure.messaging;

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

