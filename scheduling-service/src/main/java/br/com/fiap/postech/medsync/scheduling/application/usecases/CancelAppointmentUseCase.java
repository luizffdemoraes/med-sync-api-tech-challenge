package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.CancelAppointmentDTO;

public interface CancelAppointmentUseCase {
    /**
     * Cancela uma consulta existente
     * Esta operação muda o status para CANCELLED e dispara eventos
     * de histórico (APPOINTMENT_CANCELLED) e notificação (CANCELLED)
     */
    void execute(Long id, CancelAppointmentDTO request);
}
