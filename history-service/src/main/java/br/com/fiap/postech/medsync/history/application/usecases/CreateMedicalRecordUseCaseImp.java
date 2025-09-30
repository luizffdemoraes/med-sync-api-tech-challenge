package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCreatedEvent;
import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CreateMedicalRecordUseCaseImp implements CreateMedicalRecordUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateMedicalRecordUseCaseImp.class);
    private final MedicalRecordRepositoryGateway gateway;

    public CreateMedicalRecordUseCaseImp(MedicalRecordRepositoryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(AppointmentCreatedEvent event) {
        AppointmentCreatedEvent.Appointment appointment = event.getAppointment();

        // ✅ VERIFICA se já existe e ATUALIZA em vez de criar novo
        Optional<MedicalRecord> existingRecord = gateway.findByAppointmentId(appointment.getId());

        if (existingRecord.isPresent()) {
            logger.info("Medical record already exists for appointment {}, updating instead", appointment.getId());

            // Atualiza o registro existente
            MedicalRecord medicalRecord = existingRecord.get();
            medicalRecord.setAppointmentDate(appointment.getAppointmentDate());
            medicalRecord.setStatus(AppointmentStatus.valueOf(appointment.getStatus()));
            medicalRecord.setType(appointment.getType());
            medicalRecord.setNotes(appointment.getNotes()); // Atualiza notas se necessário

            gateway.update(medicalRecord);
            return; // ✅ IMPEDE criação de novo registro
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
            logger.info("Created new medical record for appointment {}", appointment.getId());

        } catch (Exception e) {
            logger.error("Error creating/updating medical record for appointment {}: {}",
                    appointment.getId(), e.getMessage(), e);
        }
    }
}