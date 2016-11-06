-- MySQL dump 10.13  Distrib 5.5.28, for Win64 (x86)
--
-- Host: localhost    Database: bookbar
-- ------------------------------------------------------
-- Server version	5.5.28

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `phoneNumber` varchar(11) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'18840824301','w'),(2,'18840824302','q');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `book` (
  `bookId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `summary` text,
  `number` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `isbn13` varchar(13) DEFAULT NULL,
  PRIMARY KEY (`bookId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'算法（第4版）','人民邮电出版社','塞奇威克 (Robert Sedgewick)韦恩 (Kevin Wayne)','本书全面讲述算法和数据结构的必备知识，具有以下几大特色。\n     算法领域的经典参考书\nSedgewick畅销著作的最新版，反映了经过几十年演化而成的算法核心知识体系\n 内容全面\n全面论述排序、搜索、图处理和字符串处理的算法和数据结构，涵盖每位程序员应知应会的50种算法\n 全新修订的代码\n全新的Java实现代码，采用模块化的编程风格，所有代码均可供读者使用\n 与实际应用相结合\n在重要的科学、工程和商业应用环境下探讨算法，给出了算法的实际代码，而非同类著作常用的伪代码\n 富于智力趣味性\n简明扼要的内容，用丰富的视觉元素展示的示例，精心设计的代码，详尽的历史和科学背景知识，各种难度的练习，这一切都将使读者手不释卷\n       科学的方法\n用合适的数学模型精确地讨论算法性能，这些模型是在真实环境中得到验证的\n 与网络相结合\n配套网站algs4.cs.princeton.edu提供了本书内容的摘要及相关的代码、测试数据、编程练习、教学课件等资源',0,'https://img3.doubanio.com/lpic/s28322244.jpg','9787115293800'),(2,'线性代数','大连理工大','本社','本教材是在大连理工大学线性代数课程多年教学实践的基础上，借鉴并吸收了国内外相关优秀教材的优点编写而成的。作者力求站在读者的角度，将理论知识阐释得能人易懂，并充分考虑到当前全国硕士研究生入学考试的需要，其内容和难易程度符合理工科线性代数课程和全国研究生入学考试大纲的要求。',0,'https://img3.doubanio.com/lpic/s5759232.jpg','9787561134337'),(3,'算法导论（原书第3版）','机械工业出版社','Thomas H.CormenCharles E.LeisersonRonald L.RivestClifford Stein','在有关算法的书中，有一些叙述非常严谨，但不够全面；另一些涉及了大量的题材，但又缺乏严谨性。本书将严谨性和全面性融为一体，深入讨论各类算法，并着力使这些算法的设计和分析能为各个层次的读者接受。全书各章自成体系，可以作为独立的学习单元；算法以英语和伪代码的形式描述，具备初步程序设计经验的人就能看懂；说明和解释力求浅显易懂，不失深度和数学严谨性。\n全书选材经典、内容丰富、结构合理、逻辑清晰，对本科生的数据结构课程和研究生的算法课程都是非常实用的教材，在IT专业人员的职业生涯中，本书也是一本案头必备的参考书或工程实践手册。\n第3版的主要变化：\n新增了van Emde Boas树和多线程算法，并且将矩阵基础移至附录。\n修订了递归式（现在称为“分治策略”）那一章的内容，更广泛地覆盖分治法。\n移除两章很少讲授的内容：二项堆和排序网络。\n修订了动态规划和贪心算法相关内容。\n流网络相关材料现在基于边上的全部流。\n由于关于矩阵基础和Strassen算法的材料移到了其他章，矩阵运算这一章的内容所占篇幅更小。\n修改了对Knuth-Morris-Pratt字符串匹配算法的讨论。\n新增100道练习和28道思考题，还更新并补充了参考文献。',1,'https://img3.doubanio.com/lpic/s25648004.jpg','9787111407010');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookloc`
--

DROP TABLE IF EXISTS `bookloc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bookloc` (
  `locationId` int(11) DEFAULT NULL,
  `bookId` int(11) DEFAULT NULL,
  `locationX` varchar(255) DEFAULT NULL,
  `locationY` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookloc`
