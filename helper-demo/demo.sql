-- CREATE SCHEMA `你的数据库名称` DEFAULT CHARACTER SET utf8mb4 ;

CREATE TABLE `app_user` (
                            `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `name` varchar(64) NOT NULL COMMENT '用户名称',
                            `mobile` bigint(11) unsigned NOT NULL COMMENT '手机号',
                            `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
                            `password` varchar(128) DEFAULT NULL COMMENT '密码',
                            `picture_url` varchar(1024) DEFAULT NULL,
                            `salt` varchar(64) DEFAULT NULL COMMENT '盐',
                            `token` varchar(128) DEFAULT NULL COMMENT 'token',
                            `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '状态:0-注销,1-正常,2-限制使用',
                            `create_time` datetime NOT NULL COMMENT '注册时间',
                            `last_modify_time` datetime NOT NULL COMMENT '修改时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `mobile_UNIQUE` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='APP用户';

CREATE TABLE `book` (
                        `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
                        `bar_code` bigint(20) unsigned DEFAULT NULL COMMENT '条形码',
                        `level_num` varchar(16) DEFAULT NULL COMMENT '等级',
                        `title` varchar(128) DEFAULT NULL COMMENT '标题',
                        `origin_title` varchar(128) DEFAULT NULL COMMENT '原标题',
                        `subtitle` varchar(128) DEFAULT NULL COMMENT '子标题',
                        `author` varchar(64) DEFAULT NULL COMMENT '作者',
                        `binding` varchar(16) DEFAULT NULL COMMENT '包装:精装,平装',
                        `pages` int(11) unsigned DEFAULT NULL COMMENT '总页数',
                        `images_medium` varchar(512) DEFAULT NULL COMMENT '封面图片(中)',
                        `images_large` varchar(512) DEFAULT NULL COMMENT '封面图片(大)',
                        `pubdate` datetime DEFAULT NULL COMMENT '出版日期',
                        `publisher` varchar(128) DEFAULT NULL COMMENT '出版商',
                        `isbn10` bigint(20) unsigned DEFAULT NULL COMMENT 'isbn(10位)',
                        `isbn13` bigint(20) unsigned DEFAULT NULL COMMENT 'isbn(13位)',
                        `summary` varchar(4096) DEFAULT NULL COMMENT '简介',
                        `price` int(11) unsigned DEFAULT NULL COMMENT '定价(分)',
                        `edition` tinyint(4) unsigned NOT NULL COMMENT '版次',
                        `owner_id` bigint(20) unsigned NOT NULL COMMENT '图书标准分类id',
                        `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '状态:1-有效，0-无效',
                        `create_time` datetime NOT NULL COMMENT '创建时间',
                        `last_modify_time` datetime NOT NULL COMMENT '修改时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `bar_code_UNIQUE` (`bar_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书';