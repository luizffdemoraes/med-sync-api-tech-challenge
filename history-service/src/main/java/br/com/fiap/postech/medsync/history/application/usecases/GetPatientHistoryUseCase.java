package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import java.util.List;

public interface GetPatientHistoryUseCase {
    List<MedicalRecordResponse> execute(Long patientId);
}