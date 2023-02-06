-- MySQL dump 10.13  Distrib 5.5.44, for osx10.8 (i386)
--
-- Host: 192.168.3.204    Database: renaserv
-- ------------------------------------------------------
-- Server version	5.5.54-0ubuntu0.12.04.1-log

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
-- Table structure for table `credential`
--

DROP TABLE IF EXISTS `credential`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credential` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `_key` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `_password` varchar(255) DEFAULT NULL,
  `_system` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `_user` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  `data_login` datetime DEFAULT NULL,
  `last_check` datetime DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `email` varchar(255) DEFAULT NULL,
  `lastcheck` datetime DEFAULT NULL,
  `lastsentmail` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evento`
--

DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evento_monitorado`
--

DROP TABLE IF EXISTS `evento_monitorado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento_monitorado` (
  `credential` int(11) NOT NULL,
  `evento` int(11) NOT NULL,
  PRIMARY KEY (`credential`,`evento`),
  KEY `evento` (`evento`),
  CONSTRAINT `evento_monitorado_ibfk_1` FOREIGN KEY (`credential`) REFERENCES `credential` (`id`),
  CONSTRAINT `evento_monitorado_ibfk_2` FOREIGN KEY (`evento`) REFERENCES `evento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_ocorrencia`
--

DROP TABLE IF EXISTS `log_ocorrencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_ocorrencia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` datetime DEFAULT NULL,
  `tratativa` text,
  `status` int(11) DEFAULT NULL,
  `credential` int(11) NOT NULL,
  `veiculo` int(11) NOT NULL,
  `data_dados` datetime NOT NULL,
  `evento` int(11) NOT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_log_ocorrencia_ocorrencia` (`credential`,`veiculo`,`data_dados`,`evento`),
  CONSTRAINT `fk_log_ocorrencia_ocorrencia` FOREIGN KEY (`credential`, `veiculo`, `data_dados`, `evento`) REFERENCES `ocorrencia` (`credential`, `veiculo`, `data_dados`, `evento`)
) ENGINE=InnoDB AUTO_INCREMENT=226 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ocorrencia`
--

DROP TABLE IF EXISTS `ocorrencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ocorrencia` (
  `credential` int(11) NOT NULL,
  `veiculo` int(11) NOT NULL,
  `data_dados` datetime NOT NULL,
  `evento` int(11) NOT NULL,
  `data_cad` datetime NOT NULL,
  `lat` float DEFAULT NULL,
  `lon` float DEFAULT NULL,
  `speed` float DEFAULT NULL,
  `io` int(11) DEFAULT NULL,
  `direction` int(11) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `data_tratativa` datetime DEFAULT NULL,
  `usuario_tratativa` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`credential`,`veiculo`,`data_dados`,`evento`),
  KEY `fk_ocorrencia_evento` (`evento`),
  CONSTRAINT `fk_ocorrencia_evento` FOREIGN KEY (`evento`) REFERENCES `evento` (`id`),
  CONSTRAINT `fk_ocorrencia_veiculo` FOREIGN KEY (`credential`, `veiculo`) REFERENCES `veiculo` (`credential`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proprietario`
--

DROP TABLE IF EXISTS `proprietario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proprietario` (
  `credential` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `telefones` varchar(1000) DEFAULT NULL,
  `contatos` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`credential`,`id`),
  CONSTRAINT `fk_proprietario_credential` FOREIGN KEY (`credential`) REFERENCES `credential` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) DEFAULT NULL,
  `senha` varchar(100) DEFAULT NULL,
  `token` varchar(100) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `data_login` datetime DEFAULT NULL,
  `last_check` datetime DEFAULT NULL,
  `tipo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_usuario` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `veiculo`
--

DROP TABLE IF EXISTS `veiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `veiculo` (
  `credential` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `placa` varchar(50) DEFAULT NULL,
  `modelo` varchar(255) DEFAULT NULL,
  `lat` float DEFAULT NULL,
  `lon` float DEFAULT NULL,
  `speed` float DEFAULT NULL,
  `io` int(11) DEFAULT NULL,
  `direction` int(11) DEFAULT NULL,
  `data_atualizacao` datetime DEFAULT NULL,
  `data_dados` datetime DEFAULT NULL,
  `proprietario` int(11) DEFAULT NULL,
  `credential_prop` int(11) DEFAULT NULL,
  `ano` varchar(255) DEFAULT NULL,
  `chassi` varchar(255) DEFAULT NULL,
  `cor` varchar(255) DEFAULT NULL,
  `renavam` varchar(255) DEFAULT NULL,
  `last_check` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`credential`,`id`),
  KEY `credential_prop` (`credential_prop`,`proprietario`),
  CONSTRAINT `fk_veiculo_credential` FOREIGN KEY (`credential`) REFERENCES `credential` (`id`),
  CONSTRAINT `veiculo_ibfk_1` FOREIGN KEY (`credential_prop`, `proprietario`) REFERENCES `proprietario` (`credential`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-06  8:11:28
