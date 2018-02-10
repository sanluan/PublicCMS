package com.publiccms.logic.component.site;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Cache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsWord;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.cms.CmsWordService;
import com.publiccms.views.pojo.entities.CmsContentRelatedStatistics;
import com.publiccms.views.pojo.entities.CmsContentStatistics;
import com.publiccms.views.pojo.entities.CmsPlaceStatistics;
import com.publiccms.views.pojo.entities.CmsTagStatistics;
import com.publiccms.views.pojo.entities.CmsWordStatistics;

/**
 *
 * StatisticsComponent
 * 
 */
@Component
public class StatisticsComponent implements Cache {
    
    private CacheEntity<Long, CmsContentStatistics> contentCache;
    private CacheEntity<Long, CmsPlaceStatistics> placeCache;
    private CacheEntity<Long, CmsContentRelatedStatistics> relatedCache;
    private CacheEntity<Long, CmsWordStatistics> wordCache;
    private CacheEntity<Long, CmsTagStatistics> tagCache;
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private CmsContentRelatedService contentRelatedService;
    @Autowired
    private CmsPlaceService placeService;
    @Autowired
    private CmsWordService wordService;
    @Autowired
    private CmsTagService tagService;

    /**
     * @param id
     * @return related clicks statistics
     */
    public CmsContentRelatedStatistics relatedClicks(Long id) {
        if (CommonUtils.notEmpty(id)) {
            CmsContentRelatedStatistics contentRelatedStatistics = relatedCache.get(id);
            if (null == contentRelatedStatistics) {
                contentRelatedStatistics = new CmsContentRelatedStatistics(id, 1, contentRelatedService.getEntity(id));
            } else {
                contentRelatedStatistics.setClicks(contentRelatedStatistics.getClicks() + 1);
            }
            List<CmsContentRelatedStatistics> list = relatedCache.put(id, contentRelatedStatistics);
            if (CommonUtils.notEmpty(list)) {
                contentRelatedService.updateStatistics(list);
            }
            return contentRelatedStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param id
     * @return tag statistics
     */
    public CmsTagStatistics searchTag(Long id) {
        if (CommonUtils.notEmpty(id)) {
            CmsTagStatistics tagStatistics = tagCache.get(id);
            if (null == tagStatistics) {
                tagStatistics = new CmsTagStatistics(id, 1, tagService.getEntity(id));
            } else {
                tagStatistics.setSearchCounts(tagStatistics.getSearchCounts() + 1);
            }
            List<CmsTagStatistics> list = tagCache.put(id, tagStatistics);
            if (CommonUtils.notEmpty(list)) {
                tagService.updateStatistics(list);
            }
            return tagStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param siteId
     * @param word
     * @return word statistics
     */
    public CmsWordStatistics search(short siteId, String word) {
        if (CommonUtils.notEmpty(word)) {
            CmsWord entity = wordService.getEntity(siteId, word);
            if (null == entity) {
                entity = new CmsWord();
                entity.setName(word);
                entity.setSiteId(siteId);
                entity.setHidden(true);
                wordService.save(entity);
            }
            CmsWordStatistics wordStatistics = wordCache.get(entity.getId());
            if (null == wordStatistics) {
                wordStatistics = new CmsWordStatistics(entity.getId(), 1, entity);
            } else {
                wordStatistics.setSearchCounts(wordStatistics.getSearchCounts() + 1);
            }
            List<CmsWordStatistics> list = wordCache.put(entity.getId(), wordStatistics);
            if (CommonUtils.notEmpty(list)) {
                wordService.updateStatistics(list);
            }
            return wordStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param id
     * @return place statistics
     */
    public CmsPlaceStatistics placeClicks(Long id) {
        if (CommonUtils.notEmpty(id)) {
            CmsPlaceStatistics placeStatistics = placeCache.get(id);
            if (null == placeStatistics) {
                placeStatistics = new CmsPlaceStatistics(id, 1, placeService.getEntity(id));
            } else {
                placeStatistics.setClicks(placeStatistics.getClicks() + 1);
            }
            List<CmsPlaceStatistics> list = placeCache.put(id, placeStatistics);
            if (CommonUtils.notEmpty(list)) {
                placeService.updateStatistics(list);
            }
            return placeStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param id
     * @return content statistics
     */
    public CmsContentStatistics clicks(Long id) {
        if (CommonUtils.notEmpty(id)) {
            CmsContentStatistics contentStatistics = contentCache.get(id);
            if (null == contentStatistics) {
                contentStatistics = new CmsContentStatistics(id, 1, 0, 0, contentService.getEntity(id));
            } else {
                contentStatistics.setClicks(contentStatistics.getClicks() + 1);
            }
            List<CmsContentStatistics> list = contentCache.put(id, contentStatistics);
            if (CommonUtils.notEmpty(list)) {
                contentService.updateStatistics(list);
            }
            return contentStatistics;
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        placeService.updateStatistics(placeCache.clear());
        contentRelatedService.updateStatistics(relatedCache.clear());
        wordService.updateStatistics(wordCache.clear());
        tagService.updateStatistics(tagCache.clear());
        contentService.updateStatistics(contentCache.clear());
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        contentCache = cacheEntityFactory.createCacheEntity("content");
        placeCache = cacheEntityFactory.createCacheEntity("place");
        relatedCache = cacheEntityFactory.createCacheEntity("related");
        wordCache = cacheEntityFactory.createCacheEntity("word");
        tagCache = cacheEntityFactory.createCacheEntity("tag");
    }
}