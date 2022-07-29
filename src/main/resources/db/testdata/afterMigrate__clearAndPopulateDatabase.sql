SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE predio;
TRUNCATE TABLE perfil_sala_turma;
TRUNCATE TABLE periodo;
TRUNCATE TABLE sala;
TRUNCATE TABLE departamento;
TRUNCATE TABLE tipo_funcionario;
TRUNCATE TABLE funcionario;
TRUNCATE TABLE turma;
TRUNCATE TABLE turma_docente;
TRUNCATE TABLE disponibilidade_sala;
TRUNCATE TABLE horario;
TRUNCATE TABLE agendamento;
TRUNCATE TABLE usuario;
TRUNCATE TABLE papel_usuario;
TRUNCATE TABLE permissao_usuario;
TRUNCATE TABLE historico_importacoes_turmas;
TRUNCATE TABLE importacoes_turmas;
TRUNCATE TABLE status_importacao_turmas;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO predio (nome_predio, descricao_predio) VALUES 
('Prédio Administrativo do CT', 'Prédio Administrativo do Centro de Tecnologia'),
('Setor de Aulas IV', 'Setor de Aulas IV'),
('NTI', 'Núcleo de Tecnologia Industrial'),
('LARHISA', 'Laboratório de Recursos Hídricos e Saneamento Ambiental'),
('CTEC', 'Complexo Tecnológico de Engenharia');

INSERT INTO perfil_sala_turma (nome_perfil_sala_turma, descricao_perfil_sala_turma) VALUES 
('Convencional','Perfil para aulas convencionais'), 
('Laboratório','Perfil para caracterizar realização de aulas de informática'), 
('Prancheta','Perfil para aulas em sala de aula com prancheta');

INSERT INTO periodo (nome_periodo, descricao_periodo, data_inicio_periodo, data_termino_periodo, is_periodo_letivo, ano_periodo, numero_periodo) VALUES 
('PERÍODO LETIVO - 2022.1','PERÍODO LETIVO de 2022.1','2022-02-01','2022-06-18',1,2022,1);

INSERT INTO sala (nome_sala, descricao_sala, capacidade_sala, tipo_quadro_sala, utilizar_distribuicao, utilizar_agendamento, distribuir, id_predio, id_perfil) VALUES 
("A1", "Sala A1", 30, "Branco", true, true, true, 1, 1),
("A2", "Sala A2", 50, "Negro", true, true, true, 1, 1),
("A3", "Sala A3", 35, "Negro", true, true, true, 2, 2),
("A4", "Sala A4", 35, "Branco", true, true, true, 2, 2),
("A5", "Sala A5", 35, "Branco", true, true, true, 2, 2);

INSERT INTO departamento (id_departamento, nome_departamento, descricao_departamento, id_departamento_sigaa) VALUES 
(1,'Departamento de Arquitetura','Departamento do Curso de Arquitetura e Urbanismo',54),
(2,'Departamento de Engenharia Biomédica','Departamento do Curso de Engenharia Biomédica',5205),
(3,'Departamento de Engenharia Civil','Departamento do Curso de Engenharia Civil',52),
(4,'Departamento de Engenharia de Computação e Automação','Departamento do Curso de Engenharia de Computação e Automação',56),
(5,'Departamento de Engenharia de Comunicações','Departamento do Curso de Comunicações',5204),
(6,'Departamento de Engenharia de Materiais','Departamento do Curso de Engenharia de Materiais',1589),
(7,'Departamento de Engenharia de Petróleo','Departamento do Curso de Engenharia de Petróleo',5075),
(8,'Departamento de Engenharia Elétrica','Departamento do Curso de Engenharia Elétrica',112),
(9,'Departamento de Engenharia Mecânica','Departamento do Curso de Engenharia Mecânica',50),
(10,'Departamento de Engenharia Produção','Departamento do Curso de Engenharia de Produção',115),
(11,'Departamento de Engenharia Química','Departamento do Curso de Engenharia Mecânica',57),
(12,'Departamento de Engenharia Têxtil','Departamento do Curso de Engenharia Têxtil',424),
(13,'Coordenação do Curso de Engenharia Mecatrônica','Coordenação do Curso de Engenharia Mecatrônica',5632),
(14,'DIVERSOS','Departamento para uma turma qualquer',0);

