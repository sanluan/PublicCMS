package com.publiccms.logic.service.sys;

// Generated 2016-3-2 13:39:54 by com.sanluan.common.source.SourceMaker

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.logic.dao.sys.SysExtendFieldDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysExtendFieldService extends BaseService<SysExtendField> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer extendId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(extendId, pageIndex, pageSize);
    }

    @SuppressWarnings("unchecked")
    public void update(Integer extendId, List<SysExtendField> entitys) {
        if (notEmpty(extendId)) {
            Set<Integer> idList = new HashSet<Integer>();
            if (notEmpty(entitys)) {
                for (SysExtendField entity : entitys) {
                    if (notEmpty(entity.getId())) {
                        update(entity.getId(), entity, new String[] { "id", "extendId" });
                    } else {
                        entity.setExtendId(extendId);
                        save(entity);
                    }
                    idList.add(entity.getId());
                }
            }
            for (SysExtendField extend : (List<SysExtendField>) getPage(extendId, null, null).getList()) {
                if (!idList.contains(extend.getId())) {
                    delete(extend.getId());
                }
            }
        }
    }

    @Autowired
    private SysExtendFieldDao dao;
}