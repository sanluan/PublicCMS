ALTER TABLE  `cms_model` ADD  `is_part` TINYINT( 1 ) NOT NULL DEFAULT  '0' COMMENT  '是否内容片段' AFTER  `is_images`;
-- 20150904 --
delete from cms_content where model_id in (select id from cms_model where is_images = 1) and parent_id is not null;
-- 20150908 请注意执行上面这条SQL之前请先编辑一遍所有图集 --