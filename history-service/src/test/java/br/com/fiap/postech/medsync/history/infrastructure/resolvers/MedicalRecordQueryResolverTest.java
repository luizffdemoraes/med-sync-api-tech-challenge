package br.com.fiap.postech.medsync.history.infrastructure.resolvers;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.application.usecases.GetAppointmentsByStatusUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.GetMedicalRecordByAppointmentIdUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.GetPatientHistoryUseCase;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.PatientAccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordQueryResolverTest {

    private GetMedicalRecordByAppointmentIdUseCase getMedicalRecordByAppointmentIdUseCase;
    private GetPatientHistoryUseCase getPatientHistoryUseCase;
    private GetAppointmentsByStatusUseCase getAppointmentsByStatusUseCase;
    private MedicalRecordQueryResolver resolver;

    @BeforeEach
    void setUp() {
        getMedicalRecordByAppointmentIdUseCase = mock(GetMedicalRecordByAppointmentIdUseCase.class);
        getPatientHistoryUseCase = mock(GetPatientHistoryUseCase.class);
        getAppointmentsByStatusUseCase = mock(GetAppointmentsByStatusUseCase.class);
        resolver = new MedicalRecordQueryResolver(
                getMedicalRecordByAppointmentIdUseCase,
                getPatientHistoryUseCase,
                getAppointmentsByStatusUseCase
        );
    }

    private Jwt jwtPatientWithUserId(long userId) {
        return Jwt.withTokenValue("t")
                .header("alg", "none")
                .claim("authorities", List.of("ROLE_PATIENT"))
                .claim("user_id", userId)
                .claim("username", "patient@example.com")
                .build();
    }

    private Jwt jwtDoctor() {
        return Jwt.withTokenValue("t")
                .header("alg", "none")
                .claim("authorities", List.of("ROLE_DOCTOR"))
                .claim("username", "doctor@example.com")
                .build();
    }

    @Test
    void testGetPatientHistory_Patient_Owner_OK() {
        MedicalRecordResponse response = new MedicalRecordResponse();
        when(getPatientHistoryUseCase.execute(1L)).thenReturn(List.of(response));

        List<MedicalRecordResponse> result = resolver.getPatientHistory(1L, jwtPatientWithUserId(1L));

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
        verify(getPatientHistoryUseCase).execute(1L);
    }

    @Test
    void testGetPatientHistory_Patient_NotOwner_DENY() {
        assertThrows(PatientAccessDeniedException.class,
                () -> resolver.getPatientHistory(2L, jwtPatientWithUserId(1L)));
        verifyNoInteractions(getPatientHistoryUseCase);
    }

    @Test
    void testGetPatientHistory_Doctor_OK() {
        MedicalRecordResponse response = new MedicalRecordResponse();
        when(getPatientHistoryUseCase.execute(99L)).thenReturn(List.of(response));

        List<MedicalRecordResponse> result = resolver.getPatientHistory(99L, jwtDoctor());

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
        verify(getPatientHistoryUseCase).execute(99L);
    }

    @Test
    void testGetAppointmentsByStatus_Patient_Owner_OK() {
        MedicalRecordResponse response = new MedicalRecordResponse();
        when(getAppointmentsByStatusUseCase.execute(5L, "COMPLETED")).thenReturn(List.of(response));

        List<MedicalRecordResponse> result = resolver.getAppointmentsByStatus(5L, "COMPLETED", jwtPatientWithUserId(5L));

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
        verify(getAppointmentsByStatusUseCase).execute(5L, "COMPLETED");
    }

    @Test
    void testGetAppointmentsByStatus_Patient_NotOwner_DENY() {
        assertThrows(PatientAccessDeniedException.class,
                () -> resolver.getAppointmentsByStatus(10L, "SCHEDULED", jwtPatientWithUserId(9L)));
        verifyNoInteractions(getAppointmentsByStatusUseCase);
    }

    @Test
    void testGetAppointmentsByStatus_Doctor_OK() {
        MedicalRecordResponse response = new MedicalRecordResponse();
        when(getAppointmentsByStatusUseCase.execute(7L, "ACTIVE")).thenReturn(List.of(response));

        List<MedicalRecordResponse> result = resolver.getAppointmentsByStatus(7L, "ACTIVE", jwtDoctor());

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
        verify(getAppointmentsByStatusUseCase).execute(7L, "ACTIVE");
    }

    @Test
    void testGetMedicalRecordByAppointmentId_OK() {
        MedicalRecordResponse response = new MedicalRecordResponse();
        when(getMedicalRecordByAppointmentIdUseCase.execute(3L)).thenReturn(response);

        MedicalRecordResponse result = resolver.getMedicalRecordByAppointmentId(3L, jwtDoctor());

        assertSame(response, result);
        verify(getMedicalRecordByAppointmentIdUseCase).execute(3L);
    }
}