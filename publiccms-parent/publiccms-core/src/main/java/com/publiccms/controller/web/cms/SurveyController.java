package com.publiccms.controller.web.cms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsSurvey;
import com.publiccms.entities.cms.CmsSurveyQuestion;
import com.publiccms.entities.cms.CmsUserSurvey;
import com.publiccms.entities.cms.CmsUserSurveyId;
import com.publiccms.entities.cms.CmsUserSurveyQuestion;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.service.cms.CmsSurveyQuestionItemService;
import com.publiccms.logic.service.cms.CmsSurveyQuestionService;
import com.publiccms.logic.service.cms.CmsSurveyService;
import com.publiccms.logic.service.cms.CmsUserSurveyQuestionService;
import com.publiccms.logic.service.cms.CmsUserSurveyService;
import com.publiccms.views.pojo.model.CmsUserSurveyQuestionParameters;

/**
 * 
 * SurveyController 问卷调查
 *
 */
@Controller
@RequestMapping("survey")
public class SurveyController {
    @Resource
    private CmsSurveyQuestionService questionService;
    @Resource
    private CmsUserSurveyService userSurveyService;
    @Resource
    private CmsUserSurveyQuestionService userQuestionquestionService;
    @Resource
    private CmsSurveyQuestionItemService itemService;
    @Resource
    protected ConfigComponent configComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;
    @Resource
    private LockComponent lockComponent;

    /**
     * @param site
     * @param session
     * @param _csrf
     * @param surveyId
     * @param captcha
     * @param userQuestionParameters
     * @param returnUrl
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(@RequestAttribute SysSite site, HttpSession session, String _csrf, long surveyId, String captcha,
            @ModelAttribute CmsUserSurveyQuestionParameters userQuestionParameters, String returnUrl, HttpServletRequest request,
            RedirectAttributes model) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (CommonUtils.notEmpty(captcha)
                || safeConfigComponent.enableCaptcha(site.getId(), SafeConfigComponent.CAPTCHA_MODULE_SURVEY)) {
            String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
            request.getSession().removeAttribute("captcha");
            if (ControllerUtils.errorCustom("captcha.error", null == sessionCaptcha || !sessionCaptcha.equalsIgnoreCase(captcha),
                    model)) {
                return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
            }
        }
        CmsSurvey entity = service.getEntity(surveyId);
        SysUser user = ControllerUtils.getUserFromSession(session);
        if (ControllerUtils.errorNotEmpty("survey", null == entity, model)
                || ControllerUtils.errorCustom("anonymousContribute", null == user && !entity.isAllowAnonymous(), model)) {
            return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
        }
        String ip = RequestUtils.getIpAddress(request);
        if (entity.isAllowAnonymous()) {
            boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_IP_SURVEY, ip, null);
            if (ControllerUtils.errorCustom("locked.ip", locked, model)) {
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_IP_SURVEY, ip, null, true);
                return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
            }
        } else if (ControllerUtils.errorNotEquals("_csrf", ControllerUtils.getWebToken(request), _csrf, model)) {
            return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
        }

        Date now = CommonUtils.getDate();
        if (!entity.isDisabled() && entity.getSiteId() == site.getId() && now.before(entity.getEndDate())
                && now.after(entity.getStartDate())) {
            @SuppressWarnings("unchecked")
            List<CmsSurveyQuestion> questionList = (List<CmsSurveyQuestion>) questionService
                    .getPage(surveyId, null, null, null, PageHandler.MAX_PAGE_SIZE).getList();
            if (null != questionList) {
                Long userId = null;
                if (null == user) {
                    userId = userSurveyService.getId();
                } else {
                    userId = user.getId();
                }
                CmsUserSurveyId userSurveyId = new CmsUserSurveyId(userId, surveyId);
                CmsUserSurvey userSurvey = userSurveyService.getEntity(userSurveyId);
                if (null == userSurvey) {
                    lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_IP_SURVEY, ip, null, true);
                    userSurvey = new CmsUserSurvey(site.getId(), null == user, ip, now);
                    userSurvey.setAnonymous(null == user);
                    userSurvey.setId(userSurveyId);
                    userSurveyService.save(userSurvey);
                    service.updateVotes(site.getId(), surveyId, 1);
                    Map<Long, CmsSurveyQuestion> questionMap = CommonUtils.listToMap(questionList, k -> k.getId(), null, null);
                    Set<Serializable> answerSet = new TreeSet<>();
                    List<CmsUserSurveyQuestion> answerList = new ArrayList<>();
                    for (CmsUserSurveyQuestion answer : userQuestionParameters.getAnswerList()) {
                        if (null != answer.getId()) {
                            CmsSurveyQuestion question = questionMap.get(answer.getId().getQuestionId());
                            if (null != question) {
                                answer.getId().setUserId(userId);
                                answer.setSiteId(site.getId());
                                answer.setSurveyId(surveyId);
                                answer.setScore(null);
                                answer.setCreateDate(null);
                                if (ArrayUtils.contains(CmsSurveyQuestionService.QUESTION_TYPES_DICT,
                                        question.getQuestionType())) {
                                    String[] itemIds = StringUtils.split(answer.getAnswer(), Constants.COMMA);
                                    if (CommonUtils.notEmpty(itemIds)) {
                                        for (String s : itemIds) {
                                            try {
                                                answerSet.add(Long.valueOf(s));
                                            } catch (NumberFormatException e) {
                                            }
                                        }
                                    }
                                    if (CmsSurveyService.SURVEY_TYPE_EXAM.equalsIgnoreCase(entity.getSurveyType())) {
                                        String[] corrects = StringUtils.split(question.getAnswer(), Constants.COMMA);
                                        answer.setScore(0);
                                        if (null != itemIds && ArrayUtils.isSameLength(itemIds, corrects)) {
                                            boolean flag = true;
                                            for (String itemId : itemIds) {
                                                if (!ArrayUtils.contains(corrects, itemId)) {
                                                    flag = false;
                                                    break;
                                                }
                                            }
                                            if (flag) {
                                                answer.setScore(question.getScore());
                                            }
                                        }
                                    }
                                }
                                answerList.add(answer);
                            }
                        }
                    }
                    itemService.updateVotes(answerSet, 1);
                    userQuestionquestionService.save(answerList);
                }
            }
        }
        model.addAttribute("id", entity.getId());
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    @Resource
    private CmsSurveyService service;
}