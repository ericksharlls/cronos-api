SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE predio;
TRUNCATE TABLE perfil_sala_turma;
TRUNCATE TABLE periodo;
TRUNCATE TABLE sala;
TRUNCATE TABLE feriado;
TRUNCATE TABLE funcionario;
TRUNCATE TABLE tipo_funcionario;

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

INSERT INTO sala (nome_sala, descricao_sala, capacidade_sala, tipo_quadro_sala, utilizar_distribuicao, utilizar_agendamento, distribuir, id_predio, id_perfil) VALUES 
("A1", "Sala A1", 30, "Branco", true, true, true, 1, 1),
("A2", "Sala A2", 50, "Negro", true, true, true, 1, 1);

INSERT INTO periodo (nome_periodo, descricao_periodo, data_inicio_periodo, data_termino_periodo, is_periodo_letivo, ano_periodo, numero_periodo) VALUES 
('RECESSO ACADÊMICO - 2021.2','RECESSO ACADÊMICO - 2021.2','2021-12-15','2022-03-27',0,2021,4),
('PERÍODO LETIVO - 2022.1','PERÍODO LETIVO de 2022.1','2022-03-28','2022-06-30',1,2022,1);

INSERT INTO feriado (descricao_feriado, data_feriado, id_periodo) VALUES 
('Quinta-feira Santa','2022-04-14',2), ('Paixão de Cristo','2022-04-15',2),
('Sábado de Aleluia','2022-04-16',2), ('Tiradentes','2022-04-21',2),
('Dia Mundial do Trabalho','2022-05-01',2), ('Corpus Christi','2022-06-16',2);

INSERT INTO tipo_funcionario (nome_tipo_funcionario,descricao_tipo_funcionario) VALUES 
('Docente','Perfil Para Docentes'),('Técnico-administrativo','Perfil Para Técnicos-administrativo'),
('Discente','Perfil Para Discentes'),('Terceirizado','Perfil Para Terceirizados');