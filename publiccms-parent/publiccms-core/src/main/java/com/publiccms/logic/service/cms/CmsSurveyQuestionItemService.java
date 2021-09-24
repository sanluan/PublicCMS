package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.List;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
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
     * @param ids
     * @param votes
     * @return entity
     */
    public List<CmsSurveyQuestionItem> updateVotes(Serializable[] ids, int votes) {
        List<CmsSurveyQuestionItem> entityList = getEntitys(ids);
        if (null != entityList) {
            for (CmsSurveyQuestionItem entity : entityList) {
                entity.setVotes(entity.getVotes() + votes);
            }
        }
        return entityList;
    }

    /**
     * @param questionId
     * @param entitys
     * @param ignoreProperties
     */
    public void update(long questionId, List<CmsSurveyQuestionItem> entitys, String[] ignoreProperties) {
        if (CommonUtils.notEmpty(entitys)) {
            for (CmsSurveyQuestionItem entity : entitys) {
                if (null != entity.getId()) {
                    CmsSurveyQuestionItem oldEntity = getEntity(entity.getId());
                    if (questionId == oldEntity.getQuestionId()) {
                        update(entity.getId(), entity, ignoreProperties);
                    }
                } else {
                    entity.setQuestionId(questionId);
                    save(entity);
                }
            }
        }
    }

    /**
     * @param questionId 
     * @param entityList
     */
    public void save(long questionId, List<CmsSurveyQuestionItem> entityList) {
        if (CommonUtils.notEmpty(entityList)) {
            for (CmsSurveyQuestionItem entity : entityList) {
                entity.setQuestionId(questionId);
                save(entity);
            }
        }
    }

    @Autowired
    private CmsSurveyQuestionItemDao dao;

}