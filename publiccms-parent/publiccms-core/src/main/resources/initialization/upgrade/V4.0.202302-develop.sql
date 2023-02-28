-- 2023-02-27 --
ALTER TABLE `log_operate` MODIFY COLUMN `content` longtext default NULL COMMENT '内容' AFTER `create_date`;
ALTER TABLE `log_operate`
    ADD COLUMN `privatefile` tinyint(1) NOT NULL DEFAULT 0 COMMENT '私有文件' AFTER 'original_name',
    DROP INDEX `log_upload_file_type`,
    ADD INDEX `log_upload_file_type` (`site_id`, `privatefile`, `file_type`, `file_size`);