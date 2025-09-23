-- Criar schemas
CREATE SCHEMA IF NOT EXISTS scheduling;
CREATE SCHEMA IF NOT EXISTS history;
CREATE SCHEMA IF NOT EXISTS notification;

-- ======================
-- Schema: scheduling
-- ======================

CREATE TABLE scheduling.users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    role       VARCHAR(20)         NOT NULL CHECK (role IN ('DOCTOR', 'NURSE', 'PATIENT')),
    name       VARCHAR(200)        NOT NULL,
    email      VARCHAR(150) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE scheduling.patients
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT REFERENCES scheduling.users (id),
    cpf               VARCHAR(11) UNIQUE,
    phone             VARCHAR(20),
    birth_date        DATE,
    address           TEXT,
    emergency_contact VARCHAR(200),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE scheduling.doctors
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES scheduling.users (id),
    crm        VARCHAR(20) UNIQUE,
    specialty  VARCHAR(100),
    department VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE scheduling.appointments
(
    id               BIGSERIAL PRIMARY KEY,
    patient_id       BIGINT REFERENCES scheduling.patients (id),
    doctor_id        BIGINT REFERENCES scheduling.doctors (id),
    appointment_date TIMESTAMP NOT NULL,
    status           VARCHAR(20) DEFAULT 'SCHEDULED'
        CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    notes            TEXT,
    created_by       BIGINT REFERENCES scheduling.users (id),
    created_at       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- ======================
-- Schema: history
-- ======================

CREATE TABLE history.medical_records
(
    id             BIGSERIAL PRIMARY KEY,
    patient_id     BIGINT    NOT NULL,
    appointment_id BIGINT    NOT NULL,
    doctor_id      BIGINT    NOT NULL,
    diagnosis      TEXT,
    prescription   TEXT,
    medical_notes  TEXT,
    symptoms       TEXT,
    treatment      TEXT,
    record_date    TIMESTAMP NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE history.patient_history
(
    id          BIGSERIAL PRIMARY KEY,
    patient_id  BIGINT      NOT NULL,
    record_type VARCHAR(50) NOT NULL,
    description TEXT,
    event_date  TIMESTAMP   NOT NULL,
    details     JSONB,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ======================
-- Schema: notifications
-- ======================
CREATE TABLE notification.notifications
(
    id               BIGSERIAL PRIMARY KEY,
    patient_id       BIGINT       NOT NULL,
    patient_email    VARCHAR(150) NOT NULL,
    appointment_date TIMESTAMP    NOT NULL,
    event_type       VARCHAR(20)  NOT NULL CHECK (event_type IN ('CREATED', 'UPDATED', 'CANCELLED')),
    status           VARCHAR(20)  DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'SENT', 'FAILED')),
    message          TEXT         NOT NULL,
    sent_at          TIMESTAMP    NULL,
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ======================
-- Dados iniciais de teste
-- ======================

INSERT INTO scheduling.users (username, password, role, name, email)
VALUES ('dr.silva', '$2a$10$exampleHash', 'DOCTOR', 'Dr. Carlos Silva', 'carlos.silva@hospital.com'),
       ('enfermeira.ana', '$2a$10$exampleHash', 'NURSE', 'Ana Souza', 'ana.souza@hospital.com'),
       ('paciente.joao', '$2a$10$exampleHash', 'PATIENT', 'João Santos', 'joao.santos@email.com');

INSERT INTO scheduling.doctors (user_id, crm, specialty, department)
VALUES (1, 'CRM-SP123456', 'Cardiologia', 'Cardiologia');

INSERT INTO scheduling.patients (user_id, cpf, phone, birth_date)
VALUES (3, '12345678901', '(11) 99999-9999', '1985-05-15');

INSERT INTO notification.notifications (patient_id, patient_name, patient_email, doctor_name, appointment_date,
                                        event_type, status, message)
VALUES (1, 'João Santos', 'joao.santos@email.com', 'Dr. Carlos Silva', '2024-10-25 14:30:00', 'CREATED', 'SENT',
        'Olá João Santos! Sua consulta com Dr. Carlos Silva está agendada para 25/10/2024 às 14:30.');