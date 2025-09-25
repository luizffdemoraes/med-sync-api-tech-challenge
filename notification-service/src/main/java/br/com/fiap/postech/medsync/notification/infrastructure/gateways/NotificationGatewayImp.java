package br.com.fiap.postech.medsync.notification.infrastructure.gateways;

import br.com.fiap.postech.medsync.notification.domain.entities.Notification;
import br.com.fiap.postech.medsync.notification.domain.entities.NotificationStatus;
import br.com.fiap.postech.medsync.notification.domain.gateways.NotificationGateway;
import br.com.fiap.postech.medsync.notification.infrastructure.persistence.entity.NotificationEntity;
import br.com.fiap.postech.medsync.notification.infrastructure.persistence.repository.NotificationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationGatewayImp implements NotificationGateway {

    private final NotificationRepository repository;

    public NotificationGatewayImp(NotificationRepository notificationRepository) {
        this.repository = notificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = toEntity(notification);
        NotificationEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void updateStatus(Long notificationId, NotificationStatus status) {
        repository.findById(notificationId.intValue()).ifPresent(entity -> {
            entity.setStatus(status.name());
            if (status == NotificationStatus.SENT) {
                entity.setSentAt(LocalDateTime.now());
            }
            repository.save(entity);
        });
    }

    // Métodos utilitários para conversão
    private NotificationEntity toEntity(Notification notification) {
        NotificationEntity entity = new NotificationEntity();
        entity.setId(notification.getId());
        entity.setPatientId(notification.getPatientId());
        entity.setPatientEmail(notification.getPatientEmail());
        entity.setAppointmentDate(notification.getAppointmentDate());
        entity.setEventType(notification.getEventType());
        entity.setStatus(notification.getStatus() != null ? notification.getStatus().name() : "PENDING");
        entity.setMessage(notification.getMessage());
        entity.setSentAt(notification.getSentAt());
        entity.setCreatedAt(notification.getCreatedAt());
        return entity;
    }

    private Notification toDomain(NotificationEntity entity) {
        if (entity == null) return null;
        return Notification.of(
                entity.getId(),
                entity.getPatientId(),
                entity.getPatientEmail(),
                entity.getAppointmentDate(),
                entity.getEventType(),
                NotificationStatus.valueOf(entity.getStatus()),
                entity.getMessage(),
                entity.getSentAt(),
                entity.getCreatedAt()
        );
    }
}