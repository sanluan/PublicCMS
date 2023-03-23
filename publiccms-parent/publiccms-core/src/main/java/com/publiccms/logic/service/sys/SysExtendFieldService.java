package com.publiccms.logic.service.sys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysExtendFieldId;
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
     * @param inputType
     * @param searchable
     * @return result list
     */
    @Transactional(readOnly = true)
    public List<SysExtendField> getList(Integer extendId, String[] inputType, Boolean searchable) {
        return dao.getList(extendId, inputType, searchable);
    }

    /**
     * @param extendId
     * @param copyExtendId
     */
    public void copy(Integer extendId, Integer copyExtendId) {
        List<SysExtendField> list = dao.getList(copyExtendId, null, null);
        if (CommonUtils.notEmpty(list)) {
            List<SysExtendField> newlist = new ArrayList<>();
            for (SysExtendField entity : list) {
                SysExtendField e = new SysExtendField();
                SysExtendFieldId id = new SysExtendFieldId();
                BeanUtils.copyProperties(entity, e);
                id.setExtendId(extendId);
                id.setCode(entity.getId().getCode());
                e.setId(id);
                newlist.add(e);
            }
            save(newlist);
        }
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
            for (SysExtendField extend : getList(extendId, null, null)) {
                if (!codeList.contains(extend.getId().getCode())) {
                    delete(extend.getId());
                }
            }
        }
    }

    @Resource
    private SysExtendFieldDao dao;

}