CREATE TABLE paciente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    cartao_sus VARCHAR(50) UNIQUE,
    data_nascimento DATE,
    sexo VARCHAR(20),
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    crm VARCHAR(50) NOT NULL UNIQUE,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    papel VARCHAR(50) NOT NULL,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

CREATE TABLE unidade (
     id BIGSERIAL PRIMARY KEY,
     nome VARCHAR(255) NOT NULL,
     codigo VARCHAR(50) UNIQUE,
     criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
     atualizado_em TIMESTAMP WITH TIME ZONE
);

CREATE TABLE procedimento (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    -- Armazenamento otimizado dos critérios de encaminhamento como JSONB
    criterios_encaminhamento JSONB,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

CREATE TABLE solicitacao (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    data_solicitacao DATE NOT NULL,
    codigo_cid VARCHAR(10),
    categoria_sub_categoria VARCHAR(255),
    priorizacao VARCHAR(50),
    encaminhar_para_fila_atendimento BOOLEAN NOT NULL DEFAULT FALSE,
    justificativa TEXT,
    observacoes_dados_clinicos TEXT,

    paciente_id BIGINT NOT NULL REFERENCES paciente(id),
    unidade_id BIGINT REFERENCES unidade(id),
    medico_solicitante_id BIGINT NOT NULL REFERENCES usuario(id),
    medico_regulador_id BIGINT REFERENCES usuario(id),

    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE,
    criado_por BIGINT REFERENCES usuario(id),
    atualizado_por BIGINT REFERENCES usuario(id)
);

CREATE TABLE item_procedimento_solicitado (
    id BIGSERIAL PRIMARY KEY,
    observacao TEXT,
    quantidade INTEGER NOT NULL,
    inter BOOLEAN NOT NULL DEFAULT FALSE,
    realizar BOOLEAN NOT NULL DEFAULT FALSE,
    cid10_item VARCHAR(10),

    solicitacao_id BIGINT NOT NULL REFERENCES solicitacao(id),
    procedimento_id BIGINT NOT NULL REFERENCES procedimento(id),

    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);