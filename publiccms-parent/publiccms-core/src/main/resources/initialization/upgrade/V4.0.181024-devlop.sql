ALTER TABLE `cms_category`
    MODIFY COLUMN `path` varchar(1000) NOT NULL COMMENT '首页路径' AFTER `template_path`,
    MODIFY COLUMN `url` varchar(1000) NOT NULL COMMENT '首页地址' AFTER `has_static`,
    MODIFY COLUMN `content_path` varchar(1000) NOT NULL COMMENT '内容路径' AFTER `url`;
ALTER TABLE `cms_content`
    MODIFY COLUMN `url` varchar(1000) NOT NULL COMMENT '地址' AFTER `has_static`;
ALTER TABLE `cms_content_attribute`
    MODIFY COLUMN `source_url` varchar(1000) NOT NULL COMMENT '来源地址' AFTER `source`;
ALTER TABLE `cms_place`
    MODIFY COLUMN `url` varchar(1000) default NULL COMMENT '超链接' AFTER `title`;
ALTER TABLE `cms_content_related`
    MODIFY COLUMN `url` varchar(1000) default NULL COMMENT '推荐链接地址' AFTER `user_id`;
