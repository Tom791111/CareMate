CREATE DATABASE  IF NOT EXISTS `caremate_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `caremate_db`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: caremate_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `care_task`
--

DROP TABLE IF EXISTS `care_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `care_task` (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `care_task`
--

LOCK TABLES `care_task` WRITE;
/*!40000 ALTER TABLE `care_task` DISABLE KEYS */;
INSERT INTO `care_task` VALUES (1,1,'協助散步','照顧者','已完成','日常任務','Demo 今日任務','2026-07-01T09:28','2026-07-01 01:28:05','2026-07-01 01:28:05');
/*!40000 ALTER TABLE `care_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caregiver`
--

DROP TABLE IF EXISTS `caregiver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caregiver` (
  `caregiver_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nationality` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `language` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`caregiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caregiver`
--

LOCK TABLES `caregiver` WRITE;
/*!40000 ALTER TABLE `caregiver` DISABLE KEYS */;
/*!40000 ALTER TABLE `caregiver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contact` (
  `contact_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elder`
--

DROP TABLE IF EXISTS `elder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elder` (
  `elder_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`elder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elder`
--

LOCK TABLES `elder` WRITE;
/*!40000 ALTER TABLE `elder` DISABLE KEYS */;
INSERT INTO `elder` VALUES (1,2,'阿公','男','1945-01-01','測試被照顧者','2026-06-30 03:18:21','2026-06-30 03:18:21');
/*!40000 ALTER TABLE `elder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emergency_log`
--

DROP TABLE IF EXISTS `emergency_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_log` (
  `emergency_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`emergency_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_log`
--

LOCK TABLES `emergency_log` WRITE;
/*!40000 ALTER TABLE `emergency_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `emergency_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_record`
--

DROP TABLE IF EXISTS `health_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_record` (
  `health_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`health_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_record`
--

LOCK TABLES `health_record` WRITE;
/*!40000 ALTER TABLE `health_record` DISABLE KEYS */;
INSERT INTO `health_record` VALUES (2,1,'125','82','36.6','上午','Dashboard 2.0 示範血壓體溫','2026-07-01T09:28','2026-07-01 01:28:05','2026-07-01 01:28:05');
/*!40000 ALTER TABLE `health_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meal_record`
--

DROP TABLE IF EXISTS `meal_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal_record` (
  `meal_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`meal_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meal_record`
--

LOCK TABLES `meal_record` WRITE;
/*!40000 ALTER TABLE `meal_record` DISABLE KEYS */;
INSERT INTO `meal_record` VALUES (1,1,'早餐','粥、蛋、青菜','350','已完成','Demo 可於飲食頁新增午餐晚餐','2026-07-01T09:28','2026-07-01 01:28:05','2026-07-01 01:28:05');
/*!40000 ALTER TABLE `meal_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine_record`
--

DROP TABLE IF EXISTS `medicine_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_record` (
  `medicine_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`medicine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_record`
--

LOCK TABLES `medicine_record` WRITE;
/*!40000 ALTER TABLE `medicine_record` DISABLE KEYS */;
INSERT INTO `medicine_record` VALUES (1,1,'血壓藥','1顆','早上','已完成','Demo 可於用藥頁修改狀態','2026-07-01T09:28','2026-07-01 01:28:05','2026-07-01 01:28:05');
/*!40000 ALTER TABLE `medicine_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'family','1234','家屬測試帳號','FAMILY','0912000000','family@test.com','ACTIVE','2026-06-30 03:18:21','2026-06-30 03:18:21'),(2,'elder','1234','被照顧者測試帳號','ELDER','0912111111','elder@test.com','ACTIVE','2026-06-30 03:18:21','2026-06-30 03:18:21'),(3,'caregiver','1234','外籍照顧者測試帳號','FOREIGN_CAREGIVER','0912222222','caregiver@test.com','ACTIVE','2026-06-30 03:18:21','2026-06-30 03:18:21'),(4,'admin','1234','管理者','ADMIN','0912333333','admin@test.com','ACTIVE','2026-06-30 03:18:21','2026-06-30 03:18:21'),(5,'1234','1234','1234','FAMILY','1234','1234','ACTIVE','2026-06-30 03:19:46','2026-06-30 03:19:46'),(6,'12','12','12','ELDER','12','12','ACTIVE','2026-06-30 04:02:36','2026-06-30 04:02:36'),(7,'1','1','1','FAMILY','1','1','ACTIVE','2026-06-30 07:03:53','2026-06-30 07:03:53'),(63,'122','122','122','FAMILY','122','122','ACTIVE','2026-07-01 03:37:04','2026-07-01 03:37:04');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mood_record`
--

DROP TABLE IF EXISTS `mood_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mood_record` (
  `mood_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mood_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mood_record`
--

LOCK TABLES `mood_record` WRITE;
/*!40000 ALTER TABLE `mood_record` DISABLE KEYS */;
INSERT INTO `mood_record` VALUES (1,1,'開心','4','良好','照顧者回報','Demo 情緒資料','2026-07-01T09:28','2026-07-01 01:28:05','2026-07-01 01:28:05');
/*!40000 ALTER TABLE `mood_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `translation_history`
--

DROP TABLE IF EXISTS `translation_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `translation_history` (
  `translation_id` int NOT NULL AUTO_INCREMENT,
  `elder_id` int DEFAULT '1',
  `col1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `col4` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `record_time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`translation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `translation_history`
--

LOCK TABLES `translation_history` WRITE;
/*!40000 ALTER TABLE `translation_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `translation_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-01 13:24:17
