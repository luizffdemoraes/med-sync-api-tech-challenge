package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCreatedEvent;
import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateMedicalRecordUseCaseImp implements CreateMedicalRecordUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateMedicalRecordUseCaseImp.class);
    private final MedicalRecordRepositoryGateway gateway;

    public CreateMedicalRecordUseCaseImp(MedicalRecordRepositoryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(AppointmentCreatedEvent event) {
        AppointmentCreatedEvent.Appointment appointment = event.getAppointment();

        if (gateway.findByAppointmentId(appointment.getId()).isPresent()) {
            logger.warn("Medical record already exists for appointment {}", appointment.getId());
        }

        try {
            MedicalRecord medicalRecord = new MedicalRecord(
                    null,
                    appointment.getId(),
                    appointment.getPatientUserId(),
                    appointment.getDoctorUserId(),
                    appointment.getAppointmentDate(),
                    AppointmentStatus.valueOf(appointment.getStatus()),
                    appointment.getType(),
                    appointment.getNotes()
            );

            gateway.create(medicalRecord);

        } catch (Exception e) {
            logger.error("Error creating medical record for appointment {}: {}",
                    appointment.getId(), e.getMessage(), e);
        }
    }

}
