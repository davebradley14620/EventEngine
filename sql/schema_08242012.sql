-- MySQL dump 10.13  Distrib 5.5.20, for Linux (x86_64)
--
-- Host: localhost    Database: eventengine
-- ------------------------------------------------------
-- Server version	5.5.20

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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `created_at` datetime NOT NULL,
  `updated_at` datetime,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `name` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(128) COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=80656 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event2category`
--

DROP TABLE IF EXISTS `event2category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event2category` (
  `eventid` int(11) NOT NULL,
  `categoryid` int(11) NOT NULL,
  PRIMARY KEY (`eventid`,`categoryid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event2users`
--

DROP TABLE IF EXISTS `event2users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event2users` (
  `eventid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  PRIMARY KEY (`eventid`,`userid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event2metadata`
--

DROP TABLE IF EXISTS `event2metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event2metadata` (
  `eventid` int(11) NOT NULL,
  `keystr` varchar(64) NOT NULL,
  `valstr` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`eventid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `events` (
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `longitude` decimal(12,6) DEFAULT NULL,
  `latitude` decimal(12,6) DEFAULT NULL,
  `start` datetime NOT NULL,
  `durationMinutes` int(11) NOT NULL,
  `isRepeating` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'N',
  `repeatCycle` enum('DAILY','WEEKLY','MONTHLY','YEARLY') CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `state` varchar(2) DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=87 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groupmembers`
--

DROP TABLE IF EXISTS `groupmembers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupmembers` (
  `created_at` datetime NOT NULL,
  `groupid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  UNIQUE KEY `groupid` (`groupid`,`userid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `screenname` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `firstname` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `lastname` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `state` varchar(2) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `zip` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `longitude` decimal(12,6) DEFAULT NULL,
  `latitude` decimal(12,6) DEFAULT NULL,
  `radius_miles` int(11) DEFAULT NULL,
  `termsread` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'N',
  `registered` datetime DEFAULT NULL,
  `registercode` int(11) NOT NULL DEFAULT '0',
  `isgroup` enum('N','Y') DEFAULT 'N',
  PRIMARY KEY (`id`),
  UNIQUE KEY `screenname` (`screenname`)
) ENGINE=MyISAM AUTO_INCREMENT=74 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

DROP TABLE IF EXISTS `user2friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user2friends` (
  `userid` int(11) NOT NULL,
  `friendid` int(11) NOT NULL,
  PRIMARY KEY (`userid`,`friendid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
-- Dump completed on 2012-08-24  1:18:30
