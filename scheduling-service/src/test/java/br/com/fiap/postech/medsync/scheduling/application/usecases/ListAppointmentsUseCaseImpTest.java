package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListAppointmentsUseCaseImpTest {

    private AppointmentGateway appointmentGateway;
    private ListAppointmentsUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        appointmentGateway = mock(AppointmentGateway.class);
        useCase = new ListAppointmentsUseCaseImp(appointmentGateway);
    }

    @Test
    void deveRetornarPageDeAppointmentDTOQuandoExistirem() {
        Long patientId = 10L;
        Long doctorId = 20L;
        String status = "CONFIRMED";
        Pageable pageable = PageRequest.of(0, 10);

        Appointment appt1 = mock(Appointment.class);
        Appointment appt2 = mock(Appointment.class);

        when(appt1.getId()).thenReturn(1L);
        when(appt1.getPatientUserId()).thenReturn(100L);
        when(appt1.getDoctorUserId()).thenReturn(200L);
        when(appt1.getPatientEmail()).thenReturn("p1@example.com");
        when(appt1.getAppointmentDate()).thenReturn(LocalDateTime.now().plusDays(1));
        when(appt1.getDurationMinutes()).thenReturn(30);
        when(appt1.getNotes()).thenReturn("nota1");
        when(appt1.getChiefComplaint()).thenReturn("queixa1");
        when(appt1.getDiagnosis()).thenReturn("diag1");
        when(appt1.getPrescription()).thenReturn("presc1");
        when(appt1.getClinicalNotes()).thenReturn("clin1");
        when(appt1.getUpdatedBy()).thenReturn(42L);

        when(appt2.getId()).thenReturn(2L);
        when(appt2.getPatientUserId()).thenReturn(101L);
        when(appt2.getDoctorUserId()).thenReturn(201L);
        when(appt2.getPatientEmail()).thenReturn("p2@example.com");
        when(appt2.getAppointmentDate()).thenReturn(LocalDateTime.now().plusDays(2));
        when(appt2.getDurationMinutes()).thenReturn(45);
        when(appt2.getNotes()).thenReturn("nota2");
        when(appt2.getChiefComplaint()).thenReturn("queixa2");
        when(appt2.getDiagnosis()).thenReturn("diag2");
        when(appt2.getPrescription()).thenReturn("presc2");
        when(appt2.getClinicalNotes()).thenReturn("clin2");
        when(appt2.getUpdatedBy()).thenReturn(43L);

        List<Appointment> appointments = Arrays.asList(appt1, appt2);
        Page<Appointment> page = new PageImpl<>(appointments, pageable, appointments.size());

        when(appointmentGateway.findAll(patientId, doctorId, status, pageable)).thenReturn(page);

        Page<AppointmentDTO> result = useCase.execute(patientId, doctorId, status, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        AppointmentDTO dto1 = result.getContent().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals(100L, dto1.getPatientUserId());
        assertEquals(200L, dto1.getDoctorUserId());
        assertEquals("p1@example.com", dto1.getPatientEmail());
        assertEquals(30, dto1.getDurationMinutes());
        assertEquals("nota1", dto1.getNotes());
        assertEquals("queixa1", dto1.getChiefComplaint());
        assertEquals("diag1", dto1.getDiagnosis());
        assertEquals("presc1", dto1.getPrescription());
        assertEquals("clin1", dto1.getClinicalNotes());
        assertEquals(42L, dto1.getUpdatedBy());

        verify(appointmentGateway).findAll(patientId, doctorId, status, pageable);
    }

    @Test
    void deveRetornarPageVaziaQuandoNaoExistiremConsultas() {
        Long patientId = 999L;
        Long doctorId = 888L;
        String status = "CANCELLED";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(appointmentGateway.findAll(patientId, doctorId, status, pageable)).thenReturn(emptyPage);

        Page<AppointmentDTO> result = useCase.execute(patientId, doctorId, status, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(appointmentGateway).findAll(patientId, doctorId, status, pageable);
    }
}