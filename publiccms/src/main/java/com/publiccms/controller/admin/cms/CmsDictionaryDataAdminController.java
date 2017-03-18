package com.publiccms.controller.admin.cms;

// Generated 2016-11-20 14:50:55 by com.sanluan.common.source.SourceGenerator

import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.JsonUtils.getString;
import static org.apache.commons.lang3.StringUtils.join;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;

import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.entities.log.LogOperate;

@Controller
@RequestMapping("cmsDictionaryData")
public class CmsDictionaryDataAdminController extends AbstractController {

	private String[] ignoreProperties = new String[]{"id"};

    @RequestMapping("save")
    public String save(CmsDictionaryData entity, HttpServletRequest request, HttpSession session) {
    	SysSite site = getSite(request);
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.cmsDictionaryData", getIpAddress(request), getDate(), getString(entity)));
        } else {
            service.save(entity);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.cmsDictionaryData", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer[] ids, HttpServletRequest request, HttpSession session) {
    	SysSite site = getSite(request);
    	if (notEmpty(ids)) {
	        service.delete(ids);
	        logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.cmsDictionaryData", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }
    
    @Autowired
    private CmsDictionaryDataService service;
}