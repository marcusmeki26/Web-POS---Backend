-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: webpos
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `code` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKacatplu22q5d1andql2jbvjy7` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'2026-10-16 01:29:46','2026-10-16 03:51:52','MAIN DISH',NULL,_binary '',_binary '\0'),(2,'2026-10-16 02:18:00','2026-10-16 02:18:00','BEVERAGES',NULL,_binary '',_binary '\0'),(3,'2026-10-16 02:18:11','2026-10-16 03:52:37','APPETIZER',NULL,_binary '',_binary '\0'),(4,'2026-10-16 03:58:22','2026-10-16 03:58:22','DESSERT',NULL,_binary '',_binary '\0');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food`
--

DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food` (
  `category_id` int DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `with_variant` bit(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` bigint NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `food_name` varchar(255) NOT NULL,
  `image_name` varchar(255) NOT NULL,
  `short_food_desc` varchar(255) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkomdx99dhk2cveaxugl2lws2u` (`category_id`),
  CONSTRAINT `FKkomdx99dhk2cveaxugl2lws2u` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
INSERT INTO `food` VALUES (3,_binary '\0',_binary '',190.00,_binary '\0','2026-10-16 01:55:55',10000,'2026-10-16 03:27:09','Decadent','7c6c15b2-3336-4de8-b881-0a01e18268fc.jpg','Decadent',NULL),(3,_binary '',_binary '\0',300.00,_binary '\0','2026-10-16 02:23:31',10001,'2026-10-16 03:51:24','Japaleno Popper Pizza','7fd43d8d-5fb8-47d5-85fa-f98bbfb58ae0.jpg','Popper Pizza',NULL);
/*!40000 ALTER TABLE `food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food_id_sequence`
--

DROP TABLE IF EXISTS `food_id_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_id_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_id_sequence`
--

LOCK TABLES `food_id_sequence` WRITE;
/*!40000 ALTER TABLE `food_id_sequence` DISABLE KEYS */;
INSERT INTO `food_id_sequence` VALUES (10002);
/*!40000 ALTER TABLE `food_id_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_variant_id` int DEFAULT NULL,
  `quantity` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `food_id` bigint DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsmhosiiwyweyooj1okkh3tsqs` (`product_variant_id`),
  KEY `FK5jd7a1m170ak8ppqrn5h0uiiv` (`food_id`),
  CONSTRAINT `FK25ot3hf10h3y3llu3ebjru4md` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`id`),
  CONSTRAINT `FK5jd7a1m170ak8ppqrn5h0uiiv` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,NULL,41,'2026-10-16 01:55:55',10000,'2026-10-16 01:55:55'),(2,NULL,15,'2026-10-16 02:23:31',10001,'2026-10-16 02:23:39');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_list`
--

DROP TABLE IF EXISTS `order_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_list` (
  `amount` decimal(38,2) NOT NULL,
  `discount_amount` decimal(38,2) DEFAULT NULL,
  `discount_percent` float DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `placed` bit(1) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `quantity` int NOT NULL,
  `size_id` int DEFAULT NULL,
  `table_id` int DEFAULT NULL,
  `vat_amount` decimal(38,2) NOT NULL,
  `number` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `updated_date` datetime(6) NOT NULL,
  `receipt_no` varchar(255) NOT NULL,
  `short_food_desc` varchar(255) NOT NULL,
  `size` varchar(255) NOT NULL,
  `entry_type` enum('ORDER','PAYMENT','VOIDED') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdrsq1vs5y6e1tatr0wtq7kx3o` (`order_id`),
  KEY `FKlerkq5f3xs2lvr57o6da3k3bj` (`table_id`),
  CONSTRAINT `FKdrsq1vs5y6e1tatr0wtq7kx3o` FOREIGN KEY (`order_id`) REFERENCES `order_type` (`id`),
  CONSTRAINT `FKlerkq5f3xs2lvr57o6da3k3bj` FOREIGN KEY (`table_id`) REFERENCES `table_list` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_list`
--

LOCK TABLES `order_list` WRITE;
/*!40000 ALTER TABLE `order_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_type`
--

DROP TABLE IF EXISTS `order_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_desc` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_type`
--

