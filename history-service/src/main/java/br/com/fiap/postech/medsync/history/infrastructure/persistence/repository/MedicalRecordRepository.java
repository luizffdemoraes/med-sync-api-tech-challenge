package br.com.fiap.postech.medsync.history.infrastructure.persistence.repository;

import br.com.fiap.postech.medsync.history.infrastructure.persistence.entity.MedicalRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {
    Optional<MedicalRecordEntity> findTopByAppointmentIdOrderByIdDesc(Long appointmentId);

    List<MedicalRecordEntity> findByPatientUserIdOrderByAppointmentDateDesc(Long patientUserId);

    List<MedicalRecordEntity> findByPatientUserIdAndStatusOrderByAppointmentDateDesc(Long patientUserId, String status);

}
