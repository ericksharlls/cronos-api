
CREATE DATABASE db_cronos DEFAULT CHARACTER SET utf8;

-- -----------------------------------------------------
-- TABELA 'predio'
-- -----------------------------------------------------
CREATE TABLE predio (
  id_predio TINYINT NOT NULL AUTO_INCREMENT,
  nome_predio VARCHAR(50) NOT NULL,
  descricao_predio VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_predio)
);

-- -----------------------------------------------------
-- TABELA 'periodo'
-- -----------------------------------------------------
CREATE TABLE periodo (
  id_periodo TINYINT NOT NULL AUTO_INCREMENT,
  nome_periodo VARCHAR(30) NOT NULL,
  descricao_periodo VARCHAR(50) NOT NULL,
  data_inicio_periodo DATE NOT NULL,
  data_termino_periodo DATE NOT NULL,
  is_periodo_letivo TINYINT(1) NOT NULL,
  ano_periodo SMALLINT NOT NULL,
  numero_periodo TINYINT NOT NULL,
  PRIMARY KEY (id_periodo)
);

-- -----------------------------------------------------
-- Tabela 'perfil_sala_turma'
-- -----------------------------------------------------
CREATE TABLE perfil_sala_turma (
  id_perfil_sala_turma TINYINT NOT NULL AUTO_INCREMENT,
  nome_perfil_sala_turma VARCHAR(50) NOT NULL,
  descricao_perfil_sala_turma VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_perfil_sala_turma)
);