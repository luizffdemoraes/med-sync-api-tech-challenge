package br.com.fiap.postech.medsync.notification.infrastructure.messaging;

import br.com.fiap.postech.medsync.notification.application.usecases.CreateNotificationUseCase;
import br.com.fiap.postech.medsync.notification.application.usecases.SendNotificationUseCase;
import br.com.fiap.postech.medsync.notification.application.usecases.UpdateNotificationStatusUseCase;
import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class NotificationMessageConsumerTest {

    @InjectMocks
    private NotificationMessageConsumer consumer;

    @Mock
    private CreateNotificationUseCase createNotificationUseCase;
    @Mock
    private SendNotificationUseCase sendNotificationUseCase;
    @Mock
    private UpdateNotificationStatusUseCase updateNotificationStatusUseCase;

    private NotificationMessageDTO dto;
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dto = new NotificationMessageDTO(1L, "test@email.com",
                LocalDateTime.of(2025, 9, 28, 14, 0),
                "MANUAL", "Mensagem");
        notification = Notification.create(1L, "test@email.com",
                LocalDateTime.of(2025, 9, 28, 14, 0),
                "MANUAL", "Mensagem");
        when(createNotificationUseCase.create(any())).thenReturn(notification);
    }

    @Test
    void receiveMessage_shouldProcessAndSendNotificationSuccessfully() throws Exception {
        doNothing().when(sendNotificationUseCase).send(notification);
        doNothing().when(updateNotificationStatusUseCase).updateStatus(notification.getId(), NotificationStatus.SENT);

        consumer.receiveMessage(dto);

        verify(createNotificationUseCase).create(any());
        verify(sendNotificationUseCase).send(notification);
        verify(updateNotificationStatusUseCase).updateStatus(notification.getId(), NotificationStatus.SENT);
    }

    @Test
    void receiveMessage_shouldUpdateStatusToFailedOnException() throws Exception {
        doThrow(new RuntimeException("Erro ao enviar")).when(sendNotificationUseCase).send(notification);
        doNothing().when(updateNotificationStatusUseCase).updateStatus(notification.getId(), NotificationStatus.FAILED);

        consumer.receiveMessage(dto);

        verify(createNotificationUseCase).create(any());
        verify(sendNotificationUseCase).send(notification);
        verify(updateNotificationStatusUseCase).updateStatus(notification.getId(), NotificationStatus.FAILED);
    }
}
