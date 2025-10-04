package br.com.fiap.postech.medsync.scheduling.infrastructure.gateways;

import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.entity.AppointmentEntity;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.repository.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AppointmentGatewayImpl implements AppointmentGateway {

    private final AppointmentRepository appointmentRepository;

    public AppointmentGatewayImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = AppointmentEntity.toEntity(appointment);
        AppointmentEntity savedEntity = appointmentRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Appointment update(Appointment appointment) {
        AppointmentEntity entity = AppointmentEntity.toEntity(appointment);
        AppointmentEntity updatedEntity = appointmentRepository.save(entity);
        return updatedEntity.toDomain();
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentEntity::toDomain);
    }

    @Override
    public Page<Appointment> findAll(Long patientId, Long doctorId, String status, Pageable pageable) {
        Page<AppointmentEntity> entities;

        if (patientId != null && doctorId != null && status != null) {
            entities = appointmentRepository.findByPatientIdAndDoctorIdAndStatus(patientId, doctorId, status, pageable);
        } else if (patientId != null && doctorId != null) {
            entities = appointmentRepository.findByPatientIdAndDoctorId(patientId, doctorId, pageable);
        } else if (patientId != null && status != null) {
            entities = appointmentRepository.findByPatientIdAndStatus(patientId, status, pageable);
        } else if (doctorId != null && status != null) {
            entities = appointmentRepository.findByDoctorIdAndStatus(doctorId, status, pageable);
        } else if (patientId != null) {
            entities = appointmentRepository.findByPatientId(patientId, pageable);
        } else if (doctorId != null) {
            entities = appointmentRepository.findByDoctorId(doctorId, pageable);
        } else if (status != null) {
            entities = appointmentRepository.findByStatus(status, pageable);
        } else {
            entities = appointmentRepository.findAll(pageable);
        }

        return entities.map(AppointmentEntity::toDomain);
    }

    @Override
    public boolean existsByDoctorAndTime(Long doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        return appointmentRepository.existsByDoctorIdAndAppointmentDateBetweenAndStatusNot(
                doctorId, startTime, endTime, "CANCELLED");
    }

    @Override
    public boolean existsByPatientAndTime(Long patientId, LocalDateTime startTime, LocalDateTime endTime) {
        return appointmentRepository.existsByPatientIdAndAppointmentDateBetweenAndStatusNot(
                patientId, startTime, endTime, "CANCELLED");
    }


    @Override
    public List<Appointment> findByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(AppointmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(AppointmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }
}