package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarRegistro() {
        when(gateway.findByAppointmentId(2L)).thenReturn(Optional.empty());

        assertThrows(MedicalRecordNotFoundException.class, () -> useCase.execute(2L));
    }
}
