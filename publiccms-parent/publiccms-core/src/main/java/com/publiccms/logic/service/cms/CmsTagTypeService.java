package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsTagType;
import com.publiccms.logic.dao.cms.CmsTagTypeDao;

/**
 *
 * CmsTagTypeService
 * 
 */
@Service
@Transactional
public class CmsTagTypeService extends BaseService<CmsTagType> {

    /**
     * @param siteId
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, name, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(short siteId, Serializable[] ids) {
        for (CmsTagType entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    /**
     * @param siteId
     * @param entitys
     * @return
     */
    public Integer[] update(short siteId, List<CmsTagType> entitys) {
        Set<Integer> idList = new HashSet<>();
        if (CommonUtils.notEmpty(entitys)) {
            for (CmsTagType entity : entitys) {
                if (null != entity.getId()) {
                    entity = getEntity(entity.getId());
                    if (siteId == entity.getSiteId()) {
                        idList.add(entity.getId());
                    }
                } else {
                    entity.setSiteId(siteId);
                    save(entity);
                    idList.add(entity.getId());
                }
            }
        }
        return idList.toArray(new Integer[idList.size()]);
    }

    @Autowired
    private CmsTagTypeDao dao;
    
}