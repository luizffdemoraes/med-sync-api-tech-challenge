-- Criar schemas
CREATE SCHEMA IF NOT EXISTS scheduling;

-- ======================
-- Schema: scheduling
-- ======================

CREATE TABLE scheduling.appointments
(
    id                  BIGSERIAL PRIMARY KEY,

    -- Dados do Paciente (para notificação)
    patient_id          BIGINT       NOT NULL,
    patient_name        VARCHAR(255) NOT NULL,
    patient_email       VARCHAR(255) NOT NULL,
    patient_phone       VARCHAR(20),

    -- Dados do Médico (para histórico)
    doctor_id           BIGINT       NOT NULL,
    doctor_name         VARCHAR(255) NOT NULL,
    doctor_crm          VARCHAR(20),
    doctor_specialty    VARCHAR(100),

    -- Dados do Agendamento
    appointment_date    TIMESTAMP    NOT NULL,
    status              VARCHAR(20) DEFAULT 'SCHEDULED'
        CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    type                VARCHAR(50) DEFAULT 'CONSULTA'
        CHECK (type IN ('CONSULTA', 'RETORNO', 'EXAME', 'CIRURGIA')),
    duration_minutes    INTEGER     DEFAULT 30,
    notes               TEXT,
    cancellation_reason TEXT,

    -- Dados Clínicos (para evento MEDICAL_DATA_ADDED)
    chief_complaint     TEXT,
    diagnosis           TEXT,
    prescription        TEXT,
    clinical_notes      TEXT,
    updated_by          BIGINT,

    -- Controle
    created_by          BIGINT,
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

-- ======================
-- Dados iniciais de teste
-- ======================

-- Dados de exemplo para testes
INSERT INTO scheduling.appointments (patient_id,
                                     patient_user_id,
                                     patient_name,
                                     patient_email,
                                     patient_phone,
                                     doctor_id,
                                     doctor_user_id,
                                     doctor_name,
                                     doctor_crm,
                                     doctor_specialty,
                                     appointment_date,
                                     type,
                                     duration_minutes,
                                     notes)
VALUES (1,
        101,
        'Luiz Moraes',
        'lffm1994@gmail.com',
        '11999998888',
        1,
        201,
        'Dra. Maria Santos',
        'CRM/SP-123456',
        'Cardiologia',
        '2024-04-01 14:30:00',
        'SCHEDULED',
        30,
        'Primeira consulta de rotina');