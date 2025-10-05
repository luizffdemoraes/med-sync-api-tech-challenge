package br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.repository;

import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.entity.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    Page<AppointmentEntity> findByPatientIdAndDoctorIdAndStatus(Long patientId, Long doctorId, String status, Pageable pageable);

    Page<AppointmentEntity> findByPatientIdAndDoctorId(Long patientId, Long doctorId, Pageable pageable);

    Page<AppointmentEntity> findByPatientIdAndStatus(Long patientId, String status, Pageable pageable);

    Page<AppointmentEntity> findByDoctorIdAndStatus(Long doctorId, String status, Pageable pageable);

    Page<AppointmentEntity> findByPatientId(Long patientId, Pageable pageable);

    Page<AppointmentEntity> findByDoctorId(Long doctorId, Pageable pageable);

    Page<AppointmentEntity> findByStatus(String status, Pageable pageable);
}
