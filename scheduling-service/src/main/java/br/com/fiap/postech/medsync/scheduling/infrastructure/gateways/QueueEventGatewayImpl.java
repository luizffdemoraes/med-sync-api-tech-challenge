package br.com.fiap.postech.medsync.scheduling.infrastructure.gateways;

import br.com.fiap.postech.medsync.scheduling.application.dtos.HistoryEventDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.NotificationMessageDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.QueueEvent;
import br.com.fiap.postech.medsync.scheduling.domain.enums.HistoryEventType;
import br.com.fiap.postech.medsync.scheduling.domain.enums.NotificationEventType;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.QueueEventGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.entity.QueueEventEntity;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.repository.QueueEventRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    // ... existing code ...
    @Override
    public void sendHistoryEvent(HistoryEventDTO historyEvent) {
        try {
            historyEventProducer.sendHistoryEvent(historyEvent);

            // Preenche informações do evento de auditoria
            QueueEvent queueEvent = new QueueEvent();
            // Aqui usamos o tipo correto (enum) referente ao evento de histórico
            queueEvent.setHistoryEventType(HistoryEventType.valueOf(historyEvent.getEventType()));
            queueEvent.setPayload(historyEvent.toString()); // Você pode serializar manualmente, se necessário
            queueEvent.setProcessed(true);
            // Opcional: registrar o ID do agendamento, se relevante no seu fluxo
            if(historyEvent.getAppointmentId() != null) {
                queueEvent.setAppointmentId(java.util.UUID.nameUUIDFromBytes(
                        String.valueOf(historyEvent.getAppointmentId()).getBytes())
                );
            }
            saveQueueEvent(queueEvent);

        } catch (Exception e) {
            QueueEvent queueEvent = new QueueEvent();
            queueEvent.setHistoryEventType(HistoryEventType.valueOf(historyEvent.getEventType()));
            queueEvent.setPayload(historyEvent.toString());
            queueEvent.setProcessed(false);
            // Registro do erro
            queueEvent.setSource("Error: " + e.getMessage());
            if(historyEvent.getAppointmentId() != null) {
                queueEvent.setAppointmentId(java.util.UUID.nameUUIDFromBytes(
                        String.valueOf(historyEvent.getAppointmentId()).getBytes())
                );
            }
            saveQueueEvent(queueEvent);
            throw new RuntimeException("Failed to send history event", e);
        }
    }

    @Override
    public void sendNotificationEvent(NotificationMessageDTO notificationEvent) {
        try {
            notificationEventProducer.sendNotificationEvent(notificationEvent);

            QueueEvent queueEvent = new QueueEvent();
            queueEvent.setNotificationEventType(
                    NotificationEventType.valueOf(notificationEvent.getEventType())
            );
            queueEvent.setPayload(notificationEvent.toString());
            queueEvent.setProcessed(true);
            // Opcional: associar appointmentId se quiser rastreabilidade por paciente
            if (notificationEvent.getPatientId() != null) {
                queueEvent.setAppointmentId(java.util.UUID.nameUUIDFromBytes(
                        String.valueOf(notificationEvent.getPatientId()).getBytes()
                ));
            }
            saveQueueEvent(queueEvent);

        } catch (Exception e) {
            QueueEvent queueEvent = new QueueEvent();
            queueEvent.setNotificationEventType(
                    NotificationEventType.valueOf(notificationEvent.getEventType())
            );
            queueEvent.setPayload(notificationEvent.toString());
            queueEvent.setProcessed(false);
            queueEvent.setSource("Error: " + e.getMessage());
            // Opcional: preencher appointmentId também aqui
            if (notificationEvent.getPatientId() != null) {
                queueEvent.setAppointmentId(java.util.UUID.nameUUIDFromBytes(
                        String.valueOf(notificationEvent.getPatientId()).getBytes()
                ));
            }
            saveQueueEvent(queueEvent);
            throw new RuntimeException("Failed to send notification event", e);
        }
    }

    @Override
    public QueueEvent saveQueueEvent(QueueEvent queueEvent) {
        QueueEventEntity entity = QueueEventEntity.toEntity(queueEvent);
        QueueEventEntity savedEntity = queueEventRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public List<QueueEvent> findQueueEventsByTypeAndStatus(String eventType, String status) {
        return queueEventRepository.findByEventTypeAndStatus(eventType, status).stream()
                .map(QueueEventEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void updateQueueEventStatus(Long eventId, String status) {
        queueEventRepository.findById(eventId).ifPresent(entity -> {
            entity.setStatus(status);
            queueEventRepository.save(entity);
        });
    }
}