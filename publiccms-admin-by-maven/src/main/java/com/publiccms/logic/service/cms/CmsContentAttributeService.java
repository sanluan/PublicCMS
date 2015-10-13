package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import static org.apache.commons.lang3.StringUtils.length;

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

    @Autowired
    private CmsContentAttributeDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        return dao.getPage(pageIndex, pageSize);
    }

    public void dealAttribute(Integer contentId, String txt, String data) {
        CmsContentAttribute attribute = getEntity(contentId);
        if (notEmpty(attribute)) {
            if (notEmpty(txt) || notEmpty(data)) {
                attribute.setText(txt);
                attribute.setWordCount(length(txt));
                attribute.setData(data);
                update(attribute.getContentId(), attribute, new String[] { "contentId" });
            } else {
                delete(attribute.getContentId());
            }
        } else {
            attribute = new CmsContentAttribute();
            attribute.setContentId(contentId);
            attribute.setText(txt);
            attribute.setWordCount(length(txt));
            attribute.setData(data);
            save(attribute);
        }
    }
}