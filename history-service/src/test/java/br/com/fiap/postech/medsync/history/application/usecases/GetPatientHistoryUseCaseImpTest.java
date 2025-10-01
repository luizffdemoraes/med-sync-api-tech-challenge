package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.InvalidPatientIdException;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class GetPatientHistoryUseCaseImpTest {

    @Mock
    private MedicalRecordRepositoryGateway gateway;
    @InjectMocks
    private GetPatientHistoryUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(MedicalRecordRepositoryGateway.class);
        useCase = new GetPatientHistoryUseCaseImp(gateway);
    }

    @Test
    void deveLancarExcecaoParaPatientIdInvalido() {
        assertThrows(InvalidPatientIdException.class, () -> useCase.execute(0L));
        assertThrows(InvalidPatientIdException.class, () -> useCase.execute(null));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarRegistros() {
        when(gateway.findByPatientId(2L)).thenReturn(List.of());
        assertThrows(MedicalRecordNotFoundException.class, () -> useCase.execute(2L));
    }
}
