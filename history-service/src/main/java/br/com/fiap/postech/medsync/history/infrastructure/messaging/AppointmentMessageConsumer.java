package br.com.fiap.postech.medsync.history.infrastructure.messaging;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.*;
import br.com.fiap.postech.medsync.history.application.usecases.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AppointmentMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentMessageConsumer.class);

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
        String messageBody = null;
        try {
            messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            AppointmentMessageDTO messageDTO = objectMapper.readValue(messageBody, AppointmentMessageDTO.class);

            // Validação básica do DTO
            if (messageDTO.getEventType() == null || messageDTO.getAppointmentId() == null) {
                logger.error("Mensagem inválida: eventType ou appointmentId está nulo. Mensagem: {}", messageBody);
                throw new AmqpRejectAndDontRequeueException("Mensagem inválida: campos obrigatórios nulos");
            }

            String eventType = messageDTO.getEventType();
            Long appointmentId = messageDTO.getAppointmentId();

            switch (eventType) {
                case "APPOINTMENT_CREATED" -> {
                    logger.info("Processing APPOINTMENT_CREATED for appointment: {}", appointmentId);
                    createMedicalRecordUseCase.execute(messageDTO.toAppointmentCreatedEvent());
                }
                case "APPOINTMENT_COMPLETED" -> {
                    logger.info("Processing APPOINTMENT_COMPLETED for appointment: {}", appointmentId);
                    updateAppointmentStatusUseCase.execute(messageDTO.toAppointmentCompletedEvent());
                }
                case "MEDICAL_DATA_ADDED" -> {
                    logger.info("Processing MEDICAL_DATA_ADDED for appointment: {}", appointmentId);
                    addMedicalDataUseCase.execute(messageDTO.toMedicalDataAddedEvent());
                }
                case "APPOINTMENT_CANCELLED" -> {
                    logger.info("Processing APPOINTMENT_CANCELLED for appointment: {}", appointmentId);
                    updateAppointmentStatusUseCase.execute(messageDTO.toAppointmentCancelledEvent());
                }
                default -> {
                    logger.warn("Evento ignorado e removido da fila: {}. Mensagem: {}", eventType, messageBody);
                    throw new AmqpRejectAndDontRequeueException("Event type não suportado: " + eventType);
                }
            }

            logger.info("Evento processado com sucesso: {} - appointment: {}", eventType, appointmentId);

        } catch (Exception e) {
            logger.error("Erro processando mensagem: {}. Mensagem original: {}", e.getMessage(), messageBody);

            // Para qualquer exceção, rejeita e não faz requeue
            throw new AmqpRejectAndDontRequeueException("Failed to process message: " + e.getMessage(), e);
        }
    }
}