package br.com.fiap.postech.medsync.scheduling.domain.gateways;

import br.com.fiap.postech.medsync.scheduling.application.dtos.HistoryEventDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.NotificationMessageDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.QueueEvent;

import java.util.List;

public interface QueueEventGateway {
    /**
     * Publica evento de histórico para o serviço de histórico médico
     */
    void sendHistoryEvent(HistoryEventDTO historyEvent);

    /**
     * Publica evento de notificação para o serviço de notificações
     */
    void sendNotificationEvent(NotificationMessageDTO notificationEvent);

    /**
     * Salva evento de fila para auditoria e processamento assíncrono
     */
    QueueEvent saveQueueEvent(QueueEvent queueEvent);

    /**
     * Busca eventos de fila por tipo e status
     */

    List<QueueEvent> findQueueEventsByTypeAndStatus(String eventType, String status);

    /**
     * Atualiza status de um evento de fila
     */
    void updateQueueEventStatus(Long eventId, String status);
}
