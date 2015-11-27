package com.publiccms.logic.service.cms;

// Generated 2015-7-10 16:36:23 by SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsTag;
import com.publiccms.logic.dao.cms.CmsTagDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsTagService extends BaseService<CmsTag> {

    @Autowired
    private CmsTagDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(String name, Integer categoryId, Integer typeId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(name, categoryId, typeId, pageIndex, pageSize);
    }

    public Integer[] saveTags(Integer contentId, String[] tagNames) {
        Integer[] newTagIds = null;
        if (notEmpty(contentId)) {
            if (notEmpty(tagNames)) {
                newTagIds = new Integer[tagNames.length];
                int i = 0;
                for (String tagName : tagNames) {
                    CmsTag tag = save(new CmsTag(tagName));
                    newTagIds[i++] = tag.getId();
                }
            }
        }
        return newTagIds;
    }
}