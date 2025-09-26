package br.com.fiap.postech.medsync.notification.infrastructure.gateways;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.gateways.EmailNotificationGateway;
import br.com.fiap.postech.medsync.notification.infrastructure.exceptions.NotificationSendException;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailNotificationGatewayImpl implements EmailNotificationGateway {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationGatewayImpl.class);

    // Injete o token do Mailtrap a partir das configurações (application.properties)
    @Value("${mailtrap.api-token}")
    private String mailtrapApiToken;

    @Override
    public void send(Notification notification) throws NotificationSendException {
        if (notification.getPatientEmail() == null || notification.getPatientEmail().isEmpty()) {
            throw new NotificationSendException("E-mail do paciente não informado.");
        }
        try {
            MailtrapConfig config = new MailtrapConfig.Builder()
                    .token(mailtrapApiToken)
                    .build();

            MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

            MailtrapMail mail = MailtrapMail.builder()
                    .from(new Address("hello@demomailtrap.co", "MedSync Notificações"))
                    .to(List.of(new Address(notification.getPatientEmail())))
                    .subject("Notificação de consulta")
                    .text(notification.getMessage())
                    .category("Notificações")
                    .build();

            client.send(mail);
        } catch (Exception e) {
            logger.error("Erro ao enviar notificação para {}: {}", notification.getPatientEmail(), e.getMessage(), e);
            throw new NotificationSendException("Falha ao enviar notificação: " + e.getMessage());
        }
    }
}