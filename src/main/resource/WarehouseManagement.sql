/*
 Navicat Premium Data Transfer

 Source Server         : wcm
 Source Server Type    : MySQL
 Source Server Version : 50624
 Source Host           : localhost
 Source Database       : WarehouseManagement

 Target Server Type    : MySQL
 Target Server Version : 50624
 File Encoding         : utf-8

 Date: 12/07/2018 17:56:04 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `CODE_TABLE`
-- ----------------------------
DROP TABLE IF EXISTS `CODE_TABLE`;
CREATE TABLE `CODE_TABLE` (
  `ID` varchar(50) NOT NULL,
  `PARENT_ID` varchar(50) NOT NULL,
  `CODE_LEVEL` int(11) DEFAULT NULL,
  `CODE_VALUE1` varchar(50) DEFAULT NULL,
  `CODE_VALUE2` longtext,
  `CODE_DESC` varchar(500) DEFAULT NULL,
  `CODE_INDEX` int(11) DEFAULT '0',
  `IS_END` int(11) DEFAULT NULL,
  `IS_VALIDATE` int(11) NOT NULL DEFAULT '1' COMMENT '0：无效\r\n            1：有效',
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `DELETE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  PRIMARY KEY (`ID`,`IS_VALIDATE`),
  KEY `Index_FK_PARENT_ID` (`PARENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
--  Records of `CODE_TABLE`
-- ----------------------------
BEGIN;
INSERT INTO `CODE_TABLE` VALUES ('1001', '0', '1', '111111', '', '重置密码', '10', '1', '1', '2016-10-12 09:05:05', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `ININVENTORY`
-- ----------------------------
DROP TABLE IF EXISTS `ININVENTORY`;
CREATE TABLE `ININVENTORY` (
  `ID` varchar(50) NOT NULL,
  `PRODUCTID` varchar(50) DEFAULT NULL COMMENT '产品序号',
  `SUPPLIER` varchar(50) DEFAULT NULL COMMENT '供应商',
  `INNUMBER` int(11) DEFAULT NULL,
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATE_USER` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `M_AUTH_FUNCTION_ROLE`
-- ----------------------------
DROP TABLE IF EXISTS `M_AUTH_FUNCTION_ROLE`;
CREATE TABLE `M_AUTH_FUNCTION_ROLE` (
  `ID` varchar(50) NOT NULL COMMENT '主键，UUID',
  `ROLE_ID` varchar(50) NOT NULL COMMENT '关联M_USER_ROLE表id',
  `FUNCTION_ID` varchar(50) NOT NULL COMMENT '关联M_FUNCTION_CONFIG表id',
  `IS_AUTH` int(11) DEFAULT '1' COMMENT '0：没有\r\n            1：有',
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  PRIMARY KEY (`ID`),
  KEY `Index_FK_ORG_ID` (`ROLE_ID`),
  KEY `Index_FK_FUNCTION_ID` (`FUNCTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织功能权限表';

-- ----------------------------
--  Records of `M_AUTH_FUNCTION_ROLE`
-- ----------------------------
BEGIN;
INSERT INTO `M_AUTH_FUNCTION_ROLE` VALUES ('402881895b35558d015b3557dfd50008', '402881895b352012015b3526e2070001', '40289ff1561610ec0156161f618b0000', '1', '2017-04-04 03:43:23', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35582afa0009', '402881895b352012015b3526e2070001', '4028e38156127c4901561284036c000f', '1', '2017-04-04 03:43:42', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35582afa000a', '402881895b352012015b3526e2070001', '4028e38156127c4901561284829e0010', '1', '2017-04-04 03:43:42', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355845e9000b', '402881895b352012015b3526e2070001', '4028e38156127c490156128619e50011', '1', '2017-04-04 03:43:49', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35585a38000c', '402881895b352012015b3526e2070001', '4028e381562095e1015620a2279d0000', '1', '2017-04-04 03:43:54', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35585a38000d', '402881895b352012015b3526e2070001', '4028e381562095e1015620a266bc0001', '1', '2017-04-04 03:43:54', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35585a39000e', '402881895b352012015b3526e2070001', '4028e381562095e1015620a2a08e0002', '1', '2017-04-04 03:43:54', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35585a39000f', '402881895b352012015b3526e2070001', '4028e381562095e1015620a2db8c0003', '1', '2017-04-04 03:43:54', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35585a390010', '402881895b352012015b3526e2070001', '4028e381562095e1015620a350330005', '1', '2017-04-04 03:43:54', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35585a390011', '402881895b352012015b3526e2070001', '4028e381562095e1015620a314db0004', '1', '2017-04-04 03:43:54', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355875400012', '402881895b352012015b3526e2070001', '4028e38156127c4901561286c5e50012', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355875400013', '402881895b352012015b3526e2070001', '4028e38156127c4901561286fed80013', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355875400014', '402881895b352012015b3526e2070001', '4028e38156127c49015612877c140015', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355875400015', '402881895b352012015b3526e2070001', '4028e38156127c490156128742b50014', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355875410016', '402881895b352012015b3526e2070001', '4028e38156127c490156128828900018', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355875410017', '402881895b352012015b3526e2070001', '4028e38156127c4901561287ee400017', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355875420018', '402881895b352012015b3526e2070001', '4028e38156127c4901561287b8080016', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b355875420019', '402881895b352012015b3526e2070001', '4028e38156127c490156128b8fc8001c', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35587542001a', '402881895b352012015b3526e2070001', '4028e38156127c49015612897b11001a', '1', '2017-04-04 03:44:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd0001b', '402881895b352012015b3526e2070001', '8a8a8c54561245c501561260c55c0000', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd1001c', '402881895b352012015b3526e2070001', '4028e38156123197015612345f940003', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd1001d', '402881895b352012015b3526e2070001', '8a8a8c54561245c501561270c5370001', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd1001e', '402881895b352012015b3526e2070001', '4028e3815612759e0156127625e50000', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd1001f', '402881895b352012015b3526e2070001', '40289ff156221ef70156222130ff0000', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd10020', '402881895b352012015b3526e2070001', '40289ff156221ef70156222175140001', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd10021', '402881895b352012015b3526e2070001', '40289ff156221ef701562221b9140002', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd10022', '402881895b352012015b3526e2070001', '40289ff156221ef701562221f2470003', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b35588fd10023', '402881895b352012015b3526e2070001', '40289ff156224e400156224f1a060000', '1', '2017-04-04 03:44:08', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b3558a3980024', '402881895b352012015b3526e2070001', '4028e38156127c490156127d01c10000', '1', '2017-04-04 03:44:13', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b3558a3980025', '402881895b352012015b3526e2070001', '4028e38156127c490156127d3c150001', '1', '2017-04-04 03:44:13', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b3558a3980026', '402881895b352012015b3526e2070001', '4028e38156127c490156127d71480002', '1', '2017-04-04 03:44:13', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b35558d015b3558a3980027', '402881895b352012015b3526e2070001', '4028e38156127c490156127da5180003', '1', '2017-04-04 03:44:13', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b358fc3015b359363540002', '402881895b358fc3015b35933fb80000', '40289ff1561610ec0156161f618b0000', '1', '2017-04-04 04:48:23', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `M_AUTH_ROLE`
-- ----------------------------
DROP TABLE IF EXISTS `M_AUTH_ROLE`;
CREATE TABLE `M_AUTH_ROLE` (
  `ID` varchar(50) NOT NULL COMMENT '主键,UUID',
  `CHANNEL_ID` varchar(50) NOT NULL COMMENT '栏目ID\r\n            关联M_CHANNEL_CONFIG表id',
  `ROLE_ID` varchar(50) DEFAULT NULL COMMENT '角色ID\r\n            关联M_USER_ROLE表ID',
  `IS_AUTH` int(11) DEFAULT '1' COMMENT '是否有权限\r\n            0：没有\r\n            1：有',
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '输入时间',
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '创建者\r\n            关联M_USER表ID字段',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '最后修改者\r\n            关联M_USER表ID字段',
  PRIMARY KEY (`ID`),
  KEY `Index_FK_CHANNEL_ID` (`CHANNEL_ID`),
  KEY `Index_FK_ROLE_ID` (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色栏目权限表';

-- ----------------------------
--  Records of `M_AUTH_ROLE`
-- ----------------------------
BEGIN;
INSERT INTO `M_AUTH_ROLE` VALUES ('402881895b358fc3015b35935b0c0001', '268f74864f4a11e6b2be0242ac110003', '402881895b358fc3015b35933fb80000', '1', '2017-04-04 04:48:21', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b358fc3015b35a29ec60005', '402881895b358fc3015b35a063800004', '402881895b358fc3015b35933fb80000', '1', '2017-04-04 05:05:01', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `M_CHANNEL_CONFIG`
-- ----------------------------
DROP TABLE IF EXISTS `M_CHANNEL_CONFIG`;
CREATE TABLE `M_CHANNEL_CONFIG` (
  `ID` varchar(50) NOT NULL COMMENT '主键，UUID',
  `PARENT_ID` varchar(50) NOT NULL COMMENT '父ID',
  `CHANNEL_NAME` varchar(50) DEFAULT NULL COMMENT '名称',
  `CHANNEL_INDEX` int(11) DEFAULT '0' COMMENT '索引',
  `CHANNEL_LEVEL` int(11) DEFAULT NULL COMMENT '等级',
  `CHANNEL_URL` varchar(500) DEFAULT NULL COMMENT 'URL',
  `CHANNEL_URL2` varchar(500) DEFAULT NULL COMMENT 'URL2',
  `IS_END` int(11) DEFAULT NULL COMMENT '是否根节点',
  `IS_VALIDATE` int(11) DEFAULT '1' COMMENT '是否有效\r\n            0：无效\r\n            1：有效',
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '输入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `DELETE_TIME` datetime DEFAULT NULL COMMENT '删除时间',
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '创建者\r\n            关联M_USER表ID字段',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '最后修改者\r\n            关联M_USER表ID字段',
  PRIMARY KEY (`ID`),
  KEY `Index_FK_PARENT_ID` (`PARENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='栏目配置表，用于配置栏目，并记录栏目url以及栏目层级';

-- ----------------------------
--  Records of `M_CHANNEL_CONFIG`
-- ----------------------------
BEGIN;
INSERT INTO `M_CHANNEL_CONFIG` VALUES ('268f74864f4a11e6b2be0242ac110003', '0', '管理中心', '0', '0', null, null, '0', '1', '2016-07-05 12:53:55', '2018-12-07 17:34:30', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('268fa9904f4a11e6b2be0242ac110003', '268f74864f4a11e6b2be0242ac110003', '系统管理', '999', '1', '', '', '0', '1', '2016-07-05 10:21:52', '2018-12-07 17:35:51', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('269078414f4a11e6b2be0242ac110003', '268fa9904f4a11e6b2be0242ac110003', '栏目管理', '4', '2', '/manager/channel', '', '1', '1', '2016-07-05 10:22:48', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880462120000', '268f74864f4a11e6b2be0242ac110003', '物资列表', '2', '1', '/manager/product', '', '1', '1', '2018-12-07 17:34:07', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e6788013301678804b8de0001', '268f74864f4a11e6b2be0242ac110003', '物资管理', '3', '1', '', '', '0', '1', '2018-12-07 17:34:30', '2018-12-07 17:35:17', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e6788013301678804f58e0002', '4028417e6788013301678804b8de0001', '出库审核', '1', '2', '/manager/inventoryauditor', '', '1', '1', '2018-12-07 17:34:45', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e6788013301678805310d0003', '4028417e6788013301678804b8de0001', '入库管理', '2', '2', '/manager/ininventory', '', '1', '1', '2018-12-07 17:35:00', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e67880133016788056ff00004', '4028417e6788013301678804b8de0001', '出库管理', '3', '2', '/manager/outinventory', '', '1', '1', '2018-12-07 17:35:16', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e6788013301678805f2f00005', '268fa9904f4a11e6b2be0242ac110003', '部门管理', '5', '2', '/manager/department', '', '1', '1', '2018-12-07 17:35:50', '2018-12-07 17:35:55', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b358fc3015b35a063800004', '268f74864f4a11e6b2be0242ac110003', 'Welcome.', '1', '1', '/manager/index/welcome', '', '1', '1', '2017-04-04 05:02:35', '2018-12-07 17:31:48', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156115ed30156115f3cab0000', '268fa9904f4a11e6b2be0242ac110003', '字典管理', '6', '2', '/manager/code', '', '1', '1', '2016-07-22 06:33:25', '2018-12-07 17:36:01', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156127e1d100004', '268fa9904f4a11e6b2be0242ac110003', '用户管理', '2', '2', '/manager/user', '', '1', '1', '2016-07-22 12:04:30', '2016-08-31 09:28:28', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156127e4d6c0005', '268fa9904f4a11e6b2be0242ac110003', '角色管理', '3', '2', '/manager/role', '', '1', '1', '2016-07-22 12:04:42', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156127e72630006', '268fa9904f4a11e6b2be0242ac110003', '个人信息', '0', '2', '/manager/loginUser', '', '1', '1', '2016-07-22 12:04:52', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156127e9ddf0007', '268fa9904f4a11e6b2be0242ac110003', '密码修改', '1', '2', '/manager/resetPassword', '', '1', '1', '2016-07-22 12:05:03', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `M_DEPARTMENT`
-- ----------------------------
DROP TABLE IF EXISTS `M_DEPARTMENT`;
CREATE TABLE `M_DEPARTMENT` (
  `ID` varchar(50) NOT NULL,
  `DEPARTMENTNAME` varchar(100) DEFAULT NULL,
  `DEPARTMENTDESC` varchar(500) DEFAULT NULL,
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(50) DEFAULT NULL,
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `M_DEPARTMENT`
-- ----------------------------
BEGIN;
INSERT INTO `M_DEPARTMENT` VALUES ('4028417e678801330167881203af0011', '后勤部', '后勤部', '2018-12-07 17:49:01', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e6788013301678814a0f50013', '编辑部', '编辑部', '2018-12-07 17:51:52', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `M_FUNCTION_CONFIG`
-- ----------------------------
DROP TABLE IF EXISTS `M_FUNCTION_CONFIG`;
CREATE TABLE `M_FUNCTION_CONFIG` (
  `ID` varchar(50) NOT NULL COMMENT 'ID主键，UUID',
  `CHANNEL_ID` varchar(50) NOT NULL COMMENT '栏目ID\r\n            关联M_CHANNEL_CONFIG表ID',
  `FUNCTION_NAME` varchar(50) DEFAULT NULL COMMENT '名称',
  `FUNCTION_INDEX` int(11) DEFAULT '0' COMMENT '索引',
  `HTML_URL` varchar(1000) DEFAULT NULL COMMENT 'URL\r\n            用于过滤器拦截请求url进行权限判断',
  `HTML_ID` varchar(50) DEFAULT NULL COMMENT '标签ID\r\n            用于页面jsp功能权限判断标识',
  `IS_VALIDATE` int(11) DEFAULT '1' COMMENT '是否有效\r\n            0：无效\r\n            1：有效',
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '输入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `DELETE_TIME` datetime DEFAULT NULL COMMENT '删除时间',
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '创建者\r\n            关联M_USER表ID字段',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '最后修改者\r\n            关联M_USER表ID字段',
  PRIMARY KEY (`ID`),
  KEY `Index_FK_CHANNEL_ID` (`CHANNEL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台功能配置表';

-- ----------------------------
--  Records of `M_FUNCTION_CONFIG`
-- ----------------------------
BEGIN;
INSERT INTO `M_FUNCTION_CONFIG` VALUES ('4028417e678801330167880892930006', '4028417e678801330167880462120000', '物资列表-新增权限', null, '/manager/product/add;/manager/department/departmentList', 'btn_product_add', '1', '2018-12-07 17:38:42', '2018-12-07 17:39:53', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880984b40007', '4028417e678801330167880462120000', '物资列表-查看列表权限', '-1', '/manager/product;/manager/product/list', '物资列表-查看列表权限', '1', '2018-12-07 17:39:44', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880a90360008', '4028417e678801330167880462120000', '物资列表-修改权限', '0', '/manager/product/update;/manager/department/departmentList', 'btn_productt_update', '1', '2018-12-07 17:40:52', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880af7130009', '4028417e678801330167880462120000', '物资列表-删除权限', '0', '/manager/product/del', 'btn_product_delete', '1', '2018-12-07 17:41:19', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880bb732000a', '4028417e678801330167880462120000', '物资列表-查看产品金额权限', '0', '/manager/product', 'global_show_price', '1', '2018-12-07 17:42:08', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880cb9af000b', '4028417e6788013301678804f58e0002', '出库审核-查看列表权限', '0', '/manager/inventoryauditor;/manager/inventoryauditor/list', '出库审核-查看列表权限', '1', '2018-12-07 17:43:14', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880d5b81000c', '4028417e6788013301678804f58e0002', '出库审核-审核权限', '-1', '/manager/product/getProduct;/manager/inventoryauditor/auditor', 'btn_inventoryAuditor', '1', '2018-12-07 17:43:56', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880e6484000d', '4028417e6788013301678805310d0003', '入库管理-查看列表权限', '0', '/manager/ininventory;/manager/ininventory/list', '入库管理-查看列表权限', '1', '2018-12-07 17:45:03', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e678801330167880f1ecf000e', '4028417e6788013301678805310d0003', '入库管理-扫码入库权限', null, '/manager/product/getProduct;/manager/ininventory/add', 'btn_ininventory_add', '1', '2018-12-07 17:45:51', '2018-12-07 17:46:07', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e67880133016788100ed7000f', '4028417e67880133016788056ff00004', '出库管理-查看列表权限', '0', '/manager/outinventory;/manager/outinventory/list', '出库管理-查看列表权限', '1', '2018-12-07 17:46:53', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e6788013301678810f5980010', '4028417e67880133016788056ff00004', '出库管理-扫码出库权限', '-1', '/manager/product/getProduct;/manager/outinventory/add;/manager/department/departmentList', 'btn_outinventory_add', '1', '2018-12-07 17:47:52', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e67880133016788128c200012', '4028417e6788013301678805f2f00005', '部门管理-查看列表权限', '0', '/manager/department;/manager/department/list', '部门管理-查看列表权限', '1', '2018-12-07 17:49:36', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e6788013301678815284f0014', '4028417e6788013301678805f2f00005', '部门管理-新增权限', '-1', '/manager/department/add', 'btn_department_add', '1', '2018-12-07 17:52:27', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e6788013301678815f5aa0015', '4028417e6788013301678805f2f00005', '部门管理-修改权限', '-2', '/manager/department/update', 'btn_department_update', '1', '2018-12-07 17:53:19', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028417e67880133016788164d6a0016', '4028417e6788013301678805f2f00005', '部门管理-删除权限', '-3', '/manager/department/del', 'btn_department_delete', '1', '2018-12-07 17:53:42', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('40289ff1561610ec0156161f618b0000', '268f74864f4a11e6b2be0242ac110003', '访问', '0', '/manager;/manager/index/left;/manager/index/welcome;/manager/userChannel;/manager/checkDataLocked', 'default_querry', '1', '2016-07-23 04:59:30', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('40289ff156221ef70156222130ff0000', '269078414f4a11e6b2be0242ac110003', '功能_管理', '6', '/manager/function;/manager/function/list;/manager/function/exist', 'btn_functon_manager', '1', '2016-07-25 12:56:55', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('40289ff156221ef70156222175140001', '269078414f4a11e6b2be0242ac110003', '功能_新增', '7', '/manager/function/add', 'btn_function_add', '1', '2016-07-25 12:57:13', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('40289ff156221ef701562221b9140002', '269078414f4a11e6b2be0242ac110003', '功能_修改', '8', '/manager/function/update', 'btn_function_update', '1', '2016-07-25 12:57:30', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('40289ff156221ef701562221f2470003', '269078414f4a11e6b2be0242ac110003', '功能_删除', '9', '/manager/function/del', 'btn_function_delete', '1', '2016-07-25 12:57:45', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('40289ff156224e400156224f1a060000', '269078414f4a11e6b2be0242ac110003', '功能_排序', '10', '/manager/function/move', 'btn_function_sort', '1', '2016-07-25 13:30:13', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156123197015612345f940003', '269078414f4a11e6b2be0242ac110003', '查询', '2', '/manager/channel;/manager/channel/channelList;/manager/channel/right;/manager/channel/exist', 'btn_channel_querry', '1', '2016-07-22 10:43:57', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e3815612759e0156127625e50000', '269078414f4a11e6b2be0242ac110003', '删除', '5', '/manager/channel/delChannel', 'btn_channel_delete', '1', '2016-07-22 11:55:48', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156127d01c10000', '4028e38156115ed30156115f3cab0000', '查询', '0', '/manager/code;/manager/code/codeList;/manager/code/exist', 'btn_code_qurry', '1', '2016-07-22 12:03:17', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156127d3c150001', '4028e38156115ed30156115f3cab0000', '新增', '1', '/manager/code/add', 'btn_code_add', '1', '2016-07-22 12:03:32', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156127d71480002', '4028e38156115ed30156115f3cab0000', '修改', '2', '/manager/code/update', 'btn_code_update', '1', '2016-07-22 12:03:46', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156127da5180003', '4028e38156115ed30156115f3cab0000', '删除', '3', '/manager/code/delCode', 'btn_code_delete', '1', '2016-07-22 12:03:59', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c4901561284036c000f', '4028e38156127c490156127e72630006', '查询', '0', '/manager/loginUser;/manager/loginUser/exist;/manager/loginUser/authority;/manager/loginUser/authority/functionList;/manager/loginUser/authority/channelList;/manager/loginUser/role/detail;/manager/loginUser/role/detailList', 'btn_owner_query', '1', '2016-07-22 12:10:56', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c4901561284829e0010', '4028e38156127c490156127e72630006', '修改', '1', '/manager/loginUser/addUser', 'btn_owner_update', '1', '2016-07-22 12:11:29', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156128619e50011', '4028e38156127c490156127e9ddf0007', '修改密码', '0', '/manager/resetPassword;/manager/resetPassword/Info', 'btn_resetPassword', '1', '2016-07-22 12:13:13', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c4901561286c5e50012', '4028e38156127c490156127e4d6c0005', '查看', '0', '/manager/role;/manager/role/roleList;/manager/role/right;', 'btn_role_querry', '1', '2016-07-22 12:13:57', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c4901561286fed80013', '4028e38156127c490156127e4d6c0005', '新增', '1', '/manager/role/add', 'btn_role_add', '1', '2016-07-22 12:14:12', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156128742b50014', '4028e38156127c490156127e4d6c0005', '删除', '3', '/manager/role/delOrg;', 'btn_role_delete', '1', '2016-07-22 12:14:29', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c49015612877c140015', '4028e38156127c490156127e4d6c0005', '修改', '2', '/manager/role/update', 'btn_role_update', '1', '2016-07-22 12:14:44', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c4901561287b8080016', '4028e38156127c490156127e4d6c0005', '用户_新增', '6', '/manager/role/user/list;/manager/role/user/userList;/manager/role/user/add;/manager/role/user/exist', 'btn_role_user_add', '1', '2016-07-22 12:14:59', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c4901561287ee400017', '4028e38156127c490156127e4d6c0005', '用户_管理', '5', '/manager/role/user/detailUser;/manager/role/user/detailUserList;/manager/role/user/del', 'btn_role_user_manager', '1', '2016-07-22 12:15:13', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156128828900018', '4028e38156127c490156127e4d6c0005', '排序', '4', '/manager/role/sort', 'btn_role_sort', '1', '2016-07-22 12:15:28', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c49015612897b11001a', '4028e38156127c490156127e4d6c0005', '数据权限_管理', '8', '/manager/role/databaseAuth;/manager/role/searchInstance;/manager/role/searchDataBaseAuth;/manager/role/saveOrgAuth', 'btn_role_data_authity_manager', '1', '2016-07-22 12:16:55', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e38156127c490156128b8fc8001c', '4028e38156127c490156127e4d6c0005', '功能权限_管理', '7', '/manager/role/authority;/manager/role/authority/list;/manager/role/authority/add;/manager/role/authority/functionList;/manager/role/authority/addFunction', 'btn_role_function_authity_manager', '1', '2016-07-22 12:19:11', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e381562095e1015620a2279d0000', '4028e38156127c490156127e1d100004', '查询', '0', '/manager/user;/manager/user/userList;/manager/user/right;/manager/user/get;/manager/user/exist;', 'btn_user_querry', '1', '2016-07-25 05:58:33', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e381562095e1015620a266bc0001', '4028e38156127c490156127e1d100004', '新增', '1', '/manager/user/add;', 'btn_user_add', '1', '2016-07-25 05:58:49', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e381562095e1015620a2a08e0002', '4028e38156127c490156127e1d100004', '修改', '2', '/manager/user/update;', 'btn_user_update', '1', '2016-07-25 05:59:04', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e381562095e1015620a2db8c0003', '4028e38156127c490156127e1d100004', '删除', '3', '/manager/user/delUser;', 'btn_user_delete', '1', '2016-07-25 05:59:19', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e381562095e1015620a314db0004', '4028e38156127c490156127e1d100004', '组织_新增', '6', '/manager/user/role;/manager/user/role/list;/manager/user/role/add;', 'btn_user_role_add', '1', '2016-07-25 05:59:33', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('4028e381562095e1015620a350330005', '4028e38156127c490156127e1d100004', '组织_管理', '5', '/manager/user/role/detail;/manager/user/role/detailList;/manager/user/role/del;', 'btn_user_role_manager', '1', '2016-07-25 05:59:49', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('8a8a8c42581a13b001581a3303210000', '8a8a8c4256dfc3870156dfcc21ed0007', '查看', '0', '/manager/updateCodeTableForChannel;/manager/channelConfig', 'btn_channelConfig', '1', '2016-10-31 18:04:48', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('8a8a8c54561245c501561260c55c0000', '269078414f4a11e6b2be0242ac110003', '新增', '1', '/manager/channel/addChannel', 'btn_channel_add', '1', '2016-07-22 11:32:27', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('8a8a8c54561245c501561270c5370001', '269078414f4a11e6b2be0242ac110003', '修改', '4', '/manager/channel/updateChannel', 'btn_channel_update', '1', '2016-07-22 11:49:55', null, null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `M_USER`
-- ----------------------------
DROP TABLE IF EXISTS `M_USER`;
CREATE TABLE `M_USER` (
  `ID` varchar(50) NOT NULL COMMENT '主键，UUID',
  `LOGIN_NAME` varchar(50) NOT NULL,
  `LOGIN_PASSWORD` varchar(50) NOT NULL,
  `USER_NAME` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `IS_VALIDATE` int(11) DEFAULT '1' COMMENT '0：无效\r\n            1：有效\r\n            2：申请中\r\n            3：已拒绝',
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `DELETE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台用户表';

-- ----------------------------
--  Records of `M_USER`
-- ----------------------------
BEGIN;
INSERT INTO `M_USER` VALUES ('402881895b35558d015b355ac1490028', 'lichen', '96e79218965eb72c92a549dd5a330112', '李晨', 'li.chen@trs.com.cn', '1', '2017-04-04 03:46:31', null, '2017-04-04 03:48:41', '6ad47ab04f4c11e6b2be0242ac110003', '402881895b35558d015b355ac1490028'), ('402881895b357069015b3576978d0000', '111111', '96e79218965eb72c92a549dd5a330112', '111111', '123@123.com', '1', '2017-04-04 04:16:56', '2017-04-04 04:49:02', '2017-04-04 04:17:02', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('6ad47ab04f4c11e6b2be0242ac110003', 'admin', '96e79218965eb72c92a549dd5a330112', '超级管理员', 'admin@mail.com', '1', '2016-07-21 13:55:19', '2017-04-04 02:49:15', '2016-08-30 23:20:30', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `M_USER_ROLE`
-- ----------------------------
DROP TABLE IF EXISTS `M_USER_ROLE`;
CREATE TABLE `M_USER_ROLE` (
  `ID` varchar(50) NOT NULL COMMENT '主键，UUID',
  `ROLE_NAME` varchar(50) NOT NULL,
  `ROLE_DESC` text,
  `ROLE_INDEX` int(11) DEFAULT '0',
  `IS_VALIDATE` int(11) DEFAULT '1' COMMENT '0：无效\r\n            1：有效\r\n            ',
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `DELETE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台角色表';

-- ----------------------------
--  Records of `M_USER_ROLE`
-- ----------------------------
BEGIN;
INSERT INTO `M_USER_ROLE` VALUES ('402881895b352012015b3526e2070001', 'Administrators', '管理员', null, '1', '2017-04-04 02:49:52', '2017-04-04 04:45:28', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b358fc3015b35933fb80000', 'Welcome', 'Welcome', null, '1', '2017-04-04 04:48:14', '2017-04-04 05:04:50', null, '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `M_USER_ROLE_CONFIG`
-- ----------------------------
DROP TABLE IF EXISTS `M_USER_ROLE_CONFIG`;
CREATE TABLE `M_USER_ROLE_CONFIG` (
  `ID` varchar(50) NOT NULL COMMENT '主键,UUID',
  `USER_ID` varchar(50) NOT NULL COMMENT '关联M_USER表id',
  `ROLE_ID` varchar(50) NOT NULL COMMENT '关联M_USER_ROLE表id',
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '关联M_USER表ID字段',
  PRIMARY KEY (`ID`),
  KEY `Index_FK_ORG_ID` (`ROLE_ID`),
  KEY `index_FK_USER_ID` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台用户角色关系表';

-- ----------------------------
--  Records of `M_USER_ROLE_CONFIG`
-- ----------------------------
BEGIN;
INSERT INTO `M_USER_ROLE_CONFIG` VALUES ('402881895b352be0015b3530ebdd0000', '402881895b352012015b3526a8d40000', '402881895b352012015b3526e2070001', '2017-04-04 03:00:50', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003'), ('402881895b352be0015b353cc91d0026', '402881895b352be0015b353c180c0024', '402881895b352012015b3526e2070001', '2017-04-04 03:13:47', '6ad47ab04f4c11e6b2be0242ac110003', '6ad47ab04f4c11e6b2be0242ac110003');
COMMIT;

-- ----------------------------
--  Table structure for `OUTINVENTORY`
-- ----------------------------
DROP TABLE IF EXISTS `OUTINVENTORY`;
CREATE TABLE `OUTINVENTORY` (
  `ID` varchar(50) NOT NULL,
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATE_USER` varchar(50) DEFAULT NULL,
  `PRODUCTID` varchar(50) DEFAULT NULL COMMENT '产品序号',
  `OUTPERSION` varchar(50) DEFAULT NULL COMMENT '提取人',
  `OUTNUMBER` int(11) DEFAULT NULL COMMENT '提取数量',
  `STATUS` int(11) DEFAULT NULL COMMENT '提取状态',
  `COMMIT` varchar(500) DEFAULT NULL COMMENT '货物用途',
  `DEPARTMENTID` varchar(50) DEFAULT NULL COMMENT '所属部门',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '最后修改者\r\n            关联M_USER表ID字段',
  `DESCRIPTION` varchar(500) DEFAULT NULL COMMENT '货物用途',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `PRODUCT`
-- ----------------------------
DROP TABLE IF EXISTS `PRODUCT`;
CREATE TABLE `PRODUCT` (
  `ID` varchar(50) NOT NULL,
  `INPUT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(50) DEFAULT NULL,
  `LAST_UPDATE_USER` varchar(50) DEFAULT NULL,
  `PRODUCT_NAME` varchar(100) DEFAULT NULL COMMENT '物品名称',
  `DEPARTMENT` varchar(100) DEFAULT NULL COMMENT '所属部门',
  `OUTINVENTORY` int(11) DEFAULT NULL COMMENT '已出库数量',
  `ALLNUMBER` int(11) DEFAULT NULL COMMENT '入库总量',
  `PRICE` decimal(10,2) DEFAULT NULL COMMENT '单价',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  View structure for `v_ininventory`
-- ----------------------------
DROP VIEW IF EXISTS `v_ininventory`;
CREATE ALGORITHM=UNDEFINED DEFINER=`trswcm`@`localhost` SQL SECURITY DEFINER VIEW `v_ininventory` AS select `i`.`ID` AS `ID`,`i`.`INPUT_TIME` AS `INPUT_TIME`,`p`.`PRICE` AS `PRICE`,`u`.`USER_NAME` AS `CREATE_USERNAME`,`i`.`PRODUCTID` AS `PRODUCTID`,`p`.`PRODUCT_NAME` AS `PRODUCT_NAME`,`i`.`SUPPLIER` AS `SUPPLIER`,`i`.`INNUMBER` AS `INNUMBER` from ((`ininventory` `i` join `m_user` `u`) join `product` `p`) where ((`i`.`CREATE_USER` = `u`.`ID`) and (`p`.`ID` = `i`.`PRODUCTID`));

-- ----------------------------
--  View structure for `v_outinventory`
-- ----------------------------
DROP VIEW IF EXISTS `v_outinventory`;
CREATE ALGORITHM=UNDEFINED DEFINER=`trswcm`@`localhost` SQL SECURITY DEFINER VIEW `v_outinventory` AS select `o`.`ID` AS `ID`,`o`.`PRODUCTID` AS `PRODUCT_ID`,`p`.`PRODUCT_NAME` AS `PRODUCT_NAME`,`o`.`OUTPERSION` AS `OUTPERSION`,`o`.`OUTNUMBER` AS `OUTNUMBER`,`p`.`PRICE` AS `PRICE`,`o`.`INPUT_TIME` AS `INPUT_TIME`,`o`.`STATUS` AS `STATUS`,`u`.`USER_NAME` AS `USER_NAME`,`o`.`COMMIT` AS `COMMIT`,`o`.`DESCRIPTION` AS `DESCRIPTION`,`d`.`DEPARTMENTNAME` AS `DEPARTMENTNAME`,`o`.`UPDATE_TIME` AS `AUDITOR_TIME`,`uu`.`USER_NAME` AS `AUDITOR_USER` from ((((`outinventory` `o` join `product` `p`) join `m_user` `u`) join `m_department` `d`) join `m_user` `uu`) where ((`o`.`PRODUCTID` = `p`.`ID`) and (`o`.`CREATE_USER` = `u`.`ID`) and (`o`.`DEPARTMENTID` = `d`.`ID`) and (`uu`.`ID` = `o`.`LAST_UPDATE_USER`));

-- ----------------------------
--  View structure for `v_product`
-- ----------------------------
DROP VIEW IF EXISTS `v_product`;
CREATE ALGORITHM=UNDEFINED DEFINER=`trswcm`@`localhost` SQL SECURITY DEFINER VIEW `v_product` AS select `p`.`ID` AS `ID`,`p`.`INPUT_TIME` AS `INPUT_TIME`,`p`.`UPDATE_TIME` AS `UPDATE_TIME`,`p`.`CREATE_USER` AS `CREATE_USER`,`u`.`USER_NAME` AS `LAST_UPDATE_USERNAME`,`p`.`LAST_UPDATE_USER` AS `LAST_UPDATE_USER`,`p`.`PRODUCT_NAME` AS `PRODUCT_NAME`,`p`.`DEPARTMENT` AS `DEPARTMENT`,`p`.`OUTINVENTORY` AS `OUTINVENTORY`,`p`.`ALLNUMBER` AS `ALLNUMBER`,`p`.`PRICE` AS `PRICE`,`m`.`DEPARTMENTNAME` AS `DEPARTMENTNAME` from ((`product` `p` join `m_department` `m`) join `m_user` `u`) where ((`p`.`DEPARTMENT` = `m`.`ID`) and (`p`.`LAST_UPDATE_USER` = `u`.`ID`));

SET FOREIGN_KEY_CHECKS = 1;
