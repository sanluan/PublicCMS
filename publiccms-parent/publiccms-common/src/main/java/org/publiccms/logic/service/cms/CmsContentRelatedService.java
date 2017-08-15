package org.publiccms.logic.service.cms;

// Generated 2016-1-25 10:53:21 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.publiccms.entities.cms.CmsContentRelated;
import org.publiccms.logic.dao.cms.CmsContentRelatedDao;
import org.publiccms.views.pojo.CmsContentRelatedStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsContentRelatedService
 * 
 */
@Service
@Transactional
public class CmsContentRelatedService extends BaseService<CmsContentRelated> {
    
    private String[] ignoreProperties = new String[] { "id", "contentId", "userId" };
    
    /**
     * @param contentId
     * @param relatedContentId
     * @param userId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long contentId, Long relatedContentId, Long userId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(contentId, relatedContentId, userId, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param entitys
     */
    public void updateStatistics(Collection<CmsContentRelatedStatistics> entitys) {
        for (CmsContentRelatedStatistics entityStatistics : entitys) {
            CmsContentRelated entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setClicks(entity.getClicks() + entityStatistics.getClicks());
            }
        }
    }

    /**
     * @param contentId
     * @param userId
     * @param entitys
     */
    @SuppressWarnings("unchecked")
    public void update(long contentId, long userId, List<CmsContentRelated> entitys) {
        Set<Long> idList = new HashSet<Long>();
        if (notEmpty(entitys)) {
            for (CmsContentRelated entity : entitys) {
                if (null != entity.getId()) {
                    update(entity.getId(), entity, ignoreProperties);
                } else {
                    entity.setContentId(contentId);
                    entity.setUserId(userId);
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