INSERT INTO tipo_funcionario (nome_tipo_funcionario,descricao_tipo_funcionario) VALUES 
('Docente','Perfil Para Docentes'),
('Técnico-administrativo','Perfil Para Técnicos-administrativo'),
('Discente','Perfil Para Discentes'),('Terceirizado','Perfil Para Terceirizados');

INSERT INTO funcionario (nome_funcionario, matricula_funcionario, cpf_funcionario, email_funcionario, telefone_funcionario, 
                                ramal_funcionario, id_sigaa_funcionario, id_tipo_funcionario) VALUES 
('Bartô Galeno', '1234567', '123456789-0', 'barto@ufrn.br', '12345-6789', '123', 120110, 1),
('Reginaldo Rossi', '7654321', '32145478999', 'reginaldo@ufrn.br', '4455-6677', '987', 120111, 1),
('Zezo dos Teclados', '8954133', '12198477899', 'zezo@ufrn.br', '2451-2311', '756', 120112, 1);

INSERT INTO usuario (id_usuario, login_usuario, senha_usuario, ativo_usuario, tentativas_login_usuario, id_funcionario) VALUES 
(1, 'barto', MD5('987456'), 1, 0, 1);

INSERT INTO papel_usuario (id_papel, nome_papel, descricao_papel) VALUES (1, 'ROLE_ADMINISTRADOR', 'Perfil para administradores');

INSERT INTO permissao_usuario (id_papel, id_usuario) VALUES (1, 1);

INSERT INTO status_importacao_turmas (id, identificador, descricao) VALUES 
(1, 'CRIADA_AGUARDANDO_EXECUCAO', 'Criada e aguardando execução'),
(2, 'ERRO_NA_EXECUCAO', 'Houve erro na execução'),
(3, 'EXECUTADA_COM_SUCESSO', 'Executada com sucesso');

-- INSERT INTO turma (codigo_componente_turma, nome_componente_turma, nome_docente_turma, horario_turma, capacidade_turma, numero_turma, 
--        alunos_matriculados_turma, distribuir, local, id_perfil, id_predio, id_periodo, id_departamento, id_sala_temp, id_turma_sigaa) VALUES 
--    ('ARQ001', 'ARQUITETURA 01', NULL, '35M12 7M456', 50, 1, 0, 1, 'INDEFINIDO', 1, 2, 1, 1, NULL, 200100),
--    ('ARQ002', 'ARQUITETURA 02', NULL, '25T56', 40, 1, 0, 1, 'INDEFINIDO', 3, 1, 1, 1, NULL, 200101),
--    ('CIV001', 'CIVIL 01', NULL, '36M56', 20, 1, 0, 1, 'INDEFINIDO', 2, 2, 1, 2, NULL, 200102),
--    ('CIV001', 'CIVIL 01', NULL, '24T1234', 30, 2, 0, 1, 'INDEFINIDO', 1, 2, 1, 2, NULL, 200103),
--    ('ELE001', 'ELETRICA 01', NULL, '25N34', 35, 1, 0, 1, 'INDEFINIDO', 3, 2, 1, 3, NULL, 200104);

-- INSERT INTO turma_docente (id_turma, id_docente) VALUES 
-- (1, 1),
-- (1, 2),
-- (2, 1),
-- (3, 3),
-- (4, 2),
-- (4, 3);

