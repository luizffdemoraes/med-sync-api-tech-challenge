package br.com.fiap.postech.medsync.history.infrastructure.resolvers;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.application.usecases.GetAppointmentsByStatusUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.GetMedicalRecordByAppointmentIdUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.GetPatientHistoryUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void testGetPatientHistory() {
        MedicalRecordResponse response = new MedicalRecordResponse();
        when(getPatientHistoryUseCase.execute(1L)).thenReturn(List.of(response));

        List<MedicalRecordResponse> result = resolver.getPatientHistory(1L);

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
        verify(getPatientHistoryUseCase).execute(1L);
    }

    @Test
    void testGetAppointmentsByStatus() {
        MedicalRecordResponse response = new MedicalRecordResponse();
        when(getAppointmentsByStatusUseCase.execute(2L, "ACTIVE")).thenReturn(List.of(response));

        List<MedicalRecordResponse> result = resolver.getAppointmentsByStatus(2L, "ACTIVE");

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
        verify(getAppointmentsByStatusUseCase).execute(2L, "ACTIVE");
    }

    @Test
    void testGetMedicalRecordByAppointmentId() {
        MedicalRecordResponse response = new MedicalRecordResponse();
        when(getMedicalRecordByAppointmentIdUseCase.execute(3L)).thenReturn(response);

        MedicalRecordResponse result = resolver.getMedicalRecordByAppointmentId(3L);

        assertSame(response, result);
        verify(getMedicalRecordByAppointmentIdUseCase).execute(3L);
    }
}
