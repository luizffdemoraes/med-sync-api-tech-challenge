package br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.handler;

import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.AppointmentNotFoundException;
import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.ErrorResponse;
import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.InvalidAppointmentException;
import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.SchedulingConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotFound(AppointmentNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("APPOINTMENT_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidAppointmentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAppointment(InvalidAppointmentException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_APPOINTMENT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SchedulingConflictException.class)
    public ResponseEntity<ErrorResponse> handleSchedulingConflict(SchedulingConflictException ex) {
        ErrorResponse error = new ErrorResponse("SCHEDULING_CONFLICT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_INPUT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_REQUEST", ex.getMessage());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
