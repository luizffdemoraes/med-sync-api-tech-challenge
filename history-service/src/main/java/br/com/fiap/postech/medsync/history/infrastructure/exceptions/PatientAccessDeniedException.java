package br.com.fiap.postech.medsync.history.infrastructure.exceptions;


public class PatientAccessDeniedException extends RuntimeException {
    public PatientAccessDeniedException(String message) {
        super(message);
    }
}