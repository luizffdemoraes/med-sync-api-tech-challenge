package br.com.fiap.postech.medsync.notification.infrastructure.persistence.repository;

import br.com.fiap.postech.medsync.notification.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
}
