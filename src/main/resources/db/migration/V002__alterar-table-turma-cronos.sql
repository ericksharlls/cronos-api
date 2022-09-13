-- MySQL dump 10.13  Distrib 5.5.62, for linux-glibc2.12 (x86_64)
--
-- Host: localhost    Database: db_cronos
-- ------------------------------------------------------
-- Server version	5.5.62

ALTER TABLE  `turma` MODIFY `id_turma_sigaa` int(11) DEFAULT NULL;

ALTER TABLE `turma` DROP COLUMN `id_sala_temp`;
ALTER TABLE `turma` DROP COLUMN `local`;
ALTER TABLE `turma` DROP COLUMN `nome_docente_turma`;