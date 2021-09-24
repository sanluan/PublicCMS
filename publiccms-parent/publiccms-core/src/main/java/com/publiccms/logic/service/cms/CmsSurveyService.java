package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.Date;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsSurvey;
import com.publiccms.logic.dao.cms.CmsSurveyDao;

/**
 *
 * CmsSurveyService
 * 
 */
@Service
@Transactional
public class CmsSurveyService extends BaseService<CmsSurvey> {
    public static final String SURVEY_TYPE_EXAM = "exam";
    public static final String SURVEY_TYPE_SURVEY = "survey";

    /**
     * @param siteId
     * @param userId
     * @param surveyType
     * @param startStartDate
     * @param endStartDate
     * @param startEndDate
     * @param endEndDate
     * @param title
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, String surveyType, Date startStartDate, Date endStartDate,
            Date startEndDate, Date endEndDate, String title, Boolean disabled, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, surveyType, startStartDate, endStartDate, startEndDate, endEndDate, title, disabled,
                orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param id
     * @param votes
     * @return entity
     */
    public CmsSurvey updateVotes(short siteId, Serializable id, int votes) {
        CmsSurvey entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setVotes(entity.getVotes() + votes);
        }
        return entity;
    }

    @Autowired
    private CmsSurveyDao dao;

}