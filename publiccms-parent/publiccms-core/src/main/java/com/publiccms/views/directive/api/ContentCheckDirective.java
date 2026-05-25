package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.TemplateComponent;
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
 * <p>使用示例
 *
 * <pre>
&lt;script&gt;
fetch('${site.dynamicPath!}api/contentCheck?ids=1,2',{"headers":{"appToken":"接口访问授权Token","authToken":"用户登录授权","authUserId":"1"}}).then(res => res.json()).then(data=>{
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
    private TemplateComponent templateComponent;

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, TemplateException {
        SysSite site = getSite(handler);
        Serializable[] ids = handler.getLongArray("ids");
        boolean uncheck = handler.getBoolean("uncheck", false);
        List<CmsContent> entityList;
        if (uncheck) {
            entityList = service.uncheck(site.getId(), user, ids);
        } else {
            entityList = service.check(site.getId(), user, ids);
        }
        for (CmsContent entity : entityList) {
            handler.put(entity.getId().toString(), false);
            if (null != entity && site.getId() == entity.getSiteId()) {
                CmsCategory category = categoryService.getEntity(entity.getCategoryId());
                try {
                    if (null != category && ControllerUtils.hasContentPermissions(user, entity)
                            && templateComponent.publish(site, entity, category, null)) {
                        if (null != entity.getParentId()) {
                            CmsContent parent = service.getEntity(entity.getParentId());
                            if (null != parent) {
                                try {
                                    templateComponent.publish(site, parent, category, null);
                                } catch (IOException | TemplateException e) {
                                }
                            }
                        } else {
                            templateComponent.publish(site, category, null, null);
                        }
                        handler.put(entity.getId().toString(), true);
                    }
                } catch (IOException | TemplateException e) {
                }
            }
        }
        logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), app.getChannel(),
                uncheck ? "uncheck.content" : "check.content", RequestUtils.getIpAddress(handler.getRequest()), CommonUtils.now(),
                StringUtils.join(ids, Constants.COMMA)));
        handler.render();
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