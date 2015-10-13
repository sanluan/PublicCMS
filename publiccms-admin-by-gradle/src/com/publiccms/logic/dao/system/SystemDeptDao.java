package com.publiccms.logic.dao.system;

// Generated 2015-7-20 11:46:39 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.system.SystemDept;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SystemDeptDao extends BaseDao<SystemDept> {
    public PageHandler getPage(Integer parentId, Integer userId, String name, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SystemDept bean");
        if (notEmpty(parentId)) {
            queryHandler.condition("(bean.parentId = :parentId)").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(userId)) {
            queryHandler.condition("(bean.userId = :userId)").setParameter("userId", userId);
        }
        if (notEmpty(name)) {
            queryHandler.condition("(bean.name like :name)").setParameter("name", like(name));
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SystemDept init(SystemDept entity) {
        return entity;
    }

    @Override
    protected Class<SystemDept> getEntityClass() {
        return SystemDept.class;
    }

}