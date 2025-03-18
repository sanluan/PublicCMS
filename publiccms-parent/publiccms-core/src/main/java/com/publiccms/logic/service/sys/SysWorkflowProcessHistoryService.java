package com.publiccms.logic.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;
import com.publiccms.logic.dao.sys.SysWorkflowProcessHistoryDao;

import jakarta.annotation.Resource;

/**
 *
 * SysWorkflowProcessService
 * 
 */
@Service
@Transactional
public class SysWorkflowProcessHistoryService extends BaseService<SysWorkflowProcessHistory> {
    /**
     * 
     */
    public static final String OPERATE_AGREE = "agree";
    /**
     * 
     */
    public static final String OPERATE_REJECT = "reject";
    /**
     * @param processId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long processId, Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(processId, userId, pageIndex, pageSize);
    }

    @Resource
    private SysWorkflowProcessHistoryDao dao;

}