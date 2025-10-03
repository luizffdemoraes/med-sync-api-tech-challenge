package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.CancelAppointmentDTO;

public interface CancelAppointmentUseCase {
    void execute(Long id, CancelAppointmentDTO request);
}
