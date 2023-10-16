package com.publiccms.logic.component.site;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Cache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.cms.CmsWord;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.cms.CmsWordService;
import com.publiccms.views.pojo.entities.ClickStatistics;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.entities.PlaceClickStatistics;

import freemarker.template.TemplateException;

/**
 *
 * StatisticsComponent
 *
 */
@Component
public class StatisticsComponent implements Cache {

    protected final Log log = LogFactory.getLog(getClass());

    private CacheEntity<Long, ClickStatistics> contentCache;
    private CacheEntity<Long, ClickStatistics> contentFileCache;
    private CacheEntity<Long, PlaceClickStatistics> placeCache;
    private CacheEntity<Long, ClickStatistics> wordCache;
    private CacheEntity<Long, ClickStatistics> tagCache;
    @Resource
    private CmsContentService contentService;
    @Resource
    private CmsContentFileService contentFileService;
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    private CmsPlaceService placeService;
    @Resource
    private CmsWordService wordService;
    @Resource
    private CmsTagService tagService;
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    protected SiteComponent siteComponent;

    /**
     * @param id
     * @return tag statistics
     */
    public ClickStatistics searchTag(Long id) {
        if (CommonUtils.notEmpty(id)) {
            ClickStatistics clickStatistics = tagCache.get(id);
            if (null == clickStatistics) {
                clickStatistics = new ClickStatistics(id, null, 1, 0, null);
                List<ClickStatistics> list = tagCache.put(id, clickStatistics);
                if (CommonUtils.notEmpty(list)) {
                    tagService.updateStatistics(list);
                }
            } else {
                clickStatistics.addClicks();
            }
            return clickStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param siteId
     * @param word
     * @param ip
     * @return word statistics
     */
    public ClickStatistics search(short siteId, String word, String ip) {
        if (CommonUtils.notEmpty(word)) {
            CmsWord entity = wordService.getEntity(siteId, word);
            if (null == entity) {
                entity = new CmsWord();
                entity.setName(word);
                entity.setIp(ip);
                entity.setSiteId(siteId);
                entity.setHidden(true);
                entity.setSearchCount(1);
                wordService.save(entity);
            }
            ClickStatistics clickStatistics = wordCache.get(entity.getId());
            if (null == clickStatistics) {
                clickStatistics = new ClickStatistics(entity.getId(), null, 1, entity.getSearchCount(), null);
                List<ClickStatistics> list = wordCache.put(entity.getId(), clickStatistics);
                if (CommonUtils.notEmpty(list)) {
                    wordService.updateStatistics(list);
                }
            } else {
                clickStatistics.addClicks();
            }
            return clickStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param site
     * @param id
     * @return place statistics
     */
    public ClickStatistics placeClicks(SysSite site, Long id) {
        if (CommonUtils.notEmpty(id)) {
            PlaceClickStatistics clickStatistics = placeCache.get(id);
            if (null == clickStatistics) {
                CmsPlace entity = placeService.getEntity(id);
                if (null != entity && !entity.isDisabled() && CmsPlaceService.STATUS_NORMAL == entity.getStatus()
                        && site.getId() == entity.getSiteId()
                        && (0 == entity.getMaxClicks() || entity.getMaxClicks() > entity.getClicks())) {
                    clickStatistics = new PlaceClickStatistics(id, entity.getSiteId(), 1, entity.getClicks(), entity.getUrl(),
                            entity.getMaxClicks());
                    List<PlaceClickStatistics> list = placeCache.put(id, clickStatistics);
                    if (CommonUtils.notEmpty(list)) {
                        placeService.updateStatistics(list);
                    }
                }
            } else {
                clickStatistics.addClicks();
                if (0 <= clickStatistics.getMaxClicks() && clickStatistics.getMaxClicks() < clickStatistics.getClicks() + clickStatistics.getOldClicks()) {
                    placeService.shelf(id, false);
                    placeCache.remove(id);
                    CmsPlace entity = placeService.getEntity(id);
                    if (null != entity) {
                        String placePath = CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, entity.getPath());
                        if (site.isUseSsi() || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), placePath))) {
                            try {
                                String filepath = siteComponent.getTemplateFilePath(site.getId(), placePath);
                                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                                CmsPageData data = metadataComponent.getTemplateData(filepath);
                                BeanComponent.getTemplateComponent().staticPlace(site, entity.getPath(), metadata, data);
                            } catch (IOException | TemplateException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
            return clickStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param site
     * @param id
     * @return content statistics
     */
    public ClickStatistics contentClicks(SysSite site, Long id) {
        if (CommonUtils.notEmpty(id)) {
            ClickStatistics clickStatistics = contentCache.get(id);
            if (null == clickStatistics) {
                CmsContent entity = contentService.getEntity(id);
                if (null != entity && !entity.isDisabled() && CmsContentService.STATUS_NORMAL == entity.getStatus()
                        && site.getId().equals(entity.getSiteId())) {
                    CmsUrlUtils.initContentUrl(site, entity);
                    clickStatistics = new ClickStatistics(id, entity.getSiteId(), 1, entity.getClicks(), entity.getUrl());
                    List<ClickStatistics> list = contentCache.put(id, clickStatistics);
                    if (CommonUtils.notEmpty(list)) {
                        contentService.updateStatistics(list);
                    }
                }
            } else {
                clickStatistics.addClicks();
            }
            return clickStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param site
     * @param content
     * @param id
     * @return content file statistics
     */
    public ClickStatistics contentFileClicks(SysSite site, CmsContent content, Long id) {
        if (CommonUtils.notEmpty(id) && null != content) {
            ClickStatistics clickStatistics = contentFileCache.get(id);
            if (null == clickStatistics) {
                CmsContentFile entity = contentFileService.getEntity(id);
                if (null != entity && content.getId() == entity.getContentId() && !content.isDisabled()
                        && CmsContentService.STATUS_NORMAL == content.getStatus() && site.getId().equals(content.getSiteId())) {
                    clickStatistics = new ClickStatistics(id, site.getId(), 1, entity.getClicks(),
                            CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), entity.getFilePath()));
                    List<ClickStatistics> list = contentCache.put(id, clickStatistics);
                    if (CommonUtils.notEmpty(list)) {
                        contentService.updateStatistics(list);
                    }
                }
            } else {
                clickStatistics.addClicks();
            }
            return clickStatistics;
        } else {
            return null;
        }
    }

    /**
     * @param id
     * @return content statistics
     */
    public ClickStatistics getContentStatistics(Long id) {
        if (CommonUtils.notEmpty(id)) {
            return contentCache.get(id);
        }
        return null;
    }

    /**
     * @param id
     * @return place clicks
     */
    public Integer getPlaceClicks(Long id) {
        if (CommonUtils.notEmpty(id)) {
            ClickStatistics clickStatistics = placeCache.get(id);
            if (null != clickStatistics) {
                return clickStatistics.getClicks();
            }
        }
        return null;
    }

    @Override
    public void clear() {
        placeService.updateStatistics(placeCache.clear(true));
        wordService.updateStatistics(wordCache.clear(true));
        tagService.updateStatistics(tagCache.clear(true));
        contentService.updateStatistics(contentCache.clear(true));
        contentFileService.updateStatistics(contentFileCache.clear(true));
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @Resource
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        contentCache = cacheEntityFactory.createCacheEntity("content");
        contentFileCache = cacheEntityFactory.createCacheEntity("contentFile");
        placeCache = cacheEntityFactory.createCacheEntity("place");
        wordCache = cacheEntityFactory.createCacheEntity("word");
        tagCache = cacheEntityFactory.createCacheEntity("tag");
    }
}