INSERT INTO horario (id_horario, horario, inicio_horario, termino_horario, horario_intermediario, turno, inicio_horario_absoluto, termino_horario_absoluto) VALUES
(1, 1, '06:30:00', '07:50:59', '07:26:00', 'M', '07:00:00', '07:50:00'),
(2, 2, '07:51:00', '08:40:59', '08:15:00', 'M', '07:50:00', '08:40:00'),
(3, 3, '08:41:00', '09:45:59', '09:20:00', 'M', '08:55:00', '09:45:00'),
(4, 4, '09:46:00', '10:35:59', '10:10:00', 'M', '09:45:00', '10:35:00'),
(5, 5, '10:36:00', '11:40:59', '11:15:00', 'M', '10:50:00', '11:40:00'),
(6, 6, '11:41:00', '12:30:59', '12:05:00', 'M', '11:40:00', '12:30:00'),
(7, 1, '12:31:00', '13:50:59', '13:25:00', 'T', '13:00:00', '13:50:00'),
(8, 2, '13:51:00', '14:40:59', '13:15:00', 'T', '13:50:00', '14:40:00'),
(9, 3, '14:41:00', '15:45:59', '15:20:00', 'T', '14:55:00', '15:45:00'),
(10, 4, '15:46:00', '16:35:59', '16:10:00', 'T', '15:45:00', '16:35:00'),
(11, 5, '16:36:00', '17:40:59', '17:15:00', 'T', '16:50:00', '17:40:00'),
(12, 6, '17:41:00', '18:30:59', '18:05:00', 'T', '17:40:00', '18:30:00'),
(13, 1, '18:31:00', '19:35:59', '19:10:00', 'N', '18:45:00', '19:35:00'),
(14, 2, '19:36:00', '20:25:59', '20:00:00', 'N', '19:35:00', '20:25:00'),
(15, 3, '20:26:00', '21:25:59', '21:00:00', 'N', '20:35:00', '21:25:00'),
(16, 4, '21:26:00', '23:00:00', '21:50:00', 'N', '21:25:00', '22:15:00');

--INSERT INTO agendamento (motivo, id_funcionario, id_periodo, hora_realizacao_agendamento, id_usuario_sistema) VALUES 
--("Aula", 1, 1, '2022-06-06 12:00:00', 1),
--("Aula Legal", 1, 1, '2022-06-06 12:01:00', 1);

--INSERT INTO disponibilidade_sala (data_reserva, id_sala, id_periodo, id_horario_sala, id_turma, id_agendamento) VALUES 
-- 35M12 7M456
--('2022-06-07', 1, 1, 1, 1, 1),
--('2022-06-09', 1, 1, 2, 1, 1),
--('2022-06-14', 1, 1, 1, 1, 1),
--('2022-06-16', 1, 1, 2, 1, 1),
--('2022-06-21', 1, 1, 1, 1, 1),
--('2022-06-23', 1, 1, 2, 1, 1),
--('2022-06-11', 2, 1, 4, 1, 1),
--('2022-06-11', 2, 1, 5, 1, 1),
--('2022-06-11', 2, 1, 6, 1, 1),
--('2022-06-18', 2, 1, 4, 1, 1),
--('2022-06-18', 2, 1, 5, 1, 1),
--('2022-06-18', 2, 1, 6, 1, 1),
-- 24T1234
--('2022-06-06', 1, 1, 7, 4, 2),
--('2022-06-06', 1, 1, 8, 4, 2),
--('2022-06-06', 1, 1, 9, 4, 2),
--('2022-06-06', 1, 1, 10, 4, 2),

--('2022-06-08', 2, 1, 7, 4, 2),
--('2022-06-08', 2, 1, 8, 4, 2),
--('2022-06-08', 2, 1, 9, 4, 2),
--('2022-06-08', 2, 1, 10, 4, 2),

--('2022-06-13', 1, 1, 7, 4, 2),
--('2022-06-13', 1, 1, 8, 4, 2),
--('2022-06-13', 1, 1, 9, 4, 2),
--('2022-06-13', 1, 1, 10, 4, 2),

--('2022-06-15', 2, 1, 7, 4, 2),
--('2022-06-15', 2, 1, 8, 4, 2),
--('2022-06-15', 2, 1, 9, 4, 2),
--('2022-06-15', 2, 1, 10, 4, 2);

-- INSERT INTO turma (id_turma, codigo_componente_turma, nome_componente_turma, nome_docente_turma, horario_turma,
--  capacidade_turma, numero_turma, alunos_matriculados_turma, distribuir, local, id_perfil, id_predio, id_periodo, id_departamento, id_sala_temp, id_turma_sigaa) VALUES 
-- (3366,'ARQ0002','DESENHO TECNICO','A DEFINIR DOCENTE','24T34',25,'1',0,0,'INDEFINIDO',3,1,1,1,0,0),
-- (3367,'ARQ0002','DESENHO TECNICO','A DEFINIR DOCENTE','4M12',25,'2',0,0,'INDEFINIDO',3,1,1,1,0,0),
-- (3368,'ARQ0002','DESENHO TECNICO','A DEFINIR DOCENTE','35T34',25,'3',0,0,'INDEFINIDO',3,1,1,1,0,0);