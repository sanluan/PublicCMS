package com.publiccms.logic.dao.system;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HqlDao extends BaseDao<Object> {
    public PageHandler getPage(String hql, Map<String, Object> paramters, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler(hql);
        if (notEmpty(paramters)) {
            for (Entry<String, Object> entry : paramters.entrySet()) {
                queryHandler.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return getMapPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected Class<Object> getEntityClass() {
        return Object.class;
    }

    @Override
    protected Object init(Object entity) {
        return entity;
    }

}
