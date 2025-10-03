package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;

public interface CreateAppointmentUseCase {
    AppointmentDTO execute(CreateAppointmentDTO request);
}
