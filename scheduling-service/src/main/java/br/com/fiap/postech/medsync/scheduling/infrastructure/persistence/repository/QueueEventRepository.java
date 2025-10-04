package br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.repository;

import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.entity.QueueEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueEventRepository extends JpaRepository<QueueEventEntity, Long> {
    List<QueueEventEntity> findByEventTypeAndStatus(String eventType, String status);
}
