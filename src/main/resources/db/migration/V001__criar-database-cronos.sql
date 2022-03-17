-- MySQL dump 10.13  Distrib 5.5.62, for linux-glibc2.12 (x86_64)
--
-- Host: localhost    Database: db_cronos
-- ------------------------------------------------------
-- Server version	5.5.62

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `db_cronos`
--

--
-- Table structure for table `agendamento`
--

DROP TABLE IF EXISTS `agendamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agendamento` (
  `id_agendamento` mediumint(9) NOT NULL AUTO_INCREMENT,
  `motivo` varchar(50) NOT NULL,
  `id_funcionario` smallint(6) NOT NULL,
  `id_periodo` tinyint(4) NOT NULL,
  `hora_realizacao_agendamento` datetime DEFAULT NULL,
  `id_usuario_sistema` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id_agendamento`),
  KEY `fk_agendamento_funcionario1_idx` (`id_funcionario`),
  KEY `fk_agendamento_periodo1_idx` (`id_periodo`),
  KEY `id_usuario_sistema` (`id_usuario_sistema`),
  CONSTRAINT `agendamento_ibfk_1` FOREIGN KEY (`id_usuario_sistema`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_agendamento_funcionario1` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`),
  CONSTRAINT `fk_agendamento_periodo1` FOREIGN KEY (`id_periodo`) REFERENCES `periodo` (`id_periodo`)
) ENGINE=InnoDB AUTO_INCREMENT=16662 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `chave`
--

DROP TABLE IF EXISTS `chave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chave` (
  `id_chave` smallint(6) NOT NULL AUTO_INCREMENT,
  `codigo_chave` varchar(20) NOT NULL,
  `id_sala` smallint(6) NOT NULL,
  `descricao_chave` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_chave`),
  UNIQUE KEY `codigo_chave_UNIQUE` (`codigo_chave`),
  KEY `fk_chave_sala1_idx` (`id_sala`),
  CONSTRAINT `fk_chave_sala1` FOREIGN KEY (`id_sala`) REFERENCES `sala` (`id_sala`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `departamento`
--

DROP TABLE IF EXISTS `departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `departamento` (
  `id_departamento` mediumint(9) NOT NULL AUTO_INCREMENT,
  `nome_departamento` varchar(90) DEFAULT NULL,
  `descricao_departamento` varchar(110) DEFAULT NULL,
  `id_departamento_sigaa` mediumint(9) NOT NULL,
  PRIMARY KEY (`id_departamento`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `disponibilidade_sala`
--

DROP TABLE IF EXISTS `disponibilidade_sala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disponibilidade_sala` (
  `id_disponibilidade_sala` mediumint(9) NOT NULL AUTO_INCREMENT,
  `data_reserva` date NOT NULL,
  `id_sala` smallint(6) NOT NULL,
  `id_periodo` tinyint(4) NOT NULL,
  `id_horario_sala` smallint(6) NOT NULL,
  `id_turma` smallint(6) DEFAULT NULL,
  `id_agendamento` mediumint(9) DEFAULT NULL,
  PRIMARY KEY (`id_disponibilidade_sala`),
  UNIQUE KEY `disponibilidade_unica` (`id_sala`,`data_reserva`,`id_horario_sala`),
  KEY `fk_disponibilidade_dia_periodo1_idx` (`id_periodo`),
  KEY `fk_disponibilidade_dia_sala1_idx` (`id_sala`),
  KEY `fk_disponibilidade_sala_turma1_idx` (`id_turma`),
  KEY `fk_disponibilidade_sala_agendamento1_idx` (`id_agendamento`),
  KEY `fk_disponibilidade_sala_horario1_idx` (`id_horario_sala`),
  CONSTRAINT `fk_disponibilidade_dia_periodo1` FOREIGN KEY (`id_periodo`) REFERENCES `periodo` (`id_periodo`),
  CONSTRAINT `fk_disponibilidade_dia_sala1` FOREIGN KEY (`id_sala`) REFERENCES `sala` (`id_sala`),
  CONSTRAINT `fk_disponibilidade_sala_agendamento1` FOREIGN KEY (`id_agendamento`) REFERENCES `agendamento` (`id_agendamento`),
  CONSTRAINT `fk_disponibilidade_sala_horario1` FOREIGN KEY (`id_horario_sala`) REFERENCES `horario` (`id_horario`),
  CONSTRAINT `fk_disponibilidade_sala_turma1` FOREIGN KEY (`id_turma`) REFERENCES `turma` (`id_turma`)
) ENGINE=InnoDB AUTO_INCREMENT=970339 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `disponibilidade_sala_agendamento_backup`
--

DROP TABLE IF EXISTS `disponibilidade_sala_agendamento_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disponibilidade_sala_agendamento_backup` (
  `id_disponibilidade_sala` mediumint(9) NOT NULL AUTO_INCREMENT,
  `data_reserva` date NOT NULL,
  `id_sala` smallint(6) NOT NULL,
  `id_periodo` tinyint(4) NOT NULL,
  `id_horario_sala` smallint(6) NOT NULL,
  `id_turma` smallint(6) DEFAULT NULL,
  `id_agendamento` mediumint(9) DEFAULT NULL,
  PRIMARY KEY (`id_disponibilidade_sala`),
  KEY `fk_disponibilidade_sala_backup_sala1_idx` (`id_sala`),
  KEY `fk_disponibilidade_sala_backup_periodo1_idx` (`id_periodo`),
  KEY `fk_disponibilidade_sala_backup_horario1_idx` (`id_horario_sala`),
  KEY `fk_disponibilidade_sala_backup_turma1_idx` (`id_turma`),
  KEY `fk_disponibilidade_sala_backup_agendamento1_idx` (`id_agendamento`),
  CONSTRAINT `fk_disponibilidade_sala_backup_agendamento1` FOREIGN KEY (`id_agendamento`) REFERENCES `agendamento` (`id_agendamento`),
  CONSTRAINT `fk_disponibilidade_sala_backup_horario1` FOREIGN KEY (`id_horario_sala`) REFERENCES `horario` (`id_horario`),
  CONSTRAINT `fk_disponibilidade_sala_backup_periodo1` FOREIGN KEY (`id_periodo`) REFERENCES `periodo` (`id_periodo`),
  CONSTRAINT `fk_disponibilidade_sala_backup_sala1` FOREIGN KEY (`id_sala`) REFERENCES `sala` (`id_sala`),
  CONSTRAINT `fk_disponibilidade_sala_backup_turma1` FOREIGN KEY (`id_turma`) REFERENCES `turma` (`id_turma`)
) ENGINE=InnoDB AUTO_INCREMENT=82322 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `feriado`
--

DROP TABLE IF EXISTS `feriado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feriado` (
  `id_feriado` smallint(6) NOT NULL AUTO_INCREMENT,
  `descricao_feriado` varchar(50) NOT NULL,
  `data_feriado` date NOT NULL,
  `id_periodo` tinyint(4) NOT NULL,
  PRIMARY KEY (`id_feriado`),
  UNIQUE KEY `data_feriado_unique` (`data_feriado`),
  KEY `fk_feriado_periodo1_idx` (`id_periodo`),
  CONSTRAINT `fk_feriado_periodo1` FOREIGN KEY (`id_periodo`) REFERENCES `periodo` (`id_periodo`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `funcionario` (
  `id_funcionario` smallint(6) NOT NULL AUTO_INCREMENT,
  `nome_funcionario` varchar(60) NOT NULL,
  `matricula_funcionario` varchar(11) DEFAULT NULL,
  `cpf_funcionario` varchar(11) DEFAULT NULL,
  `email_funcionario` varchar(50) DEFAULT NULL,
  `telefone_funcionario` varchar(11) DEFAULT NULL,
  `ramal_funcionario` varchar(3) DEFAULT NULL,
  `id_sigaa_funcionario` int(11) DEFAULT NULL,
  `id_tipo_funcionario` tinyint(4) DEFAULT '3',
  PRIMARY KEY (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=1667 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `historico_chave`
--

DROP TABLE IF EXISTS `historico_chave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_chave` (
  `id_historico_chave` mediumint(9) NOT NULL AUTO_INCREMENT,
  `hora_realizacao_operacao` datetime NOT NULL,
  `id_chave` smallint(6) NOT NULL,
  `id_operacao` smallint(6) NOT NULL,
  `id_usuario` smallint(6) DEFAULT NULL,
  `id_responsavel` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id_historico_chave`),
  KEY `fk_historico_chave_chave1_idx` (`id_chave`),
  KEY `fk_historico_chave_operacao1_idx` (`id_operacao`),
  KEY `fk_historico_chave_usuario1_idx` (`id_usuario`),
  KEY `fk_historico_chave_funcionario1` (`id_responsavel`),
  CONSTRAINT `fk_historico_chave_chave1` FOREIGN KEY (`id_chave`) REFERENCES `chave` (`id_chave`),
  CONSTRAINT `fk_historico_chave_funcionario1` FOREIGN KEY (`id_responsavel`) REFERENCES `funcionario` (`id_funcionario`),
  CONSTRAINT `fk_historico_chave_operacao1` FOREIGN KEY (`id_operacao`) REFERENCES `operacao` (`id_operacao`),
  CONSTRAINT `fk_historico_chave_usuario1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=58985 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `horario`
--

DROP TABLE IF EXISTS `horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `horario` (
  `id_horario` smallint(6) NOT NULL AUTO_INCREMENT,
  `horario` tinyint(4) NOT NULL,
  `inicio_horario` time NOT NULL,
  `termino_horario` time NOT NULL,
  `horario_intermediario` time NOT NULL,
  `turno` char(1) NOT NULL,
  `inicio_horario_absoluto` time NOT NULL,
  `termino_horario_absoluto` time NOT NULL,
  PRIMARY KEY (`id_horario`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `operacao`
--

DROP TABLE IF EXISTS `operacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operacao` (
  `id_operacao` smallint(6) NOT NULL AUTO_INCREMENT,
  `nome_operacao` varchar(20) NOT NULL,
  PRIMARY KEY (`id_operacao`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `papel_usuario`
--

DROP TABLE IF EXISTS `papel_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `papel_usuario` (
  `id_papel` tinyint(4) NOT NULL AUTO_INCREMENT,
  `nome_papel` varchar(40) NOT NULL,
  `descricao_papel` varchar(60) NOT NULL,
  PRIMARY KEY (`id_papel`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `parametros_relatorios`
--

DROP TABLE IF EXISTS `parametros_relatorios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametros_relatorios` (
  `id_parametros_relatorios` tinyint(4) NOT NULL,
  `identificador_parametro_relatorios` varchar(40) NOT NULL,
  `texto_parametro_relatorios` varchar(60) NOT NULL,
  PRIMARY KEY (`id_parametros_relatorios`),
  UNIQUE KEY `identificador_unico` (`identificador_parametro_relatorios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `perfil_sala_turma`
--

DROP TABLE IF EXISTS `perfil_sala_turma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfil_sala_turma` (
  `id_perfil_sala_turma` tinyint(4) NOT NULL AUTO_INCREMENT,
  `nome_perfil_sala_turma` varchar(50) NOT NULL,
  `descricao_perfil_sala_turma` varchar(100) NOT NULL,
  PRIMARY KEY (`id_perfil_sala_turma`),
  UNIQUE KEY `nome_perfil_sala_turma_UNIQUE` (`nome_perfil_sala_turma`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `periodo`
--

DROP TABLE IF EXISTS `periodo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `periodo` (
  `id_periodo` tinyint(4) NOT NULL AUTO_INCREMENT,
  `nome_periodo` varchar(30) NOT NULL,
  `descricao_periodo` varchar(50) NOT NULL,
  `data_inicio_periodo` date NOT NULL,
  `data_termino_periodo` date NOT NULL,
  `is_periodo_letivo` tinyint(1) NOT NULL,
  `ano_periodo` smallint(6) NOT NULL,
  `numero_periodo` tinyint(4) NOT NULL,
  PRIMARY KEY (`id_periodo`),
  UNIQUE KEY `nome_periodo_UNIQUE` (`nome_periodo`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `permissao_usuario`
--

DROP TABLE IF EXISTS `permissao_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissao_usuario` (
  `id_papel` tinyint(4) NOT NULL,
  `id_usuario` smallint(6) NOT NULL,
  PRIMARY KEY (`id_papel`,`id_usuario`),
  KEY `fk_papel_usuario_has_usuario_usuario1_idx` (`id_usuario`),
  KEY `fk_papel_usuario_has_usuario_papel_usuario1_idx` (`id_papel`),
  CONSTRAINT `fk_papel_usuario_has_usuario_papel_usuario1` FOREIGN KEY (`id_papel`) REFERENCES `papel_usuario` (`id_papel`),
  CONSTRAINT `fk_papel_usuario_has_usuario_usuario1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `predio`
--

DROP TABLE IF EXISTS `predio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `predio` (
  `id_predio` tinyint(4) NOT NULL AUTO_INCREMENT,
  `nome_predio` varchar(50) NOT NULL,
  `descricao_predio` varchar(100) NOT NULL,
  PRIMARY KEY (`id_predio`),
  UNIQUE KEY `nome_predio_UNIQUE` (`nome_predio`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `sala`
--

DROP TABLE IF EXISTS `sala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sala` (
  `id_sala` smallint(6) NOT NULL AUTO_INCREMENT,
  `nome_sala` varchar(50) NOT NULL,
  `descricao_sala` varchar(100) NOT NULL,
  `capacidade_sala` smallint(6) NOT NULL,
  `tipo_quadro_sala` varchar(30) NOT NULL,
  `utilizar_distribuicao` tinyint(1) NOT NULL DEFAULT '1',
  `utilizar_agendamento` tinyint(1) NOT NULL DEFAULT '1',
  `distribuir` tinyint(1) NOT NULL DEFAULT '1',
  `id_perfil` tinyint(4) NOT NULL,
  `id_predio` tinyint(4) NOT NULL,
  PRIMARY KEY (`id_sala`),
  KEY `fk_sala_tipo1_idx` (`id_perfil`),
  KEY `fk_sala_predio1_idx` (`id_predio`),
  CONSTRAINT `fk_sala_predio1` FOREIGN KEY (`id_predio`) REFERENCES `predio` (`id_predio`),
  CONSTRAINT `fk_sala_tipo1` FOREIGN KEY (`id_perfil`) REFERENCES `perfil_sala_turma` (`id_perfil_sala_turma`)
) ENGINE=InnoDB AUTO_INCREMENT=182 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `tipo_funcionario`
--

DROP TABLE IF EXISTS `tipo_funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_funcionario` (
  `id_tipo_funcionario` tinyint(4) NOT NULL AUTO_INCREMENT,
  `nome_tipo_funcionario` varchar(30) NOT NULL,
  `descricao_tipo_funcionario` varchar(55) NOT NULL,
  PRIMARY KEY (`id_tipo_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `turma`
--

DROP TABLE IF EXISTS `turma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turma` (
  `id_turma` smallint(6) NOT NULL AUTO_INCREMENT,
  `codigo_componente_turma` varchar(20) NOT NULL,
  `nome_componente_turma` varchar(100) NOT NULL,
  `nome_docente_turma` varchar(80) DEFAULT NULL,
  `horario_turma` varchar(50) NOT NULL,
  `capacidade_turma` smallint(6) NOT NULL,
  `numero_turma` varchar(5) NOT NULL,
  `alunos_matriculados_turma` smallint(6) NOT NULL DEFAULT '0',
  `distribuir` tinyint(1) NOT NULL DEFAULT '1',
  `local` varchar(30) NOT NULL DEFAULT 'INDEFINIDO',
  `id_perfil` tinyint(4) NOT NULL,
  `id_predio` tinyint(4) NOT NULL DEFAULT '1',
  `id_periodo` tinyint(4) NOT NULL,
  `id_departamento` mediumint(9) NOT NULL,
  `id_sala_temp` smallint(6) DEFAULT NULL,
  `id_turma_sigaa` int(11) NOT NULL,
  PRIMARY KEY (`id_turma`),
  UNIQUE KEY `turma_unica` (`codigo_componente_turma`,`horario_turma`,`numero_turma`,`id_periodo`),
  KEY `fk_turma_tipo1_idx` (`id_perfil`),
  KEY `fk_turma_predio1_idx` (`id_predio`),
  KEY `fk_turma_periodo1_idx` (`id_periodo`),
  KEY `fk_turma_departamento1_idx` (`id_departamento`),
  CONSTRAINT `fk_turma_departamento1` FOREIGN KEY (`id_departamento`) REFERENCES `departamento` (`id_departamento`),
  CONSTRAINT `fk_turma_periodo1` FOREIGN KEY (`id_periodo`) REFERENCES `periodo` (`id_periodo`),
  CONSTRAINT `fk_turma_predio1` FOREIGN KEY (`id_predio`) REFERENCES `predio` (`id_predio`),
  CONSTRAINT `fk_turma_tipo1` FOREIGN KEY (`id_perfil`) REFERENCES `perfil_sala_turma` (`id_perfil_sala_turma`)
) ENGINE=InnoDB AUTO_INCREMENT=10810 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `turma_docente`
--

DROP TABLE IF EXISTS `turma_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turma_docente` (
  `id_turma` smallint(6) NOT NULL,
  `id_docente` smallint(6) NOT NULL,
  PRIMARY KEY (`id_turma`,`id_docente`),
  KEY `fk_turma_has_funcionario_funcionario1_idx` (`id_docente`),
  KEY `fk_turma_has_funcionario_turma1_idx` (`id_turma`),
  CONSTRAINT `fk_turma_has_funcionario_funcionario1` FOREIGN KEY (`id_docente`) REFERENCES `funcionario` (`id_funcionario`),
  CONSTRAINT `fk_turma_has_funcionario_turma1` FOREIGN KEY (`id_turma`) REFERENCES `turma` (`id_turma`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id_usuario` smallint(6) NOT NULL AUTO_INCREMENT,
  `login_usuario` varchar(20) NOT NULL,
  `senha_usuario` varchar(200) NOT NULL,
  `ativo_usuario` tinyint(1) NOT NULL DEFAULT '1',
  `tentativas_login_usuario` smallint(6) NOT NULL DEFAULT '0',
  `id_funcionario` smallint(6) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  KEY `fk_usuario_funcionario1_idx` (`id_funcionario`),
  CONSTRAINT `fk_usuario_funcionario1` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-17 14:37:11
