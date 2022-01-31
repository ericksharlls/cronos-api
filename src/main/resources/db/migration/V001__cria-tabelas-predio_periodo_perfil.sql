
-- -----------------------------------------------------
-- TABELA 'predio'
-- -----------------------------------------------------
CREATE TABLE predio (
  id_predio TINYINT NOT NULL AUTO_INCREMENT,
  nome_predio VARCHAR(50) NOT NULL,
  descricao_predio VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_predio),
  UNIQUE INDEX nome_predio_UNIQUE (nome_predio ASC) VISIBLE
) engine=InnoDB default charset utf8;

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
  PRIMARY KEY (id_periodo),
  UNIQUE INDEX nome_periodo_UNIQUE (nome_periodo ASC) VISIBLE
) engine=InnoDB default charset utf8;

-- -----------------------------------------------------
-- Tabela 'perfil_sala_turma'
-- -----------------------------------------------------
CREATE TABLE perfil_sala_turma (
  id_perfil_sala_turma TINYINT NOT NULL AUTO_INCREMENT,
  nome_perfil_sala_turma VARCHAR(50) NOT NULL,
  descricao_perfil_sala_turma VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_perfil_sala_turma),
  UNIQUE INDEX nome_perfil_sala_turma_UNIQUE (nome_perfil_sala_turma ASC) VISIBLE
) engine=InnoDB default charset utf8;
