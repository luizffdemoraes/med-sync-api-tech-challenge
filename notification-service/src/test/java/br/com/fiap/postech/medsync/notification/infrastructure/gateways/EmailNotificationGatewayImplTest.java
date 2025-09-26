package br.com.fiap.postech.medsync.notification.infrastructure.gateways;


import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.infrastructure.exceptions.NotificationSendException;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailNotificationGatewayImplTest {

    private EmailNotificationGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        gateway = new EmailNotificationGatewayImpl();
        ReflectionTestUtils.setField(gateway, "mailtrapApiToken", "fake-token");
    }

    @Test
    void shouldSendEmailSuccessfully() throws Exception {
        Notification notification = Notification.create(
                1L,
                "test@email.com",
                LocalDateTime.of(2025, 9, 28, 14, 0),
                "MANUAL",
                "Olá! Você tem uma consulta agendada para %s."
        );

        MailtrapClient clientMock = Mockito.mock(MailtrapClient.class);

        try (MockedStatic<MailtrapClientFactory> factoryMockedStatic = Mockito.mockStatic(MailtrapClientFactory.class)) {
            factoryMockedStatic.when(() -> MailtrapClientFactory.createMailtrapClient(Mockito.any(MailtrapConfig.class)))
                    .thenReturn(clientMock);

            gateway.send(notification);

            Mockito.verify(clientMock, Mockito.times(1)).send(Mockito.any(MailtrapMail.class));
        }
    }

    @Test
    void shouldThrowExceptionWhenEmailIsMissing() {
        Notification notification = Notification.create(
                1L,
                "",
                LocalDateTime.of(2025, 9, 28, 14, 0),
                "MANUAL",
                "Mensagem"
        );

        NotificationSendException ex = assertThrows(NotificationSendException.class, () -> gateway.send(notification));
        assertTrue(ex.getMessage().contains("E-mail do paciente não informado"));
    }

    @Test
    void shouldThrowExceptionWhenClientFails() {
        Notification notification = Notification.create(
                1L,
                "test@email.com",
                LocalDateTime.of(2025, 9, 28, 14, 0),
                "MANUAL",
                "Mensagem"
        );

        MailtrapClient clientMock = Mockito.mock(MailtrapClient.class);
        Mockito.doThrow(new RuntimeException("Falha")).when(clientMock).send(Mockito.any(MailtrapMail.class));

        try (MockedStatic<MailtrapClientFactory> factoryMockedStatic = Mockito.mockStatic(MailtrapClientFactory.class)) {
            factoryMockedStatic.when(() -> MailtrapClientFactory.createMailtrapClient(Mockito.any(MailtrapConfig.class)))
                    .thenReturn(clientMock);

            NotificationSendException ex = assertThrows(NotificationSendException.class, () -> gateway.send(notification));
            assertTrue(ex.getMessage().contains("Falha ao enviar notificação"));
        }
    }
}
