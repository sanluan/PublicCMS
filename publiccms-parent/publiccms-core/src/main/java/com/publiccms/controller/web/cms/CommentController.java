package com.publiccms.controller.web.cms;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.cms.CmsCommentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;

/**
 * 
 * ContentController 内容
 *
 */
@Controller
@RequestMapping("comment")
public class CommentController {
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected ConfigComponent configComponent;

    private String[] ignoreProperties = new String[] { "siteId", "userId", "createDate", "checkUserId", "checkDate", "replyId",
            "replyUserId", "replies", "disabled" };

    /**
     * @param site
     * @param user
     * @param entity
     * @param returnUrl
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser user, CmsComment entity, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        if (CommonUtils.notEmpty(entity.getText())) {
            entity.setStatus(CmsCommentService.STATUS_PEND);
            if (null != entity.getId()) {
                CmsComment oldEntity = service.getEntity(entity.getId());
                if (null != oldEntity && !oldEntity.isDisabled() && oldEntity.getUserId() == user.getId()) {
                    entity.setUpdateDate(CommonUtils.getDate());
                    entity = service.update(entity.getId(), entity, ignoreProperties);
                    logOperateService
                            .save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "update.cmsComment",
                                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
                }
            } else {
                Date now = CommonUtils.getDate();
                entity.setSiteId(site.getId());
                entity.setUserId(user.getId());
                if (null != entity.getReplyId()) {
                    CmsComment reply = service.getEntity(entity.getReplyId());
                    if (null == reply) {
                        entity.setReplyId(null);
                    } else {
                        entity.setContentId(reply.getContentId());
                        if (null == entity.getReplyUserId()) {
                            entity.setReplyUserId(reply.getUserId());
                        }
                    }
                }
                if (null != entity.getReplyUserId() && entity.getReplyUserId().equals(user.getId())) {
                    entity.setReplyUserId(null);
                }
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "save.cmsComment",
                        RequestUtils.getIpAddress(request), now, JsonUtils.getString(entity)));
            }
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    @Autowired
    private CmsCommentService service;

}
