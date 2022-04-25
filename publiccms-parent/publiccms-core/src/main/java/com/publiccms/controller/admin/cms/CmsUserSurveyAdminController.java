package com.publiccms.controller.admin.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import jakarta.servlet.http.HttpServletRequest;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsUserSurvey;
import com.publiccms.entities.cms.CmsUserSurveyQuestion;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsUserSurveyQuestionService;
import com.publiccms.logic.service.cms.CmsUserSurveyService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.model.CmsUserSurveyQuestionParameters;

/**
 *
 * CmsUserSurveyAdminController
 * 
 */
@Controller
@RequestMapping("cmsUserSurvey")
public class CmsUserSurveyAdminController {

    /**
     * @param site
     * @param admin
     * @param entity
     * @param userQuestionParameters
     * @param request
     * @return operate result
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsUserSurvey entity,
            @ModelAttribute CmsUserSurveyQuestionParameters userQuestionParameters, HttpServletRequest request) {
        int socre = 0;
        if (CommonUtils.notEmpty(userQuestionParameters.getAnswerList())) {
            for (CmsUserSurveyQuestion answer : userQuestionParameters.getAnswerList()) {
                if (null != answer.getScore()) {
                    socre += answer.getScore();
                }
            }
        }
        CmsUserSurvey userSurvey = service.updateScore(site.getId(), entity.getId(), socre);
        questionService.updateScore(userQuestionParameters.getAnswerList());
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "update.cmsUserSurveyQuestion", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                JsonUtils.getString(userSurvey)));
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private CmsUserSurveyService service;
    @Resource
    private CmsUserSurveyQuestionService questionService;
    @Resource
    protected LogOperateService logOperateService;
}