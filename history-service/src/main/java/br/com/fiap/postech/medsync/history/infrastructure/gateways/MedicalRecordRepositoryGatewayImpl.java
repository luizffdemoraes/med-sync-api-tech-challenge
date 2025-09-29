package br.com.fiap.postech.medsync.history.infrastructure.gateways;

import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.persistence.entity.MedicalRecordEntity;
import br.com.fiap.postech.medsync.history.infrastructure.persistence.repository.MedicalRecordRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MedicalRecordRepositoryGatewayImpl implements MedicalRecordRepositoryGateway {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordRepositoryGatewayImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public MedicalRecord create(MedicalRecord medicalRecord) {
        MedicalRecordEntity entity = MedicalRecordEntity.fromDomain(medicalRecord);
        MedicalRecordEntity savedEntity = medicalRecordRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public MedicalRecord update(MedicalRecord medicalRecord) {
        MedicalRecordEntity entity = MedicalRecordEntity.fromDomain(medicalRecord);
        MedicalRecordEntity updatedEntity = medicalRecordRepository.save(entity);
        return updatedEntity.toDomain();
    }

    @Override
    public Optional<MedicalRecord> findByAppointmentId(Long appointmentId) {
        return medicalRecordRepository.findByAppointmentId(appointmentId)
                .map(MedicalRecordEntity::toDomain);
    }

    @Override
    public List<MedicalRecord> findByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientUserIdOrderByAppointmentDateDesc(patientId)
                .stream()
                .map(MedicalRecordEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecord> findByPatientIdAndStatus(Long patientId, String status) {
        return medicalRecordRepository.findByPatientUserIdAndStatusOrderByAppointmentDateDesc(patientId, status)
                .stream()
                .map(MedicalRecordEntity::toDomain)
                .collect(Collectors.toList());
    }
}
