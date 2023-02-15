package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
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

import freemarker.template.TemplateException;

/**
 *
 * contentCheck 内容审核接口
 * <p>
 * 参数列表
 * <ul>
 * <li><code>ids</code>:多个内容id
 * <li><code>uncheck</code>:取消审核, 【true,false】,默认为<code>false</code>
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * </ul>
 * 使用示例
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath!}api/contentCheck?ids=1,2&amp;authToken=用户登录授权&amp;authUserId=1&amp;appToken=接口访问授权Token', function(data){
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class ContentCheckDirective extends AbstractAppDirective {

    @Resource
    private CmsContentService service;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
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
        Set<Serializable> categoryIdSet = new HashSet<>();
        for (CmsContent entity : entityList) {
            if (null != entity && site.getId() == entity.getSiteId()) {
                if (CommonUtils.notEmpty(entity.getParentId())) {
                    publish(site, entity, user);
                }
                handler.put(entity.getId().toString(), publish(site, entity, user));
                categoryIdSet.add(entity.getCategoryId());
            }
        }
        for (CmsCategory category : categoryService.getEntitys(categoryIdSet)) {
            templateComponent.createCategoryFile(site, category, null, null);
        }
        logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), app.getChannel(),
                uncheck ? "uncheck.content" : "check.content", RequestUtils.getIpAddress(handler.getRequest()),
                CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        handler.render();
    }

    private boolean publish(SysSite site, CmsContent entity, SysUser user) {
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (null != categoryModel && ControllerUtils.hasContentPermissions(user, entity) && !entity.isOnlyUrl()) {
            try {
                return templateComponent.createContentFile(site, entity, null, categoryModel);
            } catch (IOException | TemplateException e) {
                return false;
            }
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