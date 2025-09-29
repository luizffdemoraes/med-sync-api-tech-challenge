package br.com.fiap.postech.medsync.history.domain.gateways;

import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepositoryGateway {
    MedicalRecord create(MedicalRecord medicalRecord);
    MedicalRecord update(MedicalRecord medicalRecord);
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);
    List<MedicalRecord> findByPatientId(Long patientId);
    List<MedicalRecord> findByPatientIdAndStatus(Long patientId, String status);
}