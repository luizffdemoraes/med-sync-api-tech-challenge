package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.InvalidPatientIdException;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPatientHistoryUseCaseImp implements GetPatientHistoryUseCase {

    private final MedicalRecordRepositoryGateway repositoryGateway;

    public GetPatientHistoryUseCaseImp(MedicalRecordRepositoryGateway repositoryGateway) {
        this.repositoryGateway = repositoryGateway;
    }

    @Override
    public List<MedicalRecordResponse> execute(Long patientId) {
        if (patientId == null || patientId <= 0) {
            throw new InvalidPatientIdException(patientId);
        }

        List<MedicalRecordResponse> records = repositoryGateway.findByPatientId(patientId)
                .stream()
                .map(MedicalRecordResponse::fromDomain)
                .toList();

        if (records.isEmpty()) {
            throw new MedicalRecordNotFoundException(patientId);
        }

        return records;
    }
}