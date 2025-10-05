package br.com.fiap.postech.medsync.scheduling.domain.gateways;

import br.com.fiap.postech.medsync.scheduling.domain.entities.QueueEvent;

public interface QueueEventGateway {
    /**
     * Salva evento de fila para auditoria e processamento assíncrono
     */
    QueueEvent saveQueueEvent(QueueEvent queueEvent);
}
