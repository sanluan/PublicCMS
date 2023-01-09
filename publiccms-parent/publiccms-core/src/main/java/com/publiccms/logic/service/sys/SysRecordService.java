package com.publiccms.logic.service.sys;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysRecord;
import com.publiccms.logic.dao.sys.SysRecordDao;

import jakarta.annotation.Resource;

/**
 *
 * SysRecordService
 * 
 */
@Service
@Transactional
public class SysRecordService extends BaseService<SysRecord> {
    /**
     * @param siteId
     * @param code
     * @param startCreateDate
     * @param endCreateDate
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String code, Date startCreateDate, Date endCreateDate, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, code, startCreateDate, endCreateDate, orderField, orderType, pageIndex, pageSize);
    }

    @Resource
    private SysRecordDao dao;
}