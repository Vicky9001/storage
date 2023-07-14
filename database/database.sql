CREATE DATABASE  IF NOT EXISTS `external` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `external`;
-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: external
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `department_id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'呼吸内科','111'),(3,'神经内科','12323'),(7,'心血管内科','12314'),(8,'内分泌科','124124'),(9,'普通外科','42442'),(10,'神经外科','222'),(11,'骨外科','111244'),(12,'妇科','1424'),(13,'产科','214425'),(14,'儿科','214425'),(15,'皮肤科','214425'),(16,'肿瘤内科','214425'),(17,'肿瘤外科','214425'),(18,'耳鼻喉科','214425'),(19,'眼科','214425'),(20,'口腔科','214425'),(21,'放射科','214425');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `distance`
--

DROP TABLE IF EXISTS `distance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `distance` (
  `related_type` tinyint NOT NULL,
  `related_id` bigint NOT NULL,
  `distance` float DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `distance`
--

LOCK TABLES `distance` WRITE;
/*!40000 ALTER TABLE `distance` DISABLE KEYS */;
INSERT INTO `distance` VALUES (0,5,30,1),(0,8,50,2),(1,1,20,3),(1,3,20,4);
/*!40000 ALTER TABLE `distance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `goods_id` bigint NOT NULL AUTO_INCREMENT,
  `goods_type` tinyint DEFAULT NULL,
  `goods_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `goods_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `grounding_num` float DEFAULT '0',
  `unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` VALUES (1,1,'999感冒灵','每盒9袋，每袋10g',55,'盒'),(2,3,'医用口罩','10个/包',220,'包'),(5,2,'红霉素软膏','200g/盒',0,'盒'),(6,2,'呼吸机','代替和改变人的正常生理呼吸工具',88,'台'),(7,2,'彩超机','非创伤性血管造影',0,'台'),(8,4,'氮气钢瓶','30L/瓶',70,'瓶'),(9,3,'体温计','19.4mm',0,'支'),(10,4,'数字胃肠造影机','200KHz的超高频逆变高压发生器',0,'台'),(11,2,'手术剪','14cm直尖',0,'把'),(12,1,'蒲地蓝消炎片','20片x3板/盒',0,'盒');
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trigger_warning_record` AFTER UPDATE ON `goods` FOR EACH ROW BEGIN
    DECLARE max_num FLOAT;
    DECLARE min_num FLOAT;
    SELECT max, min INTO max_num, min_num FROM warning WHERE goods_id = NEW.goods_id AND type = 1;
    IF EXISTS(SELECT * FROM warning_record WHERE goods_id = NEW.goods_id AND state = 0) THEN
        IF EXISTS(SELECT * FROM warning_record WHERE goods_id = NEW.goods_id AND state = 0 AND type = 1) THEN
            UPDATE warning_record SET extra_num = extra_num - (OLD.grounding_num - NEW.grounding_num), num = NEW.grounding_num WHERE goods_id = NEW.goods_id AND extra_num IS NOT NULL AND state = 0 AND type = 1;
        ELSEIF EXISTS(SELECT * FROM warning_record WHERE goods_id = NEW.goods_id AND state = 0 AND type = 2) THEN
            UPDATE warning_record SET extra_num = extra_num + (OLD.grounding_num - NEW.grounding_num), num = NEW.grounding_num WHERE goods_id = NEW.goods_id AND extra_num IS NOT NULL AND state = 0 AND type = 2;
        END IF;
    ELSE
        IF max_num IS NOT NULL AND NEW.grounding_num > max_num THEN
            INSERT INTO warning_record (warning_id, time, type, goods_id, num, extra_num, extra_time)
            VALUES ((SELECT id FROM warning WHERE goods_id = NEW.goods_id AND type = 1), NOW(), 1, NEW.goods_id, NEW.grounding_num, NEW.grounding_num - max_num, 0);
        ELSEIF min_num IS NOT NULL AND NEW.grounding_num < min_num THEN
            INSERT INTO warning_record (warning_id, time, type, goods_id, num, extra_num, extra_time)
            VALUES ((SELECT id FROM warning WHERE goods_id = NEW.goods_id AND type = 1), NOW(), 2, NEW.goods_id, NEW.grounding_num, min_num - NEW.grounding_num, 0);
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `goods_batch`
--

DROP TABLE IF EXISTS `goods_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_batch` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `goods_id` bigint DEFAULT NULL,
  `record_type` tinyint DEFAULT NULL COMMENT '1采购单，2入库单，3出库单，4内配单，5待入库单',
  `record_id` bigint DEFAULT NULL,
  `num` float DEFAULT NULL,
  `manufacture_date` date DEFAULT NULL,
  `expiration_date` date DEFAULT NULL,
  `unit_price` float DEFAULT NULL,
  `total_price` float DEFAULT NULL,
  `unit_weight` float DEFAULT NULL,
  `total_weight` float DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `related_id` bigint DEFAULT NULL,
  `position_id` bigint DEFAULT NULL COMMENT '绑定下货位（盘点统计的时候用）',
  `in_date` date DEFAULT NULL COMMENT '入库时间',
  `out_date` date DEFAULT NULL COMMENT '出库时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `goods_batch_goods_goods_id_fk` (`goods_id`) USING BTREE,
  KEY `goods_batch_supplier_id_fk` (`supplier_id`) USING BTREE,
  KEY `goods_batch_related_id_fk` (`related_id`) USING BTREE,
  CONSTRAINT `goods_batch_related_id_fk` FOREIGN KEY (`related_id`) REFERENCES `goods_batch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `goods_batch_supplier_id_fk` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_batch`
--

LOCK TABLES `goods_batch` WRITE;
/*!40000 ALTER TABLE `goods_batch` DISABLE KEYS */;
INSERT INTO `goods_batch` VALUES (11,1,1,11,403,'2023-03-25','2025-03-25',38,15314,0.5,201.5,5,NULL,NULL,NULL,NULL),(12,2,1,11,400,'2023-03-25','2025-03-25',10,4000,0.03,12,5,NULL,NULL,NULL,NULL),(13,1,1,12,200,'2023-03-25','2025-03-25',38,7600,0.5,100,NULL,NULL,NULL,NULL,NULL),(19,2,1,14,100,'2023-05-02','2025-05-02',10,8000,0.03,24,5,NULL,NULL,NULL,NULL),(30,2,1,18,700,'2023-03-25','2025-03-25',10,8000,0.03,24,5,NULL,NULL,NULL,NULL),(32,2,4,16,100,'2023-03-25','2025-03-25',10,8000,0.03,24,5,30,NULL,NULL,NULL),(33,2,4,14,35,'2023-03-25','2025-03-25',NULL,NULL,NULL,NULL,5,30,NULL,NULL,NULL),(35,1,4,16,35,'2023-03-25','2025-03-25',38,NULL,0.5,NULL,NULL,13,NULL,NULL,NULL),(40,2,3,3,50,'2023-05-02','2025-05-02',10,500,0.03,1.5,5,19,45,NULL,NULL),(41,2,3,3,20,'2023-05-02','2025-05-02',10,200,0.03,0.6,5,19,45,NULL,NULL),(42,1,2,23,100,NULL,NULL,NULL,1,NULL,NULL,5,NULL,NULL,NULL,NULL),(43,2,2,23,666,NULL,NULL,NULL,100,NULL,NULL,5,NULL,NULL,NULL,NULL),(44,1,2,24,222,NULL,NULL,NULL,200,NULL,NULL,5,NULL,NULL,NULL,NULL),(45,2,2,24,111,NULL,NULL,NULL,300,NULL,NULL,5,NULL,NULL,NULL,NULL),(46,1,3,6,100,NULL,NULL,NULL,400,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(47,2,3,7,150,NULL,NULL,NULL,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `goods_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_supplier`
--

DROP TABLE IF EXISTS `goods_supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_supplier` (
  `goods_id` bigint NOT NULL,
  `supplier_id` bigint NOT NULL,
  `unit_price` float DEFAULT NULL,
  `unit_weight` float DEFAULT NULL,
  PRIMARY KEY (`supplier_id`,`goods_id`) USING BTREE,
  KEY `good_supplier_goods_goods_id_fk` (`goods_id`) USING BTREE,
  CONSTRAINT `good_supplier_goods_goods_id_fk` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `good_supplier_supplier_supplier_id_fk` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_supplier`
--

LOCK TABLES `goods_supplier` WRITE;
/*!40000 ALTER TABLE `goods_supplier` DISABLE KEYS */;
INSERT INTO `goods_supplier` VALUES (1,5,38,0.5),(2,5,10,0.03),(1,8,37.5,0.45);
/*!40000 ALTER TABLE `goods_supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logistics_expense`
--

DROP TABLE IF EXISTS `logistics_expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logistics_expense` (
  `logistics_type` int NOT NULL AUTO_INCREMENT,
  `desc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `unit_price` float DEFAULT NULL,
  PRIMARY KEY (`logistics_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logistics_expense`
--

LOCK TABLES `logistics_expense` WRITE;
/*!40000 ALTER TABLE `logistics_expense` DISABLE KEYS */;
INSERT INTO `logistics_expense` VALUES (1,'海运',50),(2,'陆运',10),(3,'空运',30);
/*!40000 ALTER TABLE `logistics_expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_record`
--

DROP TABLE IF EXISTS `purchase_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `creator_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `creator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `state` tinyint DEFAULT '1',
  `logistics_price` double DEFAULT NULL,
  `goods_price` double DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `logistics_type` int DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `purchase_record_supplier_supplier_id_fk` (`supplier_id`) USING BTREE,
  CONSTRAINT `purchase_record_supplier_supplier_id_fk` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_record`
--

LOCK TABLES `purchase_record` WRITE;
/*!40000 ALTER TABLE `purchase_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `supplier_id` bigint NOT NULL AUTO_INCREMENT,
  `supplier_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `address` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (5,'BECON','北京海淀区','111'),(8,'林千琬','海南省海口市','8888');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','拥有所有功能权限'),(2,'库存管理员','查询库位的占用情况，处理积压库存，查询各类货物的库存及情况，以进行及时采购和对库内货物进行内配。'),(3,'审批人员','对采购单、内配单和出入库表单进行审核，检查需求是否合理合法。'),(4,'出入库管理员','对出入库进行管理'),(5,'工作人员','进行内配、打包、清点上架的完成确认。');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `real_name` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `password` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY `real_name` (`real_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','zts','111','123456'),(7,'小舒舒','小舒舒','123','666666'),(36,'abc','测试','111','11111111111');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_record`
--

DROP TABLE IF EXISTS `task_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_record` (
  `task_id` bigint NOT NULL AUTO_INCREMENT,
  `record_type` tinyint DEFAULT NULL,
  `record_id` bigint DEFAULT NULL,
  `task_type` tinyint DEFAULT NULL,
  `operator_id` bigint DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_record`
--

LOCK TABLES `task_record` WRITE;
/*!40000 ALTER TABLE `task_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE,
  KEY `fk_user_role_role_id` (`role_id`) USING BTREE,
  CONSTRAINT `fk_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(1,2),(7,3);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-09 23:32:14
CREATE DATABASE  IF NOT EXISTS `warehouse` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `warehouse`;
-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: warehouse
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `area` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `house_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `house_type` tinyint DEFAULT NULL COMMENT '仓库类型（1药品，2医疗器械，3医疗辅助用品，4放射物资及危险品）',
  `area_type` tinyint DEFAULT NULL COMMENT '(0-暂存区，1-普通存储区，2-处理品区)',
  `shelve_num` int DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'A',1,0,6),(2,'A',1,1,12),(3,'A',1,2,4),(4,'B',2,0,6),(5,'B',2,1,12),(6,'B',2,2,4),(7,'C',3,0,2),(8,'C',3,1,6),(9,'C',3,2,2),(28,'D',4,0,1),(29,'D',4,1,0),(30,'D',4,2,0);
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `check_record`
--

DROP TABLE IF EXISTS `check_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `check_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `creator_id` bigint DEFAULT NULL,
  `creator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `state` tinyint DEFAULT NULL COMMENT '1创建盘点单，2完成盘点，3盘点审核通过，4盘点审核未通过，5已根据盘点结果修正系统数据 ',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `position_id` bigint DEFAULT NULL,
  `record_id` bigint DEFAULT NULL,
  `record_type` tinyint DEFAULT NULL,
  `batch_info` bigint DEFAULT NULL,
  `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `unit_price` float(10,2) NOT NULL,
  `record_num` float DEFAULT NULL COMMENT '系统记录中的该货位物资数量',
  `real_num` float DEFAULT NULL COMMENT '经盘点该货位实际物资数量',
  `diff_num` float DEFAULT NULL COMMENT '差异数量（real_num-record_num）',
  `diff_price` float DEFAULT NULL COMMENT '总盘盈/盘亏金（含负数）',
  PRIMARY KEY (`id`,`unit_price`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `check_record`
--

LOCK TABLES `check_record` WRITE;
/*!40000 ALTER TABLE `check_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `check_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_batch`
--

DROP TABLE IF EXISTS `goods_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_batch` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `goods_id` bigint DEFAULT NULL,
  `record_type` tinyint DEFAULT NULL COMMENT '1采购单，2入库单，3出库单，4内配单，5待入库单',
  `record_id` bigint DEFAULT NULL,
  `num` float DEFAULT NULL,
  `manufacture_date` date DEFAULT NULL,
  `expiration_date` date DEFAULT NULL,
  `unit_price` float DEFAULT NULL,
  `total_price` float DEFAULT NULL,
  `unit_weight` float DEFAULT NULL,
  `total_weight` float DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `related_id` bigint DEFAULT NULL,
  `position_id` bigint DEFAULT NULL COMMENT '绑定下货位（盘点统计的时候用）',
  `in_date` date DEFAULT NULL COMMENT '入库时间',
  `out_date` date DEFAULT NULL COMMENT '出库时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `goods_batch_goods_goods_id_fk` (`goods_id`) USING BTREE,
  KEY `goods_batch_supplier_id_fk` (`supplier_id`) USING BTREE,
  KEY `goods_batch_related_id_fk` (`related_id`) USING BTREE,
  CONSTRAINT `goods_batch_related_id_fk` FOREIGN KEY (`related_id`) REFERENCES `goods_batch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_batch`
--

LOCK TABLES `goods_batch` WRITE;
/*!40000 ALTER TABLE `goods_batch` DISABLE KEYS */;
INSERT INTO `goods_batch` VALUES (11,1,1,11,403,'2023-03-25','2025-03-25',38,15314,0.5,201.5,5,NULL,NULL,NULL,NULL),(12,2,1,11,400,'2023-03-25','2025-03-25',10,4000,0.03,12,5,NULL,NULL,NULL,NULL),(13,1,1,12,200,'2023-03-25','2025-03-25',38,7600,0.5,100,NULL,NULL,NULL,NULL,NULL),(19,2,1,14,100,'2023-05-02','2025-05-02',10,8000,0.03,24,5,NULL,NULL,NULL,NULL),(30,2,1,18,700,'2023-03-25','2025-03-25',10,8000,0.03,24,5,NULL,NULL,NULL,NULL),(32,2,4,16,100,'2023-03-25','2025-03-25',10,8000,0.03,24,5,30,NULL,NULL,NULL),(33,2,4,14,35,'2023-03-25','2025-03-25',NULL,NULL,NULL,NULL,5,30,NULL,NULL,NULL),(35,1,4,16,35,'2023-03-25','2025-03-25',38,NULL,0.5,NULL,NULL,13,NULL,NULL,NULL),(40,2,3,3,50,'2023-05-02','2025-05-02',10,500,0.03,1.5,5,19,45,NULL,NULL),(41,2,3,3,20,'2023-05-02','2025-05-02',10,200,0.03,0.6,5,19,45,NULL,NULL),(42,1,2,23,100,NULL,NULL,NULL,1,NULL,NULL,5,NULL,NULL,NULL,NULL),(43,2,2,23,666,NULL,NULL,NULL,100,NULL,NULL,5,NULL,NULL,NULL,NULL),(44,1,2,24,222,NULL,NULL,NULL,200,NULL,NULL,5,NULL,NULL,NULL,NULL),(45,2,2,24,111,NULL,NULL,NULL,300,NULL,NULL,5,NULL,NULL,NULL,NULL),(46,1,3,6,100,NULL,NULL,NULL,400,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(47,2,3,7,150,NULL,NULL,NULL,500,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `goods_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `in_and_out`
--

DROP TABLE IF EXISTS `in_and_out`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `in_and_out` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `time` date DEFAULT NULL COMMENT '时间',
  `goods_num` float DEFAULT NULL COMMENT '商品数量',
  `goods_price` double DEFAULT NULL COMMENT '商品价格',
  `logistics_price` double DEFAULT NULL COMMENT '物流费用',
  `storage_cost` double DEFAULT NULL COMMENT '仓储费用',
  `total_price` double DEFAULT NULL COMMENT '总价格',
  `type` tinyint DEFAULT NULL COMMENT '1-采购 2-入库 3-出库',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='进出库统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `in_and_out`
--

LOCK TABLES `in_and_out` WRITE;
/*!40000 ALTER TABLE `in_and_out` DISABLE KEYS */;
INSERT INTO `in_and_out` VALUES (7,'2023-04-18',0,0,NULL,NULL,NULL,2),(8,'2023-04-18',0,0,0,NULL,0,3),(9,'2023-04-22',0,0,NULL,NULL,NULL,2),(10,'2023-04-22',0,0,0,NULL,0,3);
/*!40000 ALTER TABLE `in_and_out` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `out_record`
--

DROP TABLE IF EXISTS `out_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `out_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creator_id` bigint DEFAULT NULL,
  `department_id` bigint DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `state` tinyint DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `goods_price` double DEFAULT NULL,
  `logistics_type` tinyint DEFAULT NULL,
  `logistics_price` double DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `goods_id` bigint DEFAULT NULL,
  `num` float DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `out_record`
--

LOCK TABLES `out_record` WRITE;
/*!40000 ALTER TABLE `out_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `out_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position` (
  `position_id` bigint NOT NULL AUTO_INCREMENT,
  `area_id` bigint NOT NULL,
  `shelve_id` bigint NOT NULL,
  `goods_type` bigint DEFAULT NULL,
  `state` tinyint DEFAULT '0',
  `goods_num` float DEFAULT '0',
  `manufacture_date` date DEFAULT NULL,
  `expiration_date` date DEFAULT NULL,
  `batch_info` bigint DEFAULT NULL,
  `capacity` float DEFAULT NULL COMMENT '货位容量',
  `in_date` date DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (`position_id`) USING BTREE,
  KEY `area_shelve` (`area_id`) USING BTREE,
  KEY `position_goods` (`goods_type`) USING BTREE,
  CONSTRAINT `area_shelve` FOREIGN KEY (`area_id`) REFERENCES `area` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position`
--

LOCK TABLES `position` WRITE;
/*!40000 ALTER TABLE `position` DISABLE KEYS */;
INSERT INTO `position` VALUES (1,1,1,1,1,20,'2023-03-25','2023-04-10',13,50,'2023-04-01'),(2,1,2,2,1,100,'2023-03-25','2025-03-25',31,50,NULL),(3,1,3,1,0,0,NULL,NULL,NULL,50,NULL),(4,1,4,2,0,0,NULL,NULL,NULL,50,NULL),(5,1,5,NULL,0,0,NULL,NULL,NULL,50,NULL),(6,1,6,NULL,0,0,NULL,NULL,NULL,50,NULL),(7,2,1,1,0,0,NULL,NULL,NULL,50,NULL),(8,2,2,NULL,0,0,NULL,NULL,NULL,50,NULL),(9,2,3,2,1,30,'2023-03-25','2025-03-25',19,50,NULL),(10,2,4,2,0,0,NULL,NULL,NULL,50,NULL),(11,2,5,1,0,0,NULL,NULL,NULL,50,NULL),(12,2,6,1,0,0,NULL,NULL,NULL,50,NULL),(13,2,7,1,0,0,NULL,NULL,NULL,50,NULL),(14,2,8,NULL,0,0,NULL,NULL,NULL,50,NULL),(15,2,9,NULL,0,0,NULL,NULL,NULL,50,NULL),(16,2,10,NULL,0,0,NULL,NULL,NULL,50,NULL),(17,2,11,NULL,0,0,NULL,NULL,NULL,50,NULL),(18,2,12,NULL,0,0,NULL,NULL,NULL,50,NULL),(19,3,1,1,1,35,'2023-03-25','2025-03-25',13,50,NULL),(20,3,2,NULL,0,0,NULL,NULL,NULL,50,NULL),(21,3,3,NULL,0,0,NULL,NULL,NULL,50,NULL),(22,3,4,NULL,0,0,NULL,NULL,NULL,50,NULL),(23,4,1,NULL,0,0,NULL,NULL,NULL,50,NULL),(24,4,2,NULL,0,0,NULL,NULL,NULL,50,NULL),(25,4,3,NULL,0,0,NULL,NULL,NULL,50,NULL),(26,4,4,NULL,0,0,NULL,NULL,NULL,50,NULL),(27,4,5,NULL,0,0,NULL,NULL,NULL,50,NULL),(28,4,6,NULL,0,0,NULL,NULL,NULL,50,NULL),(29,5,1,NULL,0,0,NULL,NULL,NULL,50,NULL),(30,5,2,NULL,0,0,NULL,NULL,NULL,50,NULL),(31,5,3,NULL,0,0,NULL,NULL,NULL,50,NULL),(32,5,4,NULL,0,0,NULL,NULL,NULL,50,NULL),(33,5,5,NULL,0,0,NULL,NULL,NULL,50,NULL),(34,5,6,NULL,0,0,NULL,NULL,NULL,50,NULL),(35,5,7,NULL,0,0,NULL,NULL,NULL,50,NULL),(36,5,8,NULL,0,0,NULL,NULL,NULL,50,NULL),(37,5,8,NULL,0,0,NULL,NULL,NULL,50,NULL),(38,5,8,NULL,0,0,NULL,NULL,NULL,50,NULL),(39,5,8,NULL,0,0,NULL,NULL,NULL,50,NULL),(40,5,8,NULL,0,0,NULL,NULL,NULL,50,NULL),(41,6,1,NULL,0,0,NULL,NULL,NULL,50,NULL),(42,6,2,NULL,0,0,NULL,NULL,NULL,50,NULL),(43,6,3,NULL,0,0,NULL,NULL,NULL,50,NULL),(44,6,4,NULL,0,0,NULL,NULL,NULL,50,NULL),(45,7,1,2,1,20,'2023-05-02','2025-05-02',41,50,NULL),(46,7,2,NULL,0,0,NULL,NULL,NULL,50,NULL),(47,8,1,NULL,0,0,NULL,NULL,NULL,50,NULL),(48,8,2,NULL,0,0,NULL,NULL,NULL,50,NULL),(49,8,3,NULL,0,0,NULL,NULL,NULL,50,NULL),(50,8,4,NULL,0,0,NULL,NULL,NULL,50,NULL),(51,8,5,NULL,0,0,NULL,NULL,NULL,50,NULL),(52,8,6,NULL,0,0,NULL,NULL,NULL,50,NULL),(53,9,7,2,1,35,'2023-03-25','2025-03-25',31,50,NULL),(54,9,8,2,1,35,'2023-03-25','2025-03-25',31,50,NULL);
/*!40000 ALTER TABLE `position` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `p_insert` AFTER INSERT ON `position` FOR EACH ROW BEGIN
	update area,
	(select area_id,count(shelve_id) as c from position group by area_id) p
	set area.shelve_num = p.c 
	where area.id = p.area_id;
    
	update goods,
	(select goods_type,sum(goods_num) as s from position group by goods_type) g
	set goods.grounding_num = g.s
	where goods.goods_id = g.goods_type;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `p_update` AFTER UPDATE ON `position` FOR EACH ROW BEGIN
	update area,
	(select area_id,count(shelve_id) as c from position group by area_id) p
	set area.shelve_num = p.c 
	where area.id = p.area_id;
    
	update goods,
	(select goods_type,sum(goods_num) as s from position group by goods_type) g
	set goods.grounding_num = g.s
	where goods.goods_id = g.goods_type;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_warning_record_num` AFTER UPDATE ON `position` FOR EACH ROW BEGIN
    -- 当position表里的goods_num变化时，更新相应的warning_record
    IF NEW.goods_num != OLD.goods_num THEN
        UPDATE warning_record
        SET num = NEW.goods_num
        WHERE position_id = OLD.position_id
        AND state = 0;
    END IF;
    
    -- 当goods_num=0时，将相应的warning_record的state设置为1
    IF NEW.goods_num = 0 THEN
        UPDATE warning_record
        SET state = 1
        WHERE position_id = OLD.position_id
        AND state = 0;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `p_delete` AFTER DELETE ON `position` FOR EACH ROW BEGIN
	update area,
	(select area_id,count(shelve_id) as c from position group by area_id) p
	set area.shelve_num = p.c 
	where area.id = p.area_id;
    
	update goods,
	(select goods_type,sum(goods_num) as s from position group by goods_type) g
	set goods.grounding_num = g.s
	where goods.goods_id = g.goods_type;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `task_record`
--

DROP TABLE IF EXISTS `task_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_record` (
  `task_id` bigint NOT NULL AUTO_INCREMENT,
  `record_type` tinyint DEFAULT NULL,
  `record_id` bigint DEFAULT NULL,
  `task_type` tinyint DEFAULT NULL,
  `operator_id` bigint DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_record`
--

LOCK TABLES `task_record` WRITE;
/*!40000 ALTER TABLE `task_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer_record`
--

DROP TABLE IF EXISTS `transfer_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transfer_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `creator_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `creator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `state` tinyint DEFAULT '1',
  `origin_position` bigint DEFAULT NULL,
  `target_position` bigint DEFAULT NULL,
  `num` float DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `transfer_record_position_position_id_fk` (`origin_position`) USING BTREE,
  KEY `transfer_record_position_position_id_fk2` (`target_position`) USING BTREE,
  CONSTRAINT `transfer_record_position_position_id_fk` FOREIGN KEY (`origin_position`) REFERENCES `position` (`position_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `transfer_record_position_position_id_fk2` FOREIGN KEY (`target_position`) REFERENCES `position` (`position_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer_record`
--

LOCK TABLES `transfer_record` WRITE;
/*!40000 ALTER TABLE `transfer_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `transfer_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `waiting_record`
--

DROP TABLE IF EXISTS `waiting_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waiting_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `waiting_record`
--

LOCK TABLES `waiting_record` WRITE;
/*!40000 ALTER TABLE `waiting_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `waiting_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehousing_record`
--

DROP TABLE IF EXISTS `warehousing_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehousing_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL COMMENT '入库单自动创建时间',
  `state` tinyint DEFAULT '1' COMMENT '1待预检验收，2已删除，3待确认收货，4审核不通过，5待入库上架，6已上架',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `goods_price` double DEFAULT NULL,
  `creator_id` bigint DEFAULT NULL,
  `creator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehousing_record`
--

LOCK TABLES `warehousing_record` WRITE;
/*!40000 ALTER TABLE `warehousing_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `warehousing_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warning`
--

DROP TABLE IF EXISTS `warning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warning` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creator_id` bigint DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `type` tinyint DEFAULT NULL COMMENT '1库存预警 2过期预警 3 库龄预警 ',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `goods_id` bigint DEFAULT NULL,
  `max` float DEFAULT NULL,
  `min` float DEFAULT NULL COMMENT '预警下限',
  `expiration` int DEFAULT NULL COMMENT '距离过期x天前预警',
  `store_age` int DEFAULT NULL COMMENT '库龄/天',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `warning_goods_id_fk` (`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warning`
--

LOCK TABLES `warning` WRITE;
/*!40000 ALTER TABLE `warning` DISABLE KEYS */;
/*!40000 ALTER TABLE `warning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warning_record`
--

DROP TABLE IF EXISTS `warning_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warning_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `warning_id` bigint DEFAULT NULL,
  `time` date DEFAULT NULL,
  `type` tinyint DEFAULT NULL COMMENT '1 库存过剩 2库存不足 3即将过期 4已过期 5积压预警',
  `position_id` bigint DEFAULT NULL,
  `goods_id` bigint DEFAULT NULL,
  `num` float DEFAULT NULL,
  `extra_num` float DEFAULT NULL COMMENT '异常数量',
  `extra_time` int DEFAULT NULL COMMENT '异常时长/天(state=3 距过期时长，4已过期时长，5已积压时长)',
  `state` tinyint DEFAULT '0' COMMENT '0 未解决 1 已解决',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `warning_record_warning_id_fk` (`warning_id`) USING BTREE,
  CONSTRAINT `warning_record_warning_id_fk` FOREIGN KEY (`warning_id`) REFERENCES `warning` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warning_record`
--

LOCK TABLES `warning_record` WRITE;
/*!40000 ALTER TABLE `warning_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `warning_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trigger_warning_record_update` BEFORE UPDATE ON `warning_record` FOR EACH ROW BEGIN
    DECLARE max_num FLOAT;
    DECLARE min_num FLOAT;
    SELECT max, min INTO max_num, min_num FROM warning WHERE id = NEW.warning_id AND type = 1;
    IF NEW.state = 0 AND NEW.type = 1 AND max_num IS NOT NULL AND NEW.num < max_num THEN
        SET NEW.state = 1;
    ELSEIF NEW.state = 0 AND NEW.type = 2 AND min_num IS NOT NULL AND NEW.num > min_num THEN
        SET NEW.state = 1;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-09 23:32:15
