package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.MedicalDataRequestDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;

public class AddMedicalDataUseCaseImp implements AddMedicalDataUseCase {

    private final AppointmentGateway appointmentGateway;
    private final HistoryEventProducer historyEventProducer;

    private final NotificationEventProducer notificationEventProducer;

    public AddMedicalDataUseCaseImp(
            AppointmentGateway appointmentGateway,
            HistoryEventProducer historyEventProducer,
            NotificationEventProducer notificationEventProducer
    ) {
        this.appointmentGateway = appointmentGateway;
        this.historyEventProducer = historyEventProducer;
        this.notificationEventProducer = notificationEventProducer;
    }

    @Override
    public AppointmentDTO execute(Long appointmentId, MedicalDataRequestDTO medicalData) {
        // 1. Busca a consulta existente
        Appointment existingAppointment = appointmentGateway.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // 2. Adiciona/atualiza os dados médicos na entidade
        existingAppointment.addMedicalData(
                medicalData.getChiefComplaint(),
                medicalData.getDiagnosis(),
                medicalData.getPrescription(),
                medicalData.getClinicalNotes(),
                medicalData.getUpdatedBy()
        );

        // 3. Atualiza a consulta no banco
        Appointment updatedAppointment = appointmentGateway.update(existingAppointment);

        // 4. Mapeia entidade para DTO
        AppointmentDTO appointmentDTO = AppointmentDTO.fromDomain(updatedAppointment);

        // 5. Dispara evento histórico (MEDICAL_DATA_ADDED)
        historyEventProducer.publishMedicalDataAdded(appointmentId, medicalData);

        // 6. Dispara notificação (UPDATED)
        notificationEventProducer.publishAppointmentEvent(appointmentDTO, "UPDATED");

        // 7. Retorna DTO para controller
        return appointmentDTO;
    }
}
