SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE predio;
TRUNCATE TABLE perfil_sala_turma;
TRUNCATE TABLE periodo;
TRUNCATE TABLE sala;

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