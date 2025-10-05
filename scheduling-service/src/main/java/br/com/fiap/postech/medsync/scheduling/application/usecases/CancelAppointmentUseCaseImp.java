package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CancelAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;

public class CancelAppointmentUseCaseImp implements CancelAppointmentUseCase {

    private final AppointmentGateway appointmentGateway;
    private final HistoryEventProducer historyEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    public CancelAppointmentUseCaseImp(
            AppointmentGateway appointmentGateway,
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer
    ) {
        this.appointmentGateway = appointmentGateway;
        this.historyEventProducer = historyEventProducer;
        this.notificationEventProducer = notificationEventProducer;
    }

    @Override
    public void execute(Long id, CancelAppointmentDTO request) {
        // 1. Busca a consulta existente
        Appointment existingAppointment = appointmentGateway.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        // 2. Cancela a consulta com o motivo
        existingAppointment.cancel(request.getCancellationReason());

        // 2.1. Atualiza quem realizou o cancelamento
        existingAppointment.setUpdatedBy(request.getUpdatedBy());

        // 3. Atualiza a consulta no banco
        Appointment cancelledAppointment = appointmentGateway.update(existingAppointment);

        // 4. Converte para DTO para os producers
        AppointmentDTO appointmentDTO = AppointmentDTO.fromDomain(cancelledAppointment);

        // 5. Dispara evento histórico (APPOINTMENT_CANCELLED) - ✅ FUNÇÃO CORRETA
        historyEventProducer.publishAppointmentCancelled(appointmentDTO, request.getCancellationReason());

        // 6. Dispara notificação (CANCELLED) - ✅ FUNÇÃO CORRETA
        notificationEventProducer.publishAppointmentEvent(appointmentDTO, "CANCELLED");
    }
}
