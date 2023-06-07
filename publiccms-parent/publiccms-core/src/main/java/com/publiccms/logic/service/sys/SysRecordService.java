package com.publiccms.logic.service.sys;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRecord;
import com.publiccms.entities.sys.SysRecordId;
import com.publiccms.logic.dao.sys.SysRecordDao;

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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SysRecord saveOrUpdate(SysRecordId id, String data) {
        SysRecord entity = getEntity(id);
        if (CommonUtils.notEmpty(data)) {
            if (null == entity) {
                entity = new SysRecord();
                entity.setId(id);
                entity.setData(data);
                save(entity);
            } else if (!data.equals(entity.getData())) {
                entity.setData(data);
                entity.setUpdateDate(CommonUtils.getDate());
            }
        }
        return entity;
    }

    @Resource
    private SysRecordDao dao;
}