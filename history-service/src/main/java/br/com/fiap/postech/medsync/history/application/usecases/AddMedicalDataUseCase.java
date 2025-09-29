package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.MedicalDataAddedEvent;

public interface AddMedicalDataUseCase {
    void execute(MedicalDataAddedEvent event);
}