package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;

public interface GetAppointmentUseCase {
    AppointmentDTO execute(Long id);
}
