package br.com.fiap.postech.medsync.scheduling.application.usecases;


import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;

public interface CompleteAppointmentUseCase {
    /**
     * Marca uma consulta como concluída
     * Esta operação atualiza o status da consulta para finalizado
     * e pode disparar eventos relacionados ao término
     */
    AppointmentDTO execute(Long appointmentId, Long updatedBy);
}
