package com.publiccms.logic.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysWorkflowProcessItem;
import com.publiccms.entities.sys.SysWorkflowProcessItemId;
import com.publiccms.logic.dao.sys.SysWorkflowProcessItemDao;

import jakarta.annotation.Resource;

/**
 *
 * SysWorkflowProcessItemService
 * 
 */
@Service
@Transactional
public class SysWorkflowProcessItemService extends BaseService<SysWorkflowProcessItem> {

    public void createOrUpdate(String itemType, String itemId, long processId) {
        if (CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId)) {
            SysWorkflowProcessItemId id = new SysWorkflowProcessItemId(itemType, itemId);
            SysWorkflowProcessItem entity = getEntity(id);
            if (null == entity) {
                entity = new SysWorkflowProcessItem(id, processId);
                save(entity);
            } else {
                entity.setProcessId(processId);
            }
        }
    }

    @Resource
    private SysWorkflowProcessItemDao dao;

}