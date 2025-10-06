package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.PatientAccessDeniedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMedicalRecordByAppointmentIdUseCaseImpTest {

    @Mock
    private MedicalRecordRepositoryGateway gateway;
    @InjectMocks
    private GetMedicalRecordByAppointmentIdUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(MedicalRecordRepositoryGateway.class);
        useCase = new GetMedicalRecordByAppointmentIdUseCaseImp(gateway);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarRegistro() {
        when(gateway.findByAppointmentId(2L)).thenReturn(Optional.empty());

        assertThrows(MedicalRecordNotFoundException.class, () -> useCase.execute(2L));
    }

    @Test
    void pacienteOwner_devePermitir() {
        var record = medicalRecord(1L);
        when(gateway.findByAppointmentId(3L)).thenReturn(Optional.of(record));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(jwtPatient(1L), null));

        MedicalRecordResponse resp = useCase.execute(3L);

        assertNotNull(resp);
    }

    @Test
    void pacienteNaoOwner_deveNegar() {
        var record = medicalRecord(2L);
        when(gateway.findByAppointmentId(3L)).thenReturn(Optional.of(record));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(jwtPatient(1L), null));

        assertThrows(PatientAccessDeniedException.class, () -> useCase.execute(3L));
    }

    @Test
    void pacienteSemUserId_deveNegar() {
        var record = medicalRecord(1L);
        when(gateway.findByAppointmentId(3L)).thenReturn(Optional.of(record));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(jwtPatientMissingUserId(), null));

        assertThrows(PatientAccessDeniedException.class, () -> useCase.execute(3L));
    }

    @Test
    void doctor_devePermitirSemChecarOwner() {
        var record = medicalRecord(999L);
        when(gateway.findByAppointmentId(3L)).thenReturn(Optional.of(record));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(jwtDoctor(), null));

        MedicalRecordResponse resp = useCase.execute(3L);

        assertNotNull(resp);
    }

    @Test
    void semJwt_noContexto_devePermitir() {
        var record = medicalRecord(42L);
        when(gateway.findByAppointmentId(3L)).thenReturn(Optional.of(record));
        // sem authentication no contexto

        MedicalRecordResponse resp = useCase.execute(3L);

        assertNotNull(resp);
    }

    // ===== helpers =====

    private MedicalRecord medicalRecord(Long patientUserId) {
        MedicalRecord r = new MedicalRecord();
        r.setId(100L);
        r.setAppointmentId(3L);
        r.setPatientUserId(patientUserId);
        r.setDoctorUserId(77L);
        r.setAppointmentDate(LocalDateTime.now());
        r.setStatus(AppointmentStatus.SCHEDULED);
        return r;
    }

    private Jwt jwtPatient(long userId) {
        return Jwt.withTokenValue("t")
                .header("alg", "none")
                .claim("authorities", java.util.List.of("ROLE_PATIENT"))
                .claim("user_id", userId)
                .claim("username", "patient@example.com")
                .build();
    }

    private Jwt jwtPatientMissingUserId() {
        return Jwt.withTokenValue("t")
                .header("alg", "none")
                .claim("authorities", java.util.List.of("ROLE_PATIENT"))
                .claim("username", "patient@example.com")
                .build();
    }

    private Jwt jwtDoctor() {
        return Jwt.withTokenValue("t")
                .header("alg", "none")
                .claim("authorities", java.util.List.of("ROLE_DOCTOR"))
                .claim("username", "doctor@example.com")
                .build();
    }
}
