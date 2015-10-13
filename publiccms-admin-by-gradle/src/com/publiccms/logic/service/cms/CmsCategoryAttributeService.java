package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.logic.dao.cms.CmsCategoryAttributeDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsCategoryAttributeService extends BaseService<CmsCategoryAttribute> {
    @Autowired
    private CmsCategoryAttributeDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        return dao.getPage(pageIndex, pageSize);
    }

    public void updateAttribute(Integer categoryId, String data) {
        CmsCategoryAttribute attribute = getEntity(categoryId);
        if (notEmpty(attribute)) {
            if (notEmpty(data)) {
                attribute.setData(data);
                update(attribute.getCategoryId(), attribute, new String[] { "categoryId" });
            } else {
                delete(attribute.getCategoryId());
            }
        } else {
            save(new CmsCategoryAttribute(categoryId, data));
        }
    }
}