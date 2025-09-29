package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCompletedEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCancelledEvent;

public interface UpdateAppointmentStatusUseCase {
    void execute(AppointmentCompletedEvent event);
    void execute(AppointmentCancelledEvent event);
}