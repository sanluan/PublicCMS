package com.publiccms.logic.service.sys;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.logic.dao.sys.SysExtendFieldDao;

/**
 *
 * SysExtendFieldService
 * 
 */
@Service
@Transactional
public class SysExtendFieldService extends BaseService<SysExtendField> {

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param extendId
     * @return
     */
    @Transactional(readOnly = true)
    public List<SysExtendField> getList(Integer extendId) {
        return dao.getList(extendId);
    }

    /**
     * @param extendId
     * @param entitys
     */
    public void update(Integer extendId, List<SysExtendField> entitys) {
        if (CommonUtils.notEmpty(extendId)) {
            Set<String> codeList = new HashSet<>();
            if (CommonUtils.notEmpty(entitys)) {
                for (SysExtendField entity : entitys) {
                    if (0 != entity.getId().getExtendId()) {
                        if (null == getEntity(entity.getId())) {
                            save(entity);
                        } else {
                            update(entity.getId(), entity, ignoreProperties);
                        }
                    } else {
                        entity.getId().setExtendId(extendId);
                        save(entity);
                    }
                    codeList.add(entity.getId().getCode());
                }
            }
            for (SysExtendField extend : getList(extendId)) {
                if (!codeList.contains(extend.getId().getCode())) {
                    delete(extend.getId());
                }
            }
        }
    }

    @Autowired
    private SysExtendFieldDao dao;

}