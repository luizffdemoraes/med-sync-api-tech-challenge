package br.com.fiap.postech.medsync.notification.infrastructure.dependency;

import br.com.fiap.postech.medsync.notification.application.usecases.*;
import br.com.fiap.postech.medsync.notification.domain.gateways.EmailNotificationGateway;
import br.com.fiap.postech.medsync.notification.domain.gateways.NotificationGateway;
import br.com.fiap.postech.medsync.notification.infrastructure.gateways.EmailNotificationGatewayImpl;
import br.com.fiap.postech.medsync.notification.infrastructure.gateways.NotificationGatewayImp;
import br.com.fiap.postech.medsync.notification.infrastructure.messaging.NotificationMessageConsumer;
import br.com.fiap.postech.medsync.notification.infrastructure.persistence.repository.NotificationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DependencyInjectionConfig {
    // Use Cases

    @Bean
    public CreateNotificationUseCase createNotificationUseCase(NotificationGateway notificationGateway) {
        return new CreateNotificationUseCaseImp(notificationGateway);
    }

    @Bean
    public SendNotificationUseCase sendNotificationUseCase(EmailNotificationGateway emailNotificationGateway) {
        return new SendNotificationUseCaseImp(emailNotificationGateway);
    }

    @Bean
    public UpdateNotificationStatusUseCase updateNotificationStatusUseCase(NotificationGateway notificationGateway) {
        return new UpdateNotificationStatusUseCaseImp(notificationGateway);
    }

    // Gateways

    @Bean
    public NotificationGateway notificationGateway(NotificationRepository repository) {
        return new NotificationGatewayImp(repository);
    }

    @Bean
    public EmailNotificationGateway emailNotificationGateway() {
        return new EmailNotificationGatewayImpl();
    }

    // Consumer

    @Bean
    public NotificationMessageConsumer notificationMessageConsumer(CreateNotificationUseCase createNotificationUseCase,
                                                                   SendNotificationUseCase sendNotificationUseCase,
                                                                   UpdateNotificationStatusUseCase updateNotificationStatusUseCase) {
        return new NotificationMessageConsumer(createNotificationUseCase, sendNotificationUseCase, updateNotificationStatusUseCase);
    }


}
