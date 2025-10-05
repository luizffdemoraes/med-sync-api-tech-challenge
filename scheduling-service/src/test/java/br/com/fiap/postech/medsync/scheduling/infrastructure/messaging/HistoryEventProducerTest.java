package br.com.fiap.postech.medsync.scheduling.infrastructure.messaging;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.HistoryEventDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.MedicalDataRequestDTO;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentStatus;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentType;
import br.com.fiap.postech.medsync.scheduling.infrastructure.config.rabbitmq.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class HistoryEventProducerTest {

    private RabbitTemplate rabbitTemplate;
    private HistoryEventProducer producer;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        producer = new HistoryEventProducer(rabbitTemplate);
    }

    @Test
    void testPublishAppointmentCompleted() {
        AppointmentDTO appointment = mock(AppointmentDTO.class);
        when(appointment.getId()).thenReturn(42L);

        producer.publishAppointmentCompleted(appointment);

        ArgumentCaptor<HistoryEventDTO> captor = ArgumentCaptor.forClass(HistoryEventDTO.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.HISTORY_EXCHANGE),
                eq(RabbitMQConfig.APPOINTMENT_COMPLETED_KEY),
                captor.capture()
        );

        HistoryEventDTO event = captor.getValue();
        assertNotNull(event);
        assertEquals("APPOINTMENT_COMPLETED", event.getEventType());
        assertEquals(42L, event.getAppointmentId());
    }

    @Test
    void testPublishAppointmentCancelled() {
        AppointmentDTO appointment = mock(AppointmentDTO.class);
        when(appointment.getId()).thenReturn(100L);

        producer.publishAppointmentCancelled(appointment, "Paciente não compareceu");

        ArgumentCaptor<HistoryEventDTO> captor = ArgumentCaptor.forClass(HistoryEventDTO.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.HISTORY_EXCHANGE),
                eq(RabbitMQConfig.APPOINTMENT_CANCELLED_KEY),
                captor.capture()
        );

        HistoryEventDTO event = captor.getValue();
        assertNotNull(event);
        assertEquals("APPOINTMENT_CANCELLED", event.getEventType());
        assertEquals(100L, event.getAppointmentId());
    }

    @Test
    void testPublishMedicalDataAdded() {
        MedicalDataRequestDTO request = mock(MedicalDataRequestDTO.class);
        when(request.getChiefComplaint()).thenReturn("Dor de cabeça");
        when(request.getDiagnosis()).thenReturn("Enxaqueca");
        when(request.getPrescription()).thenReturn("Analgésico");
        when(request.getClinicalNotes()).thenReturn("Observações");

        producer.publishMedicalDataAdded(11L, request);

        ArgumentCaptor<HistoryEventDTO> captor = ArgumentCaptor.forClass(HistoryEventDTO.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.HISTORY_EXCHANGE),
                eq(RabbitMQConfig.MEDICAL_DATA_ADDED_KEY),
                captor.capture()
        );

        HistoryEventDTO event = captor.getValue();
        assertNotNull(event);
        assertEquals("MEDICAL_DATA_ADDED", event.getEventType());
        assertEquals(11L, event.getAppointmentId());
    }

    @Test
    void testPublishAppointmentCreated_minimalAssertions() {
        AppointmentDTO appointment = mock(AppointmentDTO.class);
        when(appointment.getId()).thenReturn(7L);

        // Evita NPE ao chamar appointment.getStatus().name() e getType().name()
        when(appointment.getPatientUserId()).thenReturn(1L);
        when(appointment.getDoctorUserId()).thenReturn(2L);
        when(appointment.getAppointmentDate()).thenReturn(LocalDateTime.now());

        AppointmentStatus statusMock = mock(AppointmentStatus.class);
        when(statusMock.name()).thenReturn("SCHEDULED");
        when(appointment.getStatus()).thenReturn(statusMock);

        AppointmentType typeMock = mock(AppointmentType.class);
        when(typeMock.name()).thenReturn("CONSULTATION");
        when(appointment.getType()).thenReturn(typeMock);

        when(appointment.getDurationMinutes()).thenReturn(30);
        when(appointment.getNotes()).thenReturn("observações de teste");

        producer.publishAppointmentCreated(appointment);

        ArgumentCaptor<HistoryEventDTO> captor = ArgumentCaptor.forClass(HistoryEventDTO.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.HISTORY_EXCHANGE),
                eq(RabbitMQConfig.APPOINTMENT_CREATED_KEY),
                captor.capture()
        );

        HistoryEventDTO event = captor.getValue();
        assertNotNull(event);
        assertEquals("APPOINTMENT_CREATED", event.getEventType());
        assertEquals(7L, event.getAppointmentId());
    }
}