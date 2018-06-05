package com.publiccms.logic.dao.sys;

import java.util.List;

// Generated 2018-6-5 18:21:28 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModuleLang;

/**
 *
 * SysModuleLangDao
 * 
 */
@Repository
public class SysModuleLangDao extends BaseDao<SysModuleLang> {

    /**
     * 
     * @param moduleId
     * @param lang
     * @return results page
     */
    public List<?> getList(String moduleId, String lang) {
        QueryHandler queryHandler = getQueryHandler("from SysModuleLang bean");
        if (CommonUtils.notEmpty(moduleId)) {
            queryHandler.condition("bean.id.moduleId = :moduleId").setParameter("moduleId", moduleId);
        }
        if (null != lang) {
            queryHandler.condition("bean.id.lang = :lang").setParameter("lang", lang);
        }
        return getList(queryHandler);
    }

    @Override
    protected SysModuleLang init(SysModuleLang entity) {
        return entity;
    }

}