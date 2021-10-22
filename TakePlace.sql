CREATE DATABASE  IF NOT EXISTS `takeplace` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `takeplace`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: takeplace
-- ------------------------------------------------------
-- Server version	5.6.21

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
-- Table structure for table `aula`
--

DROP TABLE IF EXISTS `aula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aula` (
  `Nome` varchar(50) NOT NULL,
  `NumeroPosti` int(11) DEFAULT NULL,
  PRIMARY KEY (`Nome`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aula`
--

LOCK TABLES `aula` WRITE;
/*!40000 ALTER TABLE `aula` DISABLE KEYS */;
INSERT INTO `aula` VALUES ('DEFAULT',32),('F1',32),('F9',32);
/*!40000 ALTER TABLE `aula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posizione`
--

DROP TABLE IF EXISTS `posizione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posizione` (
  `ID` int(11) NOT NULL,
  `Aula` varchar(45) NOT NULL,
  `Disponibilità` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`,`Aula`),
  KEY `Aula` (`Aula`),
  CONSTRAINT `posizione_ibfk_1` FOREIGN KEY (`Aula`) REFERENCES `aula` (`Nome`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posizione`
--

LOCK TABLES `posizione` WRITE;
/*!40000 ALTER TABLE `posizione` DISABLE KEYS */;
INSERT INTO `posizione` VALUES (1,'DEFAULT ','\0'),(1,'F1',''),(1,'F9',''),(2,'DEFAULT','\0'),(2,'F1','\0'),(2,'F9','\0'),(3,'DEFAULT','\0'),(3,'F1',''),(3,'F9',''),(4,'DEFAULT','\0'),(4,'F1','\0'),(4,'F9','\0'),(5,'DEFAULT','\0'),(5,'F1','\0'),(5,'F9','\0'),(6,'DEFAULT','\0'),(6,'F1',''),(6,'F9',''),(7,'DEFAULT','\0'),(7,'F1','\0'),(7,'F9','\0'),(8,'DEFAULT','\0'),(8,'F1',''),(8,'F9',''),(9,'DEFAULT','\0'),(9,'F1',''),(9,'F9',''),(10,'DEFAULT','\0'),(10,'F1','\0'),(10,'F9','\0'),(11,'DEFAULT','\0'),(11,'F1',''),(11,'F9',''),(12,'DEFAULT','\0'),(12,'F1','\0'),(12,'F9','\0'),(13,'DEFAULT','\0'),(13,'F1','\0'),(13,'F9','\0'),(14,'DEFAULT','\0'),(14,'F1',''),(14,'F9',''),(15,'DEFAULT','\0'),(15,'F1','\0'),(15,'F9','\0'),(16,'DEFAULT','\0'),(16,'F1',''),(16,'F9',''),(17,'DEFAULT','\0'),(17,'F1',''),(17,'F9',''),(18,'DEFAULT','\0'),(18,'F1','\0'),(18,'F9','\0'),(19,'DEFAULT','\0'),(19,'F1',''),(19,'F9',''),(20,'DEFAULT','\0'),(20,'F1','\0'),(20,'F9','\0'),(21,'DEFAULT','\0'),(21,'F1','\0'),(21,'F9','\0'),(22,'DEFAULT','\0'),(22,'F1',''),(22,'F9',''),(23,'DEFAULT','\0'),(23,'F1','\0'),(23,'F9','\0'),(24,'DEFAULT','\0'),(24,'F1',''),(24,'F9',''),(25,'DEFAULT','\0'),(25,'F1',''),(25,'F9',''),(26,'DEFAULT','\0'),(26,'F1','\0'),(26,'F9','\0'),(27,'DEFAULT','\0'),(27,'F1',''),(27,'F9',''),(28,'DEFAULT','\0'),(28,'F1','\0'),(28,'F9','\0'),(29,'DEFAULT','\0'),(29,'F1','\0'),(29,'F9','\0'),(30,'DEFAULT','\0'),(30,'F1',''),(30,'F9',''),(31,'DEFAULT','\0'),(31,'F1','\0'),(31,'F9','\0'),(32,'DEFAULT','\0'),(32,'F1',''),(32,'F9','');
/*!40000 ALTER TABLE `posizione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prenotazione`
--

DROP TABLE IF EXISTS `prenotazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prenotazione` (
  `Codice` int(11) NOT NULL AUTO_INCREMENT,
  `Data` date DEFAULT NULL,
  `OraInizio` time DEFAULT NULL,
  `OraFine` time DEFAULT NULL,
  `Aula` varchar(45) DEFAULT NULL,
  `Posizione` int(11) DEFAULT NULL,
  `Utente` int(11) DEFAULT NULL,
  `OraAccesso` time DEFAULT NULL,
  `OraPrenotazione` time DEFAULT NULL,
  `DataPrenotazione` date DEFAULT NULL,
  PRIMARY KEY (`Codice`),
  KEY `Aula` (`Aula`),
  KEY `Posizione` (`Posizione`,`Aula`),
  KEY `Utente` (`Utente`),
  CONSTRAINT `prenotazione_ibfk_1` FOREIGN KEY (`Aula`) REFERENCES `posizione` (`Aula`),
  CONSTRAINT `prenotazione_ibfk_2` FOREIGN KEY (`Posizione`) REFERENCES `posizione` (`ID`),
  CONSTRAINT `prenotazione_ibfk_3` FOREIGN KEY (`Posizione`, `Aula`) REFERENCES `posizione` (`ID`, `Aula`),
  CONSTRAINT `prenotazione_ibfk_4` FOREIGN KEY (`Utente`) REFERENCES `utente` (`Matricola`)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazione`
--

LOCK TABLES `prenotazione` WRITE;
/*!40000 ALTER TABLE `prenotazione` DISABLE KEYS */;
INSERT INTO `prenotazione` VALUES (195,'2021-02-19','08:00:00','19:59:59','F1',1,111111,'08:07:05','15:06:18','2021-02-14'),(196,'2021-02-19','08:00:00','19:59:59','F1',3,222222,NULL,'15:06:26','2021-02-14'),(197,'2021-02-19','08:00:00','19:59:59','F1',6,333333,'08:08:07','15:06:33','2021-02-14'),(198,'2021-02-19','08:00:00','19:59:59','F1',8,444444,'08:08:25','15:06:40','2021-02-14'),(199,'2021-02-19','08:00:00','19:59:59','F1',11,555555,'08:08:44','15:06:48','2021-02-14'),(200,'2021-02-19','08:00:00','19:59:59','F1',14,666666,'08:09:02','15:06:55','2021-02-14'),(201,'2021-02-19','08:00:00','19:59:59','F1',16,777777,'08:09:20','15:07:01','2021-02-14'),(202,'2021-02-19','08:00:00','19:59:59','F1',32,888888,'08:07:26','15:07:12','2021-02-14'),(203,'2021-02-19','08:00:00','19:59:59','F1',19,999999,'08:07:45','15:07:21','2021-02-14'),(204,'2021-02-19','08:00:00','19:59:59','F9',1,123456,'08:09:52','15:09:35','2021-02-14'),(205,'2021-02-19','08:00:00','12:59:59','F9',6,234567,NULL,'15:09:45','2021-02-14'),(207,'2021-02-15','08:00:00','19:59:59','F1',1,123456,'12:29:02','12:28:53','2021-02-15'),(208,'2021-02-15','08:00:00','19:59:59','F1',6,234567,'12:29:20','12:28:58','2021-02-15'),(209,'2021-02-15','08:00:00','19:59:59','F1',8,987654,NULL,'12:33:47','2021-02-15'),(210,'2021-02-16','08:00:00','19:59:59','F1',1,123456,NULL,'12:39:24','2021-02-15'),(211,'2021-02-16','08:00:00','19:59:59','F1',6,234567,NULL,'12:39:31','2021-02-15'),(212,'2021-02-18','08:00:00','19:59:59','F1',1,111111,NULL,'12:39:46','2021-02-15'),(213,'2021-02-18','08:00:00','19:59:59','F1',3,333333,NULL,'12:39:55','2021-02-15');
/*!40000 ALTER TABLE `prenotazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utente` (
  `Matricola` int(11) NOT NULL,
  PRIMARY KEY (`Matricola`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (101010),(101099),(111111),(111222),(112233),(121212),(123098),(123123),(123432),(123433),(123451),(123456),(123543),(131313),(132456),(171717),(222222),(222333),(223344),(234567),(333333),(334455),(444444),(445566),(528043),(554312),(554433),(555444),(555555),(556212),(556677),(575757),(577943),(578043),(578044),(657676),(666666),(667788),(676767),(686822),(765432),(776655),(777777),(778822),(876543),(887878),(888888),(987654),(987765),(994455),(999990),(999999);
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-15 12:43:38
