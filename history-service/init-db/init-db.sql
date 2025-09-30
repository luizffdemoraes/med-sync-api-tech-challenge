-- Criar schemas
CREATE SCHEMA IF NOT EXISTS history;


-- =============================================
-- Tabela: history.medical_records
-- Descrição: Armazena histórico completo de consultas
-- Simplificada e otimizada para consultas GraphQL
-- =============================================

CREATE TABLE history.medical_records
(
    -- Identificador único
    id               BIGSERIAL PRIMARY KEY,

    -- 🔗 IDs de referência (conectam com Auth Service)
    appointment_id   BIGINT      NOT NULL,
    patient_user_id  BIGINT      NOT NULL,
    doctor_user_id   BIGINT      NOT NULL,

    -- 📅 Dados básicos do agendamento
    appointment_date TIMESTAMP   NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED'
        CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    type             VARCHAR(50), -- 'CONSULTA', 'RETORNO', 'EXAME', 'CIRURGIA'

    -- 🩺 DADOS CLÍNICOS SIMPLIFICADOS (preenchidos após consulta)
    chief_complaint  TEXT,        -- Principal queixa do paciente (ex: "Dor de cabeça")
    diagnosis        TEXT,        -- Diagnóstico principal (ex: "Hipertensão arterial")
    prescription     TEXT,        -- Prescrição médica (ex: "Captopril 25mg 2x/dia")
    notes            TEXT,        -- Anotações gerais do médico

    -- ⏰ Metadados de controle
    created_at       TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,

        -- ✅ CONSTRAINT CORRIGIDA: DENTRO dos parênteses da tabela
        CONSTRAINT unique_appointment_id UNIQUE (appointment_id)
);

-- =============================================
-- ÍNDICES para performance das consultas GraphQL
-- =============================================

-- Consultas por paciente (mais comum)
CREATE INDEX idx_medical_records_patient
    ON history.medical_records (patient_user_id, appointment_date DESC);

-- Consultas por data (relatórios, dashboards)
CREATE INDEX idx_medical_records_date
    ON history.medical_records (appointment_date);

-- Consultas por status (ex: listar apenas realizadas)
CREATE INDEX idx_medical_records_status
    ON history.medical_records (status);

-- Consulta combinada (paciente + status)
CREATE INDEX idx_medical_records_patient_status
    ON history.medical_records (patient_user_id, status);

-- =============================================
-- EXEMPLOS DE USO PRÁTICO
-- =============================================

-- 1. Histórico completo de um paciente (GraphQL)
-- SELECT * FROM history.medical_records
-- WHERE patient_user_id = 101
-- ORDER BY appointment_date DESC;

-- 2. Consultas realizadas de um paciente
-- SELECT * FROM history.medical_records
-- WHERE patient_user_id = 101 AND status = 'COMPLETED'
-- ORDER BY appointment_date DESC;

-- 3. Próximas consultas agendadas
-- SELECT * FROM history.medical_records
-- WHERE patient_user_id = 101 AND status = 'SCHEDULED'
-- AND appointment_date > NOW()
-- ORDER BY appointment_date ASC;

