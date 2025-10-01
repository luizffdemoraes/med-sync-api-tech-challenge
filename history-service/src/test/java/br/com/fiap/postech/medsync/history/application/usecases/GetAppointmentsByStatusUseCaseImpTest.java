package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.InvalidPatientIdException;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GetAppointmentsByStatusUseCaseImpTest {

    private MedicalRecordRepositoryGateway gateway;
    private GetAppointmentsByStatusUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(MedicalRecordRepositoryGateway.class);
        useCase = new GetAppointmentsByStatusUseCaseImp(gateway);
    }

    @Test
    void deveRetornarRegistrosQuandoExistem() {
        MedicalRecordResponse response = mock(MedicalRecordResponse.class);
        when(gateway.findByPatientIdAndStatus(1L, "SCHEDULED")).thenReturn(List.of(mock(br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord.class)));
        mockStatic(MedicalRecordResponse.class).when(() -> MedicalRecordResponse.fromDomain(any())).thenReturn(response);

        List<MedicalRecordResponse> result = useCase.execute(1L, "SCHEDULED");
        assertEquals(1, result.size());
    }

    @Test
    void deveLancarExcecaoParaPatientIdInvalido() {
        assertThrows(InvalidPatientIdException.class, () -> useCase.execute(0L, "SCHEDULED"));
        assertThrows(InvalidPatientIdException.class, () -> useCase.execute(null, "SCHEDULED"));
    }

    @Test
    void deveLancarExcecaoParaStatusInvalido() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(1L, null));
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(1L, ""));
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(1L, "   "));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarRegistros() {
        when(gateway.findByPatientIdAndStatus(1L, "SCHEDULED")).thenReturn(List.of());
        assertThrows(MedicalRecordNotFoundException.class, () -> useCase.execute(1L, "SCHEDULED"));
    }
}
