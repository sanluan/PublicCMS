package com.publiccms.controller.admin.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

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
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;

import com.publiccms.entities.cms.CmsSurveyQuestionItem;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.cms.CmsSurveyQuestionItemService;
/**
 *
 * CmsSurveyQuestionItemAdminController
 * 
 */
@Controller
@RequestMapping("cmsSurveyQuestionItem")
public class CmsSurveyQuestionItemAdminController {

    private String[] ignoreProperties = new String[]{ "id" };
    
    /**
     * @param site
     * @param admin
     * @param entity
     * @param request
     * @param model
     * @return operate result
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsSurveyQuestionItem entity, HttpServletRequest request,
             ModelMap model) {
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "update.cmsSurveyQuestionItem", 
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.cmsSurveyQuestionItem", 
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param site
     * @param admin 
     * @param _csrf 
     * @param model
     * @return operate result
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, String _csrf, HttpServletRequest request, 
            ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            service.delete(ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "delete.cmsSurveyQuestionItem",
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private CmsSurveyQuestionItemService service;
    @Autowired
    protected LogOperateService logOperateService;
}