package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.UpdateAppointmentDTO;

public interface UpdateAppointmentUseCase {
    /**
     * Atualiza uma consulta existente
     * Esta funcionalidade inclui atualizações de dados médicos e status da consulta
     * através do UpdateAppointmentDTO
     */
    AppointmentDTO execute(Long id, UpdateAppointmentDTO request);
}
