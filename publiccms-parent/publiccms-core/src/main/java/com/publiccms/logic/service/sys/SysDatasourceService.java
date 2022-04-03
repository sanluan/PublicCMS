package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

// Generated 2021-8-2 11:31:34 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDatasource;
import com.publiccms.logic.dao.sys.SysDatasourceDao;

/**
 *
 * SysDatasourceService
 * 
 */
@Service
@Transactional
public class SysDatasourceService extends BaseService<SysDatasource> {

    /**
     * @param disabled
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional
    public PageHandler getPage(Boolean disabled, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(disabled, orderType, pageIndex, pageSize);
    }
    
    /**
     * @param startUpdateDate
     * @return results list
     */
    public List<SysDatasource> getList(Date startUpdateDate) {
        return dao.getList(startUpdateDate);
    }

    /**
     * @param id
     * @return
     */
    public SysDatasource disabled(Serializable id) {
        SysDatasource entity = getEntity(id);
        if (null != entity && !entity.isDisabled()) {
            entity.setDisabled(true);
            entity.setUpdateDate(CommonUtils.getDate());
        }
        return entity;
    }

    @Resource
    private SysDatasourceDao dao;

}