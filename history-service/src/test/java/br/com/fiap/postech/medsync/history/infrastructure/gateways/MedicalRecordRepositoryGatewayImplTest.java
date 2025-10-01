package br.com.fiap.postech.medsync.history.infrastructure.gateways;

import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.infrastructure.persistence.entity.MedicalRecordEntity;
import br.com.fiap.postech.medsync.history.infrastructure.persistence.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MedicalRecordRepositoryGatewayImplTest {

    private MedicalRecordRepository medicalRecordRepository;
    private MedicalRecordRepositoryGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        medicalRecordRepository = mock(MedicalRecordRepository.class);
        gateway = new MedicalRecordRepositoryGatewayImpl(medicalRecordRepository);
    }

    @Test
    void testCreate() {
        MedicalRecord record = new MedicalRecord(
                1L, 1L, 1L, 2L, LocalDateTime.now(),
                AppointmentStatus.SCHEDULED, "Consulta", "Notas da consulta"
        );
        record.setChiefComplaint("Dor de cabeça");
        record.setDiagnosis("Enxaqueca");
        record.setPrescription("Analgésico");

        MedicalRecordEntity savedEntity = MedicalRecordEntity.fromDomain(record);
        savedEntity.setId(1L);

        when(medicalRecordRepository.save(any(MedicalRecordEntity.class))).thenReturn(savedEntity);

        MedicalRecord result = gateway.create(record);

        assertEquals(record.getId(), result.getId());
        assertEquals(record.getAppointmentId(), result.getAppointmentId());
        assertEquals(record.getPatientUserId(), result.getPatientUserId());
        assertEquals(record.getDoctorUserId(), result.getDoctorUserId());
        verify(medicalRecordRepository).save(any(MedicalRecordEntity.class));
    }


    @Test
    void testFindByAppointmentId() {
        Long appointmentId = 1L;
        MedicalRecordEntity entity = mock(MedicalRecordEntity.class);
        MedicalRecord record = mock(MedicalRecord.class);

        when(medicalRecordRepository.findTopByAppointmentIdOrderByIdDesc(appointmentId)).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(record);

        Optional<MedicalRecord> result = gateway.findByAppointmentId(appointmentId);

        assertTrue(result.isPresent());
        assertEquals(record, result.get());
    }
}
