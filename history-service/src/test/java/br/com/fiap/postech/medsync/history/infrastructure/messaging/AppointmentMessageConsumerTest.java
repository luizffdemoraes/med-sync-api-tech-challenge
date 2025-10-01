// src/test/java/br/com/fiap/postech/medsync/history/infrastructure/messaging/AppointmentMessageConsumerTest.java
package br.com.fiap.postech.medsync.history.infrastructure.messaging;


import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCancelledEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCompletedEvent;
import br.com.fiap.postech.medsync.history.infrastructure.messaging.AppointmentMessageDTO;
import br.com.fiap.postech.medsync.history.application.usecases.CreateMedicalRecordUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.UpdateAppointmentStatusUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.AddMedicalDataUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentMessageConsumerTest {

    private CreateMedicalRecordUseCase createMedicalRecordUseCase;
    private UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;
    private AddMedicalDataUseCase addMedicalDataUseCase;
    private ObjectMapper objectMapper;
    private AppointmentMessageConsumer consumer;

    @BeforeEach
    void setUp() {
        createMedicalRecordUseCase = mock(CreateMedicalRecordUseCase.class);
        updateAppointmentStatusUseCase = mock(UpdateAppointmentStatusUseCase.class);
        addMedicalDataUseCase = mock(AddMedicalDataUseCase.class);
        objectMapper = mock(ObjectMapper.class);
        consumer = new AppointmentMessageConsumer(
                createMedicalRecordUseCase,
                updateAppointmentStatusUseCase,
                addMedicalDataUseCase,
                objectMapper
        );
    }

    @Test
    void testReceiveAppointmentCreated() throws Exception {
        AppointmentMessageDTO dto = mock(AppointmentMessageDTO.class);
        when(dto.getEventType()).thenReturn("APPOINTMENT_CREATED");
        when(dto.getAppointmentId()).thenReturn(1L);
        when(objectMapper.readValue(anyString(), eq(AppointmentMessageDTO.class))).thenReturn(dto);

        Message message = new Message("{\"eventType\":\"APPOINTMENT_CREATED\",\"appointmentId\":1}".getBytes());
        consumer.receive(message);

        verify(createMedicalRecordUseCase).execute(any());
    }

    @Test
    void testReceiveAppointmentCompleted() throws Exception {
        AppointmentMessageDTO dto = mock(AppointmentMessageDTO.class);
        when(dto.getEventType()).thenReturn("APPOINTMENT_COMPLETED");
        when(dto.getAppointmentId()).thenReturn(2L);
        when(objectMapper.readValue(anyString(), eq(AppointmentMessageDTO.class))).thenReturn(dto);

        Message message = new Message("{\"eventType\":\"APPOINTMENT_COMPLETED\",\"appointmentId\":2}".getBytes());
        consumer.receive(message);

        verify(updateAppointmentStatusUseCase).execute((AppointmentCompletedEvent) any());
    }

    @Test
    void testReceiveMedicalDataAdded() throws Exception {
        AppointmentMessageDTO dto = mock(AppointmentMessageDTO.class);
        when(dto.getEventType()).thenReturn("MEDICAL_DATA_ADDED");
        when(dto.getAppointmentId()).thenReturn(3L);
        when(objectMapper.readValue(anyString(), eq(AppointmentMessageDTO.class))).thenReturn(dto);

        Message message = new Message("{\"eventType\":\"MEDICAL_DATA_ADDED\",\"appointmentId\":3}".getBytes());
        consumer.receive(message);

        verify(addMedicalDataUseCase).execute(any());
    }


    @Test
    void testReceiveAppointmentCancelled() throws Exception {
        AppointmentMessageDTO dto = mock(AppointmentMessageDTO.class);
        when(dto.getEventType()).thenReturn("APPOINTMENT_CANCELLED");
        when(dto.getAppointmentId()).thenReturn(4L);
        when(objectMapper.readValue(anyString(), eq(AppointmentMessageDTO.class))).thenReturn(dto);

        // Mock do retorno do método toAppointmentCancelledEvent
        AppointmentCancelledEvent cancelledEvent = mock(AppointmentCancelledEvent.class);
        when(dto.toAppointmentCancelledEvent()).thenReturn(cancelledEvent);

        Message message = new Message("{\"eventType\":\"APPOINTMENT_CANCELLED\",\"appointmentId\":4}".getBytes());
        consumer.receive(message);

        verify(updateAppointmentStatusUseCase).execute(any(AppointmentCancelledEvent.class));
    }

    @Test
    void testReceiveInvalidEventTypeThrowsException() throws Exception {
        AppointmentMessageDTO dto = mock(AppointmentMessageDTO.class);
        when(dto.getEventType()).thenReturn("UNKNOWN_EVENT");
        when(dto.getAppointmentId()).thenReturn(5L);
        when(objectMapper.readValue(anyString(), eq(AppointmentMessageDTO.class))).thenReturn(dto);

        Message message = new Message("{\"eventType\":\"UNKNOWN_EVENT\",\"appointmentId\":5}".getBytes());

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumer.receive(message));
    }

    @Test
    void testReceiveMissingFieldsThrowsException() throws Exception {
        AppointmentMessageDTO dto = mock(AppointmentMessageDTO.class);
        when(dto.getEventType()).thenReturn(null);
        when(dto.getAppointmentId()).thenReturn(null);
        when(objectMapper.readValue(anyString(), eq(AppointmentMessageDTO.class))).thenReturn(dto);

        Message message = new Message("{\"eventType\":null,\"appointmentId\":null}".getBytes());

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumer.receive(message));
    }

    @Test
    void testReceiveJsonParseErrorThrowsException() throws Exception {
        when(objectMapper.readValue(anyString(), eq(AppointmentMessageDTO.class))).thenThrow(new RuntimeException("JSON error"));

        Message message = new Message("invalid json".getBytes());

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumer.receive(message));
    }
}
