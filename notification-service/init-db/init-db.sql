-- Criar schemas
CREATE SCHEMA IF NOT EXISTS notification;

-- ======================
-- Schema: notifications
-- ======================
CREATE TABLE notification.notifications
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
