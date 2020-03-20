package com.publiccms.controller.admin.cms;

import java.util.Date;
import java.util.Set;

// Generated 2018-11-7 16:25:07 by com.publiccms.common.generator.SourceGenerator

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCommentService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;

/**
 *
 * CmsCommentAdminController
 * 
 */
@Controller
@RequestMapping("cmsComment")
public class CmsCommentAdminController {
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsContentService contentService;

    private String[] ignoreProperties = new String[] { "siteId", "userId", "createDate", "checkUserId", "checkDate", "status",
            "replyId", "replyUserId", "replies", "disabled" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param returnUrl
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsComment entity,
            HttpServletRequest request, ModelMap model) {
        if (null != entity.getId()) {
            CmsComment oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity.setUpdateDate(CommonUtils.getDate());
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "update.cmsComment", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            Date now = CommonUtils.getDate();
            entity.setSiteId(site.getId());
            entity.setUserId(admin.getId());
            entity.setStatus(CmsCommentService.STATUS_NORMAL);
            entity.setCheckUserId(admin.getId());
            entity.setCheckDate(now);
            if (null != entity.getReplyId()) {
                CmsComment reply = service.updateReplies(site.getId(), entity.getReplyId(), 1);
                if (null == reply) {
                    entity.setReplyId(null);
                } else {
                    entity.setContentId(reply.getContentId());
                    if (null == entity.getReplyUserId()) {
                        entity.setReplyUserId(reply.getUserId());
                    }
                }
            }
            if (null != entity.getReplyUserId() && entity.getReplyUserId().equals(admin.getId())) {
                entity.setReplyUserId(null);
            }
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "save.cmsComment", RequestUtils.getIpAddress(request), now, JsonUtils.getString(entity)));
        }
        if (CmsCommentService.STATUS_NORMAL == entity.getStatus()) {
            CmsContent content = contentService.getEntity(entity.getContentId());
            if (null != content && !content.isDisabled()) {
                templateComponent.createContentFile(site, content, null, null);
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    @Csrf
    public String check(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            Set<CmsContent> contentSet = service.check(site.getId(), ids, admin.getId());
            for (CmsContent content : contentSet) {
                templateComponent.createContentFile(site, content, null, null);// 静态化
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "check.cmsComment", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("uncheck")
    @Csrf
    public String uncheck(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            Set<CmsContent> contentSet = service.uncheck(site.getId(), ids);
            for (CmsContent content : contentSet) {
                templateComponent.createContentFile(site, content, null, null);// 静态化
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "uncheck.cmsComment", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return operate result
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            Set<CmsContent> contentSet = service.delete(site.getId(), ids);
            for (CmsContent content : contentSet) {
                templateComponent.createContentFile(site, content, null, null);// 静态化
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.cmsComment", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private CmsCommentService service;
}