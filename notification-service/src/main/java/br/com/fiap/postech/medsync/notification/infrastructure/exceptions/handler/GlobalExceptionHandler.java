package br.com.fiap.postech.medsync.notification.infrastructure.exceptions.handler;


import br.com.fiap.postech.medsync.notification.infrastructure.exceptions.NotificationSendException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotificationSendException.class)
    public ResponseEntity<String> handleNotificationSendException(NotificationSendException ex) {
        // Aqui retorna o erro como string, você pode customizar para um objeto se desejar.
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao enviar notificação: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno no servidor: " + ex.getMessage());
    }
}