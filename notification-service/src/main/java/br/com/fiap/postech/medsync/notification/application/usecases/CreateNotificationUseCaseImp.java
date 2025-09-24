package br.com.fiap.postech.medsync.notification.application.usecases;

public class CreateNotificationUseCaseImp implements CreateNotificationUseCase {

    private final NotificationRepositoryGateway repository;

    @Autowired
    public CreateNotificationUseCaseImp(NotificationRepositoryGateway repository) {
        this.repository = repository;
    }

    @Override
    public Notification create(Notification notification) {
        return repository.save(notification);
    }
}

