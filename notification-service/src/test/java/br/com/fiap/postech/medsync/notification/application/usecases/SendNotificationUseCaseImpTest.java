package br.com.fiap.postech.medsync.notification.application.usecases;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.gateways.EmailNotificationGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class SendNotificationUseCaseImpTest {

    @InjectMocks
    private SendNotificationUseCaseImp sendNotificationUseCaseImp;

    @Mock
    private EmailNotificationGateway emailNotificationGateway;


    @BeforeEach
    void setUp() {
        emailNotificationGateway = mock(EmailNotificationGateway.class);
        sendNotificationUseCaseImp = new SendNotificationUseCaseImp(emailNotificationGateway);
    }

    @Test
    void shouldSendNotification() throws Exception {
        Notification notification = Notification.create(
                1L,
                "lffm1994@gmail.com",
                LocalDateTime.of(2025, 9, 28, 14, 0),
                "MANUAL",
                "Olá! Você tem uma consulta agendada para %s. %s"
        );

        sendNotificationUseCaseImp.send(notification);

        verify(emailNotificationGateway, times(1)).send(notification);
    }
}