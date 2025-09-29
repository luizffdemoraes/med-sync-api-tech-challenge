package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;

public interface GetMedicalRecordByAppointmentIdUseCase {
    MedicalRecordResponse execute(Long appointmentId);
}