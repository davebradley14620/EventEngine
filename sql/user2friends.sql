DROP TABLE IF EXISTS `user2friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user2friends` (
  `userid` int(11) NOT NULL,
  `friendid` int(11) NOT NULL,
  PRIMARY KEY (`userid`,`friendid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

