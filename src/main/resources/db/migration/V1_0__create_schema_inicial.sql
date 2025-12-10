-- V2__Reestruturacao_FHIR_Snapshot.sql

-- 1. Limpar tabelas antigas (cuidado: apaga dados existentes)
DROP TABLE IF EXISTS item_procedimento_solicitado CASCADE;
DROP TABLE IF EXISTS solicitacao CASCADE;
DROP TABLE IF EXISTS procedimento CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS unidade CASCADE;
DROP TABLE IF EXISTS paciente CASCADE;
-- Apague também as novas se já tiverem sido criadas erradas
DROP TABLE IF EXISTS service_request_details CASCADE;
DROP TABLE IF EXISTS service_requests CASCADE;
DROP TABLE IF EXISTS procedimentos CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS unidades CASCADE;
DROP TABLE IF EXISTS pacientes CASCADE;

-- 2. Recriar com a NOVA estrutura (UUID e FHIR)

CREATE TABLE unidades (
                          id UUID PRIMARY KEY,
                          nome VARCHAR(255) NOT NULL,
                          cnes VARCHAR(20),
                          tipo_unidade VARCHAR(100),
                          ativo BOOLEAN DEFAULT TRUE,
                          criado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE usuarios (
                          id UUID PRIMARY KEY,
                          nome VARCHAR(255) NOT NULL,
                          crm VARCHAR(20),
                          tipo VARCHAR(50) NOT NULL, -- SOLICITANTE, REGULADOR
                          ativo BOOLEAN DEFAULT TRUE,
                          unidade_id UUID REFERENCES unidades(id),
                          criado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE pacientes (
                           id UUID PRIMARY KEY,
                           nome VARCHAR(255) NOT NULL,
                           cpf VARCHAR(14) NOT NULL UNIQUE,
                           cartao_sus VARCHAR(20),
                           data_nascimento DATE,
                           ativo BOOLEAN DEFAULT TRUE,
                           criado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE procedimentos (
                               id UUID PRIMARY KEY,
                               codigo VARCHAR(20) NOT NULL UNIQUE, -- ex: 0205020097
                               nome VARCHAR(500) NOT NULL,
                               sistema VARCHAR(50) NOT NULL, -- ex: SIGTAP
                               ativo BOOLEAN DEFAULT TRUE,
                               criado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE service_requests (
                                  id UUID PRIMARY KEY,
-- Campos FHIR
                                  intent VARCHAR(20) NOT NULL, -- order
                                  status VARCHAR(20) NOT NULL, -- statusFhir
                                  priority VARCHAR(20),
                                  authored_on TIMESTAMP NOT NULL,

    -- Campos Flattened (Categoria e Diagnóstico)
                                  category_system VARCHAR(255),
                                  category_code VARCHAR(50),
                                  category_display VARCHAR(255),
                                  reason_system VARCHAR(255),
                                  reason_code VARCHAR(10),
                                  reason_display VARCHAR(500),

    -- Negócio
                                  note TEXT,
                                  queue_flag BOOLEAN,
                                  status_regulacao VARCHAR(50), -- PENDENTE, REGULADA

    -- Relacionamentos
                                  patient_id UUID NOT NULL REFERENCES pacientes(id),
                                  requester_id UUID NOT NULL REFERENCES usuarios(id),
                                  performer_id UUID NOT NULL REFERENCES unidades(id),

                                  criado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE service_request_details (
                                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- PostgreSQL gen
                                         service_request_id UUID NOT NULL REFERENCES service_requests(id),

    -- SNAPSHOT (Sem FK para procedimentos)
                                         code VARCHAR(20) NOT NULL,
                                         display VARCHAR(500) NOT NULL,
                                         code_system VARCHAR(100) NOT NULL,
                                         quantity INTEGER DEFAULT 1,

                                         criado_em TIMESTAMP DEFAULT NOW()
);