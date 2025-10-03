package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.UpdateAppointmentDTO;

public interface UpdateAppointmentUseCase {
    AppointmentDTO execute(Long id, UpdateAppointmentDTO request);
}
