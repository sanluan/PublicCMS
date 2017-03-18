package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysEmailToken;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysEmailTokenDao extends BaseDao<SysEmailToken> {
    public PageHandler getPage(Long userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysEmailToken bean");
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int delete(Date createDate) {
        if (notEmpty(createDate)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysEmailToken bean");
            queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", createDate);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysEmailToken init(SysEmailToken entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}