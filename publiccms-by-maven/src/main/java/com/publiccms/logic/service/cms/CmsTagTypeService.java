package com.publiccms.logic.service.cms;

import java.io.Serializable;

// Generated 2015-7-10 16:36:23 by com.sanluan.common.source.SourceMaker

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsTagType;
import com.publiccms.logic.dao.cms.CmsTagTypeDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsTagTypeService extends BaseService<CmsTagType> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, String name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, name, pageIndex, pageSize);
    }

    public void delete(int siteId, Serializable[] ids) {
        for (CmsTagType entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    public Integer[] update(int siteId, List<CmsTagType> entitys) {
        Set<Integer> idList = new HashSet<Integer>();
        if (notEmpty(entitys)) {
            for (CmsTagType entity : entitys) {
                if (notEmpty(entity.getId())) {
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