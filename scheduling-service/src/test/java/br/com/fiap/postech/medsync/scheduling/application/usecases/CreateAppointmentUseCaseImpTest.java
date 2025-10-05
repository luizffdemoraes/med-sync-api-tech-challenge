package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CreateAppointmentUseCaseImpTest {

    private AppointmentGateway appointmentGateway;
    private HistoryEventProducer historyEventProducer;
    private NotificationEventProducer notificationEventProducer;
    private CreateAppointmentUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        appointmentGateway = mock(AppointmentGateway.class);
        historyEventProducer = mock(HistoryEventProducer.class);
        notificationEventProducer = mock(NotificationEventProducer.class);
        useCase = new CreateAppointmentUseCaseImp(appointmentGateway, historyEventProducer, notificationEventProducer);
    }

    @Test
    void deveCriarEPublicarEventosQuandoDadosForemValidos() {
        CreateAppointmentDTO dto = new CreateAppointmentDTO(
                1L,
                "patient@example.com",
                2L,
                LocalDateTime.now().plusDays(1),
                "CONSULTATION",
                30,
                "Observações"
        );

        Appointment createdAppointment = mock(Appointment.class);

        when(appointmentGateway.save(any(Appointment.class))).thenReturn(createdAppointment);

        AppointmentDTO result = useCase.execute(dto);

        assertNotNull(result, "Resultado não deve ser nulo");
        verify(appointmentGateway).save(any(Appointment.class));
        verify(historyEventProducer).publishAppointmentCreated(any(AppointmentDTO.class));
        verify(notificationEventProducer).publishAppointmentEvent(any(AppointmentDTO.class), eq("CREATED"));
    }

    @Test
    void deveLancarQuandoGatewayFalharAoCriar() {
        CreateAppointmentDTO dto = new CreateAppointmentDTO(
                1L,
                "patient@example.com",
                2L,
                LocalDateTime.now().plusDays(1),
                "CONSULTATION",
                30,
                "Observações"
        );

        when(appointmentGateway.save(any(Appointment.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> useCase.execute(dto));

        verify(appointmentGateway).save(any(Appointment.class));
        verifyNoInteractions(historyEventProducer, notificationEventProducer);
    }
}