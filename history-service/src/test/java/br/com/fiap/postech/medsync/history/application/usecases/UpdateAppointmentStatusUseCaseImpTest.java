package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCancelledEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCompletedEvent;
import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class UpdateAppointmentStatusUseCaseImpTest {

    private MedicalRecordRepositoryGateway gateway;
    private UpdateAppointmentStatusUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(MedicalRecordRepositoryGateway.class);
        useCase = new UpdateAppointmentStatusUseCaseImp(gateway);
    }

    @Test
    void deveAtualizarStatusParaCompletedQuandoRegistroExiste() {
        AppointmentCompletedEvent event = mock(AppointmentCompletedEvent.class);
        AppointmentCompletedEvent.Appointment appointment = mock(AppointmentCompletedEvent.Appointment.class);
        MedicalRecord record = mock(MedicalRecord.class);

        when(event.getAppointment()).thenReturn(appointment);
        when(appointment.getId()).thenReturn(1L);
        when(gateway.findByAppointmentId(1L)).thenReturn(Optional.of(record));

        useCase.execute(event);

        verify(record).setStatus(AppointmentStatus.COMPLETED);
        verify(gateway).update(record);
    }

    @Test
    void deveAtualizarStatusParaCancelledQuandoRegistroExiste() {
        AppointmentCancelledEvent event = mock(AppointmentCancelledEvent.class);
        AppointmentCancelledEvent.Appointment appointment = mock(AppointmentCancelledEvent.Appointment.class);
        MedicalRecord record = mock(MedicalRecord.class);

        when(event.getAppointment()).thenReturn(appointment);
        when(appointment.getId()).thenReturn(2L);
        when(gateway.findByAppointmentId(2L)).thenReturn(Optional.of(record));

        useCase.execute(event);

        verify(record).setStatus(AppointmentStatus.CANCELLED);
        verify(gateway).update(record);
    }

    @Test
    void naoDeveAtualizarQuandoRegistroNaoExiste() {
        AppointmentCompletedEvent event = mock(AppointmentCompletedEvent.class);
        AppointmentCompletedEvent.Appointment appointment = mock(AppointmentCompletedEvent.Appointment.class);

        when(event.getAppointment()).thenReturn(appointment);
        when(appointment.getId()).thenReturn(3L);
        when(gateway.findByAppointmentId(3L)).thenReturn(Optional.empty());

        useCase.execute(event);

        verify(gateway, never()).update(any());
    }
}
