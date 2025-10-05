package br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.handler;

import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.AppointmentNotFoundException;
import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.ErrorResponse;
import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.InvalidAppointmentException;
import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.SchedulingConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleAppointmentNotFound() {
        var ex = new AppointmentNotFoundException("Agendamento não encontrado");
        ResponseEntity<ErrorResponse> response = handler.handleAppointmentNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("APPOINTMENT_NOT_FOUND", response.getBody().getCode());
        assertEquals("Agendamento não encontrado", response.getBody().getMessage());
    }

    @Test
    void testHandleInvalidAppointment() {
        var ex = new InvalidAppointmentException("Agendamento inválido");
        ResponseEntity<ErrorResponse> response = handler.handleInvalidAppointment(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("INVALID_APPOINTMENT", response.getBody().getCode());
        assertEquals("Agendamento inválido", response.getBody().getMessage());
    }

    @Test
    void testHandleSchedulingConflict() {
        var ex = new SchedulingConflictException("Conflito de agendamento");
        ResponseEntity<ErrorResponse> response = handler.handleSchedulingConflict(ex);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("SCHEDULING_CONFLICT", response.getBody().getCode());
        assertEquals("Conflito de agendamento", response.getBody().getMessage());
    }

    @Test
    void testHandleIllegalArgument() {
        var ex = new IllegalArgumentException("Entrada inválida");
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("INVALID_INPUT", response.getBody().getCode());
        assertEquals("Entrada inválida", response.getBody().getMessage());
    }
}