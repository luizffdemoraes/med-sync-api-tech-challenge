package br.com.fiap.postech.medsync.notification.application.usecases;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.gateways.EmailNotificationGateway;
import br.com.fiap.postech.medsync.notification.domain.gateways.NotificationGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreateNotificationUseCaseImpTest {

    @InjectMocks
    private CreateNotificationUseCaseImp createNotificationUseCaseImp;

    @Mock
    private NotificationGateway notificationGateway;


    @BeforeEach
    void setUp() {
        notificationGateway = mock(NotificationGateway.class);
        createNotificationUseCaseImp = new CreateNotificationUseCaseImp(notificationGateway);
    }

    @Test
    void shouldSaveNotificationWhenCreating() {
        Notification notification = Notification.create(
                1L,
                "lffm1994@gmail.com",
                LocalDateTime.of(2025, 9, 28, 14, 0),
                "MANUAL",
                "Olá! Você tem uma consulta agendada para %s. %s"
        );

        when(notificationGateway.save(notification)).thenReturn(notification);

        Notification result = createNotificationUseCaseImp.create(notification);

        verify(notificationGateway, times(1)).save(notification);
        assertEquals(notification, result);
    }
}