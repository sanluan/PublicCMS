package com.publiccms.views.controller.admin.cms;

// Generated 2016-3-22 11:21:35 by com.sanluan.common.source.SourceMaker

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.cms.CmsWord;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsWordService;
import com.publiccms.logic.service.log.LogLoginService;

@Controller
@RequestMapping("cmsWord")
public class CmsWordAdminController extends AbstractController {

	@RequestMapping("save")
	public String save(CmsWord entity, HttpServletRequest request, HttpSession session, ModelMap model) {
		SysSite site = getSite(request);
		if (notEmpty(entity.getId())) {
			CmsWord oldEntity = service.getEntity(entity.getId());
			if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
				return TEMPLATE_ERROR;
			}
			entity = service.update(entity.getId(), entity, new String[] { "id", "siteId" });
			if (notEmpty(entity)) {
				logOperateService.save(new LogOperate(entity.getSiteId(), getAdminFromSession(session).getId(),
						LogLoginService.CHANNEL_WEB_MANAGER, "update.word", getIpAddress(request), getDate(),
						entity.getId() + ":" + entity.getName()));
			}
		} else {
			entity.setSiteId(site.getId());
			service.save(entity);
			logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
					LogLoginService.CHANNEL_WEB_MANAGER, "save.word", getIpAddress(request), getDate(),
					entity.getId() + ":" + entity.getName()));
		}
		return TEMPLATE_DONE;
	}

	@RequestMapping("delete")
	public String delete(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
		SysSite site = getSite(request);
		CmsWord entity = service.getEntity(id);
		if (notEmpty(entity)) {
			if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
				return TEMPLATE_ERROR;
			}
			service.delete(id);
			logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
					LogLoginService.CHANNEL_WEB_MANAGER, "delete.word", getIpAddress(request), getDate(),
					id + ":" + entity.getName()));
		}
		return TEMPLATE_DONE;
	}

	@RequestMapping("hidden")
	public String hidden(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
		SysSite site = getSite(request);
		CmsWord entity = service.getEntity(id);
		if (notEmpty(entity)) {
			if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
				return TEMPLATE_ERROR;
			}
			service.updateStatus(id, true);
			logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
					LogLoginService.CHANNEL_WEB_MANAGER, "hidden.word", getIpAddress(request), getDate(),
					id + ":" + entity.getName()));
		}
		return TEMPLATE_DONE;
	}

	@RequestMapping("show")
	public String show(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
		SysSite site = getSite(request);
		CmsWord entity = service.getEntity(id);
		if (notEmpty(entity)) {
			if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
				return TEMPLATE_ERROR;
			}
			service.updateStatus(id, false);
			logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
					LogLoginService.CHANNEL_WEB_MANAGER, "show.word", getIpAddress(request), getDate(),
					id + ":" + entity.getName()));
		}
		return TEMPLATE_DONE;
	}

	@Autowired
	private CmsWordService service;
}