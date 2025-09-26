package br.com.fiap.postech.medsync.notification.application.usecases;


import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;
import br.com.fiap.postech.medsync.notification.domain.gateways.NotificationGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

class UpdateNotificationStatusUseCaseImpTest {

    @InjectMocks
    private UpdateNotificationStatusUseCaseImp updateNotificationStatusUseCaseImp;

    @Mock
    private NotificationGateway notificationGateway;


    @BeforeEach
    void setUp() {
        notificationGateway = mock(NotificationGateway.class);
        updateNotificationStatusUseCaseImp = new UpdateNotificationStatusUseCaseImp(notificationGateway);
    }

    @Test
    void shouldUpdateNotificationStatus() {
        Long notificationId = 1L;
        NotificationStatus status = NotificationStatus.SENT;

        updateNotificationStatusUseCaseImp.updateStatus(notificationId, status);

        verify(notificationGateway, times(1)).updateStatus(notificationId, status);
    }
}