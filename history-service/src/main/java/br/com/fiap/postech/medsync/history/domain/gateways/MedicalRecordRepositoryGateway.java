package br.com.fiap.postech.medsync.history.domain.gateways;

import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import java.util.Optional;

public interface MedicalRecordRepositoryGateway {
    MedicalRecord create(MedicalRecord medicalRecord);
    MedicalRecord update(MedicalRecord medicalRecord);
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);
}