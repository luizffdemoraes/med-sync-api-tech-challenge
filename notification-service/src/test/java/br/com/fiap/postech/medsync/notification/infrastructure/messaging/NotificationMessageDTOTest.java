package br.com.fiap.postech.medsync.notification.infrastructure.messaging;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMessageDTOTest {

    @Test
    void shouldCreateDTOAndConvertToDomain() {
        Long patientId = 1L;
        String patientEmail = "test@email.com";
        LocalDateTime appointmentDate = LocalDateTime.of(2025, 9, 28, 14, 0);
        String eventType = "MANUAL";
        String message = "Mensagem";

        NotificationMessageDTO dto = new NotificationMessageDTO(patientId, patientEmail, appointmentDate, eventType, message);

        assertEquals(patientId, dto.getPatientId());
        assertEquals(patientEmail, dto.getPatientEmail());
        assertEquals(appointmentDate, dto.getAppointmentDate());
        assertEquals(eventType, dto.getEventType());
        assertEquals(message, dto.getMessage());

        Notification notification = dto.toDomain();
        assertEquals(patientId, notification.getPatientId());
        assertEquals(patientEmail, notification.getPatientEmail());
        assertEquals(appointmentDate, notification.getAppointmentDate());
        assertEquals(eventType, notification.getEventType());
        assertEquals(message, notification.getMessage());
    }

    @Test
    void shouldSettersWorkCorrectly() {
        NotificationMessageDTO dto = new NotificationMessageDTO(null, null, null, null, null);

        dto.setPatientId(2L);
        dto.setPatientEmail("other@email.com");
        LocalDateTime date = LocalDateTime.now();
        dto.setAppointmentDate(date);
        dto.setEventType("AUTO");
        dto.setMessage("Outra mensagem");

        assertEquals(2L, dto.getPatientId());
        assertEquals("other@email.com", dto.getPatientEmail());
        assertEquals(date, dto.getAppointmentDate());
        assertEquals("AUTO", dto.getEventType());
        assertEquals("Outra mensagem", dto.getMessage());
    }
}
