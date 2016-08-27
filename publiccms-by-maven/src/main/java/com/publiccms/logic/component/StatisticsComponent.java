package com.publiccms.logic.component;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Cacheable;
import com.publiccms.entities.cms.CmsWord;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.cms.CmsWordService;
import com.publiccms.views.pojo.CmsContentRelatedStatistics;
import com.publiccms.views.pojo.CmsContentStatistics;
import com.publiccms.views.pojo.CmsPlaceStatistics;
import com.publiccms.views.pojo.CmsTagStatistics;
import com.publiccms.views.pojo.CmsWordStatistics;
import com.sanluan.common.base.Base;

@Component
public class StatisticsComponent extends Base implements Cacheable {
    private static List<Long> contentCachedlist = synchronizedList(new ArrayList<Long>());
    private static Map<Long, CmsContentStatistics> cachedMap = synchronizedMap(new HashMap<Long, CmsContentStatistics>());
    private static List<Long> placeCachedlist = synchronizedList(new ArrayList<Long>());
    private static Map<Long, CmsPlaceStatistics> placeCachedMap = synchronizedMap(new HashMap<Long, CmsPlaceStatistics>());
    private static List<Long> relatedCachedlist = synchronizedList(new ArrayList<Long>());
    private static Map<Long, CmsContentRelatedStatistics> relatedCachedMap = synchronizedMap(
            new HashMap<Long, CmsContentRelatedStatistics>());
    private static List<Long> wordCachedlist = synchronizedList(new ArrayList<Long>());
    private static Map<Long, CmsWordStatistics> wordCachedMap = synchronizedMap(new HashMap<Long, CmsWordStatistics>());
    private static List<Long> tagCachedlist = synchronizedList(new ArrayList<Long>());
    private static Map<Long, CmsTagStatistics> tagCachedMap = synchronizedMap(new HashMap<Long, CmsTagStatistics>());
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

    private void clearCache(int size) {
        if (size < contentCachedlist.size()) {
            List<CmsContentStatistics> list = new ArrayList<CmsContentStatistics>();
            for (int i = 0; i < size / 10; i++) {
                list.add(cachedMap.remove(contentCachedlist.remove(0)));
            }
            contentService.updateStatistics(list);
        }
    }

    private void clearPlaceCache(int size) {
        if (size < placeCachedlist.size()) {
            List<CmsPlaceStatistics> list = new ArrayList<CmsPlaceStatistics>();
            for (int i = 0; i < size / 10; i++) {
                list.add(placeCachedMap.remove(placeCachedlist.remove(0)));
            }
            placeService.updateStatistics(list);
        }
    }

    private void clearRelatedCache(int size) {
        if (size < relatedCachedlist.size()) {
            List<CmsContentRelatedStatistics> list = new ArrayList<CmsContentRelatedStatistics>();
            for (int i = 0; i < size / 10; i++) {
                list.add(relatedCachedMap.remove(relatedCachedlist.remove(0)));
            }
            contentRelatedService.updateStatistics(list);
        }
    }

    private void clearWordCache(int size) {
        if (size < wordCachedlist.size()) {
            List<CmsWordStatistics> list = new ArrayList<CmsWordStatistics>();
            for (int i = 0; i < size / 10; i++) {
                list.add(wordCachedMap.remove(wordCachedlist.remove(0)));
            }
            wordService.updateStatistics(list);
        }
    }

    private void clearTagCache(int size) {
        if (size < tagCachedlist.size()) {
            List<CmsTagStatistics> list = new ArrayList<CmsTagStatistics>();
            for (int i = 0; i < size / 10; i++) {
                list.add(tagCachedMap.remove(tagCachedlist.remove(0)));
            }
            tagService.updateStatistics(list);
        }
    }

    public CmsContentRelatedStatistics relatedClicks(Long id) {
        if (notEmpty(id)) {
            CmsContentRelatedStatistics contentRelatedStatistics = relatedCachedMap.get(id);
            if (empty(contentRelatedStatistics)) {
                clearRelatedCache(100);
                contentRelatedStatistics = new CmsContentRelatedStatistics(id, 1, contentRelatedService.getEntity(id));
                relatedCachedlist.add(id);
            } else {
                contentRelatedStatistics.setClicks(contentRelatedStatistics.getClicks() + 1);
            }
            relatedCachedMap.put(id, contentRelatedStatistics);
            return contentRelatedStatistics;
        } else {
            return null;
        }
    }

