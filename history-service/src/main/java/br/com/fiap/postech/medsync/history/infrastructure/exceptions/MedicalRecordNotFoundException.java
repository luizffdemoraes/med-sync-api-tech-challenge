package br.com.fiap.postech.medsync.history.infrastructure.exceptions;


public class MedicalRecordNotFoundException extends RuntimeException {
    public MedicalRecordNotFoundException(String message) {
        super(message);
    }

    public MedicalRecordNotFoundException(Long patientId) {
        super("Medical record not found for patient with ID: " + patientId);
    }
}