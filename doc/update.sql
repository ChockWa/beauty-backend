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
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
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
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称',
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
  vip_end_time datetime null comment 'vip有效时间',
  avator varchar(128) null comment '头像',
  point int(5) null comment '积分',
  coin int(5) null comment '币数',
  last_sign_time datetime null comment '最後簽到時間',
  sign_count int(3) null comment '連續簽到次數',
  last_receive_time datetime null comment '最後领取時間',
  primary key (uid),
  unique key idx_username(user_name)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户表';
INSERT INTO `sys_user` VALUES ('91a96c4621974583b987c8b72f2f9ed4', 'chockwa', '569badc2c8b71a6f19a23704e1b17b99', '91a96c4621974583b987c8b72f2f9ed4', NULL, NULL, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


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

create table sys_qm_info(
  id varchar(64) not null comment 'id',
  area int(2) null comment '地區',
  name varchar(64) null comment '名稱',
  description varchar(256) comment '描述',
  cover varchar(200) null comment '封面',
  image varchar(1024) comment '图片',
  contact varchar(128) null comment '聯繫方式',
  score varchar(4) null comment '熱度',
  price int(4) null comment '价格',
  create_time datetime null comment '创建时间',
  primary key (id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = 'qm信息表';

create table sys_qm_comment(
  id bigint auto_increment not null,
  uid varchar(64) null comment '用户id',
  qm_id varchar(64) null comment 'id',
  comment varchar(512) null comment '评论',
  create_time datetime null comment '创建时间',
  primary key (id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = 'qm评论表';

create table sys_charge_log(
  id bigint auto_increment not null,
  uid varchar(64) null comment '用户id',
  charge_code varchar(32) null comment '码',
  charge_coin int(4) null comment '币',
  charge_price int(4) null comment '金額',
  create_time datetime null comment '创建时间',
  primary key (id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '充值表';

create table sys_qm_buy_log(
  id bigint auto_increment not null,
  uid varchar(64) null comment '用户id',
  qm_id varchar(64) null comment 'qm id',
  create_time datetime null comment '创建时间',
  primary key (id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用戶購買qm記錄表';

create table sys_sign_log(
  id bigint auto_increment not null,
  uid varchar(64) null comment '用户id',
  create_time datetime null comment '创建时间',
  primary key (id)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用戶簽到表';

create table sys_card (
  card_no varchar(64) null comment '卡密',
  uid varchar(64) null comment '用户id',
  type int(3) null comment '卡類型',
  status int(3) null comment '狀態1-已使用2-未使用',
  create_time datetime null comment '創建時間',
  use_time datetime null comment '使用時間',
  primary key(card_no)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '卡密表';

create table sys_qm_confirm(
  id varchar(36) null comment 'qmid',
  uid varchar(36) null comment 'uid',
  area int(2) null comment '地區',
  name varchar(64) null comment '名稱',
  description varchar(256) comment '描述',
  cover varchar(128) null comment '封面',
  image varchar(1024) comment '图片',
  contact varchar(128) null comment '聯繫方式',
  status int(1) null comment '審核狀態1-通過0-待審核-1不通過',
  create_time datetime null comment '創建時間',
  primary key(uid)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用戶上傳qm表';

alter table sys_qm_info add column status int(2) not null default 1 comment '狀態';
-- 20191029
alter table sys_qm_info add column type int(1) not null default 1 comment '1-qm2-sn';
alter table sys_qm_info add column contact_code varchar(128) null comment '聯繫方式二維碼';

-- 20191104
alter table sys_user add column last_receive_time datetime null comment '最後领取時間';
