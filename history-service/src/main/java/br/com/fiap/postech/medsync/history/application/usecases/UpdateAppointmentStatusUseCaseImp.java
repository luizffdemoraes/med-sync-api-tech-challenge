package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCancelledEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCompletedEvent;
import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UpdateAppointmentStatusUseCaseImp implements UpdateAppointmentStatusUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateAppointmentStatusUseCaseImp.class);
    private final MedicalRecordRepositoryGateway gateway;

    public UpdateAppointmentStatusUseCaseImp(MedicalRecordRepositoryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(AppointmentCompletedEvent event) {
        updateAppointmentStatus(event.getAppointment().getId(), AppointmentStatus.COMPLETED);
    }

    @Override
    public void execute(AppointmentCancelledEvent event) {
        updateAppointmentStatus(event.getAppointment().getId(), AppointmentStatus.CANCELLED);
    }

    private void updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Optional<MedicalRecord> medicalRecordOpt = gateway.findByAppointmentId(appointmentId);

        if (medicalRecordOpt.isEmpty()) {
            logger.warn("Medical record not found for appointment {}. Cannot update status to {}",
                    appointmentId, status);
            return;
        }

        try {
            MedicalRecord medicalRecord = medicalRecordOpt.get();
            medicalRecord.setStatus(status);

            gateway.update(medicalRecord);
        } catch (Exception e) {
            logger.error("Error updating status for appointment {}: {}",
                    appointmentId, e.getMessage(), e);
        }
    }
}
