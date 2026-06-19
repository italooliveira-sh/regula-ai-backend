-- Remodelagem para estrutura baseada na fila de encaminhamentos

-- Remover estrutura FHIR legada
DROP TABLE IF EXISTS service_request_details CASCADE;
DROP TABLE IF EXISTS service_requests CASCADE;
DROP TABLE IF EXISTS procedimentos CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS unidades CASCADE;
DROP TABLE IF EXISTS pacientes CASCADE;

-- Novas tabelas principais
CREATE TABLE pacientes (
    id UUID PRIMARY KEY,
    cod_usuario VARCHAR(20) NOT NULL UNIQUE,
    numero_prontuario VARCHAR(30),
    sexo VARCHAR(10),
    nascimento DATE,
    idade INTEGER,
    bairro VARCHAR(120),
    municipio VARCHAR(120),
    microarea VARCHAR(120),
    equipe VARCHAR(120),
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP
);

CREATE TABLE estabelecimentos (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(500),
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_estabelecimentos_nome_endereco
    ON estabelecimentos(nome, endereco);

CREATE TABLE profissionais (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_profissionais_nome
    ON profissionais(nome);

CREATE TABLE procedimentos (
    id UUID PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    descricao VARCHAR(500),
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP
);

CREATE TABLE diagnosticos (
    id UUID PRIMARY KEY,
    cid_codigo VARCHAR(10) NOT NULL UNIQUE,
    descricao VARCHAR(500),
    criado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE encaminhamentos (
    id UUID PRIMARY KEY,
    cod_consulta VARCHAR(20) NOT NULL UNIQUE,
    paciente_id UUID NOT NULL REFERENCES pacientes(id),
    dt_cadastro TIMESTAMP,
    prioridade VARCHAR(100),
    posicao_fila INTEGER,
    regulacao_flag BOOLEAN,
    busca_ativa_flag BOOLEAN,
    tipo VARCHAR(100),
    situacao VARCHAR(200),
    dt_ultima_alteracao TIMESTAMP,
    motivo_encaminhamento TEXT,
    origem_solicitacao VARCHAR(200),
    processo_automatico_flag BOOLEAN,
    solicitacao_leito BOOLEAN,
    data_controle DATE,
    perfil VARCHAR(100),
    cid_principal_id UUID REFERENCES diagnosticos(id),
    cid_prioritario_id UUID REFERENCES diagnosticos(id),
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_encaminhamentos_paciente
    ON encaminhamentos(paciente_id);
CREATE INDEX IF NOT EXISTS idx_encaminhamentos_prioridade
    ON encaminhamentos(prioridade);
CREATE INDEX IF NOT EXISTS idx_encaminhamentos_situacao
    ON encaminhamentos(situacao);
CREATE INDEX IF NOT EXISTS idx_encaminhamentos_dt_cadastro
    ON encaminhamentos(dt_cadastro);

CREATE TABLE encaminhamento_estabelecimento (
    id UUID PRIMARY KEY,
    encaminhamento_id UUID NOT NULL REFERENCES encaminhamentos(id),
    estabelecimento_id UUID NOT NULL REFERENCES estabelecimentos(id),
    papel VARCHAR(50) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_enc_estab_unico
    ON encaminhamento_estabelecimento(encaminhamento_id, estabelecimento_id, papel);

CREATE TABLE encaminhamento_profissional (
    id UUID PRIMARY KEY,
    encaminhamento_id UUID NOT NULL REFERENCES encaminhamentos(id),
    profissional_id UUID NOT NULL REFERENCES profissionais(id),
    papel VARCHAR(50) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_enc_prof_unico
    ON encaminhamento_profissional(encaminhamento_id, profissional_id, papel);

CREATE TABLE encaminhamento_procedimento (
    id UUID PRIMARY KEY,
    encaminhamento_id UUID NOT NULL REFERENCES encaminhamentos(id),
    procedimento_id UUID NOT NULL REFERENCES procedimentos(id),
    tipo VARCHAR(50) NOT NULL,
    quantidade INTEGER
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_enc_proc_unico
    ON encaminhamento_procedimento(encaminhamento_id, procedimento_id, tipo);

CREATE TABLE agendamentos (
    id UUID PRIMARY KEY,
    encaminhamento_id UUID NOT NULL REFERENCES encaminhamentos(id) UNIQUE,
    data_consulta TIMESTAMP,
    data_reservada TIMESTAMP,
    limite_confirmacao_reserva TIMESTAMP,
    dt_realizacao_agendamento TIMESTAMP
);

CREATE TABLE negativas (
    id UUID PRIMARY KEY,
    encaminhamento_id UUID NOT NULL REFERENCES encaminhamentos(id) UNIQUE,
    motivo_negativa VARCHAR(500),
    data_negativa DATE,
    justificativa_negativa TEXT,
    responsavel_negativa VARCHAR(255),
    justificativa_cancelamento TEXT
);
