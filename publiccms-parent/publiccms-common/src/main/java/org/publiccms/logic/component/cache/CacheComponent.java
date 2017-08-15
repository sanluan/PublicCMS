package org.publiccms.logic.component.cache;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.publiccms.common.api.Cache;
import org.publiccms.logic.service.tools.HqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 *
 * CacheComponent
 * 
 */
@Component
public class CacheComponent {
    @Autowired
    private List<Cache> cacheableList;
    private List<AbstractCachingViewResolver> cachingViewResolverList = new ArrayList<>();

    @Autowired
    private HqlService hqlService;

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