    public CmsTagStatistics searchTag(Long id) {
        if (notEmpty(id)) {
            CmsTagStatistics tagStatistics = tagCachedMap.get(id);
            if (empty(tagStatistics)) {
                clearTagCache(100);
                tagStatistics = new CmsTagStatistics(id, 1, tagService.getEntity(id));
                tagCachedlist.add(id);
            } else {
                tagStatistics.setSearchCounts(tagStatistics.getSearchCounts() + 1);
            }
            tagCachedMap.put(id, tagStatistics);
            return tagStatistics;
        } else {
            return null;
        }
    }

    public CmsWordStatistics search(int siteId, String word) {
        if (notEmpty(word)) {
            CmsWord entity = wordService.getEntity(siteId, word);
            if (empty(entity)) {
                entity = new CmsWord();
                entity.setName(word);
                entity.setSiteId(siteId);
                entity.setHidden(true);
                wordService.save(entity);
            }
            long id = entity.getId();
            CmsWordStatistics wordStatistics = wordCachedMap.get(id);
            if (empty(wordStatistics)) {
                clearWordCache(100);
                wordStatistics = new CmsWordStatistics(entity.getId(), 1, entity);
                wordCachedlist.add(id);
            } else {
                wordStatistics.setSearchCounts(wordStatistics.getSearchCounts() + 1);
            }
            wordCachedMap.put(id, wordStatistics);
            return wordStatistics;
        } else {
            return null;
        }
    }

    public CmsPlaceStatistics placeClicks(Long id) {
        if (notEmpty(id)) {
            CmsPlaceStatistics placeStatistics = placeCachedMap.get(id);
            if (empty(placeStatistics)) {
                clearPlaceCache(100);
                placeStatistics = new CmsPlaceStatistics(id, 1, placeService.getEntity(id));
                placeCachedlist.add(id);
            } else {
                placeStatistics.setClicks(placeStatistics.getClicks() + 1);
            }
            placeCachedMap.put(id, placeStatistics);
            return placeStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics clicks(Long id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = cachedMap.get(id);
            if (empty(contentStatistics)) {
                clearCache(300);
                contentStatistics = new CmsContentStatistics(id, 1, 0, 0, contentService.getEntity(id));
                contentCachedlist.add(id);
            } else {
                contentStatistics.setClicks(contentStatistics.getClicks() + 1);
            }
            cachedMap.put(id, contentStatistics);
            return contentStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics comments(Long id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = cachedMap.get(id);
            if (empty(contentStatistics)) {
                clearCache(300);
                contentStatistics = new CmsContentStatistics(id, 0, 1, 0, contentService.getEntity(id));
                contentCachedlist.add(id);
            } else {
                contentStatistics.setComments(contentStatistics.getComments() + 1);
            }
            cachedMap.put(id, contentStatistics);
            return contentStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics scores(Long id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = cachedMap.get(id);
            if (empty(contentStatistics)) {
                clearCache(300);
                contentStatistics = new CmsContentStatistics(id, 0, 0, 1, contentService.getEntity(id));
                contentCachedlist.add(id);
            } else {
                contentStatistics.setComments(contentStatistics.getComments() + 1);
            }
            cachedMap.put(id, contentStatistics);
            return contentStatistics;
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        placeService.updateStatistics(placeCachedMap.values());
        placeCachedlist.clear();
        placeCachedMap.clear();
        contentRelatedService.updateStatistics(relatedCachedMap.values());
        relatedCachedlist.clear();
        relatedCachedMap.clear();
        wordService.updateStatistics(wordCachedMap.values());
        wordCachedlist.clear();
        wordCachedMap.clear();
        tagService.updateStatistics(tagCachedMap.values());
        tagCachedlist.clear();
        tagCachedMap.clear();
        contentService.updateStatistics(cachedMap.values());
        contentCachedlist.clear();
        cachedMap.clear();
    }
}