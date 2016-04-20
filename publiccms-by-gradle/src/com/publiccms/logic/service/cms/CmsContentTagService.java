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
    public PageHandler getPage(Integer tagId, Integer contentId, Integer[] contentIds, Integer pageIndex, Integer pageSize) {
        return dao.getPage(tagId, contentId, contentIds, pageIndex, pageSize);
    }

    public int delete(Integer[] tagIds, Integer[] contentIds) {
        return dao.delete(tagIds, contentIds);
    }

    public void update(Integer contentId, String tagIds) {
        if (notEmpty(tagIds) && notEmpty(contentId)) {
            String[] tagIdsArray = splitByWholeSeparator(tagIds, ",");
            if (notEmpty(tagIdsArray)) {
                Set<Integer> idSet = new HashSet<Integer>();
                for (int i = 0; i < tagIdsArray.length; i++) {
                    idSet.add(Integer.parseInt(tagIdsArray[i]));
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
                for (int tagid : idSet) {
                    save(new CmsContentTag(tagid, contentId));
                }
            }
        }
    }
    
    @Autowired
    private CmsContentTagDao dao;
}