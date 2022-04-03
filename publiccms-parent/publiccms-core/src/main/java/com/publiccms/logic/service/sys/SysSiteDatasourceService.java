package com.publiccms.logic.service.sys;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSiteDatasource;
import com.publiccms.entities.sys.SysSiteDatasourceId;
import com.publiccms.logic.dao.sys.SysSiteDatasourceDao;

/**
 *
 * SysSiteDatasourceService
 * 
 */
@Service
@Transactional
public class SysSiteDatasourceService extends BaseService<SysSiteDatasource> {

    /**
     * @param siteId
     * @param datasource
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional
    public List<SysSiteDatasource> getList(Short siteId, String datasource) {
        return dao.getList(siteId, datasource);
    }

    /**
     * @param siteIds
     * @param datasource
     */
    public void dealSiteDataSources(Short[] siteIds, String datasource) {
        List<SysSiteDatasource> list = getList(null, datasource);
        if (CommonUtils.notEmpty(siteIds)) {
            for (SysSiteDatasource siteDatasource : list) {
                if (!ArrayUtils.contains(siteIds, siteDatasource.getId().getSiteId())) {
                    delete(siteDatasource.getId());
                }
                siteIds = ArrayUtils.removeElement(siteIds, siteDatasource.getId().getSiteId());
            }
            for (Short siteId : siteIds) {
                save(new SysSiteDatasource(new SysSiteDatasourceId(siteId, datasource)));
            }
        } else {
            deleteByDatasource(datasource);
        }
    }

    /**
     * @param siteId
     * @return
     */
    public int deleteBySiteId(Short siteId) {
        return dao.deleteBySiteId(siteId);
    }

    /**
     * @param datasource
     * @return
     */
    public int deleteByDatasource(String datasource) {
        return dao.deleteByDatasource(datasource);
    }

    @Resource
    private SysSiteDatasourceDao dao;

}