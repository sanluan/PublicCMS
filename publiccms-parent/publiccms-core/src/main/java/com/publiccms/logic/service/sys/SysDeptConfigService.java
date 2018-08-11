package com.publiccms.logic.service.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDeptConfig;
import com.publiccms.entities.sys.SysDeptConfigId;
import com.publiccms.logic.dao.sys.SysDeptConfigDao;

/**
 *
 * SysDeptConfigService
 * 
 */
@Service
@Transactional
public class SysDeptConfigService extends BaseService<SysDeptConfig> {

    /**
     * @param deptId
     * @param config
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer deptId, String config, Integer pageIndex, Integer pageSize) {
        return dao.getPage(deptId, config, pageIndex, pageSize);
    }

    /**
     * @param deptId
     * @param configs
     */
    public void updateDeptConfigs(Integer deptId, String[] configs) {
        if (CommonUtils.notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptConfig> list = (List<SysDeptConfig>) getPage(deptId, null, null, null).getList();
            for (SysDeptConfig deptConfig : list) {
                if (ArrayUtils.contains(configs, deptConfig.getId().getConfig())) {
                    configs = ArrayUtils.removeElement(configs, deptConfig.getId().getConfig());
                } else {
                    delete(deptConfig.getId());
                }
            }
            if (CommonUtils.notEmpty(configs)) {
                for (String config : configs) {
                    save(new SysDeptConfig(new SysDeptConfigId(deptId, config)));
                }
            }

        }
    }
    
    /**
     * @param deptId
     * @param config
     */
    public void delete(Integer deptId, String config) {
        if (CommonUtils.notEmpty(config) || CommonUtils.notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptConfig> list = (List<SysDeptConfig>) getPage(deptId, config, null, null).getList();
            for (SysDeptConfig deptConfig : list) {
                delete(deptConfig.getId());
            }
        }
    }

    @Autowired
    private SysDeptConfigDao dao;
    
}