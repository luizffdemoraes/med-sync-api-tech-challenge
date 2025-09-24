package br.com.fiap.postech.medsync.notification.application.usecases;

public class UpdateNotificationStatusUseCaseImp implements UpdateNotificationStatusUseCase {

    private final NotificationRepositoryGateway repository;

    @Autowired
    public UpdateNotificationStatusUseCaseImp(NotificationRepositoryGateway repository) {
        this.repository = repository;
    }

    @Override
    public void updateStatus(Long notificationId, NotificationStatus status) {
        repository.updateStatus(notificationId, status);
    }
}

