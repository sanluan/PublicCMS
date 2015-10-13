package com.publiccms.logic.dao.cms;

// Generated 2015-7-20 11:47:55 by SourceMaker

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsForm;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsFormDao extends BaseDao<CmsForm> {
    public PageHandler getPage(String title, Date startCreateDate, Date endCreateDate, 
                Boolean disabled, 
                String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsForm bean");
        if (notEmpty(title)) {
            queryHandler.condition("bean.title like :title").setParameter("title", like(title));
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if("asc".equalsIgnoreCase(orderType)){
            orderType = "asc";
        }else{
            orderType = "desc";
        }
        if(!notEmpty(orderField)){
            orderField="";
        }
        switch(orderField) {
            case "createDate" : queryHandler.append("order by bean.createDate " + orderType); break;
            default : queryHandler.append("order by bean.id "+orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsForm init(CmsForm entity) {
        return entity;
    }

    @Override
    protected Class<CmsForm> getEntityClass() {
        return CmsForm.class;
    }

}