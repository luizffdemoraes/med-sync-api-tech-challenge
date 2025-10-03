package br.com.fiap.postech.medsync.scheduling.domain.enums;

public enum HistoryEventType {
    APPOINTMENT_CREATED,        // Agendamento criado
    APPOINTMENT_UPDATED,        // Agendamento atualizado
    APPOINTMENT_COMPLETED,      // Agendamento concluído
    APPOINTMENT_CANCELLED,      // Agendamento cancelado
    MEDICAL_DATA_ADDED          // Dados médicos adicionados
}