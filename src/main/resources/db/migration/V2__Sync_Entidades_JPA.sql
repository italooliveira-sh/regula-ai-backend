ALTER TABLE unidades
    ADD COLUMN IF NOT EXISTS cnpj VARCHAR(18) UNIQUE,
    ADD COLUMN IF NOT EXISTS telefone VARCHAR(20),
    ADD COLUMN IF NOT EXISTS email VARCHAR(255),
    ADD COLUMN IF NOT EXISTS endereco VARCHAR(500),
    ADD COLUMN IF NOT EXISTS municipio VARCHAR(100),
    ADD COLUMN IF NOT EXISTS uf VARCHAR(2),
    ADD COLUMN IF NOT EXISTS cep VARCHAR(10),
    ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP;

ALTER TABLE usuarios
    ADD COLUMN IF NOT EXISTS crm_uf VARCHAR(2),
    ADD COLUMN IF NOT EXISTS cpf VARCHAR(14),
    ADD COLUMN IF NOT EXISTS email VARCHAR(255),
    ADD COLUMN IF NOT EXISTS telefone VARCHAR(20),
    ADD COLUMN IF NOT EXISTS especialidade VARCHAR(100),
    ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP;

ALTER TABLE pacientes
    ADD COLUMN IF NOT EXISTS sexo VARCHAR(20),
    ADD COLUMN IF NOT EXISTS telefone VARCHAR(20),
    ADD COLUMN IF NOT EXISTS email VARCHAR(255),
    ADD COLUMN IF NOT EXISTS endereco VARCHAR(500),
    ADD COLUMN IF NOT EXISTS municipio VARCHAR(100),
    ADD COLUMN IF NOT EXISTS uf VARCHAR(2),
    ADD COLUMN IF NOT EXISTS cep VARCHAR(10),
    ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP;

ALTER TABLE procedimentos
    ADD COLUMN IF NOT EXISTS descricao TEXT,
    ADD COLUMN IF NOT EXISTS grupo VARCHAR(100),
    ADD COLUMN IF NOT EXISTS subgrupo VARCHAR(100),
    ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP;

ALTER TABLE service_requests
    RENAME COLUMN patient_id TO paciente_id;

ALTER TABLE service_requests
    ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP,
    ADD COLUMN IF NOT EXISTS criado_por UUID REFERENCES usuarios(id),
    ADD COLUMN IF NOT EXISTS atualizado_por UUID REFERENCES usuarios(id),
    ADD COLUMN IF NOT EXISTS regulador_id UUID REFERENCES usuarios(id),
    ADD COLUMN IF NOT EXISTS unidade_origem_id UUID REFERENCES unidades(id),
    ADD COLUMN IF NOT EXISTS justificativa_regulacao TEXT,
    ADD COLUMN IF NOT EXISTS data_regulacao TIMESTAMP;

ALTER TABLE service_request_details
    ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP;

CREATE INDEX IF NOT EXISTS idx_sr_regulador ON service_requests(regulador_id);
CREATE INDEX IF NOT EXISTS idx_sr_unidade_origem ON service_requests(unidade_origem_id);
CREATE INDEX IF NOT EXISTS idx_sr_data_regulacao ON service_requests(data_regulacao);

UPDATE service_requests
SET criado_em = authored_on
WHERE criado_em IS NULL;