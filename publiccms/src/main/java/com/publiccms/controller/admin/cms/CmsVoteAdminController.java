package com.publiccms.controller.admin.cms;

import static com.sanluan.common.tools.JsonUtils.getString;

// Generated 2016-3-3 17:43:26 by com.sanluan.common.source.SourceGenerator

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsVoteService;
import com.publiccms.logic.service.log.LogLoginService;

@Controller
@RequestMapping("cmsVote")
public class CmsVoteAdminController extends AbstractController {
    @Autowired
    private CmsVoteService service;

    private String[] ignoreProperties = new String[] { "id", "siteId", "itemExtendId", "userCounts" };

    @RequestMapping("save")
    public String save(CmsVote entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            CmsVote oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.vote", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.vote", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        CmsVote entity = service.getEntity(id);
        if (null != entity) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.vote", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}