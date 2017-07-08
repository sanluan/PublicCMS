package org.publiccms.views.directive.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.publiccms.logic.component.template.TemplateComponent.INCLUDE_DIRECTORY;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.template.MetadataComponent;
import org.publiccms.logic.component.template.TemplateComponent;
import org.publiccms.views.pojo.CmsPlaceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

import freemarker.template.TemplateException;

/**
 *
 * IncludePlaceDirective
 * 
 */
@Component
public class IncludePlaceDirective extends AbstractTemplateDirective {
    
    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        String[] paths = handler.getStringArray("paths");
        if (notEmpty(path)) {
            SysSite site = getSite(handler);
            CmsPlaceMetadata metadata = metadataComponent
                    .getPlaceMetadata(siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + path));
            if (site.isUseSsi()) {
                handler.print(new StringBuilder("<!--#include virtual=\"/").append(INCLUDE_DIRECTORY).append(path).append("\"-->")
                        .toString());
            } else {
                templateComponent.printPlace(handler.getWriter(), site, path, metadata);
            }
        } else if (notEmpty(paths)) {
            SysSite site = getSite(handler);
            Map<String, String> map = new ConcurrentHashMap<>();
            if (site.isUseSsi()) {
                for (String p : paths) {
                    map.put(p, new StringBuilder("<!--#include virtual=\"/").append(INCLUDE_DIRECTORY).append(p).append("\"-->")
                            .toString());
                }
                handler.put("map", map).render();
            } else {
                Set<String> set = new HashSet<String>();
                for (String p : paths) {
                    set.add(p);
                }
                for (String p : set) {
                    pool.execute(new RanderTask(p, set.size(), map, getSite(handler), handler));
                }
                handler.setRenderd();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private MetadataComponent metadataComponent;

    /**
     * 
     * RanderTask 渲染线程
     *
     */
    class RanderTask implements Runnable {
        private String path;
        private int count;
        private Map<String, String> resultMap;
        private SysSite site;
        private RenderHandler handler;

        public RanderTask(String path, int count, Map<String, String> resultMap, SysSite site, RenderHandler handler) {
            this.path = path;
            this.count = count;
            this.resultMap = resultMap;
            this.site = site;
            this.handler = handler;
        }

        @Override
        public void run() {
            CmsPlaceMetadata metadata = metadataComponent
                    .getPlaceMetadata(siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + path));
            try {
                resultMap.put(path, templateComponent.printPlace(site, path, metadata));
            } catch (IOException | TemplateException e) {
                log.error(e);
                resultMap.put(path, e.getMessage());
            }
            synchronized (this) {
                if (resultMap.size() >= count) {
                    try {
                        handler.put("map", resultMap).render();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
    }
}
