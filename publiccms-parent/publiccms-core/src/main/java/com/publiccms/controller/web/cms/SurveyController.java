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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsSurvey;
import com.publiccms.entities.cms.CmsSurveyQuestion;
import com.publiccms.entities.cms.CmsUserSurvey;
import com.publiccms.entities.cms.CmsUserSurveyId;
import com.publiccms.entities.cms.CmsUserSurveyQuestion;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsSurveyQuestionItemService;
import com.publiccms.logic.service.cms.CmsSurveyQuestionService;
import com.publiccms.logic.service.cms.CmsSurveyService;
import com.publiccms.logic.service.cms.CmsUserSurveyQuestionService;
import com.publiccms.logic.service.cms.CmsUserSurveyService;
import com.publiccms.views.pojo.model.CmsUserSurveyQuestionParameters;

/**
 * 
 * VoteController 投票
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
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping("save")
    @Csrf
    @ResponseBody
    public boolean save(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long surveyId,
            @ModelAttribute CmsUserSurveyQuestionParameters userQuestionParameters, HttpServletRequest request) {
        CmsSurvey entity = service.getEntity(surveyId);
        if (null != entity) {
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
                        userSurvey = new CmsUserSurvey(site.getId(), now);
                        userSurvey.setId(userSurveyId);
                        userSurveyService.save(userSurvey);
                        service.updateVotes(site.getId(), userSurveyId, 1);
                    }
                    Map<Long, CmsSurveyQuestion> questionMap = CommonUtils.listToMap(questionList, k -> k.getId(), null, null);
                    Set<Long> answerSet = new TreeSet<>();
                    List<CmsUserSurveyQuestion> answerList = new ArrayList<>();
                    for (CmsUserSurveyQuestion answer : userQuestionParameters.getAnswerList()) {
                        if (null != answer.getId()) {
                            CmsSurveyQuestion question = questionMap.get(answer.getId().getQuestionId());
                            if (null != question) {
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
                                        String[] correct = StringUtils.split(question.getAnswer(), CommonConstants.COMMA);
                                        boolean flag = true;
                                        answer.setScore(0);
                                        if (ArrayUtils.isSameLength(itemIds,
                                                StringUtils.split(question.getAnswer(), CommonConstants.COMMA))) {
                                            if (null != itemIds) {
                                                for (String itemId : itemIds) {
                                                    if (!ArrayUtils.contains(correct, itemId)) {
                                                        flag = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (flag) {
                                                answer.setScore(question.getScore());
                                            }
                                        }
                                    }
                                }
                                answer.getId().setUserId(user.getId());
                                answerList.add(answer);
                            }
                        }
                    }
                    itemService.updateVotes(answerSet.toArray(new Long[answerSet.size()]), 1);
                    userQuestionquestionService.save(answerList);
                }
            }
        }
        return false;
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

}