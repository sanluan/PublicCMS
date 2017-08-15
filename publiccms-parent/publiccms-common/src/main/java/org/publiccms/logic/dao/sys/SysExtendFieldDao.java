package org.publiccms.logic.dao.sys;

// Generated 2016-3-2 13:39:54 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.ArrayList;
import java.util.List;

import org.publiccms.entities.sys.SysExtendField;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysExtendFieldDao
 * 
 */
@Repository
public class SysExtendFieldDao extends BaseDao<SysExtendField> {

    /**
     * @param extendId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SysExtendField> getList(Integer extendId) {
        if (notEmpty(extendId)) {
            QueryHandler queryHandler = getQueryHandler("from SysExtendField bean");
            queryHandler.condition("bean.id.extendId = :extendId").setParameter("extendId", extendId);
            queryHandler.order("bean.sort asc");
            return (List<SysExtendField>) getList(queryHandler);
        }
        return new ArrayList<>();
    }

    @Override
    protected SysExtendField init(SysExtendField entity) {
        return entity;
    }

}