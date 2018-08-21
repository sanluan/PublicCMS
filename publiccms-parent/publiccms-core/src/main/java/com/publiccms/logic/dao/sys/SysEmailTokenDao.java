package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysEmailToken;

/**
 *
 * SysEmailTokenDao
 * 
 */
@Repository
public class SysEmailTokenDao extends BaseDao<SysEmailToken> {

    /**
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysEmailToken bean");
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param now
     * @return number of data deleted
     */
    public int delete(Date now) {
        if (null != now) {
            QueryHandler queryHandler = getQueryHandler("delete from SysEmailToken bean");
            queryHandler.condition("bean.expiryDate is not null");
            queryHandler.condition("bean.expiryDate <= :expiryDate").setParameter("expiryDate", now);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysEmailToken init(SysEmailToken entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}