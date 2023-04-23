package com.publiccms.logic.component.parameter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractStringParameterHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryService;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;

/**
 * CategoryCodeParameterComponent 分类编码参数处理组件
 */
@Component
@Priority(5)
public class CategoryCodeParameterComponent extends AbstractStringParameterHandler<CmsCategory> {
    /**
     * 
     */
    public static final String INPUTTYPE_CATEGORYCODE = "category.code";
    @Resource
    private CmsCategoryService service;
    @Resource
    private CmsCategoryAttributeService attributeService;

    @Override
    public String getType() {
        return INPUTTYPE_CATEGORYCODE;
    }

    @Override
    public List<CmsCategory> getParameterValueList(SysSite site, String[] ids) {
        List<CmsCategory> entityList = service.getEntitysByCodes(site.getId(), ids);
        entityList = entityList.stream().filter(entity -> !entity.isDisabled()).toList();
        entityList.forEach(e -> CmsUrlUtils.initCategoryUrl(site, e));
        return entityList;
    }

    @Override
    public CmsCategory getParameterValue(SysSite site, String id) {
        CmsCategory entity = service.getEntityByCode(site.getId(), id);
        if (null != entity && !entity.isDisabled() && entity.getSiteId() == site.getId()) {
            CmsUrlUtils.initCategoryUrl(site, entity);
            entity.setAttribute(ExtendUtils.getAttributeMap(attributeService.getEntity(entity.getId())));
            return entity;
        }
        return null;
    }
}
