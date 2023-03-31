package com.publiccms.logic.dao.sys;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;

/**
 *
 * SysExtendFieldDao
 * 
 */
@Repository
public class SysExtendFieldDao extends BaseDao<SysExtendField> {

    /**
     * @param extendId
     * @param inputType
     * @param searchable
     * @return results page
     */
    public List<SysExtendField> getList(Integer extendId, String[] inputType, Boolean searchable) {
        if (CommonUtils.notEmpty(extendId)) {
            QueryHandler queryHandler = getQueryHandler("from SysExtendField bean");
            queryHandler.condition("bean.id.extendId = :extendId").setParameter("extendId", extendId);
            if (CommonUtils.notEmpty(inputType)) {
                queryHandler.condition("bean.inputType in (:inputType)").setParameter("inputType", inputType);
            }
            if (null != searchable) {
                queryHandler.condition("bean.searchable = :searchable").setParameter("searchable", searchable);
            }
            queryHandler.order("bean.sort asc");
            return getEntityList(queryHandler);
        }
        return Collections.emptyList();
    }

    @Override
    protected SysExtendField init(SysExtendField entity) {
        if (CommonUtils.empty(entity.getName())) {
            entity.setName(entity.getId().getCode());
        }
        return entity;
    }

}