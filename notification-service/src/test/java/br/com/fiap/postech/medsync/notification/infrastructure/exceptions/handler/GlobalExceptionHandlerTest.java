package br.com.fiap.postech.medsync.notification.infrastructure.exceptions.handler;

import br.com.fiap.postech.medsync.notification.infrastructure.exceptions.NotificationSendException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleNotificationSendException() {
        NotificationSendException ex = new NotificationSendException("Falha ao enviar e-mail");
        ResponseEntity<String> response = handler.handleNotificationSendException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Erro ao enviar notificação: Falha ao enviar e-mail"));
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new Exception("Erro genérico");
        ResponseEntity<String> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Erro interno no servidor: Erro genérico"));
    }
}
