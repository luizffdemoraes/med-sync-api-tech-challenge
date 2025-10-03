package br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions;

public class InvalidAppointmentException extends RuntimeException {
    public InvalidAppointmentException(String message) {
        super(message);
    }
}