--

LOCK TABLES `bookloc` WRITE;
/*!40000 ALTER TABLE `bookloc` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookloc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow`
--

DROP TABLE IF EXISTS `borrow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `borrow` (
  `borrowId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `bookId` int(11) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`borrowId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow`
--

LOCK TABLES `borrow` WRITE;
/*!40000 ALTER TABLE `borrow` DISABLE KEYS */;
INSERT INTO `borrow` VALUES (7,1,2,'2016年11月05日'),(8,1,1,'2016年11月05日');
/*!40000 ALTER TABLE `borrow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection` (
  `collectionId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `bookId` int(11) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`collectionId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection` DISABLE KEYS */;
INSERT INTO `collection` VALUES (1,1,1,'2016年11月05日'),(6,1,2,'2016年11月05日');
/*!40000 ALTER TABLE `collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `figuredetail`
--

DROP TABLE IF EXISTS `figuredetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `figuredetail` (
  `figureDetailId` int(11) NOT NULL AUTO_INCREMENT,
  `figureId` int(11) DEFAULT NULL,
  `bssid` varchar(255) DEFAULT NULL,
  `level` int(3) DEFAULT NULL,
  PRIMARY KEY (`figureDetailId`)
) ENGINE=InnoDB AUTO_INCREMENT=3559 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `figuredetail`
--

LOCK TABLES `figuredetail` WRITE;
/*!40000 ALTER TABLE `figuredetail` DISABLE KEYS */;
INSERT INTO `figuredetail` VALUES (1,1,'20:dc:e6:6a:fe:40',99),(2,1,'78:eb:14:a7:32:ca',99),(3,1,'28:6c:07:40:fc:1a',72),(4,1,'42:1a:a9:bf:0d:86',61),(5,1,'42:1a:a9:bf:0d:87',59),(6,1,'42:1a:a9:bf:0d:85',61),(7,1,'b0:d5:9d:0d:ea:12',90),(8,1,'28:2c:b2:f1:b2:a6',50),(9,1,'64:09:80:44:a5:d5',41),(10,1,'42:1a:a9:bf:0a:e7',46),(11,1,'f2:1a:a9:bf:07:c0',44),(12,2,'20:dc:e6:6a:fe:40',99),(13,2,'78:eb:14:a7:32:ca',99),(14,2,'28:6c:07:40:fc:1a',72),(15,2,'42:1a:a9:bf:0d:86',61),(16,2,'42:1a:a9:bf:0d:87',61),(17,2,'42:1a:a9:bf:0d:85',61),(18,2,'b0:d5:9d:0d:ea:12',90),(19,2,'64:09:80:44:a5:d5',41),(20,2,'42:1a:a9:bf:0a:e7',46),(21,2,'f2:1a:a9:bf:07:c0',44),(22,2,'92:1a:a9:bf:0d:6b',61),(23,2,'cc:81:da:05:38:d0',11),(24,2,'92:1a:a9:bf:0d:6a',57),(25,2,'92:1a:a9:bf:0d:6c',55),(26,2,'f2:1a:a9:bf:07:c2',35),(890,91,'20:dc:e6:6a:fe:40',99),(891,91,'78:eb:14:a7:32:ca',99),(892,91,'b0:d5:9d:0d:ea:12',92),(893,91,'28:6c:07:40:fc:1a',57),(894,91,'64:09:80:44:a5:d5',39),(895,91,'42:1a:a9:bf:0d:86',39),(896,91,'42:1a:a9:bf:0d:87',39),(897,91,'42:1a:a9:bf:0d:85',37),(2828,332,'20:dc:e6:6a:fe:40',99),(2829,332,'78:eb:14:a7:32:ca',99),(2830,332,'b0:d5:9d:0d:ea:12',88),(2831,332,'28:2c:b2:f1:b2:a6',50),(2832,332,'42:1a:a9:bf:0d:86',66),(2833,332,'28:6c:07:40:fc:1a',68),(2834,332,'92:1a:a9:bf:0d:6b',46),(2835,332,'42:1a:a9:bf:0d:85',63),(2836,332,'42:1a:a9:bf:0d:87',66),(2837,332,'92:1a:a9:bf:0d:6c',48),(2838,332,'92:1a:a9:bf:0d:6a',48),(2839,332,'64:09:80:44:a5:d5',37),(2840,332,'72:1a:a9:bf:0d:88',26),(2841,332,'f2:1a:a9:bf:07:c1',33),(2842,332,'42:1a:a9:bf:0a:e6',33),(2843,332,'42:1a:a9:bf:0a:e5',35),(2844,332,'f2:1a:a9:bf:07:c2',33),(2910,338,'20:dc:e6:6a:fe:40',99),(2911,338,'78:eb:14:a7:32:ca',94),(2912,338,'b0:d5:9d:0d:ea:12',52),(2913,338,'42:1a:a9:bf:0d:86',57),(2914,338,'28:6c:07:40:fc:1a',50),(2915,338,'92:1a:a9:bf:0d:6b',46),(2916,338,'42:1a:a9:bf:0d:85',55),(2917,338,'42:1a:a9:bf:0d:87',57),(2918,338,'92:1a:a9:bf:0d:6c',48),(2919,338,'92:1a:a9:bf:0d:6a',46),(2920,338,'64:09:80:44:a5:d5',46),(3548,392,'20:dc:e6:6a:fe:40',99),(3549,392,'78:eb:14:a7:32:ca',92),(3550,392,'28:6c:07:40:fc:1a',59),(3551,392,'28:2c:b2:f1:b2:a6',59),(3552,392,'64:09:80:44:a5:d6',19),(3553,392,'42:1a:a9:bf:0d:86',44),(3554,392,'92:1a:a9:bf:0d:6b',50),(3555,392,'42:1a:a9:bf:0d:85',44),(3556,392,'42:1a:a9:bf:0d:87',44),(3557,392,'92:1a:a9:bf:0d:6a',46),(3558,392,'92:1a:a9:bf:0d:6c',48);
/*!40000 ALTER TABLE `figuredetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `figureprint`
--

DROP TABLE IF EXISTS `figureprint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `figureprint` (
  `figureId` int(11) NOT NULL AUTO_INCREMENT,
  `locationX` varchar(255) DEFAULT NULL,
  `locationY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`figureId`)
) ENGINE=InnoDB AUTO_INCREMENT=393 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `figureprint`
--

LOCK TABLES `figureprint` WRITE;
/*!40000 ALTER TABLE `figureprint` DISABLE KEYS */;
INSERT INTO `figureprint` VALUES (1,'707.34503','1168.2826'),(2,'687.3635','943.3998'),(23,'0','0'),(91,'0','0'),(332,'0','0'),(338,'0','0'),(392,'0','0');
/*!40000 ALTER TABLE `figureprint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `library`
--

DROP TABLE IF EXISTS `library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `library` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library`
--

LOCK TABLES `library` WRITE;
/*!40000 ALTER TABLE `library` DISABLE KEYS */;
INSERT INTO `library` VALUES (1,'测试图书馆1',''),(2,'测试图书馆2',' '),(3,'测试图书馆3',' ');
/*!40000 ALTER TABLE `library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `locationId` int(11) NOT NULL AUTO_INCREMENT,
  `x` varchar(255) DEFAULT NULL,
  `y` varchar(255) DEFAULT NULL,
  `wifiInfo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`locationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservation` (
  `reservationId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `bookId` int(11) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`reservationId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (2,1,2,'2016年11月05日');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-06 10:12:20
