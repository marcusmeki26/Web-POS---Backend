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
  `image_path` varchar(255) NOT NULL,
  `short_food_desc` varchar(255) NOT NULL,
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
INSERT INTO `food` VALUES (420.00,_binary '\0',10000,'APPETIZER','Overloaded French Fries','','Overloaded FF'),(0.00,_binary '',10001,'BEVERAGE','Coke','','Coke'),(490.00,_binary '\0',10002,'DESSERT','Panna Cotta','','Panna Cotta'),(350.00,_binary '\0',10003,'MAIN DISH','Sweet Chili Salmon','','SC Salmon'),(599.00,_binary '\0',10004,'APPETIZER','Shrimp Cocktail','','Shrimp Cocktail');
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
INSERT INTO `food_id_sequence` VALUES (10005);
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (2,1,8,10001),(3,2,9,10001),(4,3,10,10001),(5,NULL,49,10002),(6,NULL,12,10003),(7,NULL,0,10004),(20,NULL,3,10000);
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
  `entry_type` tinyint NOT NULL,
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
  CONSTRAINT `FKlerkq5f3xs2lvr57o6da3k3bj` FOREIGN KEY (`table_id`) REFERENCES `table_list` (`id`),
  CONSTRAINT `order_list_chk_1` CHECK ((`entry_type` between 0 and 2))
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variant`
--

LOCK TABLES `product_variant` WRITE;
/*!40000 ALTER TABLE `product_variant` DISABLE KEYS */;
INSERT INTO `product_variant` VALUES (1,25.00,1,10001),(2,50.00,2,10001),(3,70.00,3,10001);
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
INSERT INTO `trans_sum_id_sequence` VALUES (10242);
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
INSERT INTO `transaction_line` VALUES (0.00,-3920.00,490.00,-8,NULL,4,10068,'2026-09-21 12:07:20.836083','R000001','Panna Cotta','None',-490.00,0.00,10002),(0.00,-85.00,85.00,-1,NULL,3,10072,'2026-09-21 12:08:08.116829','R000004','Overloaded FF','None',-85.00,0.00,10000),(0.00,-350.00,350.00,-1,NULL,3,10073,'2026-09-21 12:08:08.122230','R000004','SC Salmon','None',-350.00,0.00,10003),(0.00,-490.00,490.00,-1,NULL,1,10076,'2026-09-21 12:08:18.920488','R000002','Panna Cotta','None',-490.00,0.00,10002),(0.00,-85.00,85.00,-1,NULL,1,10077,'2026-09-21 12:08:18.920488','R000002','Overloaded FF','None',-85.00,0.00,10000),(0.00,-350.00,350.00,-1,NULL,1,10078,'2026-09-21 12:08:18.929052','R000002','SC Salmon','None',-350.00,0.00,10003),(0.00,-25.00,25.00,-1,1,2,10082,'2026-09-21 12:08:32.476092','R000003','Coke','12 oz',-25.00,0.00,10001),(0.00,-50.00,50.00,-1,2,2,10083,'2026-09-21 12:08:32.479206','R000003','Coke','16 oz',-50.00,0.00,10001),(0.00,-70.00,70.00,-1,3,2,10084,'2026-09-21 12:08:32.482337','R000003','Coke','32 oz',-70.00,0.00,10001),(0.00,-85.00,85.00,-1,NULL,2,10088,'2026-09-21 12:11:38.173922','R000005','Overloaded FF','None',-85.00,0.00,10000),(0.00,-50.00,25.00,-2,1,1,10092,'2026-09-21 12:12:12.742657','R000006','Coke','12 oz',-25.00,0.00,10001),(0.00,-150.00,50.00,-3,2,1,10093,'2026-09-21 12:12:12.749594','R000006','Coke','16 oz',-50.00,0.00,10001),(0.00,-210.00,70.00,-3,3,1,10094,'2026-09-21 12:12:12.751706','R000006','Coke','32 oz',-70.00,0.00,10001),(0.00,-85.00,85.00,-1,NULL,4,10098,'2026-09-21 12:12:24.112553','R000007','Overloaded FF','None',-85.00,0.00,10000),(0.00,-350.00,350.00,-1,NULL,4,10099,'2026-09-21 12:12:24.115961','R000007','SC Salmon','None',-350.00,0.00,10003),(0.00,-490.00,490.00,-1,NULL,4,10100,'2026-09-21 12:12:24.120731','R000007','Panna Cotta','None',-490.00,0.00,10002),(0.00,-50.00,50.00,-1,2,4,10101,'2026-09-21 12:12:24.120731','R000007','Coke','16 oz',-50.00,0.00,10001),(0.00,-25.00,25.00,-1,1,4,10102,'2026-09-21 12:12:24.120731','R000007','Coke','12 oz',-25.00,0.00,10001),(0.00,-70.00,70.00,-1,3,4,10103,'2026-09-21 12:12:24.128800','R000007','Coke','32 oz',-70.00,0.00,10001),(0.00,-980.00,490.00,-2,NULL,1,10107,'2026-09-21 13:17:05.480438','R000008','Panna Cotta','None',-490.00,0.00,10002),(0.00,-490.00,490.00,-1,NULL,4,10111,'2026-09-21 13:34:00.401515','R000009','Panna Cotta','None',-490.00,0.00,10002),(0.00,-25.00,25.00,-1,1,4,10112,'2026-09-21 13:34:00.403784','R000009','Coke','12 oz',-25.00,0.00,10001),(0.00,-85.00,85.00,-1,NULL,3,10116,'2026-09-22 11:06:55.173214','R000010','Overloaded FF','None',-85.00,0.00,10000),(0.00,-490.00,490.00,-1,NULL,3,10117,'2026-09-22 11:06:55.180724','R000010','Panna Cotta','None',-490.00,0.00,10002),(0.00,-170.00,85.00,-2,NULL,2,10120,'2026-09-22 13:14:59.771001','R000011','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,4,10124,'2026-09-22 13:31:19.095436','R000012','Overloaded FF','None',-85.00,0.00,10000),(0.00,-50.00,50.00,-1,2,4,10125,'2026-09-22 13:31:19.102444','R000012','Coke','16 oz',-50.00,0.00,10001),(0.00,-170.00,85.00,-2,NULL,2,10128,'2026-09-23 08:43:42.719430','R000013','Overloaded FF','None',-85.00,0.00,10000),(0.00,-1960.00,490.00,-4,NULL,2,10129,'2026-09-23 08:43:42.727965','R000013','Panna Cotta','None',-490.00,0.00,10002),(0.00,-100.00,25.00,-4,1,1,10132,'2026-09-23 09:01:23.607849','R000014','Coke','12 oz',-25.00,0.00,10001),(0.00,-100.00,50.00,-2,2,1,10133,'2026-09-23 09:01:23.612842','R000014','Coke','16 oz',-50.00,0.00,10001),(0.00,-210.00,70.00,-3,3,1,10134,'2026-09-23 09:01:23.616853','R000014','Coke','32 oz',-70.00,0.00,10001),(0.00,-1470.00,490.00,-3,NULL,1,10138,'2026-09-23 09:01:52.338669','R000015','Panna Cotta','None',-490.00,0.00,10002),(0.00,-85.00,85.00,-1,NULL,1,10143,'2026-09-23 09:03:45.402249','R000016','Overloaded FF','None',-85.00,0.00,10000),(0.00,-490.00,490.00,-1,NULL,3,10147,'2026-09-23 09:05:00.899007','R000017','Panna Cotta','None',-490.00,0.00,10002),(0.00,-490.00,490.00,-1,NULL,1,10152,'2026-09-23 09:08:44.309054','R000018','Panna Cotta','None',-490.00,0.00,10002),(0.00,-85.00,85.00,-1,NULL,1,10153,'2026-09-23 09:08:44.314245','R000018','Overloaded FF','None',-85.00,0.00,10000),(0.00,-490.00,490.00,-2,NULL,4,10157,'2026-09-23 09:18:00.812046','R022','Panna Cotta','None',-490.00,0.00,10002),(0.00,-250.00,25.00,-10,1,5,10160,'2026-09-23 09:30:00.781154','R022','Coke','12 oz',-25.00,0.00,10001),(0.00,-50.00,50.00,-1,2,5,10161,'2026-09-23 09:30:00.789151','R022','Coke','16 oz',-50.00,0.00,10001),(0.00,-980.00,490.00,-2,NULL,1,10164,'2026-09-23 09:33:43.468910','R000019','Panna Cotta','None',-490.00,0.00,10002),(0.00,-75.00,25.00,-3,1,1,10165,'2026-09-23 09:33:43.474640','R000019','Coke','12 oz',-25.00,0.00,10001),(0.00,-100.00,50.00,-2,2,1,10166,'2026-09-23 09:33:43.479111','R000019','Coke','16 oz',-50.00,0.00,10001),(0.00,-140.00,70.00,-2,3,1,10167,'2026-09-23 09:33:43.483645','R000019','Coke','32 oz',-70.00,0.00,10001),(0.00,-25.00,25.00,-1,1,2,10171,'2026-09-23 09:39:41.237636','R00020','Coke','12 oz',-25.00,0.00,10001),(0.00,-70.00,70.00,-1,3,2,10172,'2026-09-23 09:39:41.242221','R00020','Coke','32 oz',-70.00,0.00,10001),(0.00,-50.00,50.00,-1,2,2,10173,'2026-09-23 09:39:41.245224','R00020','Coke','16 oz',-50.00,0.00,10001),(0.00,-50.00,50.00,-3,2,3,10177,'2026-09-23 09:40:00.889952','R0021','Coke','16 oz',-50.00,0.00,10001),(0.00,-70.00,70.00,-8,3,3,10178,'2026-09-23 09:40:00.893772','R0021','Coke','32 oz',-70.00,0.00,10001),(0.00,-25.00,25.00,-6,1,3,10179,'2026-09-23 09:40:00.896788','R0021','Coke','12 oz',-25.00,0.00,10001),(0.00,-50.00,25.00,-2,1,4,10183,'2026-09-23 09:41:51.056097','R23','Coke','12 oz',-25.00,0.00,10001),(0.00,-70.00,70.00,-1,3,4,10184,'2026-09-23 09:41:51.057343','R23','Coke','32 oz',-70.00,0.00,10001),(0.00,-85.00,85.00,-1,NULL,1,10187,'2026-09-23 09:43:31.193134','R24','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,1,10190,'2026-09-23 09:48:11.564342','R25','Overloaded FF','None',-85.00,0.00,10000),(0.00,-490.00,490.00,-1,NULL,1,10191,'2026-09-23 09:48:11.566428','R25','Panna Cotta','None',-490.00,0.00,10002),(0.00,-85.00,85.00,-1,NULL,1,10195,'2026-09-23 09:49:39.720873','R26','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,1,10199,'2026-09-23 09:50:08.144800','R27','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,1,10203,'2026-09-23 09:54:26.351950','R28','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,4,10207,'2026-09-23 09:56:24.261007','R29','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,1,10210,'2026-09-23 09:56:39.646156','R30','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,1,10213,'2026-09-23 09:57:49.020079','R31','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,1,10217,'2026-09-23 09:59:37.012372','R32','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,3,10221,'2026-09-23 10:02:59.537481','R33','Overloaded FF','None',-85.00,0.00,10000),(0.00,-85.00,85.00,-1,NULL,3,10225,'2026-09-23 10:08:37.575008','R34','Overloaded FF','None',-85.00,0.00,10000),(0.00,-490.00,490.00,-1,NULL,4,10229,'2026-09-23 10:09:50.140154','R35','Panna Cotta','None',-490.00,0.00,10002),(0.00,-85.00,85.00,-1,NULL,3,10234,'2026-09-23 10:12:12.565503','R36','Overloaded FF','None',-85.00,0.00,10000),(0.00,-490.00,490.00,-1,NULL,1,10238,'2026-09-23 10:15:04.276842','R37','Panna Cotta','None',-490.00,0.00,10002);
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
INSERT INTO `transaction_payment` VALUES (10069,4000.00,'',_binary '\0',1,'R000001','2026-09-21 12:07:20.844077',4),(10070,-80.00,'',_binary '',1,'R000001','2026-09-21 12:07:20.848110',4),(10074,435.00,'',_binary '\0',1,'R000004','2026-09-21 12:08:08.125075',3),(10079,1000.00,'',_binary '\0',1,'R000002','2026-09-21 12:08:18.929052',1),(10080,-75.00,'',_binary '',1,'R000002','2026-09-21 12:08:18.929052',1),(10085,15.00,'',_binary '\0',1,'R000003','2026-09-21 12:08:32.485805',2),(10086,130.00,'',_binary '\0',1,'R000003','2026-09-21 12:08:32.490413',2),(10089,100.00,'',_binary '\0',1,'R000005','2026-09-21 12:11:38.182291',2),(10090,-15.00,'',_binary '',1,'R000005','2026-09-21 12:11:38.186326',2),(10095,500.00,'',_binary '\0',1,'R000006','2026-09-21 12:12:12.751706',1),(10096,-90.00,'',_binary '',1,'R000006','2026-09-21 12:12:12.761589',1),(10104,1200.00,'',_binary '\0',1,'R000007','2026-09-21 12:12:24.128800',4),(10105,-130.00,'',_binary '',1,'R000007','2026-09-21 12:12:24.132642',4),(10108,1000.00,'',_binary '\0',1,'R000008','2026-09-21 13:17:05.485213',1),(10109,-20.00,'',_binary '',1,'R000008','2026-09-21 13:17:05.491610',1),(10113,500.00,'',_binary '\0',1,'R000009','2026-09-21 13:34:00.403784',4),(10114,15.00,'',_binary '\0',1,'R000009','2026-09-21 13:34:00.409691',4),(10118,575.00,'',_binary '\0',1,'R000010','2026-09-22 11:06:55.182722',3),(10121,65.00,'',_binary '\0',1,'R000011','2026-09-22 13:14:59.781475',2),(10122,105.00,'',_binary '\0',1,'R000011','2026-09-22 13:14:59.787015',2),(10126,135.00,'',_binary '\0',1,'R000012','2026-09-22 13:31:19.106434',4),(10130,2130.00,'',_binary '\0',1,'R000013','2026-09-23 08:43:42.732964',2),(10135,450.00,'',_binary '\0',1,'R000014','2026-09-23 09:01:23.620844',1),(10136,-40.00,'',_binary '',1,'R000014','2026-09-23 09:01:23.625839',1),(10139,147.00,'',_binary '\0',1,'R000015','2026-09-23 09:01:52.343320',1),(10140,1500.00,'',_binary '\0',1,'R000015','2026-09-23 09:01:52.346316',1),(10141,-177.00,'',_binary '',1,'R000015','2026-09-23 09:01:52.349326',1),(10144,100.00,'',_binary '\0',1,'R000016','2026-09-23 09:03:45.406251',1),(10145,-15.00,'',_binary '',1,'R000016','2026-09-23 09:03:45.408250',1),(10148,100.00,'',_binary '\0',1,'R000017','2026-09-23 09:05:00.903027',3),(10149,400.00,'',_binary '\0',1,'R000017','2026-09-23 09:05:00.906067',3),(10150,-10.00,'',_binary '',1,'R000017','2026-09-23 09:05:00.908022',3),(10154,600.00,'',_binary '\0',1,'R000018','2026-09-23 09:08:44.318268',1),(10155,-25.00,'',_binary '',1,'R000018','2026-09-23 09:08:44.321269',1),(10158,490.00,'',_binary '\0',1,'R022','2026-09-23 09:18:00.819515',4),(10162,300.00,'',_binary '\0',1,'R022','2026-09-23 09:30:00.793156',5),(10168,1300.00,'',_binary '\0',1,'R000019','2026-09-23 09:33:43.487649',1),(10169,-5.00,'',_binary '',1,'R000019','2026-09-23 09:33:43.490667',1),(10174,150.00,'',_binary '\0',1,'R00020','2026-09-23 09:39:41.248225',2),(10175,-5.00,'',_binary '',1,'R00020','2026-09-23 09:39:41.251695',2),(10180,150.00,'',_binary '\0',1,'R0021','2026-09-23 09:40:00.899792',3),(10181,-5.00,'',_binary '',1,'R0021','2026-09-23 09:40:00.902769',3),(10185,120.00,'',_binary '\0',1,'R23','2026-09-23 09:41:51.061368',4),(10188,85.00,'',_binary '\0',1,'R24','2026-09-23 09:43:31.193134',1),(10192,600.00,'',_binary '\0',1,'R25','2026-09-23 09:48:11.567430',1),(10193,-25.00,'',_binary '',1,'R25','2026-09-23 09:48:11.569065',1),(10196,100.00,'',_binary '\0',1,'R26','2026-09-23 09:49:39.722873',1),(10197,-15.00,'',_binary '',1,'R26','2026-09-23 09:49:39.724674',1),(10200,100.00,'',_binary '\0',1,'R27','2026-09-23 09:50:08.145799',1),(10201,-15.00,'',_binary '',1,'R27','2026-09-23 09:50:08.146801',1),(10204,90.00,'',_binary '\0',1,'R28','2026-09-23 09:54:26.352953',1),(10205,-5.00,'',_binary '',1,'R28','2026-09-23 09:54:26.353953',1),(10208,85.00,'',_binary '\0',1,'R29','2026-09-23 09:56:24.263723',4),(10211,85.00,'',_binary '\0',1,'R30','2026-09-23 09:56:39.647156',1),(10214,90.00,'',_binary '\0',1,'R31','2026-09-23 09:57:49.021085',1),(10215,-5.00,'',_binary '',1,'R31','2026-09-23 09:57:49.023078',1),(10218,588.00,'',_binary '\0',1,'R32','2026-09-23 09:59:37.012372',1),(10219,-503.00,'',_binary '',1,'R32','2026-09-23 09:59:37.013362',1),(10222,5.00,'',_binary '\0',1,'R33','2026-09-23 10:02:59.539472',3),(10223,80.00,'',_binary '\0',1,'R33','2026-09-23 10:02:59.539472',3),(10226,100.00,'',_binary '\0',1,'R34','2026-09-23 10:08:37.576224',3),(10227,-15.00,'',_binary '',1,'R34','2026-09-23 10:08:37.577231',3),(10230,5.00,'',_binary '\0',1,'R35','2026-09-23 10:09:50.141156',4),(10231,500.00,'',_binary '\0',1,'R35','2026-09-23 10:09:50.142155',4),(10232,-10.00,'',_binary '',1,'R35','2026-09-23 10:09:50.142155',4),(10235,100.00,'',_binary '\0',1,'R36','2026-09-23 10:12:12.569517',3),(10236,-15.00,'',_binary '',1,'R36','2026-09-23 10:12:12.571513',3),(10239,500.00,'',_binary '\0',1,'R37','2026-09-23 10:15:04.277928',1),(10240,-10.00,'',_binary '',1,'R37','2026-09-23 10:15:04.279944',1);
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
INSERT INTO `transaction_summary` VALUES (0.00,490.00,3500.00,8,10071,'2026-09-21 12:07:20.851087','R000001','0000000001',3920.00,420.00),(0.00,435.00,388.39,2,10075,'2026-09-21 12:08:08.125075','R000004','0000000002',435.00,46.61),(0.00,925.00,825.89,3,10081,'2026-09-21 12:08:18.936792','R000002','0000000003',925.00,99.11),(0.00,145.00,129.46,3,10087,'2026-09-21 12:08:32.490413','R000003','0000000003',145.00,15.54),(0.00,85.00,75.89,1,10091,'2026-09-21 12:11:38.190702','R000005','0000000003',85.00,9.11),(0.00,145.00,366.07,8,10097,'2026-09-21 12:12:12.763485','R000006','0000000004',410.00,43.93),(0.00,1070.00,955.35,6,10106,'2026-09-21 12:12:24.137019','R000007','0000000005',1070.00,114.65),(0.00,490.00,875.00,2,10110,'2026-09-21 13:17:05.491610','R000008','0000000006',980.00,105.00),(0.00,515.00,459.82,2,10115,'2026-09-21 13:34:00.411852','R000009','0000000007',515.00,55.18),(0.00,575.00,513.39,2,10119,'2026-09-22 11:06:55.189242','R000010','0000000008',575.00,61.61),(0.00,85.00,151.79,2,10123,'2026-09-22 13:14:59.790482','R000011','0000000009',170.00,18.21),(0.00,135.00,120.53,2,10127,'2026-09-22 13:31:19.109453','R000012','0000000010',135.00,14.47),(0.00,575.00,1901.79,6,10131,'2026-09-23 08:43:42.737963','R000013','0000000011',2130.00,228.21),(0.00,145.00,366.08,9,10137,'2026-09-23 09:01:23.628852','R000014','0000000012',410.00,43.92),(0.00,490.00,1312.50,3,10142,'2026-09-23 09:01:52.351731','R000015','0000000013',1470.00,157.50),(0.00,85.00,75.89,1,10146,'2026-09-23 09:03:45.411250','R000016','0000000014',85.00,9.11),(0.00,490.00,437.50,1,10151,'2026-09-23 09:05:00.910008','R000017','0000000015',490.00,52.50),(0.00,575.00,513.39,2,10156,'2026-09-23 09:08:44.323265','R000018','0000000016',575.00,61.61),(0.00,490.00,437.50,2,10159,'2026-09-23 09:18:00.823908','R022','0000000017',490.00,52.50),(0.00,75.00,273.21,11,10163,'2026-09-23 09:30:00.797153','R022','0000000018',300.00,26.79),(0.00,635.00,1224.28,9,10170,'2026-09-23 09:33:43.493652','R000019','0000000018',1295.00,70.72),(0.00,145.00,129.46,3,10176,'2026-09-23 09:39:41.255696','R00020','0000000018',145.00,15.54),(0.00,145.00,129.46,17,10182,'2026-09-23 09:40:00.906767','R0021','0000000018',145.00,15.54),(0.00,95.00,109.82,3,10186,'2026-09-23 09:41:51.061368','R23','0000000018',120.00,10.18),(0.00,85.00,75.89,1,10189,'2026-09-23 09:43:31.198478','R24','0000000019',85.00,9.11),(0.00,575.00,513.39,2,10194,'2026-09-23 09:48:11.570064','R25','0000000020',575.00,61.61),(0.00,85.00,75.89,1,10198,'2026-09-23 09:49:39.726866','R26','0000000021',85.00,9.11),(0.00,85.00,75.89,1,10202,'2026-09-23 09:50:08.148798','R27','0000000022',85.00,9.11),(0.00,85.00,75.89,1,10206,'2026-09-23 09:54:26.354953','R28','0000000023',85.00,9.11),(0.00,85.00,75.89,1,10209,'2026-09-23 09:56:24.264728','R29','0000000024',85.00,9.11),(0.00,85.00,75.89,1,10212,'2026-09-23 09:56:39.648156','R30','0000000025',85.00,9.11),(0.00,85.00,75.89,1,10216,'2026-09-23 09:57:49.024079','R31','0000000026',85.00,9.11),(0.00,85.00,75.89,1,10220,'2026-09-23 09:59:37.013362','R32','0000000027',85.00,9.11),(0.00,85.00,75.89,1,10224,'2026-09-23 10:02:59.540473','R33','0000000028',85.00,9.11),(0.00,85.00,75.89,1,10228,'2026-09-23 10:08:37.577231','R34','0000000029',85.00,9.11),(0.00,490.00,437.50,1,10233,'2026-09-23 10:09:50.143155','R35','0000000030',495.00,52.50),(0.00,85.00,75.89,1,10237,'2026-09-23 10:12:12.572512','R36','0000000031',85.00,9.11),(0.00,490.00,437.50,1,10241,'2026-09-23 10:15:04.280933','R37','0000000032',490.00,52.50);
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
  `user_role` tinyint NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`),
  CONSTRAINT `user_chk_1` CHECK ((`user_role` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (10004,'Marcus','Felixmeña',NULL,'$2a$12$CJkBJul.L4k0OaT8ZsIQK.yozUthvKrINyY6JCTuu9VBEbpnIiQD.','2026-09-21 17:22:17.183237','2026-09-21 17:22:17.183237',2,'d3e587dd-3585-4d78-9ab4-bcb8adae879e','marcus23'),(10007,'sir','squire','my name','$2a$12$jwTJmWvh22Vf6cn/nmXQSeRwywniJG.G1YcT6jhvgl8USDHNWY9O.','2026-09-22 08:47:29.142429','2026-09-22 08:47:29.142429',0,'a59e61e0-d84a-4036-a808-3ea84658b9f6','sir');
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
INSERT INTO `user_id_sequence` VALUES (10008);
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

-- Dump completed on 2026-09-28 15:37:00
