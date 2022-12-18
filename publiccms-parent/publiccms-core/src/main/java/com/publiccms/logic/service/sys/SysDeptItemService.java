package com.publiccms.logic.service.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.generator.SourceGenerator

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDeptItem;
import com.publiccms.entities.sys.SysDeptItemId;
import com.publiccms.logic.dao.sys.SysDeptItemDao;

/**
 *
 * SysDeptItemService
 * 
 */
@Service
@Transactional
public class SysDeptItemService extends BaseService<SysDeptItem> {
    /**
     * 
     */
    public static final String ITEM_TYPE_CATEGORY = "category";
    /**
     * 
     */
    public static final String ITEM_TYPE_CONFIG = "config";
    /**
     * 
     */
    public static final String ITEM_TYPE_PAGE = "page";

    /**
     * @param deptId
     * @param itemType
     * @param itemId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer deptId, String itemType, String itemId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(deptId, itemType, itemId, pageIndex, pageSize);
    }

    /**
     * @param deptId
     * @param itemType
     * @param itemIds
     */
    public void save(Integer deptId, String itemType, String[] itemIds) {
        if (CommonUtils.notEmpty(deptId) && CommonUtils.notEmpty(itemIds)) {
            for (String itemId : itemIds) {
                save(new SysDeptItem(new SysDeptItemId(deptId, itemType, itemId)));
            }
        }
    }

    /**
     * @param deptId
     * @param itemType
     * @param itemIds
     */
    public void update(Integer deptId, String itemType, String[] itemIds) {
        if (CommonUtils.notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptItem> list = (List<SysDeptItem>) getPage(deptId, itemType, null, null, null).getList();
            for (SysDeptItem deptItem : list) {
                if (ArrayUtils.contains(itemIds, deptItem.getId().getItemId())) {
                    itemIds = ArrayUtils.removeElement(itemIds, deptItem.getId().getItemId());
                } else {
                    delete(deptItem.getId());
                }
            }
            if (CommonUtils.notEmpty(itemIds)) {
                for (String itemId : itemIds) {
                    save(new SysDeptItem(new SysDeptItemId(deptId, itemType, itemId)));
                }
            }

        }
    }

    /**
     * @param deptId
     * @param itemType
     * @param itemId
     */
    public void delete(Integer deptId, String itemType, String itemId) {
        if (CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId) || CommonUtils.notEmpty(deptId)) {
            dao.delete(deptId, itemType, itemId);
        }
    }

    @Resource
    private SysDeptItemDao dao;

}