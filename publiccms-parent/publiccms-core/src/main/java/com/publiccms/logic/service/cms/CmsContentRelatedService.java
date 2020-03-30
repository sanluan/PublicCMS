package com.publiccms.logic.service.cms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.logic.dao.cms.CmsContentRelatedDao;

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
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long contentId, Long relatedContentId, Long userId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(contentId, relatedContentId, userId, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param contentId
     * @param userId
     * @param entitys
     */
    @SuppressWarnings("unchecked")
    public void update(long contentId, long userId, List<CmsContentRelated> entitys) {
        Set<Long> idList = new HashSet<>();
        if (CommonUtils.notEmpty(entitys)) {
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