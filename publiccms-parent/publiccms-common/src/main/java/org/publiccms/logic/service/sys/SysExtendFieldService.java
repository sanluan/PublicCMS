package org.publiccms.logic.service.sys;

// Generated 2016-3-2 13:39:54 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.publiccms.entities.sys.SysExtendField;
import org.publiccms.logic.dao.sys.SysExtendFieldDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;

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
        if (notEmpty(extendId)) {
            Set<String> codeList = new HashSet<String>();
            if (notEmpty(entitys)) {
                for (SysExtendField entity : entitys) {
                    if (notEmpty(entity.getId().getExtendId())) {
                        update(entity.getId(), entity, ignoreProperties);
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