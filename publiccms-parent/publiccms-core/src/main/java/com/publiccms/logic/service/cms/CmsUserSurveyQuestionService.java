package com.publiccms.logic.service.cms;

import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserSurveyQuestion;
import com.publiccms.logic.dao.cms.CmsUserSurveyQuestionDao;

/**
 *
 * CmsUserSurveyQuestionService
 * 
 */
@Service
@Transactional
public class CmsUserSurveyQuestionService extends BaseService<CmsUserSurveyQuestion> {

    /**
     * @param siteId
     * @param userId
     * @param questionId
     * @param surveyId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, Long questionId, Long surveyId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, questionId, surveyId, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param entityList
     */
    public void updateScore(List<CmsUserSurveyQuestion> entityList) {
        if (CommonUtils.notEmpty(entityList)) {
            for (CmsUserSurveyQuestion entity : entityList) {
                CmsUserSurveyQuestion oldEntity = getEntity(entity.getId());
                if (null != oldEntity) {
                    oldEntity.setScore(entity.getScore());
                }
            }
        }
    }

    @Resource
    private CmsUserSurveyQuestionDao dao;

}