package com.publiccms.logic.service.cms;

// Generated 2015-7-10 16:36:23 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentTag;
import com.publiccms.logic.dao.cms.CmsContentTagDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsContentTagService extends BaseService<CmsContentTag> {

    @Autowired
    private CmsContentTagDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer tagId, Integer contentId, Integer[] tagIds, Integer[] contentIds, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(tagId, contentId, tagIds, contentIds, pageIndex, pageSize);
    }

    public int delete(Integer tagId, Integer contentId) {
        return dao.delete(tagId, contentId);
    }

    public void updateContentTags(Integer contentId, String tagIds) {
        if (notEmpty(tagIds) && notEmpty(contentId)) {
            String[] categoryStringIds = splitByWholeSeparator(tagIds, ",");
            if (notEmpty(categoryStringIds)) {
                Integer[] tagIdsArray = new Integer[categoryStringIds.length];
                for (int i = 0; i < categoryStringIds.length; i++) {
                    tagIdsArray[i] = Integer.parseInt(categoryStringIds[i]);
                }
                @SuppressWarnings("unchecked")
                List<CmsContentTag> list = (List<CmsContentTag>) getPage(null, contentId, null, null, null, null).getList();
                for (CmsContentTag contentTag : list) {
                    if (contains(tagIdsArray, contentTag.getId())) {
                        tagIdsArray = removeElement(tagIdsArray, contentTag.getId());
                    } else {
                        delete(contentTag.getId());
                    }
                }
                if (notEmpty(tagIdsArray)) {
                    for (int tagid : tagIdsArray) {
                        save(new CmsContentTag(tagid, contentId));
                    }
                }
            }
        }
    }
}