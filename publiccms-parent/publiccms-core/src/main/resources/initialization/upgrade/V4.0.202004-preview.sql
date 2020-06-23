-- 2020-06-23 --
ALTER TABLE `cms_tag` 
    DROP INDEX `cms_tag_site_id`,
    ADD INDEX `cms_tag_site_id`(`site_id`, `name`),
    ADD INDEX  `cms_tag_type_id` (`type_id`);
ALTER TABLE `cms_tag_type`
    DROP INDEX `cms_tag_type_site_id`, 
    ADD INDEX `cms_tag_type_site_id` (`site_id`,`name`);