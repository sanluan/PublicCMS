package org.publiccms.logic.dao.sys;

// Generated 2016-7-16 11:56:50 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.sys.SysCluster;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
    public PageHandler getPage(Date startHeartbeatDate, Date endHeartbeatDate, Boolean master, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysCluster bean");
        if (notEmpty(startHeartbeatDate)) {
            queryHandler.condition("bean.heartbeatDate > :startHeartbeatDate").setParameter("startHeartbeatDate",
                    startHeartbeatDate);
        }
        if (notEmpty(endHeartbeatDate)) {
            queryHandler.condition("bean.heartbeatDate <= :endHeartbeatDate").setParameter("endHeartbeatDate", endHeartbeatDate);
        }
        if (notEmpty(master)) {
            queryHandler.condition("bean.master = :master").setParameter("master", master);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "createDate":
            queryHandler.order("bean.createDate " + orderType);
            break;
        case "heartbeatDate":
            queryHandler.order("bean.heartbeatDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysCluster init(SysCluster entity) {
        return entity;
    }

}