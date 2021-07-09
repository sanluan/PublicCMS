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
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<SysExtendField> getList(Integer extendId, String[] inputType) {
        if (CommonUtils.notEmpty(extendId)) {
            QueryHandler queryHandler = getQueryHandler("from SysExtendField bean");
            queryHandler.condition("bean.id.extendId = :extendId").setParameter("extendId", extendId);
            queryHandler.condition("bean.inputType in (:inputType)").setParameter("inputType", inputType);
            queryHandler.order("bean.sort asc");
            return (List<SysExtendField>) getList(queryHandler);
        }
        return Collections.emptyList();
    }

    @Override
    protected SysExtendField init(SysExtendField entity) {
        return entity;
    }

}