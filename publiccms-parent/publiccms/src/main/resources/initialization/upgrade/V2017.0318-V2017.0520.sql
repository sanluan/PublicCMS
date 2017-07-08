-- 20170412 --
DROP TABLE IF EXISTS `cms_content_tag`;
ALTER TABLE `cms_place` MODIFY COLUMN `user_id` bigint(20) NULL COMMENT '提交用户' AFTER `path`;