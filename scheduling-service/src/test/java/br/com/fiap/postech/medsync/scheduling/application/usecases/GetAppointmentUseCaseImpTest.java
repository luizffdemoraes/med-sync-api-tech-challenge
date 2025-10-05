package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class GetAppointmentUseCaseImpTest {

    private AppointmentGateway appointmentGateway;
    private GetAppointmentUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        appointmentGateway = mock(AppointmentGateway.class);
        useCase = new GetAppointmentUseCaseImp(appointmentGateway);
    }

    @Test
    void deveRetornarAppointmentDTOQuandoConsultaExistir() {
        Long appointmentId = 1L;
        Appointment existingAppointment = mock(Appointment.class);

        Long id = 1L;
        Long patientUserId = 10L;
        Long doctorUserId = 20L;
        String patientEmail = "patient@example.com";
        LocalDateTime appointmentDate = LocalDateTime.now().plusDays(1);
        Integer duration = 45;
        String notes = "Observações";
        String chiefComplaint = "Dor de cabeça";
        String diagnosis = "Enxaqueca";
        String prescription = "Paracetamol";
        String clinicalNotes = "Repouso";
        Long updatedBy = 42L;

        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(existingAppointment.getId()).thenReturn(id);
        when(existingAppointment.getPatientUserId()).thenReturn(patientUserId);
        when(existingAppointment.getDoctorUserId()).thenReturn(doctorUserId);
        when(existingAppointment.getPatientEmail()).thenReturn(patientEmail);
        when(existingAppointment.getAppointmentDate()).thenReturn(appointmentDate);
        when(existingAppointment.getDurationMinutes()).thenReturn(duration);
        when(existingAppointment.getNotes()).thenReturn(notes);
        when(existingAppointment.getChiefComplaint()).thenReturn(chiefComplaint);
        when(existingAppointment.getDiagnosis()).thenReturn(diagnosis);
        when(existingAppointment.getPrescription()).thenReturn(prescription);
        when(existingAppointment.getClinicalNotes()).thenReturn(clinicalNotes);
        when(existingAppointment.getUpdatedBy()).thenReturn(updatedBy);

        AppointmentDTO result = useCase.execute(appointmentId);

        assertNotNull(result, "Resultado não deve ser nulo");
        assertEquals(id, result.getId());
        assertEquals(patientUserId, result.getPatientUserId());
        assertEquals(doctorUserId, result.getDoctorUserId());
        assertEquals(patientEmail, result.getPatientEmail());
        assertEquals(appointmentDate, result.getAppointmentDate());
        assertEquals(duration, result.getDurationMinutes());
        assertEquals(notes, result.getNotes());
        assertEquals(chiefComplaint, result.getChiefComplaint());
        assertEquals(diagnosis, result.getDiagnosis());
        assertEquals(prescription, result.getPrescription());
        assertEquals(clinicalNotes, result.getClinicalNotes());
        assertEquals(updatedBy, result.getUpdatedBy());

        verify(appointmentGateway).findById(appointmentId);
    }

    @Test
    void deveLancarQuandoConsultaNaoExistir() {
        Long appointmentId = 99L;
        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> useCase.execute(appointmentId));

        verify(appointmentGateway).findById(appointmentId);
        verifyNoMoreInteractions(appointmentGateway);
    }
}