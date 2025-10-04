package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.UpdateAppointmentDTO;

public interface AddMedicalDataUseCase {
    /**
     * Adiciona dados médicos a uma consulta existente
     * Esta funcionalidade será tratada pelo UpdateAppointmentUseCase
     * através do UpdateAppointmentDTO
     */
    AppointmentDTO execute(Long appointmentId, AppointmentDTO medicalData);
}