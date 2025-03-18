package com.publiccms.logic.dao.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;

/**
 *
 * SysWorkflowProcessHistoryDao
 * 
 */
@Repository
public class SysWorkflowProcessHistoryDao extends BaseDao<SysWorkflowProcessHistory> {

    /**
     * @param processId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long processId, Long userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysWorkflowProcessHistory bean");
        if (null != processId) {
            queryHandler.condition("bean.processId = :processId").setParameter("processId", processId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        queryHandler.order("bean.id asc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysWorkflowProcessHistory init(SysWorkflowProcessHistory entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}