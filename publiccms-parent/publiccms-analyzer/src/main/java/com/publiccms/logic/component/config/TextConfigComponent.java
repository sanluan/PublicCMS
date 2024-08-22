package com.publiccms.logic.component.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.typography.font.sfntly.Font;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FontUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;

import jakarta.annotation.Resource;

/**
 *
 * TextConfigComponent 文本配置组件
 *
 */
@Component
public class TextConfigComponent implements Config, SiteCache {

    /**
     * config code
     */
    public static final String CONFIG_CODE = ContentConfigComponent.CONFIG_CODE;

    /**
     * config description code
     */
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);
    /**
     * confuse font
     */
    public static final String CONFIG_CONFUSE_FONT = "confuse.font";

    private CacheEntity<Short, Font> cache;
    @Resource
    protected ConfigDataComponent configDataComponent;
    @Resource
    protected SiteComponent siteComponent;

    /**
     * @param siteId
     * @param showAll
     * @return config code or null
     */
    @Override
    public String getCode(short siteId, boolean showAll) {
        return CONFIG_CODE;
    }

    /**
     * @param locale
     * @return
     */
    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, "page.content");
    }

    /**
     * @param siteId
     * @param config
     * @return font
     */
    private Font getFont(short siteId, String fontfile) {
        Font font = cache.get(siteId);
        if (null == font) {
            synchronized (cache) {
                font = cache.get(siteId);
                if (null == font && CommonUtils.notEmpty(fontfile)) {
                    String fontfilePath = siteComponent.getPrivateFilePath(siteId, fontfile);
                    if (CmsFileUtils.exists(fontfilePath)) {
                        try {
                            font = FontUtils.loadFont(new File(fontfilePath));
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
        return font;

    }

    public Font getFont(short siteId) {
        Map<String, String> config = configDataComponent.getConfigData(siteId, CONFIG_CODE);
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_CONFUSE_FONT))) {
            return getFont(siteId, config.get(CONFIG_CONFUSE_FONT));
        }
        return null;
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_CONFUSE_FONT, INPUTTYPE_PRIVATEFILE, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_CONFUSE_FONT)), null,
                null));
        return extendFieldList;
    }

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear(false);
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
        cache = cacheEntityFactory.createCacheEntity(CONFIG_CODE, CacheEntityFactory.MEMORY_CACHE_ENTITY);
    }

    @Override
    public boolean exportable() {
        return true;
    }
}
