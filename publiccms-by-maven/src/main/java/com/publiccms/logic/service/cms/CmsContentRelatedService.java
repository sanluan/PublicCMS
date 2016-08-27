package com.publiccms.logic.service.cms;

// Generated 2016-1-25 10:53:21 by com.sanluan.common.source.SourceMaker

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.logic.dao.cms.CmsContentRelatedDao;
import com.publiccms.views.pojo.CmsContentRelatedStatistics;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsContentRelatedService extends BaseService<CmsContentRelated> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Long contentId, Long relatedContentId, Long userId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(contentId, relatedContentId, userId, orderField, orderType, pageIndex, pageSize);
    }

    public void updateStatistics(Collection<CmsContentRelatedStatistics> entitys) {
        for (CmsContentRelatedStatistics entityStatistics : entitys) {
            CmsContentRelated entity = getEntity(entityStatistics.getId());
            if (notEmpty(entity)) {
                entity.setClicks(entity.getClicks() + entityStatistics.getClicks());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void update(long contentId, List<CmsContentRelated> entitys) {
        Set<Long> idList = new HashSet<Long>();
        if (notEmpty(entitys)) {
            for (CmsContentRelated entity : entitys) {
                if (notEmpty(entity.getId())) {
                    update(entity.getId(), entity, new String[] { "id" });
                } else {
                    entity.setContentId(contentId);
                    save(entity);
                }
                idList.add(entity.getId());
            }
        }
        for (CmsContentRelated extend : (List<CmsContentRelated>) getPage(contentId, null, null, null, null, null, null)
                .getList()) {
            if (!idList.contains(extend.getId())) {
                delete(extend.getId());
            }
        }
    }

    @Autowired
    private CmsContentRelatedDao dao;
}