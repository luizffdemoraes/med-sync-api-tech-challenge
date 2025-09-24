package br.com.fiap.postech.medsync.notification.application.usecases;

public class SendNotificationUseCaseImp implements SendNotificationUseCase {

    private final NotificationSenderGateway senderGateway;

    @Autowired
    public SendNotificationUseCaseImp(NotificationSenderGateway senderGateway) {
        this.senderGateway = senderGateway;
    }

    @Override
    public void send(Notification notification) {
        senderGateway.send(notification);
    }
}

