package br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions;


public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(Long id) {
        super("Appointment not found with id: " + id);
    }
}