LOCK TABLES `order_type` WRITE;
/*!40000 ALTER TABLE `order_type` DISABLE KEYS */;
INSERT INTO `order_type` VALUES (1,'2026-10-16 03:14:15','2026-10-16 03:14:15','DINE-IN'),(2,'2026-10-16 03:14:15','2026-10-16 03:14:15','TAKE OUT');
/*!40000 ALTER TABLE `order_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_transaction_line`
--

DROP TABLE IF EXISTS `post_transaction_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_transaction_line` (
  `discount_amount` decimal(10,2) NOT NULL,
  `gross_amount` decimal(10,2) NOT NULL,
  `net_amount` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `receipt_no` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_transaction_line`
--

LOCK TABLES `post_transaction_line` WRITE;
/*!40000 ALTER TABLE `post_transaction_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_transaction_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variant`
--

DROP TABLE IF EXISTS `product_variant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_variant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` decimal(38,2) NOT NULL,
  `size_id` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `food_id` bigint DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FKnlh6ue70urd8oosgfpi1nyx9q` (`food_id`),
  KEY `FKn1veiq5y5r3fb6qw0n030o7mh` (`size_id`),
  CONSTRAINT `FKn1veiq5y5r3fb6qw0n030o7mh` FOREIGN KEY (`size_id`) REFERENCES `size` (`id`),
  CONSTRAINT `FKnlh6ue70urd8oosgfpi1nyx9q` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variant`
--

LOCK TABLES `product_variant` WRITE;
/*!40000 ALTER TABLE `product_variant` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_variant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `size`
--

DROP TABLE IF EXISTS `size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `size` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `size_code` varchar(255) NOT NULL,
  `size_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `size`
--

LOCK TABLES `size` WRITE;
/*!40000 ALTER TABLE `size` DISABLE KEYS */;
/*!40000 ALTER TABLE `size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_list`
--

DROP TABLE IF EXISTS `table_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_list` (
  `id` int NOT NULL AUTO_INCREMENT,
  `no_of_seats` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `table_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_list`
--

LOCK TABLES `table_list` WRITE;
/*!40000 ALTER TABLE `table_list` DISABLE KEYS */;
INSERT INTO `table_list` VALUES (1,2,'2026-10-16 03:14:14','2026-10-16 03:14:14','Table 1'),(2,2,'2026-10-16 03:14:14','2026-10-16 03:14:14','Table 2'),(3,2,'2026-10-16 03:14:14','2026-10-16 03:14:14','Table 3'),(4,4,'2026-10-16 03:14:14','2026-10-16 03:14:14','Table 4'),(5,6,'2026-10-16 03:14:14','2026-10-16 03:14:14','Table 5');
/*!40000 ALTER TABLE `table_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_detail`
--

DROP TABLE IF EXISTS `tax_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tax_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) NOT NULL,
  `tax_percentage` double NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tax_description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tax_detail`
--

LOCK TABLES `tax_detail` WRITE;
/*!40000 ALTER TABLE `tax_detail` DISABLE KEYS */;
INSERT INTO `tax_detail` VALUES (1,_binary '',12,'2026-10-16 03:14:13','2026-10-16 03:14:13','12% Tax'),(2,_binary '\0',10,'2026-10-16 03:14:13','2026-10-16 03:14:13','10% Tax');
/*!40000 ALTER TABLE `tax_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tender_type`
--

DROP TABLE IF EXISTS `tender_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tender_type` (
  `change_used` bit(1) DEFAULT NULL,
  `max_tender_amount` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `code` varchar(255) NOT NULL,
  `tender_description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tender_type`
--

LOCK TABLES `tender_type` WRITE;
/*!40000 ALTER TABLE `tender_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `tender_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trans_sum_id_sequence`
--

DROP TABLE IF EXISTS `trans_sum_id_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trans_sum_id_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trans_sum_id_sequence`
--

LOCK TABLES `trans_sum_id_sequence` WRITE;
/*!40000 ALTER TABLE `trans_sum_id_sequence` DISABLE KEYS */;
INSERT INTO `trans_sum_id_sequence` VALUES (10005);
/*!40000 ALTER TABLE `trans_sum_id_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_line`
--

DROP TABLE IF EXISTS `transaction_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_line` (
  `discount_amount` decimal(10,2) NOT NULL,
  `gross_amount` decimal(10,2) NOT NULL,
  `net_amount` decimal(10,2) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `quantity` int NOT NULL,
  `size_id` int DEFAULT NULL,
  `table_id` int DEFAULT NULL,
  `vat_amount` decimal(10,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` bigint NOT NULL,
  `number` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `receipt_no` varchar(255) NOT NULL,
  `short_food_desc` varchar(255) NOT NULL,
  `size` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcx5asowvl9f32hqi3afoap31n` (`table_id`),
  CONSTRAINT `FKcx5asowvl9f32hqi3afoap31n` FOREIGN KEY (`table_id`) REFERENCES `table_list` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_line`
--

LOCK TABLES `transaction_line` WRITE;
/*!40000 ALTER TABLE `transaction_line` DISABLE KEYS */;
INSERT INTO `transaction_line` VALUES (0.00,-190.00,-760.00,190.00,-4,NULL,1,0.00,'2026-10-16 03:23:05',10000,10000,'2026-10-16 11:23:04.729099','2026-10-16 03:23:05','R000001','Decadent','None'),(0.00,-300.00,-600.00,300.00,-2,NULL,1,0.00,'2026-10-16 03:23:05',10001,10001,'2026-10-16 11:23:04.740674','2026-10-16 03:23:05','R000001','Popper Pizza','None');
/*!40000 ALTER TABLE `transaction_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_payment`
--

DROP TABLE IF EXISTS `transaction_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_payment` (
  `amount_tendered` decimal(38,2) NOT NULL,
  `change_line` bit(1) NOT NULL,
  `id` int NOT NULL,
  `table_id` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `number` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `card_no` varchar(255) DEFAULT NULL,
  `receipt_no` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6whil7x9ixswetg3kjojl9ug7` (`table_id`),
  CONSTRAINT `FK6whil7x9ixswetg3kjojl9ug7` FOREIGN KEY (`table_id`) REFERENCES `table_list` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_payment`
--

LOCK TABLES `transaction_payment` WRITE;
/*!40000 ALTER TABLE `transaction_payment` DISABLE KEYS */;
INSERT INTO `transaction_payment` VALUES (1500.00,_binary '\0',10002,1,'2026-10-16 03:23:05',1,'2026-10-16 11:23:04.746131','2026-10-16 03:23:05','','R000001'),(-140.00,_binary '',10003,1,'2026-10-16 03:23:05',1,'2026-10-16 11:23:04.754382','2026-10-16 03:23:05','','R000001');
/*!40000 ALTER TABLE `transaction_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_summary`
--

DROP TABLE IF EXISTS `transaction_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_summary` (
  `discount_amount` decimal(10,2) NOT NULL,
  `gross_sales` decimal(10,2) NOT NULL,
  `net_sales` decimal(10,2) NOT NULL,
  `payment` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  `vat_amount` decimal(10,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `receipt_no` varchar(255) NOT NULL,
  `transaction_no` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_summary`
--

LOCK TABLES `transaction_summary` WRITE;
/*!40000 ALTER TABLE `transaction_summary` DISABLE KEYS */;
INSERT INTO `transaction_summary` VALUES (0.00,490.00,1214.28,1360.00,6,145.72,'2026-10-16 03:23:05',10004,'2026-10-16 11:23:04.754382','2026-10-16 03:23:05','R000001','0000000001');
/*!40000 ALTER TABLE `transaction_summary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `name_on_receipt` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `user_role` enum('CASHIER','MANAGER','OWNER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (10000,'2026-10-16 01:54:59','2026-10-16 01:54:59','Daniel Marcus','Felix','Marcus','$2a$12$owei9RllPtDh7FHRyabyfus1D1ESRyHLsWFfcBFNpm8W3S26L9xp.','0ddd17ad-79e9-4bda-8f95-0ce1994bc7df','marcus23','OWNER'),(10001,'2026-10-16 03:01:28','2026-10-16 03:01:28','Daniel','Marcus','Felix','$2a$12$0EQPBDMZCtS55BWTDrQMC.vfyVJ.f1fkR0BWANtCJb0sOTz4kyJ/u','a3621bda-166d-4aea-baf9-d2e84d2a4e64','cashier','CASHIER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_id_sequence`
--

DROP TABLE IF EXISTS `user_id_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_id_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_id_sequence`
--

LOCK TABLES `user_id_sequence` WRITE;
/*!40000 ALTER TABLE `user_id_sequence` DISABLE KEYS */;
INSERT INTO `user_id_sequence` VALUES (10002);
/*!40000 ALTER TABLE `user_id_sequence` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-16 15:18:03
