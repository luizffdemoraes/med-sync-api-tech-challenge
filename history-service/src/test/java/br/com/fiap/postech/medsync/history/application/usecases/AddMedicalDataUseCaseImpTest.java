// src/test/java/br/com/fiap/postech/medsync/history/application/usecases/AddMedicalDataUseCaseImpTest.java
package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.MedicalDataAddedEvent;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class AddMedicalDataUseCaseImpTest {

    private MedicalRecordRepositoryGateway gateway;
    private AddMedicalDataUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(MedicalRecordRepositoryGateway.class);
        useCase = new AddMedicalDataUseCaseImp(gateway);
    }

    @Test
    void deveAtualizarRegistroMedicoQuandoEncontrado() {
        MedicalRecord record = new MedicalRecord();
        MedicalDataAddedEvent event = mock(MedicalDataAddedEvent.class);
        when(event.getAppointmentId()).thenReturn(1L);
        var clinicalData = mock(MedicalDataAddedEvent.ClinicalData.class);
        when(event.getClinicalData()).thenReturn(clinicalData);
        when(clinicalData.getChiefComplaint()).thenReturn("Dor de cabeça");
        when(clinicalData.getDiagnosis()).thenReturn("Enxaqueca");
        when(clinicalData.getPrescription()).thenReturn("Paracetamol");
        when(clinicalData.getNotes()).thenReturn("Repouso");

        when(gateway.findByAppointmentId(1L)).thenReturn(Optional.of(record));

        useCase.execute(event);

        verify(gateway).update(record);
        verify(event, times(4)).getClinicalData();
    }

    @Test
    void naoDeveAtualizarQuandoRegistroNaoEncontrado() {
        MedicalDataAddedEvent event = mock(MedicalDataAddedEvent.class);
        when(event.getAppointmentId()).thenReturn(2L);
        when(gateway.findByAppointmentId(2L)).thenReturn(Optional.empty());

        useCase.execute(event);

        verify(gateway, never()).update(any());
    }
}
