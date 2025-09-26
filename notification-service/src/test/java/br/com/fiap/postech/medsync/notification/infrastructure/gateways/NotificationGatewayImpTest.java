package br.com.fiap.postech.medsync.notification.infrastructure.gateways;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;
import br.com.fiap.postech.medsync.notification.infrastructure.persistence.entity.NotificationEntity;
import br.com.fiap.postech.medsync.notification.infrastructure.persistence.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationGatewayImpTest {

    @InjectMocks
    private NotificationGatewayImp gateway;

    @Mock
    private NotificationRepository repository;

    private NotificationEntity mockEntity;
    private Notification mockNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockNotification = Notification.create(
                1L, "test@email.com",
                LocalDateTime.of(2025, 9, 28, 14, 0),
                "MANUAL", "Mensagem"
        );
        mockEntity = new NotificationEntity();
        mockEntity.setId(1L);
        mockEntity.setPatientId(1L);
        mockEntity.setPatientEmail("test@email.com");
        mockEntity.setAppointmentDate(LocalDateTime.of(2025, 9, 28, 14, 0));
        mockEntity.setEventType("MANUAL");
        mockEntity.setStatus("PENDING");
        mockEntity.setMessage("Mensagem");
        mockEntity.setCreatedAt(mockNotification.getCreatedAt());
    }

    @Test
    void shouldSaveNotification() {
        when(repository.save(any(NotificationEntity.class))).thenReturn(mockEntity);

        Notification saved = gateway.save(mockNotification);

        assertNotNull(saved);
        assertEquals(mockNotification.getPatientEmail(), saved.getPatientEmail());
        verify(repository).save(any(NotificationEntity.class));
    }

    @Test
    void shouldUpdateNotificationStatus() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(mockEntity));
        when(repository.save(any(NotificationEntity.class))).thenReturn(mockEntity);

        gateway.updateStatus(1L, NotificationStatus.SENT);

        verify(repository).findById(anyInt());
        verify(repository).save(any(NotificationEntity.class));
    }
}
