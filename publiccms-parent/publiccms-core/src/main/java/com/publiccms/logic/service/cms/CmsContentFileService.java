package com.publiccms.logic.service.cms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.logic.dao.cms.CmsContentFileDao;

/**
 *
 * CmsContentFileService
 * 
 */
@Service
@Transactional
public class CmsContentFileService extends BaseService<CmsContentFile> {

    public static final String[] ignoreProperties = new String[] { "id", "userId", "contentId" };

    /**
     * @param contentId
     * @param userId
     * @param fileTypes
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long contentId, Long userId, String[] fileTypes, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(contentId, userId, fileTypes, orderField, orderType, pageIndex, pageSize);
    }
    
    /**
     * @param contentId
     * @param fileTypes
     * @return results list
     */
    @Transactional(readOnly = true)
    public List<CmsContentFile> getList(long contentId, String[] fileTypes) {
        return dao.getList(contentId, fileTypes);
    }

    /**
     * @param contentId
     * @param userId
     * @param files
     * @param images
     */
    @SuppressWarnings("unchecked")
    public void update(long contentId, Long userId, List<CmsContentFile> files, List<CmsContentFile> images) {
        Set<Long> idList = new HashSet<>();
        if (CommonUtils.notEmpty(images)) {
            for (CmsContentFile entity : images) {
                entity.setFileType(CmsFileUtils.getFileType(CmsFileUtils.getSuffix(entity.getFilePath())));
                if (null != entity.getId()) {
                    update(entity.getId(), entity, ignoreProperties);
                } else {
                    entity.setUserId(userId);
                    entity.setContentId(contentId);
                    save(entity);
                }
                idList.add(entity.getId());
            }
        }
        if (CommonUtils.notEmpty(files)) {
            for (CmsContentFile entity : files) {
                entity.setFileType(CmsFileUtils.getFileType(CmsFileUtils.getSuffix(entity.getFilePath())));
                if (CmsFileUtils.FILE_TYPE_IMAGE.equals(entity.getFileType())) {
                    entity.setFileType(CmsFileUtils.FILE_TYPE_OTHER);
                }
                if (null != entity.getId()) {
                    update(entity.getId(), entity, ignoreProperties);
                } else {
                    entity.setContentId(contentId);
                    entity.setUserId(userId);
                    save(entity);
                }
                idList.add(entity.getId());
            }
        }
        for (CmsContentFile file : (List<CmsContentFile>) getPage(contentId, null, null, null, null, null, null).getList()) {
            if (!idList.contains(file.getId())) {
                delete(file.getId());
            }
        }
    }

    @Resource
    private CmsContentFileDao dao;

}