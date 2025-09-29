package br.com.fiap.postech.medsync.history.infrastructure.config.dependency;

import br.com.fiap.postech.medsync.history.application.usecases.AddMedicalDataUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.CreateMedicalRecordUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.UpdateAppointmentStatusUseCase;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.gateways.MedicalRecordRepositoryGatewayImpl;
import br.com.fiap.postech.medsync.history.infrastructure.messaging.AppointmentMessageConsumer;
import br.com.fiap.postech.medsync.history.infrastructure.persistence.repository.MedicalRecordRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DependencyInjectionConfig {

    // Use Cases



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
