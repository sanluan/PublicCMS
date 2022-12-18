package com.publiccms.logic.service.cms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.cms.CmsDictionaryExcludeId;
import com.publiccms.logic.dao.cms.CmsDictionaryExcludeDao;

/**
 *
 * CmsDictionaryExcludeService
 * 
 */
@Service
@Transactional
public class CmsDictionaryExcludeService extends BaseService<CmsDictionaryExclude> {

    /**
     * @param siteId
     * @param dictionaryId
     * @param excludeList
     */
    public void update(short siteId, String dictionaryId, List<CmsDictionaryExclude> excludeList) {
        Set<CmsDictionaryExcludeId> idSet = new HashSet<>();
        if (CommonUtils.notEmpty(excludeList)) {
            for (CmsDictionaryExclude entity : excludeList) {
                if (null != entity.getId()) {
                    entity.getId().setSiteId(siteId);
                    entity.getId().setDictionaryId(dictionaryId);
                    CmsDictionaryExclude oldEntity = getEntity(entity.getId());
                    if (null == oldEntity) {
                        save(entity);
                    }
                    idSet.add(entity.getId());
                }
            }
        }
        List<CmsDictionaryExclude> list = getList(siteId, dictionaryId, null);
        for (CmsDictionaryExclude entity : list) {
            if (!idSet.contains(entity.getId())) {
                delete(entity.getId());
                excludeValueService.delete(entity.getId());
            }
        }
    }

    /**
     * @param siteId
     * @param dictionaryId
     * @param excludeDictionaryId
     * @return data list
     */
    @Transactional(readOnly = true)
    public List<CmsDictionaryExclude> getList(short siteId, String dictionaryId, String excludeDictionaryId) {
        return dao.getList(siteId, dictionaryId, excludeDictionaryId);
    }

    /**
     * @param siteId
     * @param dictionaryIds
     * @return the number of entities deleted
     */
    public int delete(short siteId, String[] dictionaryIds) {
        return dao.delete(siteId, dictionaryIds);
    }

    @Resource
    private CmsDictionaryExcludeDao dao;
    @Resource
    private CmsDictionaryExcludeValueService excludeValueService;

}