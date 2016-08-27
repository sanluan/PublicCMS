package com.publiccms.logic.service.cms;

// Generated 2015-7-10 16:36:23 by com.sanluan.common.source.SourceMaker

import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Transactional(readOnly = true)
    public PageHandler getPage(Long tagId, Long contentId, Long[] contentIds, Integer pageIndex, Integer pageSize) {
        return dao.getPage(tagId, contentId, contentIds, pageIndex, pageSize);
    }

    public int delete(Long[] tagIds, Long[] contentIds) {
        return dao.delete(tagIds, contentIds);
    }

    public void update(Long contentId, String tagIds) {
        if (notEmpty(tagIds) && notEmpty(contentId)) {
            String[] tagIdsArray = splitByWholeSeparator(tagIds, BLANK_SPACE);
            if (notEmpty(tagIdsArray)) {
                Set<Long> idSet = new HashSet<Long>();
                for (int i = 0; i < tagIdsArray.length; i++) {
                    idSet.add(Long.parseLong(tagIdsArray[i]));
                }
                @SuppressWarnings("unchecked")
                List<CmsContentTag> list = (List<CmsContentTag>) getPage(null, contentId, null, null, null).getList();
                for (CmsContentTag contentTag : list) {
                    if (idSet.contains(contentTag.getId())) {
                        idSet.remove(contentTag.getId());
                    } else {
                        delete(contentTag.getId());
                    }
                }
                for (long tagid : idSet) {
                    save(new CmsContentTag(tagid, contentId));
                }
            }
        }
    }
    
    @Autowired
    private CmsContentTagDao dao;
}