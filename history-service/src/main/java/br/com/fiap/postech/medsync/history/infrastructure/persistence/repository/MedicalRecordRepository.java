package br.com.fiap.postech.medsync.history.infrastructure.persistence.repository;

import br.com.fiap.postech.medsync.history.infrastructure.persistence.entity.MedicalRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {
    @Query("SELECT m FROM MedicalRecordEntity m WHERE m.appointmentId = :appointmentId")
    Optional<MedicalRecordEntity> findByAppointmentId(@Param("appointmentId") Long appointmentId);
    List<MedicalRecordEntity> findByPatientUserIdOrderByAppointmentDateDesc(Long patientUserId);
    List<MedicalRecordEntity> findByPatientUserIdAndStatusOrderByAppointmentDateDesc(Long patientUserId, String status);

}
