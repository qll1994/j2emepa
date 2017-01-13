CREATE DATABASE  IF NOT EXISTS `pannes` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `pannes`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: pannes
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `pannes`
--

DROP TABLE IF EXISTS `pannes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pannes` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `heure` datetime NOT NULL,
  `typepanne` enum('reseau','crash_disque','probleme_memoire') NOT NULL,
  `machine` char(16) NOT NULL,
  `typemachine` enum('serveur','pare-feux','routeur') NOT NULL,
  `reparee` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pannes`
--

LOCK TABLES `pannes` WRITE;
/*!40000 ALTER TABLE `pannes` DISABLE KEYS */;
INSERT INTO `pannes` VALUES (1,'2016-12-26 22:57:54','crash_disque','00d3341d389c347b','serveur',0),(2,'2016-12-27 10:13:22','reseau','a1b2e3b14bbeeeef','pare-feux',0),(3,'2016-12-27 10:13:57','reseau','d08898543572f188','routeur',0),(4,'2016-12-27 12:30:46','reseau','ebe1774ef2716ecb','routeur',0),(5,'2016-12-27 12:31:45','crash_disque','0417f95f724de4c5','serveur',0),(6,'2016-12-27 12:31:50','probleme_memoire','1331d0e024f49c19','serveur',0),(7,'2016-12-27 14:55:01','reseau','648b4ec320826026','pare-feux',0),(8,'2016-12-27 14:55:01','probleme_memoire','024929edd0f8bea0','serveur',0);
/*!40000 ALTER TABLE `pannes` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `pannes`.`pannes_BEFORE_INSERT` BEFORE INSERT ON `pannes` FOR EACH ROW
BEGIN
	IF NEW.typepanne != 'réseau'
    AND (NEW.typemachine = 'pare-feux' OR NEW.typemachine = 'routeur')
		THEN
			SIGNAL SQLSTATE '45001'
            SET MESSAGE_TEXT = 'The breakdown is incompatible with the type of machine.';
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping events for database 'pannes'
--

--
-- Dumping routines for database 'pannes'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-27 16:45:52
