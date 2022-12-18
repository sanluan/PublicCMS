package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsSurveyQuestionItem;
import com.publiccms.logic.dao.cms.CmsSurveyQuestionItemDao;
import com.publiccms.views.pojo.entities.QuestionItem;

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
    public List<CmsSurveyQuestionItem> updateVotes(Collection<Serializable> ids, int votes) {
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
    public void update(long questionId, List<QuestionItem> entitys, String[] ignoreProperties) {
        if (CommonUtils.notEmpty(entitys)) {
            for (QuestionItem entity : entitys) {
                if (null != entity.getId()) {
                    CmsSurveyQuestionItem oldEntity = getEntity(entity.getId());
                    if (questionId == oldEntity.getQuestionId()) {
                        update(entity.getId(), entity, ignoreProperties);
                    }
                } else {
                    CmsSurveyQuestionItem temp = new CmsSurveyQuestionItem(questionId, 0, entity.getTitle(), entity.getSort());
                    save(temp);
                    entity.setId(temp.getId());
                }
            }
        }
    }

    /**
     * @param questionId
     * @param entityList
     */
    public void save(long questionId, List<QuestionItem> entityList) {
        if (CommonUtils.notEmpty(entityList)) {
            for (QuestionItem entity : entityList) {
                CmsSurveyQuestionItem temp = new CmsSurveyQuestionItem(questionId, 0, entity.getTitle(), entity.getSort());
                save(temp);
                entity.setId(temp.getId());
            }
        }
    }

    @Resource
    private CmsSurveyQuestionItemDao dao;

}