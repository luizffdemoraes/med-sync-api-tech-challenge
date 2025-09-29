package br.com.fiap.postech.medsync.history.infrastructure.messaging;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.*;
import br.com.fiap.postech.medsync.history.application.usecases.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMessageConsumer {

    private final CreateMedicalRecordUseCase createMedicalRecordUseCase;
    private final UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;
    private final AddMedicalDataUseCase addMedicalDataUseCase;

    public AppointmentMessageConsumer(
            CreateMedicalRecordUseCase createMedicalRecordUseCase,
            UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase,
            AddMedicalDataUseCase addMedicalDataUseCase) {
        this.createMedicalRecordUseCase = createMedicalRecordUseCase;
        this.updateAppointmentStatusUseCase = updateAppointmentStatusUseCase;
        this.addMedicalDataUseCase = addMedicalDataUseCase;
    }

    @RabbitListener(queues = "#{@appointmentQueue.name}")
    public void receive(AppointmentMessageDTO message) {
        String eventType = message.getEventType();

        switch (eventType) {
            case "APPOINTMENT_CREATED" -> createMedicalRecordUseCase.execute(message.toAppointmentCreatedEvent());
            case "APPOINTMENT_COMPLETED" -> updateAppointmentStatusUseCase.execute(message.toAppointmentCompletedEvent());
            case "MEDICAL_DATA_ADDED" -> addMedicalDataUseCase.execute(message.toMedicalDataAddedEvent());
            case "APPOINTMENT_CANCELLED" -> updateAppointmentStatusUseCase.execute(message.toAppointmentCancelledEvent());
            default -> throw new IllegalArgumentException("Evento não suportado: " + eventType);
        }
    }
}