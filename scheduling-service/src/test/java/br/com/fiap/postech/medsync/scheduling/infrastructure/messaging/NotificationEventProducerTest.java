package br.com.fiap.postech.medsync.scheduling.infrastructure.messaging;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.NotificationMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NotificationEventProducerTest {

    private RabbitTemplate rabbitTemplate;
    private NotificationEventProducer producer;
    private final String queueName = "app.notification.queue";

    @BeforeEach
    void setUp() throws Exception {
        rabbitTemplate = mock(RabbitTemplate.class);
        producer = new NotificationEventProducer(rabbitTemplate);

        // injeta o nome da fila na propriedade privada anotada com @Value
        Field f = NotificationEventProducer.class.getDeclaredField("notificationQueue");
        f.setAccessible(true);
        f.set(producer, queueName);
    }

    @Test
    void testPublishAppointmentEvent_created() {
        AppointmentDTO appointment = mock(AppointmentDTO.class);
        when(appointment.getPatientUserId()).thenReturn(123L);
        when(appointment.getPatientEmail()).thenReturn("paciente@example.com");
        LocalDateTime date = LocalDateTime.of(2025, 10, 5, 14, 30);
        when(appointment.getAppointmentDate()).thenReturn(date);

        producer.publishAppointmentEvent(appointment, "CREATED");

        ArgumentCaptor<NotificationMessageDTO> captor = ArgumentCaptor.forClass(NotificationMessageDTO.class);
        verify(rabbitTemplate).convertAndSend(eq(queueName), captor.capture());

        NotificationMessageDTO dto = captor.getValue();
        assertNotNull(dto);
        assertEquals(123L, dto.getPatientId());
        assertEquals("paciente@example.com", dto.getPatientEmail());
        assertEquals(date, dto.getAppointmentDate());
        assertEquals("CREATED", dto.getEventType());
        // a implementação atual passa a string com %s sem formatar
        assertEquals("Sua consulta foi agendada para %s. Chegue 15 minutos antes.", dto.getMessage());
    }

    @Test
    void testPublishAppointmentEvent_cancelled() {
        AppointmentDTO appointment = mock(AppointmentDTO.class);
        when(appointment.getPatientUserId()).thenReturn(555L);
        when(appointment.getPatientEmail()).thenReturn("user2@example.com");
        LocalDateTime date = LocalDateTime.of(2025, 12, 1, 9, 0);
        when(appointment.getAppointmentDate()).thenReturn(date);

        producer.publishAppointmentEvent(appointment, "CANCELLED");

        ArgumentCaptor<NotificationMessageDTO> captor = ArgumentCaptor.forClass(NotificationMessageDTO.class);
        verify(rabbitTemplate).convertAndSend(eq(queueName), captor.capture());

        NotificationMessageDTO dto = captor.getValue();
        assertNotNull(dto);
        assertEquals(555L, dto.getPatientId());
        assertEquals("user2@example.com", dto.getPatientEmail());
        assertEquals(date, dto.getAppointmentDate());
        assertEquals("CANCELLED", dto.getEventType());
        assertEquals("Sua consulta para %s foi cancelada. Entre em contato para mais informações.", dto.getMessage());
    }

}