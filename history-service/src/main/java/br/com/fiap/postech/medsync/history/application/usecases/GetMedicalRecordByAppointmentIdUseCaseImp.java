package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;

public class GetMedicalRecordByAppointmentIdUseCaseImp implements GetMedicalRecordByAppointmentIdUseCase {

    private final MedicalRecordRepositoryGateway repositoryGateway;

    public GetMedicalRecordByAppointmentIdUseCaseImp(MedicalRecordRepositoryGateway repositoryGateway) {
        this.repositoryGateway = repositoryGateway;
    }

    @Override
    public MedicalRecordResponse execute(Long appointmentId) {
        return repositoryGateway.findByAppointmentId(appointmentId)
                .map(MedicalRecordResponse::fromDomain)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found for appointmentId: " + appointmentId));
    }
}