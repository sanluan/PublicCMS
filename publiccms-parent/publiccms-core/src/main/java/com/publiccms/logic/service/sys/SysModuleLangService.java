package com.publiccms.logic.service.sys;

import java.util.List;

// Generated 2018-6-5 18:21:28 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModuleLang;
import com.publiccms.entities.sys.SysModuleLangId;
import com.publiccms.logic.dao.sys.SysModuleLangDao;

/**
 *
 * SysModuleLangService
 * 
 */
@Service
@Transactional
public class SysModuleLangService extends BaseService<SysModuleLang> {

    /**
     * 
     * @param moduleId
     * @param lang
     * @return results page
     */
    @Transactional
    public List<?> getList(String moduleId, String lang) {
        return dao.getList(moduleId, lang);
    }

    /**
     * @param oldId
     * @param moduleId
     * @param entityList
     */
    public void save(String oldId, String moduleId, List<SysModuleLang> entityList) {
        if (CommonUtils.notEmpty(entityList)) {
            for (SysModuleLang entity : entityList) {
                if (CommonUtils.notEmpty(oldId) && !oldId.equals(moduleId)) {
                    delete(new SysModuleLangId(oldId, entity.getId().getLang()));
                }
                SysModuleLang oldEntity = getEntity(new SysModuleLangId(moduleId, entity.getId().getLang()));
                if (null == oldEntity) {
                    entity.getId().setModuleId(moduleId);
                    save(entity);
                } else {
                    oldEntity.setValue(entity.getValue());
                }
            }
        }
    }

    /**
     * @param moduleId
     */
    public void delete(String moduleId) {
        @SuppressWarnings("unchecked")
        List<SysModuleLang> entityList = (List<SysModuleLang>) getList(moduleId, null);
        if (CommonUtils.notEmpty(entityList)) {
            for (SysModuleLang entity : entityList) {
                delete(entity.getId());
            }
        }
    }

    @Resource
    private SysModuleLangDao dao;

}