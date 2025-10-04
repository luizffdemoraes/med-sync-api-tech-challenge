package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;

public interface CreateAppointmentUseCase {
    /**
     * Cria uma nova consulta
     * Esta operação dispara eventos de histórico (SCHEDULED)
     * e notificação (CREATED) através dos producers
     */
    AppointmentDTO execute(CreateAppointmentDTO request);
}
