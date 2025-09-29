package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.messaging.MedicalDataAddedEvent;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AddMedicalDataUseCaseImp implements AddMedicalDataUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AddMedicalDataUseCaseImp.class);
    private final MedicalRecordRepositoryGateway gateway;

    public AddMedicalDataUseCaseImp(MedicalRecordRepositoryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(MedicalDataAddedEvent event) {
        Optional<MedicalRecord> medicalRecordOpt = gateway.findByAppointmentId(event.getAppointmentId());

        if (medicalRecordOpt.isEmpty()) {
            logger.error("Medical record not found for appointment {}", event.getAppointmentId());
        }

        MedicalRecord medicalRecord = medicalRecordOpt.get();

        medicalRecord.setChiefComplaint(event.getClinicalData().getChiefComplaint());
        medicalRecord.setDiagnosis(event.getClinicalData().getDiagnosis());
        medicalRecord.setPrescription(event.getClinicalData().getPrescription());
        medicalRecord.setNotes(event.getClinicalData().getNotes());

        gateway.update(medicalRecord);
    }
}
