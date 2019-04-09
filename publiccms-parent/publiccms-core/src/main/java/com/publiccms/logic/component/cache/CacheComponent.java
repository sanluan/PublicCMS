package com.publiccms.logic.component.cache;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.publiccms.common.api.Cache;
import com.publiccms.logic.service.tools.HqlService;

/**
 *
 * CacheComponent
 * 
 */
@Component
public class CacheComponent {
    @Autowired
    private List<Cache> cacheableList;
    @Autowired
    private HqlService hqlService;
    private List<AbstractCachingViewResolver> cachingViewResolverList = new ArrayList<>();

    /**
     * 
     */
    @PreDestroy
    public void clear() {
        for (Cache cache : cacheableList) {
            cache.clear();
        }
        clearViewCache();
        hqlService.clear();
    }
    
    /**
     * 
     */
    public void clearViewCache() {
        for (AbstractCachingViewResolver cachingViewResolver : cachingViewResolverList) {
            cachingViewResolver.clearCache();
        }
    }

    /**
     * @param cachingViewResolver
     */
    public void registerCachingViewResolverList(AbstractCachingViewResolver cachingViewResolver) {
        cachingViewResolverList.add(cachingViewResolver);
    }
}
