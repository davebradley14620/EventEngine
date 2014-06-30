-- MySQL dump 10.13  Distrib 5.1.42, for unknown-linux-gnu (x86_64)
--
-- Host: localhost    Database: eventengine
-- ------------------------------------------------------
-- Server version	5.1.42-log

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
-- Current Database: `eventengine`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `eventengine` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `eventengine`;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `sequence` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=80656 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `isgroup` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'N',
   `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
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
  `screenname` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `registered` datetime DEFAULT NULL,
  `registercode` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `screenname` (`screenname`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `groupmembers`;
CREATE TABLE `groupmembers` (
  `created_at` datetime NOT NULL,
  `groupid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
   UNIQUE KEY (`groupid`,`userid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(256),
  `longitude` decimal(12,6) DEFAULT NULL,
  `latitude` decimal(12,6) DEFAULT NULL,
  `start` datetime NOT NULL,
  `durationMinutes` int(11) NOT NULL,
  `isRepeating` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'N',
  `repeatCycle` enum('DAILY','WEEKLY','MONTHLY','YEARLY') CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `event2category`;
CREATE TABLE `event2category` (
  `eventid` int(11) NOT NULL,
  `categoryid` int(11) NOT NULL,
  PRIMARY KEY (`eventid`,`categoryid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `event2metadata`;
CREATE TABLE `event2metadata` (
  `eventid` int(11) NOT NULL,
  `keystr` varchar(64) NOT NULL,
  `valstr` varchar(64),
  PRIMARY KEY (`eventid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `event2contracts`;
CREATE TABLE `event2contracts` (
  `eventid` int(11) NOT NULL,
  `contractid` int(11) NOT NULL,
  PRIMARY KEY (`eventid`,`contractid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
