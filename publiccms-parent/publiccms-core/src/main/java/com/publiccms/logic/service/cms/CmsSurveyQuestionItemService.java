package com.publiccms.logic.service.cms;

import java.io.Serializable;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsSurveyQuestionItem;
import com.publiccms.logic.dao.cms.CmsSurveyQuestionItemDao;

/**
 *
 * CmsSurveyQuestionItemService
 * 
 */
@Service
@Transactional
public class CmsSurveyQuestionItemService extends BaseService<CmsSurveyQuestionItem> {

    /**
     * @param questionId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long questionId, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(questionId, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param votes
     * @return entity
     */
    public CmsSurveyQuestionItem updateVotes(Serializable id, int votes) {
        CmsSurveyQuestionItem entity = getEntity(id);
        if (null != entity) {
            entity.setVotes(entity.getVotes() + votes);
        }
        return entity;
    }

    @Autowired
    private CmsSurveyQuestionItemDao dao;

}