-- 2019-03-29 --
ALTER TABLE `cms_content_file`
    ADD COLUMN `width` int(0) NULL COMMENT '宽度' AFTER `file_size`,
    ADD COLUMN `height` int(0) NULL COMMENT '高度' AFTER `width`;
ALTER TABLE `log_upload`
    ADD COLUMN `width` int(0) NULL COMMENT '宽度' AFTER `file_size`,
    ADD COLUMN `height` int(0) NULL COMMENT '高度' AFTER `width`;