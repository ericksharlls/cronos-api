-- MySQL dump 10.13  Distrib 5.5.62, for linux-glibc2.12 (x86_64)
--
-- Host: localhost    Database: db_cronos
-- ------------------------------------------------------
-- Server version	5.5.62

ALTER TABLE `importacoes_turmas` ADD CONSTRAINT `fk_periodo` FOREIGN KEY (`id_periodo`) REFERENCES `periodo` (`id_periodo`);