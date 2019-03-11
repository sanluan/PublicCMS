package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogOperateService;

/**
 *
 * ContentClickDirective
 * 
 */
@Component
public class ContentCheckDirective extends AbstractAppDirective {

    @Autowired
    private CmsContentService service;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private TemplateComponent templateComponent;

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        SysSite site = getSite(handler);
        Serializable[] ids = handler.getLongArray("ids");
        boolean uncheck = handler.getBoolean("uncheck", false);
        List<CmsContent> entityList;
        if (uncheck) {
            entityList = service.uncheck(site.getId(), user, ids);
        } else {
            entityList = service.check(site.getId(), user, ids);
        }
        Set<Integer> categoryIdSet = new HashSet<>();
        for (CmsContent entity : entityList) {
            if (null != entity && site.getId() == entity.getSiteId()) {
                if (CommonUtils.notEmpty(entity.getParentId())) {
                    publish(site, entity, user);
                }
                handler.put(entity.getId().toString(), publish(site, entity, user));
                categoryIdSet.add(entity.getCategoryId());
            }
        }
        for (CmsCategory category : categoryService.getEntitys(categoryIdSet.toArray(new Integer[categoryIdSet.size()]))) {
            templateComponent.createCategoryFile(site, category, null, null);
        }
        logOperateService
                .save(new LogOperate(site.getId(), user.getId(), app.getChannel(), uncheck ? "uncheck.content" : "check.content",
                        RequestUtils.getIpAddress(handler.getRequest()), CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
    }

    private boolean publish(SysSite site, CmsContent entity, SysUser user) {
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (null != categoryModel && (user.isOwnsAllContent() || entity.getUserId() == user.getId()) && !entity.isOnlyUrl()) {
            return templateComponent.createContentFile(site, entity, null, categoryModel);
        }
        return false;
    }

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}