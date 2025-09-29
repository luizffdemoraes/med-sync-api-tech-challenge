package br.com.fiap.postech.medsync.history.infrastructure.exceptions;


public class InvalidPatientIdException extends RuntimeException {
    public InvalidPatientIdException(String message) {
        super(message);
    }

    public InvalidPatientIdException(Long patientId) {
        super("Invalid patient ID: " + patientId);
    }
}