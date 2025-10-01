package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCreatedEvent;
import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

class CreateMedicalRecordUseCaseImpTest {

    private MedicalRecordRepositoryGateway gateway;
    private CreateMedicalRecordUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(MedicalRecordRepositoryGateway.class);
        useCase = new CreateMedicalRecordUseCaseImp(gateway);
    }

    @Test
    void deveCriarRegistroMedicoQuandoNaoExiste() {
        AppointmentCreatedEvent event = mock(AppointmentCreatedEvent.class);
        AppointmentCreatedEvent.Appointment appointment = mock(AppointmentCreatedEvent.Appointment.class);

        when(event.getAppointment()).thenReturn(appointment);
        when(appointment.getId()).thenReturn(1L);
        when(appointment.getPatientUserId()).thenReturn(2L);
        when(appointment.getDoctorUserId()).thenReturn(3L);
        when(appointment.getAppointmentDate()).thenReturn(LocalDateTime.now());
        when(appointment.getStatus()).thenReturn(AppointmentStatus.SCHEDULED.name());
        when(appointment.getType()).thenReturn("Consulta");
        when(appointment.getNotes()).thenReturn("Primeira consulta");

        when(gateway.findByAppointmentId(1L)).thenReturn(Optional.empty());

        useCase.execute(event);

        verify(gateway).create(any(MedicalRecord.class));
        verify(gateway, never()).update(any());
    }

    @Test
    void deveAtualizarRegistroMedicoQuandoJaExiste() {
        AppointmentCreatedEvent event = mock(AppointmentCreatedEvent.class);
        AppointmentCreatedEvent.Appointment appointment = mock(AppointmentCreatedEvent.Appointment.class);

        MedicalRecord existingRecord = new MedicalRecord();
        when(event.getAppointment()).thenReturn(appointment);
        when(appointment.getId()).thenReturn(1L);
        when(appointment.getAppointmentDate()).thenReturn(LocalDateTime.now());
        when(appointment.getStatus()).thenReturn(AppointmentStatus.SCHEDULED.name());
        when(appointment.getType()).thenReturn("Retorno");
        when(appointment.getNotes()).thenReturn("Reavaliação");

        when(gateway.findByAppointmentId(1L)).thenReturn(Optional.of(existingRecord));

        useCase.execute(event);

        verify(gateway).update(existingRecord);
        verify(gateway, never()).create(any());
    }
}
