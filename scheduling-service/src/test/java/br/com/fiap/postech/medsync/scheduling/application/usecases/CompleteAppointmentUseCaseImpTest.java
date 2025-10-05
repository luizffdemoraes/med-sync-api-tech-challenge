package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CompleteAppointmentUseCaseImpTest {

    private AppointmentGateway appointmentGateway;
    private HistoryEventProducer historyEventProducer;
    private NotificationEventProducer notificationEventProducer;
    private CompleteAppointmentUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        appointmentGateway = mock(AppointmentGateway.class);
        historyEventProducer = mock(HistoryEventProducer.class);
        notificationEventProducer = mock(NotificationEventProducer.class);
        useCase = new CompleteAppointmentUseCaseImp(appointmentGateway, historyEventProducer, notificationEventProducer);
    }

    @Test
    void deveCompletarEPublicarEventosQuandoConsultaExistir() {
        Long appointmentId = 1L;
        Long updatedBy = 42L;
        Appointment existingAppointment = mock(Appointment.class);
        Appointment completedAppointment = mock(Appointment.class);

        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentGateway.update(existingAppointment)).thenReturn(completedAppointment);

        AppointmentDTO result = useCase.execute(appointmentId, updatedBy);

        assertNotNull(result, "Resultado não deve ser nulo");
        verify(appointmentGateway).findById(appointmentId);
        verify(existingAppointment).complete();
        verify(existingAppointment).setUpdatedBy(updatedBy);
        verify(appointmentGateway).update(existingAppointment);

        verify(historyEventProducer).publishAppointmentCompleted(any(AppointmentDTO.class));
        verify(notificationEventProducer).publishAppointmentEvent(any(AppointmentDTO.class), eq("UPDATED"));
    }

    @Test
    void deveLancarQuandoConsultaNaoExistir() {
        Long appointmentId = 99L;
        Long updatedBy = 1L;

        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> useCase.execute(appointmentId, updatedBy));

        verify(appointmentGateway).findById(appointmentId);
        verifyNoMoreInteractions(appointmentGateway, historyEventProducer, notificationEventProducer);
    }
}