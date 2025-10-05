-- Criar schemas
CREATE SCHEMA IF NOT EXISTS scheduling;

-- ======================
-- Schema: scheduling
-- ======================

CREATE TABLE scheduling.appointments
(
    id                  BIGSERIAL PRIMARY KEY,

    -- Dados do Paciente (para notificação) - ✅ ADERENTE AOS EVENTOS
    patient_id          BIGINT       NOT NULL,
    patient_email       VARCHAR(255) NOT NULL,

    -- Dados do Médico (para histórico) - ✅ ADERENTE AOS EVENTOS
    doctor_id           BIGINT       NOT NULL,

    -- Dados do Agendamento - ✅ ADERENTE AOS EVENTOS
    appointment_date    TIMESTAMP    NOT NULL,
    status              VARCHAR(20) DEFAULT 'SCHEDULED'
        CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    type                VARCHAR(50) DEFAULT 'CONSULTATION'
        CHECK (type IN ('CONSULTATION', 'FOLLOW_UP', 'EXAM', 'SURGERY')),
    duration_minutes    INTEGER     DEFAULT 30,
    notes               TEXT,
    cancellation_reason TEXT,

    -- Dados Clínicos (para evento MEDICAL_DATA_ADDED) - ✅ ADERENTE AOS EVENTOS
    chief_complaint     TEXT,
    diagnosis           TEXT,
    prescription        TEXT,
    clinical_notes      TEXT,
    updated_by          BIGINT, -- ✅ NOVO CAMPO PARA MEDICAL_DATA_ADDED

    -- Controle
    created_at          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- Tabela única para eventos das filas
CREATE TABLE scheduling.queue_events
(
    id            BIGSERIAL PRIMARY KEY,
    event_type    VARCHAR(50)  NOT NULL
        CHECK (event_type IN ('NOTIFICATION', 'HISTORY')),
    queue_name    VARCHAR(100) NOT NULL,
    routing_key   VARCHAR(100) NOT NULL,
    message_body  JSONB        NOT NULL,
    status        VARCHAR(20) DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'SENT', 'FAILED')),
    retry_count   INTEGER     DEFAULT 0,
    created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    sent_at       TIMESTAMP,
    error_message TEXT
);
