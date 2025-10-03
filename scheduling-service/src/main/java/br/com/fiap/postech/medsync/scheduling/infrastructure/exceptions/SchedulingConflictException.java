package br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions;

public class SchedulingConflictException extends RuntimeException {
    public SchedulingConflictException(String message) {
        super(message);
    }
}
