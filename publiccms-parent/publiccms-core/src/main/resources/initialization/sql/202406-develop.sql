-- 2025-12-05 --
UPDATE sys_module SET has_child = 1 WHERE id = 'myself_profile';
-- 2025-12-16 --


UPDATE sys_module SET authorized_url = 'cmsContent/preview,cmsContent/previewBeforeSave' WHERE id = 'content_view';