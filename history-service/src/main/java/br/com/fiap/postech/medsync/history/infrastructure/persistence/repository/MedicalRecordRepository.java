package br.com.fiap.postech.medsync.history.infrastructure.persistence.repository;

import br.com.fiap.postech.medsync.history.infrastructure.persistence.entity.MedicalRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {
    List<MedicalRecordEntity> findByAppointmentId(Long appointmentId);

    List<MedicalRecordEntity> findByPatientUserIdOrderByAppointmentDateDesc(Long patientUserId);

    List<MedicalRecordEntity> findByPatientUserIdAndStatusOrderByAppointmentDateDesc(Long patientUserId, String status);

}
