/*
 Navicat Premium Data Transfer

 Source Server         : myhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 192.168.246.128:3306
 Source Schema         : beauty

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 29/03/2019 19:05:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_source
-- ----------------------------
DROP TABLE IF EXISTS `sys_source`;
CREATE TABLE `sys_source`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `type` int(2) NULL DEFAULT NULL COMMENT '类型',
  `category` int(2) NULL DEFAULT NULL COMMENT '分类',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `description` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `cover` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面',
  `pics` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预览图片',
  `time` int(5) NULL DEFAULT NULL COMMENT '时长单位:分钟',
  `download_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下载码',
  `download_link` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下载地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源表' ROW_FORMAT = 
Dynamic;

-- ----------------------------
-- Records of sys_source
-- ----------------------------
INSERT INTO `sys_source` VALUES (1, 1, 1, 'fdsd', 'fwefwef', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_source_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_source_detail`;
CREATE TABLE `sys_source_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `source_id` bigint(20) NULL DEFAULT NULL COMMENT '资源id',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称',
  `pic_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片url',
  `delete_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_source_id`(`source_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源详情表' ROW_FORMAT = 
Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
