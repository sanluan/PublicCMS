package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by com.sanluan.common.source.SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.dao.cms.CmsContentAttributeDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsContentAttributeService extends BaseService<CmsContentAttribute> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        return dao.getPage(pageIndex, pageSize);
    }

    public void updateAttribute(Long contentId, CmsContentAttribute entity) {
        CmsContentAttribute attribute = getEntity(contentId);
        if (notEmpty(attribute)) {
            if (notEmpty(entity)) {
                update(attribute.getContentId(), entity, new String[] { "contentId" });
            } else {
                delete(attribute.getContentId());
            }
        } else {
            if (notEmpty(entity)) {
                entity.setContentId(contentId);
                save(entity);
            }
        }
    }

    @Autowired
    private CmsContentAttributeDao dao;
}