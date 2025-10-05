package br.com.fiap.postech.medsync.scheduling.infrastructure.config.dependency;

import br.com.fiap.postech.medsync.scheduling.application.usecases.*;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.QueueEventGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.controllers.AppointmentController;
import br.com.fiap.postech.medsync.scheduling.infrastructure.gateways.AppointmentGatewayImpl;
import br.com.fiap.postech.medsync.scheduling.infrastructure.gateways.QueueEventGatewayImpl;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.repository.AppointmentRepository;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.repository.QueueEventRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DependencyInjectionConfig {

    // Controller
    @Bean
    public AppointmentController appointmentController(
            CreateAppointmentUseCase createAppointmentUseCase,
            AddMedicalDataUseCase addMedicalDataUseCase,
            CompleteAppointmentUseCase completeAppointmentUseCase,
            GetAppointmentUseCase getAppointmentUseCase,
            ListAppointmentsUseCase listAppointmentsUseCase,
            CancelAppointmentUseCase cancelAppointmentUseCase
    ) {
        return new AppointmentController(
                createAppointmentUseCase,
                addMedicalDataUseCase,
                completeAppointmentUseCase,
                getAppointmentUseCase,
                listAppointmentsUseCase,
                cancelAppointmentUseCase
        );
    }

    // Gateways
    @Bean
    public AppointmentGateway appointmentGateway(AppointmentRepository repository) {
        return new AppointmentGatewayImpl(repository);
    }

    @Bean
    public QueueEventGateway queueEventGateway(
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer,
            QueueEventRepository queueEventRepository
    ) {
        return new QueueEventGatewayImpl(historyEventProducer, notificationEventProducer, queueEventRepository);
    }

    // USE CASES
    @Bean
    public CreateAppointmentUseCase createAppointmentUseCase(
            AppointmentGateway appointmentGateway,
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer
    ) {
        return new CreateAppointmentUseCaseImp(appointmentGateway, historyEventProducer, notificationEventProducer);
    }

    @Bean
    public AddMedicalDataUseCase addMedicalDataUseCase(
            AppointmentGateway appointmentGateway,
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer
    ) {
        return new AddMedicalDataUseCaseImp(appointmentGateway, historyEventProducer, notificationEventProducer);
    }

    @Bean
    public CompleteAppointmentUseCase completeAppointmentUseCase(
            AppointmentGateway appointmentGateway,
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer
    ) {
        return new CompleteAppointmentUseCaseImp(appointmentGateway, historyEventProducer, notificationEventProducer);
    }

    @Bean
    public CancelAppointmentUseCase cancelAppointmentUseCase(
            AppointmentGateway appointmentGateway,
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer
    ) {
        return new CancelAppointmentUseCaseImp(appointmentGateway, historyEventProducer, notificationEventProducer);
    }

    @Bean
    public GetAppointmentUseCase getAppointmentUseCase(AppointmentGateway appointmentGateway) {
        return new GetAppointmentUseCaseImp(appointmentGateway);
    }

    @Bean
    public ListAppointmentsUseCase listAppointmentsUseCase(AppointmentGateway appointmentGateway) {
        return new ListAppointmentsUseCaseImp(appointmentGateway);
    }

    // Producers
    @Bean
    public NotificationEventProducer notificationEventProducer(RabbitTemplate rabbitTemplate) {
        return new NotificationEventProducer(rabbitTemplate);
    }

    @Bean
    public HistoryEventProducer historyEventProducer(RabbitTemplate rabbitTemplate) {
        return new HistoryEventProducer(rabbitTemplate);
    }

}
