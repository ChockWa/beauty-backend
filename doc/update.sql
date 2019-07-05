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
  `id` VARCHAR(36) NOT NULL COMMENT '自增id',
  `type` int(2) NULL DEFAULT NULL COMMENT '类型',
  `category` int(2) NULL DEFAULT NULL COMMENT '分类',
  org int(3) null comment '所属机构',
  model varchar(16) null comment '人物',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `description` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `cover` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面',
  `pics` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预览图片',
  `time` int(5) NULL DEFAULT NULL COMMENT '时长单位:分钟',
  zip_download_link varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'zip下载地址',
  `download_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下载码',
  `download_link` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下载地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源表' ROW_FORMAT =
Dynamic;

-- ----------------------------
-- Records of sys_source
-- ----------------------------

-- ----------------------------
-- Table structure for sys_source_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_source_detail`;
CREATE TABLE `sys_source_detail`  (
  `id` VARCHAR(36) NOT NULL COMMENT '自增id',
  `source_id` VARCHAR(36) NULL DEFAULT NULL COMMENT '资源id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称',
  `thumb_image` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '带域名缩略图',
  `origin_thumb_image` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '不带域名缩略图',
  `pic_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '带域名图片url',
  `origin_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '不带域名地址',
  `delete_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_source_id`(`source_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源详情表' ROW_FORMAT = 
Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

create table sys_user(
  uid varchar(64) not null comment 'uid',
  user_name varchar(64) null comment '用户名',
  password varchar(64) null comment '密码',
  salt varchar(64) null comment '盐',
  email varchar(64) null comment '邮箱',
  mobile varchar(64) null comment '手机号',
  is_vip int(2) not null default 0 comment '是否vip',
  status int(2) null comment '正常',
  create_time datetime null comment '创建时间',
  update_time datetime null comment '更新时间',
  avator varchar(128) null comment '头像',
  point int(5) null comment '积分',
  primary key (uid),
  unique key idx_username(user_name)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户表';
INSERT INTO `sys_user` VALUES ('91a96c4621974583b987c8b72f2f9ed4', 'chockwa', '569badc2c8b71a6f19a23704e1b17b99', '91a96c4621974583b987c8b72f2f9ed4', NULL, NULL, 0, 1, NULL, NULL, NULL);


create table sys_source_hot(
  source_id VARCHAR(36) NOT NULL COMMENT '资源id',
  count int(10) null comment '点击次数',
  download_count int(10) null comment '下载次数',
  primary key (source_id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '热资源表';

create table sys_log(
  id bigint auto_increment not null,
  method varchar(32) null comment '方法',
  params varchar(256) null comment '参数',
  ip varchar(32) null comment 'ip',
  create_time datetime null comment '创建时间',
  primary key (id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '日志表';

create table sys_point_log(
  id bigint auto_increment not null,
  uid varchar(64) null comment '用户id',
  point int(5) null comment '变动积分',
  description varchar(64) null comment '描述',
  create_time datetime null comment '创建时间',
  primary key (id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '积分记录表';

create table sys_download_log(
  id bigint auto_increment not null,
  uid varchar(64) null comment '用户id',
  source_id VARCHAR(36) NOT NULL COMMENT '资源id',
  create_time datetime null comment '创建时间',
  primary key (id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '下载记录表';


