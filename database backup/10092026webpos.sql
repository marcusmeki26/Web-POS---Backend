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
  `code` varchar(255) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('APPETIZER'),('BEVERAGE'),('DESSERT'),('MAIN DISH');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food`
--

DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food` (
  `price` decimal(10,2) NOT NULL,
  `with_variant` bit(1) NOT NULL,
  `id` bigint NOT NULL,
  `category_code` varchar(255) DEFAULT NULL,
  `food_name` varchar(255) NOT NULL,
  `short_food_desc` varchar(255) NOT NULL,
  `image_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKowcaxj73hkgavmpqaivj5o4yb` (`food_name`),
  KEY `FK4hdcerod71a1rtq9pphrj12od` (`category_code`),
  CONSTRAINT `FK4hdcerod71a1rtq9pphrj12od` FOREIGN KEY (`category_code`) REFERENCES `category` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
INSERT INTO `food` VALUES (290.00,_binary '\0',10001,'MAIN DISH','Decadent','Decadent','0df506d0-bb0a-4fbb-907c-0eea99592d8f.jpg'),(0.00,_binary '',10002,'BEVERAGE','Coke','Coke','2e3112f2-b687-47ca-bcb4-ccbd6cfe5ec7.jpg'),(390.00,_binary '\0',10003,'APPETIZER','Japaleno Popper Pizza','Japaleno Popper Pizza','8f4d4665-bfcd-49fa-82f0-c3b8633546b5.jpg');
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
INSERT INTO `food_id_sequence` VALUES (10004);
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
  `food_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsmhosiiwyweyooj1okkh3tsqs` (`product_variant_id`),
  KEY `FK5jd7a1m170ak8ppqrn5h0uiiv` (`food_id`),
  CONSTRAINT `FK25ot3hf10h3y3llu3ebjru4md` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`id`),
  CONSTRAINT `FK5jd7a1m170ak8ppqrn5h0uiiv` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,NULL,5,10001),(2,1,1,10002),(3,2,5,10002),(4,3,8,10002),(5,NULL,10,10003);
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
  `entry_type` enum('ORDER','PAYMENT','VOIDED') NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `placed` bit(1) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `quantity` int NOT NULL,
  `size_id` int DEFAULT NULL,
  `table_id` int DEFAULT NULL,
  `number` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `updated_date` datetime(6) NOT NULL,
  `receipt_no` varchar(255) NOT NULL,
  `short_food_desc` varchar(255) NOT NULL,
  `size` varchar(255) NOT NULL,
  `discount_percent` float DEFAULT NULL,
  `vat_amount` decimal(38,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdrsq1vs5y6e1tatr0wtq7kx3o` (`order_id`),
  KEY `FKlerkq5f3xs2lvr57o6da3k3bj` (`table_id`),
  CONSTRAINT `FKdrsq1vs5y6e1tatr0wtq7kx3o` FOREIGN KEY (`order_id`) REFERENCES `order_type` (`id`),
  CONSTRAINT `FKlerkq5f3xs2lvr57o6da3k3bj` FOREIGN KEY (`table_id`) REFERENCES `table_list` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_list`
--

LOCK TABLES `order_list` WRITE;
/*!40000 ALTER TABLE `order_list` DISABLE KEYS */;
INSERT INTO `order_list` VALUES (290.00,0.00,'ORDER',6,1,_binary '',290.00,1,NULL,1,10001,'2026-10-07 15:14:26.464372','2026-10-07 15:14:38.316091','R000001','Decadent','None',0,31.07),(290.00,0.00,'ORDER',11,NULL,_binary '\0',290.00,1,NULL,NULL,10001,'2026-10-07 17:08:12.911685','2026-10-07 17:08:12.911685','R000002','Decadent','None',0,31.07),(20.00,0.00,'ORDER',12,NULL,_binary '\0',20.00,1,1,NULL,10002,'2026-10-07 17:17:04.544705','2026-10-07 17:17:04.544705','R000002','Coke','12 oz',0,2.14),(1560.00,0.00,'ORDER',13,NULL,_binary '\0',390.00,4,NULL,NULL,10003,'2026-10-07 17:17:19.284270','2026-10-07 17:17:19.681981','R000002','Japaleno Popper Pizza','None',0,167.14);
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
  `order_desc` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_type`
--

LOCK TABLES `order_type` WRITE;
/*!40000 ALTER TABLE `order_type` DISABLE KEYS */;
INSERT INTO `order_type` VALUES (1,'DINE-IN'),(2,'TAKE OUT');
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
  `id` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
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
  `food_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnlh6ue70urd8oosgfpi1nyx9q` (`food_id`),
  KEY `FKn1veiq5y5r3fb6qw0n030o7mh` (`size_id`),
  CONSTRAINT `FKn1veiq5y5r3fb6qw0n030o7mh` FOREIGN KEY (`size_id`) REFERENCES `size` (`id`),
  CONSTRAINT `FKnlh6ue70urd8oosgfpi1nyx9q` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variant`
--

LOCK TABLES `product_variant` WRITE;
/*!40000 ALTER TABLE `product_variant` DISABLE KEYS */;
INSERT INTO `product_variant` VALUES (1,20.00,1,10002),(2,30.00,2,10002),(3,40.00,3,10002);
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
  `size_code` varchar(255) NOT NULL,
  `size_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `size`
--

LOCK TABLES `size` WRITE;
/*!40000 ALTER TABLE `size` DISABLE KEYS */;
INSERT INTO `size` VALUES (1,'12OZ','12 oz'),(2,'16OZ','16 oz'),(3,'32OZ','32 oz'),(4,'S','Small'),(5,'M','Medium'),(6,'L','Large');
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
  `table_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_list`
--

LOCK TABLES `table_list` WRITE;
/*!40000 ALTER TABLE `table_list` DISABLE KEYS */;
INSERT INTO `table_list` VALUES (1,2,'Table 1'),(2,2,'Table 2'),(3,2,'Table 3'),(4,4,'Table 4'),(5,6,'Table 5');
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
  `tax_description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tax_detail`
--

LOCK TABLES `tax_detail` WRITE;
/*!40000 ALTER TABLE `tax_detail` DISABLE KEYS */;
INSERT INTO `tax_detail` VALUES (1,_binary '',12,'12% Tax'),(2,_binary '\0',10,'10% Tax');
/*!40000 ALTER TABLE `tax_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tender_type`
--

DROP TABLE IF EXISTS `tender_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tender_type` (
  `code` varchar(255) NOT NULL,
  `change_used` bit(1) DEFAULT NULL,
  `max_tender_amount` double DEFAULT NULL,
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
INSERT INTO `trans_sum_id_sequence` VALUES (10246);
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
  `net_amount` decimal(10,2) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `quantity` int NOT NULL,
  `size_id` int DEFAULT NULL,
  `table_id` int DEFAULT NULL,
  `id` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `receipt_no` varchar(255) NOT NULL,
  `short_food_desc` varchar(255) NOT NULL,
  `size` varchar(255) NOT NULL,
  `gross_amount` decimal(10,2) NOT NULL,
  `vat_amount` decimal(10,2) NOT NULL,
  `number` bigint NOT NULL,
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
/*!40000 ALTER TABLE `transaction_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_payment`
--

DROP TABLE IF EXISTS `transaction_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_payment` (
  `id` int NOT NULL,
  `amount_tendered` decimal(38,2) NOT NULL,
  `card_no` varchar(255) DEFAULT NULL,
  `change_line` bit(1) NOT NULL,
  `number` bigint NOT NULL,
  `receipt_no` varchar(255) NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `table_id` int DEFAULT NULL,
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
  `quantity` int NOT NULL,
  `id` bigint NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `receipt_no` varchar(255) NOT NULL,
  `transaction_no` varchar(255) NOT NULL,
  `payment` decimal(10,2) NOT NULL,
  `vat_amount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_summary`
--

LOCK TABLES `transaction_summary` WRITE;
/*!40000 ALTER TABLE `transaction_summary` DISABLE KEYS */;
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
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `name_on_receipt` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `time_created` datetime(6) NOT NULL,
  `time_updated` datetime(6) NOT NULL,
  `user_role` enum('CASHIER','MANAGER','OWNER') NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (10004,'Marcus','Felixmeña',NULL,'$2a$12$CJkBJul.L4k0OaT8ZsIQK.yozUthvKrINyY6JCTuu9VBEbpnIiQD.','2026-09-21 17:22:17.183237','2026-09-21 17:22:17.183237','OWNER','d3e587dd-3585-4d78-9ab4-bcb8adae879e','marcus23'),(10008,'sir','squire','my name','$2a$12$Aky.m2/iH8R7d5d1Wjq.Z.R5Ht8QaM8fphT6avMA0py3UOn15INpG','2026-10-07 15:11:46.789819','2026-10-07 15:11:46.789819','CASHIER','8a19a2f7-1f33-4f7a-8840-98b7f3a59855','sir');
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
INSERT INTO `user_id_sequence` VALUES (10009);
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

-- Dump completed on 2026-10-09 15:29:56
