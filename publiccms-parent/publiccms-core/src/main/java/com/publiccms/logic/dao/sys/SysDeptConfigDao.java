package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDeptConfig;

/**
 *
 * SysDeptConfigDao
 * 
 */
@Repository
public class SysDeptConfigDao extends BaseDao<SysDeptConfig> {

    /**
     * @param deptId
     * @param config 
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer deptId, String config, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDeptConfig bean");
        if (CommonUtils.notEmpty(deptId)) {
            queryHandler.condition("bean.id.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (CommonUtils.notEmpty(config)) {
            queryHandler.condition("bean.id.config = :config").setParameter("config", config);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysDeptConfig init(SysDeptConfig entity) {
        return entity;
    }

}