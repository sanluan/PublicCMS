package com.publiccms.logic.service.cms;

// Generated 2016-1-25 10:53:21 by com.sanluan.common.source.SourceMaker

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.logic.dao.cms.CmsContentFileDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsContentFileService extends BaseService<CmsContentFile> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Long contentId, Long userId, Boolean image, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(contentId, userId, image, orderField, orderType, pageIndex, pageSize);
    }

    @SuppressWarnings("unchecked")
    public void update(long contentId, Long userId, List<CmsContentFile> files, List<CmsContentFile> images) {
        Set<Long> idList = new HashSet<Long>();
        if (notEmpty(images)) {
            for (CmsContentFile entity : images) {
                if (notEmpty(entity.getId())) {
                    update(entity.getId(), entity, new String[] { "id", "userId", "contentId", "image" });
                } else {
                    entity.setImage(true);
                    entity.setUserId(userId);
                    entity.setContentId(contentId);
                    save(entity);
                }
                idList.add(entity.getId());
            }
        }
        if (notEmpty(files)) {
            for (CmsContentFile entity : files) {
                if (notEmpty(entity.getId())) {
                    update(entity.getId(), entity, new String[] { "id", "userId", "contentId", "image" });
                } else {
                    entity.setContentId(contentId);
                    entity.setUserId(userId);
                    save(entity);
                }
                idList.add(entity.getId());
            }
        }
        for (CmsContentFile extend : (List<CmsContentFile>) getPage(contentId, null, null, null, null, null, null).getList()) {
            if (!idList.contains(extend.getId())) {
                delete(extend.getId());
            }
        }
    }

    @Autowired
    private CmsContentFileDao dao;
}