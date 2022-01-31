-- -----------------------------------------------------
-- Table 'sala'
-- -----------------------------------------------------
CREATE TABLE sala (
  id_sala INT NOT NULL AUTO_INCREMENT,
  nome_sala VARCHAR(50) NOT NULL,
  descricao_sala VARCHAR(100) NOT NULL,
  capacidade_sala SMALLINT NOT NULL,
  tipo_quadro_sala VARCHAR(30) NOT NULL,
  utilizar_distribuicao TINYINT NOT NULL,
  utilizar_agendamento TINYINT NOT NULL,
  distribuir TINYINT NOT NULL,
  id_predio TINYINT NOT NULL,
  id_perfil TINYINT NOT NULL,
  PRIMARY KEY (id_sala),
  UNIQUE INDEX nome_sala_UNIQUE (nome_sala ASC) VISIBLE,
  INDEX fk_sala_predio_idx (id_predio ASC) VISIBLE,
  INDEX fk_sala_perfil_sala_turma1_idx (id_perfil ASC) VISIBLE,
  CONSTRAINT fk_sala_predio
    FOREIGN KEY (id_predio)
    REFERENCES predio (id_predio)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_sala_perfil_sala_turma1
    FOREIGN KEY (id_perfil)
    REFERENCES perfil_sala_turma (id_perfil_sala_turma)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
