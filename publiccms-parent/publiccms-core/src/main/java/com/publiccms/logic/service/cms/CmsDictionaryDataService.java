package com.publiccms.logic.service.cms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.cms.CmsDictionaryDataId;
import com.publiccms.logic.dao.cms.CmsDictionaryDataDao;

/**
 *
 * CmsDictionaryDataService
 * 
 */
@Service
@Transactional
public class CmsDictionaryDataService extends BaseService<CmsDictionaryData> {
    /**
     * @param siteId
     * @param dictionaryId
     * @param dataList
     */
    public void save(short siteId, String dictionaryId, List<CmsDictionaryData> dataList) {
        if (CommonUtils.notEmpty(dataList)) {
            for (CmsDictionaryData entity : dataList) {
                if (null != entity.getId()) {
                    entity.getId().setSiteId(siteId);
                    entity.getId().setDictionaryId(dictionaryId);
                    save(entity);
                }
            }
        }
    }

    /**
     * @param siteId
     * @param dictionaryId
     * @param dataList
     */
    public void update(short siteId, String dictionaryId, List<CmsDictionaryData> dataList) {
        Set<CmsDictionaryDataId> idSet = new HashSet<>();
        if (CommonUtils.notEmpty(dataList)) {
            for (CmsDictionaryData entity : dataList) {
                if (null != entity.getId()) {
                    entity.getId().setSiteId(siteId);
                    entity.getId().setDictionaryId(dictionaryId);
                    CmsDictionaryData oldEntity = getEntity(entity.getId());
                    if (null == oldEntity) {
                        save(entity);
                    } else {
                        oldEntity.setText(entity.getText());
                    }
                    idSet.add(entity.getId());
                }
            }
        }
        List<CmsDictionaryData> list = getList(siteId, dictionaryId);
        for (CmsDictionaryData entity : list) {
            if (!idSet.contains(entity.getId())) {
                delete(entity.getId());
            }
        }
    }

    /**
     * @param siteId
     * @param dictionaryId
     * @return data list
     */
    @Transactional(readOnly = true)
    public List<CmsDictionaryData> getList(short siteId, String dictionaryId) {
        return dao.getList(siteId, dictionaryId);
    }

    /**
     * @param siteId
     * @param dictionaryIds
     * @return the number of entities deleted
     */
    public int delete(short siteId, String[] dictionaryIds) {
        return dao.delete(siteId, dictionaryIds);
    }

    @Autowired
    private CmsDictionaryDataDao dao;

}