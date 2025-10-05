package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;

public class CompleteAppointmentUseCaseImp implements CompleteAppointmentUseCase {

    private final AppointmentGateway appointmentGateway;
    private final HistoryEventProducer historyEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    public CompleteAppointmentUseCaseImp(
            AppointmentGateway appointmentGateway,
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer
    ) {
        this.appointmentGateway = appointmentGateway;
        this.historyEventProducer = historyEventProducer;
        this.notificationEventProducer = notificationEventProducer;
    }

    @Override
    public AppointmentDTO execute(Long appointmentId) {
        // 1. Busca a consulta existente
        Appointment existingAppointment = appointmentGateway.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // 2. Marca como completada
        existingAppointment.complete();

        // 3. Atualiza a consulta no banco
        Appointment completedAppointment = appointmentGateway.update(existingAppointment);

        // 4. Mapeia entidade para DTO
        AppointmentDTO appointmentDTO = AppointmentDTO.fromDomain(completedAppointment);

        // 5. Dispara evento histórico (APPOINTMENT_COMPLETED) - ✅ FUNÇÃO CORRETA
        historyEventProducer.publishAppointmentCompleted(appointmentDTO);

        // 6. Dispara notificação (UPDATED) - ✅ FUNÇÃO CORRETA
        notificationEventProducer.publishAppointmentEvent(appointmentDTO, "UPDATED");

        // 7. Retorna DTO para controller
        return appointmentDTO;
    }
}