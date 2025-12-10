-- Inserir Unidade
INSERT INTO unidades (id, nome, cnes, tipo_unidade) VALUES
('c3d4e5f6-a7b8-9012-cdef-123456789012', 'Hospital Geral de Fortaleza', '1234567', 'HOSPITAL');

-- Inserir Médico Solicitante
INSERT INTO usuarios (id, nome, crm, tipo, unidade_id) VALUES
('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'Dr. Roberto Cláudio', '55555', 'SOLICITANTE', 'c3d4e5f6-a7b8-9012-cdef-123456789012');

-- Inserir Paciente
INSERT INTO pacientes (id, nome, cpf, cartao_sus) VALUES
('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Dona Maria da Silva', '111.222.333-44', '898000000000001');

-- Inserir Procedimento (Catálogo)
-- O backend vai copiar estes dados para o ItemSolicitacao
INSERT INTO procedimentos (id, codigo, nome, sistema) VALUES
('d4e5f6a7-b8c9-0123-defa-234567890123', '0205020097', 'ULTRASSONOGRAFIA MAMARIA BILATERAL', 'SIGTAP');