package com.publiccms.controller.web.cms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsSurvey;
import com.publiccms.entities.cms.CmsSurveyQuestion;
import com.publiccms.entities.cms.CmsUserSurvey;
import com.publiccms.entities.cms.CmsUserSurveyId;
import com.publiccms.entities.cms.CmsUserSurveyQuestion;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.SiteConfigComponent;
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

    /**
     * @param site
     * @param user
     * @param surveyId
     * @param userQuestionParameters
     * @param returnUrl
     * @param request
     * @return
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long surveyId,
            @ModelAttribute CmsUserSurveyQuestionParameters userQuestionParameters, String returnUrl,
            HttpServletRequest request) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        CmsSurvey entity = service.getEntity(surveyId);
        if (null != entity) {
            String ip = RequestUtils.getIpAddress(request);
            Date now = CommonUtils.getDate();
            if (!entity.isDisabled() && entity.getSiteId() == site.getId() && now.before(entity.getEndDate())
                    && now.after(entity.getStartDate())) {
                @SuppressWarnings("unchecked")
                List<CmsSurveyQuestion> questionList = (List<CmsSurveyQuestion>) questionService
                        .getPage(surveyId, null, null, null, PageHandler.MAX_PAGE_SIZE).getList();
                if (null != questionList) {
                    CmsUserSurveyId userSurveyId = new CmsUserSurveyId(user.getId(), surveyId);
                    CmsUserSurvey userSurvey = userSurveyService.getEntity(userSurveyId);
                    if (null == userSurvey) {
                        userSurvey = new CmsUserSurvey(site.getId(), ip, now);
                        userSurvey.setId(userSurveyId);
                        userSurveyService.save(userSurvey);
                        service.updateVotes(site.getId(), surveyId, 1);
                        Map<Long, CmsSurveyQuestion> questionMap = CommonUtils.listToMap(questionList, k -> k.getId(), null,
                                null);
                        Set<Long> answerSet = new TreeSet<>();
                        List<CmsUserSurveyQuestion> answerList = new ArrayList<>();
                        for (CmsUserSurveyQuestion answer : userQuestionParameters.getAnswerList()) {
                            if (null != answer.getId()) {
                                CmsSurveyQuestion question = questionMap.get(answer.getId().getQuestionId());
                                if (null != question) {
                                    answer.getId().setUserId(user.getId());
                                    answer.setSiteId(site.getId());
                                    answer.setSurveyId(surveyId);
                                    answer.setScore(null);
                                    answer.setCreateDate(null);
                                    if (ArrayUtils.contains(CmsSurveyQuestionService.QUESTION_TYPES_DICT,
                                            question.getQuestionType())) {
                                        String[] itemIds = StringUtils.split(answer.getAnswer(), CommonConstants.COMMA);
                                        if (CommonUtils.notEmpty(itemIds)) {
                                            for (String s : itemIds) {
                                                try {
                                                    answerSet.add(Long.valueOf(s));
                                                } catch (NumberFormatException e) {
                                                }
                                            }
                                        }
                                        if (CmsSurveyService.SURVEY_TYPE_EXAM.equalsIgnoreCase(entity.getSurveyType())) {
                                            String[] corrects = StringUtils.split(question.getAnswer(), CommonConstants.COMMA);
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
                        itemService.updateVotes(answerSet.toArray(new Long[answerSet.size()]), 1);
                        userQuestionquestionService.save(answerList);
                    }
                }
            }
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    @Autowired
    private CmsSurveyService service;
    @Autowired
    private CmsSurveyQuestionService questionService;
    @Autowired
    private CmsUserSurveyService userSurveyService;
    @Autowired
    private CmsUserSurveyQuestionService userQuestionquestionService;
    @Autowired
    private CmsSurveyQuestionItemService itemService;
    @Autowired
    protected SiteConfigComponent siteConfigComponent;
}