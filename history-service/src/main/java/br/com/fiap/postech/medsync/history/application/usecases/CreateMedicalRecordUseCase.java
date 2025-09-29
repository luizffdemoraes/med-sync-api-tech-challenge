package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCreatedEvent;

public interface CreateMedicalRecordUseCase {
    void execute(AppointmentCreatedEvent event);
}