-- ======================
-- SCHEMAS
-- ======================
CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS scheduling;
CREATE SCHEMA IF NOT EXISTS notification;
CREATE SCHEMA IF NOT EXISTS history;

-- ======================
-- AUTH
-- ======================
CREATE TABLE IF NOT EXISTS auth.roles
(
    id        SERIAL PRIMARY KEY,
    authority VARCHAR(50) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS auth.users
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255)        NOT NULL,
    email            VARCHAR(255) UNIQUE NOT NULL,
    login            VARCHAR(255) UNIQUE NOT NULL,
    password         VARCHAR(255)        NOT NULL,
    last_update_date TIMESTAMP           NOT NULL DEFAULT NOW(),
    street           VARCHAR(255)        NOT NULL,
    number           BIGINT              NOT NULL,
    city             VARCHAR(255)        NOT NULL,
    state            VARCHAR(255)        NOT NULL,
    zip_code         VARCHAR(255)        NOT NULL
    );

CREATE TABLE IF NOT EXISTS auth.user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES auth.users (id),
    FOREIGN KEY (role_id) REFERENCES auth.roles (id)
    );

INSERT INTO auth.roles (authority)
SELECT r.authority
FROM (VALUES ('ROLE_DOCTOR'), ('ROLE_NURSE'), ('ROLE_PATIENT')) AS r(authority)
WHERE NOT EXISTS (SELECT 1 FROM auth.roles ar WHERE ar.authority = r.authority);

-- ======================
-- SCHEDULING
-- ======================
CREATE TABLE IF NOT EXISTS scheduling.appointments
(
    id                  BIGSERIAL PRIMARY KEY,

    -- Dados do Paciente (para notificação)
    patient_id          BIGINT       NOT NULL,
    patient_email       VARCHAR(255) NOT NULL,

    -- Dados do Médico (para histórico)
    doctor_id           BIGINT       NOT NULL,

    -- Dados do Agendamento
    appointment_date    TIMESTAMP    NOT NULL,
    status              VARCHAR(20)  DEFAULT 'SCHEDULED'
    CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    type                VARCHAR(50)  DEFAULT 'CONSULTATION'
    CHECK (type IN ('CONSULTATION', 'FOLLOW_UP', 'EXAM', 'SURGERY')),
    duration_minutes    INTEGER      DEFAULT 30,
    notes               TEXT,
    cancellation_reason TEXT,

    -- Dados Clínicos (evento MEDICAL_DATA_ADDED)
    chief_complaint     TEXT,
    diagnosis           TEXT,
    prescription        TEXT,
    clinical_notes      TEXT,
    updated_by          BIGINT,

    -- Controle
    created_at          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
    );

-- ======================
-- NOTIFICATION
-- ======================
CREATE TABLE IF NOT EXISTS notification.notifications
(
    id               BIGSERIAL PRIMARY KEY,
    patient_id       BIGINT       NOT NULL,
    patient_email    VARCHAR(150) NOT NULL,
    appointment_date TIMESTAMP    NOT NULL,
    event_type       VARCHAR(20)  NOT NULL CHECK (event_type IN ('CREATED', 'UPDATED', 'CANCELLED', 'MANUAL')),
    status           VARCHAR(20)  DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'SENT', 'FAILED')),
    message          TEXT         NOT NULL,
    sent_at          TIMESTAMP    NULL,
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
    );

-- ======================
-- HISTORY (GraphQL)
-- ======================
CREATE TABLE IF NOT EXISTS history.medical_records
(
    id               BIGSERIAL PRIMARY KEY,

    appointment_id   BIGINT      NOT NULL,
    patient_user_id  BIGINT      NOT NULL,
    doctor_user_id   BIGINT      NOT NULL,

    appointment_date TIMESTAMP   NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED'
    CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    type             VARCHAR(50),

    chief_complaint  TEXT,
    diagnosis        TEXT,
    prescription     TEXT,
    notes            TEXT,

    created_at       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT unique_appointment_id UNIQUE (appointment_id)
    );

-- INDEXES para History
CREATE INDEX IF NOT EXISTS idx_medical_records_patient
    ON history.medical_records (patient_user_id, appointment_date DESC);

CREATE INDEX IF NOT EXISTS idx_medical_records_date
    ON history.medical_records (appointment_date);

CREATE INDEX IF NOT EXISTS idx_medical_records_status
    ON history.medical_records (status);

CREATE INDEX IF NOT EXISTS idx_medical_records_patient_status
    ON history.medical_records (patient_user_id, status);