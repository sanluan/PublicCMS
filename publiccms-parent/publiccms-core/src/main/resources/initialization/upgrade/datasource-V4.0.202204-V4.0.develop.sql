-- 2022-05-10 --
ALTER TABLE `cms_content` 
    ADD COLUMN `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新用户' AFTER `check_date`;