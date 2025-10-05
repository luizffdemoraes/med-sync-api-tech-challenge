package br.com.fiap.postech.medsync.scheduling.infrastructure.gateways;

import br.com.fiap.postech.medsync.scheduling.domain.entities.QueueEvent;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.QueueEventGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.entity.QueueEventEntity;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.repository.QueueEventRepository;
import org.springframework.stereotype.Component;

@Component
public class QueueEventGatewayImpl implements QueueEventGateway {

    private final HistoryEventProducer historyEventProducer;
    private final NotificationEventProducer notificationEventProducer;
    private final QueueEventRepository queueEventRepository;

    public QueueEventGatewayImpl(HistoryEventProducer historyEventProducer,
                                 NotificationEventProducer notificationEventProducer,
                                 QueueEventRepository queueEventRepository) {
        this.historyEventProducer = historyEventProducer;
        this.notificationEventProducer = notificationEventProducer;
        this.queueEventRepository = queueEventRepository;
    }

    @Override
    public QueueEvent saveQueueEvent(QueueEvent queueEvent) {
        QueueEventEntity entity = QueueEventEntity.toEntity(queueEvent);
        QueueEventEntity savedEntity = queueEventRepository.save(entity);
        return savedEntity.toDomain();
    }
}