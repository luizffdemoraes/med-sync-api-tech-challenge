package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.InvalidPatientIdException;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAppointmentsByStatusUseCaseImp implements GetAppointmentsByStatusUseCase {

    private final MedicalRecordRepositoryGateway repositoryGateway;

    public GetAppointmentsByStatusUseCaseImp(MedicalRecordRepositoryGateway repositoryGateway) {
        this.repositoryGateway = repositoryGateway;
    }

    @Override
    public List<MedicalRecordResponse> execute(Long patientId, String status) {
        if (patientId == null || patientId <= 0) {
            throw new InvalidPatientIdException(patientId);
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        List<MedicalRecordResponse> records = repositoryGateway.findByPatientIdAndStatus(patientId, status)
                .stream()
                .map(MedicalRecordResponse::fromDomain)
                .toList();

        if (records.isEmpty()) {
            throw new MedicalRecordNotFoundException("No appointments found for patient " + patientId + " with status " + status);
        }

        return records;
    }
}