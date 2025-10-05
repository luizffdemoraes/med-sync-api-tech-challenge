package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CancelAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CancelAppointmentUseCaseImpTest {

    private AppointmentGateway appointmentGateway;
    private HistoryEventProducer historyEventProducer;
    private NotificationEventProducer notificationEventProducer;
    private CancelAppointmentUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        appointmentGateway = mock(AppointmentGateway.class);
        historyEventProducer = mock(HistoryEventProducer.class);
        notificationEventProducer = mock(NotificationEventProducer.class);
        useCase = new CancelAppointmentUseCaseImp(appointmentGateway, historyEventProducer, notificationEventProducer);
    }

    @Test
    void deveCancelarEPublicarEventosQuandoConsultaExistir() {
        Long appointmentId = 1L;
        Appointment existingAppointment = mock(Appointment.class);
        Appointment cancelledAppointment = mock(Appointment.class);

        CancelAppointmentDTO dto = new CancelAppointmentDTO();
        dto.setCancellationReason("Paciente desistiu");
        dto.setUpdatedBy(42L);

        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentGateway.update(existingAppointment)).thenReturn(cancelledAppointment);

        // Executa o caso de uso
        useCase.execute(appointmentId, dto);

        // Verificações
        verify(appointmentGateway).findById(appointmentId);
        verify(existingAppointment).cancel(dto.getCancellationReason());
        verify(existingAppointment).setUpdatedBy(dto.getUpdatedBy());
        verify(appointmentGateway).update(existingAppointment);

        verify(historyEventProducer).publishAppointmentCancelled(any(AppointmentDTO.class), eq(dto.getCancellationReason()));
        verify(notificationEventProducer).publishAppointmentEvent(any(AppointmentDTO.class), eq("CANCELLED"));
    }

    @Test
    void deveLancarQuandoConsultaNaoExistir() {
        Long appointmentId = 99L;
        CancelAppointmentDTO dto = new CancelAppointmentDTO();
        dto.setCancellationReason("Motivo qualquer");
        dto.setUpdatedBy(1L);

        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> useCase.execute(appointmentId, dto));

        verify(appointmentGateway).findById(appointmentId);
        verifyNoMoreInteractions(appointmentGateway, historyEventProducer, notificationEventProducer);
    }
}