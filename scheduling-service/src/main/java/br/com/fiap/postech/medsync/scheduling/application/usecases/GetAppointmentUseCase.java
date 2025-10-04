package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;

public interface GetAppointmentUseCase {
    /**
     * Obtém uma consulta específica pelo ID
     * Retorna todos os dados da consulta incluindo histórico e informações médicas
     */
    AppointmentDTO execute(Long id);
}
