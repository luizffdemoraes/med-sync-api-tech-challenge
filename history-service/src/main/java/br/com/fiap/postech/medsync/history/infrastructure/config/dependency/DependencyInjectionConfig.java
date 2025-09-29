package br.com.fiap.postech.medsync.history.infrastructure.config.dependency;

import br.com.fiap.postech.medsync.history.application.usecases.*;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.gateways.MedicalRecordRepositoryGatewayImpl;
import br.com.fiap.postech.medsync.history.infrastructure.messaging.AppointmentMessageConsumer;
import br.com.fiap.postech.medsync.history.infrastructure.persistence.repository.MedicalRecordRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DependencyInjectionConfig {

    // Use Cases
    @Bean
    public GetMedicalRecordByAppointmentIdUseCase getMedicalRecordByAppointmentIdUseCase(MedicalRecordRepositoryGateway gateway) {
        return new GetMedicalRecordByAppointmentIdUseCaseImp(gateway);
    }

    @Bean
    public AddMedicalDataUseCase addMedicalDataUseCase(MedicalRecordRepositoryGateway gateway) {
        return new AddMedicalDataUseCaseImp(gateway);
    }

    @Bean
    public CreateMedicalRecordUseCase createMedicalRecordUseCase(MedicalRecordRepositoryGateway gateway) {
        return new CreateMedicalRecordUseCaseImp(gateway);
    }

    @Bean
    public UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase(MedicalRecordRepositoryGateway gateway) {
        return new UpdateAppointmentStatusUseCaseImp(gateway);
    }

    // Gateways

    @Bean
    public MedicalRecordRepositoryGateway notificationGateway(MedicalRecordRepository repository) {
        return new MedicalRecordRepositoryGatewayImpl(repository);
    }

    // Consumer

    @Bean
    public AppointmentMessageConsumer appointmentMessageConsumer(CreateMedicalRecordUseCase createMedicalRecordUseCase,
                                                                 UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase,
                                                                 AddMedicalDataUseCase addMedicalDataUseCase) {
        return new AppointmentMessageConsumer(createMedicalRecordUseCase, updateAppointmentStatusUseCase, addMedicalDataUseCase);
    }
}
