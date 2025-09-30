package br.com.fiap.postech.medsync.history.infrastructure.messaging;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.*;
import br.com.fiap.postech.medsync.history.application.usecases.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AppointmentMessageConsumer {

    private final CreateMedicalRecordUseCase createMedicalRecordUseCase;
    private final UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;
    private final AddMedicalDataUseCase addMedicalDataUseCase;
    private final ObjectMapper objectMapper;

    public AppointmentMessageConsumer(
            CreateMedicalRecordUseCase createMedicalRecordUseCase,
            UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase,
            AddMedicalDataUseCase addMedicalDataUseCase,
            ObjectMapper objectMapper) {
        this.createMedicalRecordUseCase = createMedicalRecordUseCase;
        this.updateAppointmentStatusUseCase = updateAppointmentStatusUseCase;
        this.addMedicalDataUseCase = addMedicalDataUseCase;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "#{@appointmentQueue.name}")
    public void receive(Message message) {
        try {
            // Converte manualmente o payload de byte[] para String e depois para DTO
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received raw message: " + messageBody);

            AppointmentMessageDTO messageDTO = objectMapper.readValue(messageBody, AppointmentMessageDTO.class);

            String eventType = messageDTO.getEventType();
            System.out.println("Processing event: " + eventType);

            switch (eventType) {
                case "APPOINTMENT_CREATED" ->
                        createMedicalRecordUseCase.execute(messageDTO.toAppointmentCreatedEvent());
                case "APPOINTMENT_COMPLETED" ->
                        updateAppointmentStatusUseCase.execute(messageDTO.toAppointmentCompletedEvent());
                case "MEDICAL_DATA_ADDED" ->
                        addMedicalDataUseCase.execute(messageDTO.toMedicalDataAddedEvent());
                case "APPOINTMENT_CANCELLED" ->
                        updateAppointmentStatusUseCase.execute(messageDTO.toAppointmentCancelledEvent());
                default ->
                        throw new IllegalArgumentException("Evento não suportado: " + eventType);
            }

            System.out.println("Event processed successfully: " + eventType);

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to process message", e);
        }
    }
}