package com.publiccms.logic.service.cms;

// Generated 2015-7-10 16:36:23 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentTag;
import com.publiccms.entities.cms.CmsTag;
import com.publiccms.logic.dao.cms.CmsContentTagDao;
import com.publiccms.logic.dao.cms.CmsTagDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsContentTagService extends BaseService<CmsContentTag, CmsContentTagDao> {

	@Autowired
	private CmsContentTagDao dao;

	@Autowired
	private CmsTagDao tagDao;

	@Transactional(readOnly = true)
	public PageHandler getPage(Integer tagId, Integer contentId, Integer[] tagIds, Integer[] contentIds, Integer pageIndex,
			Integer pageSize) {
		return dao.getPage(tagId, contentId, tagIds, contentIds, pageIndex, pageSize);
	}

	public Integer[] updateContentTags(Integer contentId, Integer[] tagIds, String[] tagNames) {
		Integer[] newTagIds = null;
		if (notEmpty(contentId)) {
			@SuppressWarnings("unchecked")
			List<CmsContentTag> list = (List<CmsContentTag>) getPage(null, contentId, null, null, null, null).getList();
			for (CmsContentTag contentTag : list) {
				if (contains(tagIds, contentTag.getId())) {
					tagIds = removeElement(tagIds, contentTag.getId());
				} else {
					delete(contentTag.getId());
				}
			}
			newTagIds = saveContentTags(contentId, tagIds, tagNames);
		}
		return newTagIds;
	}

	public Integer[] saveContentTags(Integer contentId, Integer[] tagIds, String[] tagNames) {
		Integer[] newTagIds = null;
		if (notEmpty(contentId)) {
			if (notEmpty(tagIds)) {
				for (int tagid : tagIds) {
					save(new CmsContentTag(tagid, contentId));
				}
			}
			if (notEmpty(tagNames)) {
				newTagIds = new Integer[tagNames.length];
				int i = 0;
				for (String tagName : tagNames) {
					CmsTag tag = tagDao.save(new CmsTag(tagName));
					save(new CmsContentTag(tag.getId(), contentId));
					newTagIds[i++] = tag.getId();
				}
			}
		}
		return newTagIds;
	}

	@Override
	protected CmsContentTagDao getDao() {
		return dao;
	}
}