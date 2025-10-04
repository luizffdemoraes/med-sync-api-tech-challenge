package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;

public class CreateAppointmentUseCaseImp implements CreateAppointmentUseCase {

    private final AppointmentGateway appointmentGateway;
    private final HistoryEventProducer historyEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    public CreateAppointmentUseCaseImp(
            AppointmentGateway appointmentGateway,
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer
    ) {
        this.appointmentGateway = appointmentGateway;
        this.historyEventProducer = historyEventProducer;
        this.notificationEventProducer = notificationEventProducer;
    }

    @Override
    public AppointmentDTO execute(CreateAppointmentDTO request) {
        // 1. Converte DTO para entidade do domínio
        Appointment appointment = Appointment.fromCreateDTO(request);

        // 2. Salva entidade no banco via gateway
        Appointment savedAppointment = appointmentGateway.save(appointment);

        // 3. Mapeia entidade para DTO para envio nos producers e retorno
        AppointmentDTO appointmentDTO = AppointmentDTO.fromDomain(savedAppointment);

        // 4. Dispara evento histórico (APPOINTMENT_CREATED)
        historyEventProducer.publishAppointmentCreated(appointmentDTO);

        // 5. Dispara notificação (CREATED)
        notificationEventProducer.publishAppointmentEvent(appointmentDTO, "CREATED");

        // 6. Retorna DTO para controller
        return appointmentDTO;
    }
}