package com.publiccms.logic.dao.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsSurveyQuestionItem;

/**
 *
 * CmsSurveyQuestionItemDao
 * 
 */
@Repository
public class CmsSurveyQuestionItemDao extends BaseDao<CmsSurveyQuestionItem> {

    /**
     * @param questionId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long questionId, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsSurveyQuestionItem bean");
        if (CommonUtils.notEmpty(questionId)) {
            queryHandler.condition("bean.questionId = :questionId").setParameter("questionId", questionId);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "votes":
            queryHandler.order("bean.votes").append(orderType);
            break;
        default:
            queryHandler.order("bean.sort asc");
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsSurveyQuestionItem init(CmsSurveyQuestionItem entity) {
        if (CommonUtils.notEmpty(entity.getTitle())) {
            entity.setTitle(CommonUtils.keep(entity.getTitle(), 255));
        }
        return entity;
    }

}