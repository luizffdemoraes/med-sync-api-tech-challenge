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

            logger.debug("Mensagem recebida: {}", messageBody);

            //  Ignora completamente o __TypeId__
            AppointmentMessageDTO messageDTO = objectMapper.readValue(messageBody, AppointmentMessageDTO.class);

            // Validação básica do DTO
            if (messageDTO.getEventType() == null) {
                logger.error("Mensagem inválida: eventType está nulo. Mensagem: {}", messageBody);
                throw new AmqpRejectAndDontRequeueException("Mensagem inválida: eventType nulo");
            }

            String eventType = messageDTO.getEventType();
            Long appointmentId = messageDTO.getAppointmentId();

            logger.info("Processando evento: {} para appointment: {}", eventType, appointmentId);

            switch (eventType) {
                case "APPOINTMENT_CREATED" -> {
                    AppointmentCreatedEvent event = messageDTO.toAppointmentCreatedEvent();
                    if (event != null && event.getAppointment() != null) {
                        createMedicalRecordUseCase.execute(event);
                    } else {
                        logger.error("Dados inválidos para APPOINTMENT_CREATED. Mensagem: {}", messageBody);
                        throw new AmqpRejectAndDontRequeueException("Dados inválidos para APPOINTMENT_CREATED");
                    }
                }
                case "APPOINTMENT_COMPLETED" -> {
                    AppointmentCompletedEvent event = messageDTO.toAppointmentCompletedEvent();
                    if (event != null && event.getAppointment() != null) {
                        updateAppointmentStatusUseCase.execute(event);
                    } else {
                        logger.error("Dados inválidos para APPOINTMENT_COMPLETED. Mensagem: {}", messageBody);
                        throw new AmqpRejectAndDontRequeueException("Dados inválidos para APPOINTMENT_COMPLETED");
                    }
                }
                case "MEDICAL_DATA_ADDED" -> {
                    MedicalDataAddedEvent event = messageDTO.toMedicalDataAddedEvent();
                    if (event != null && event.getClinicalData() != null) {
                        addMedicalDataUseCase.execute(event);
                    } else {
                        logger.error("Dados inválidos para MEDICAL_DATA_ADDED. Mensagem: {}", messageBody);
                        throw new AmqpRejectAndDontRequeueException("Dados inválidos para MEDICAL_DATA_ADDED");
                    }
                }
                case "APPOINTMENT_CANCELLED" -> {
                    AppointmentCancelledEvent event = messageDTO.toAppointmentCancelledEvent();
                    if (event != null && event.getAppointment() != null) {
                        updateAppointmentStatusUseCase.execute(event);
                    } else {
                        logger.error("Dados inválidos para APPOINTMENT_CANCELLED. Mensagem: {}", messageBody);
                        throw new AmqpRejectAndDontRequeueException("Dados inválidos para APPOINTMENT_CANCELLED");
                    }
                }
                default -> {
                    logger.warn("Evento ignorado e removido da fila: {}. Mensagem: {}", eventType, messageBody);
                    throw new AmqpRejectAndDontRequeueException("Event type não suportado: " + eventType);
                }
            }

            logger.info("Evento processado com sucesso: {} - appointment: {}", eventType, appointmentId);

        } catch (Exception e) {
            logger.error("Erro processando mensagem: {}. Mensagem original: {}", e.getMessage(), messageBody, e);
            throw new AmqpRejectAndDontRequeueException("Failed to process message: " + e.getMessage());
        }
    }
}