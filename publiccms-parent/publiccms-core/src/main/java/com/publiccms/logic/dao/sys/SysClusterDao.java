package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.entities.sys.SysCluster;

/**
 *
 * SysClusterDao
 * 
 */
@Repository
public class SysClusterDao extends BaseDao<SysCluster> {

    /**
     * @param startHeartbeatDate
     * @param endHeartbeatDate
     * @param master
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Date startHeartbeatDate, Date endHeartbeatDate, Boolean master, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysCluster bean");
        if (null != startHeartbeatDate) {
            queryHandler.condition("bean.heartbeatDate > :startHeartbeatDate").setParameter("startHeartbeatDate",
                    startHeartbeatDate);
        }
        if (null != endHeartbeatDate) {
            queryHandler.condition("bean.heartbeatDate <= :endHeartbeatDate").setParameter("endHeartbeatDate", endHeartbeatDate);
        }
        if (null != master) {
            queryHandler.condition("bean.master = :master").setParameter("master", master);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "createDate":
            queryHandler.order("bean.createDate ").append(orderType);
            break;
        case "heartbeatDate":
            queryHandler.order("bean.heartbeatDate ").append(orderType);
            break;
        default:
            queryHandler.order("bean.id ").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysCluster init(SysCluster entity) {
        return entity;
    }

}