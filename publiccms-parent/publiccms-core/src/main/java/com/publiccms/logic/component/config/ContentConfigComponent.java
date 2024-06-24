package com.publiccms.logic.component.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;

import jakarta.annotation.Resource;

/**
 *
 * ContentConfigComponent 内容配置组件
 *
 */
@Component
public class ContentConfigComponent implements SiteCache, Config {

    /**
     * config code
     */
    public static final String CONFIG_CODE = "content";
    /**
     * keywords
     */
    public static final String CONFIG_KEYWORDS = "keywords";
    /**
     * open in window
     */
    public static final String CONFIG_NEWWINDOW = "inwindow";
    /**
     * keywords
     */
    public static final String CONFIG_MAX_COUNT = "max_count";
    /**
     * keywords
     */
    public static final int DEFAULT_MAX_COUNT = 10;

    private CacheEntity<Short, KeywordsConfig> cache;

    @Resource
    protected ConfigDataComponent configDataComponent;

    /**
     * @param siteId
     * @return keywords config
     */
    public KeywordsConfig getKeywordsConfig(short siteId) {
        KeywordsConfig keywordsConfig = cache.get(siteId);
        if (null == keywordsConfig) {
            synchronized (cache) {
                keywordsConfig = cache.get(siteId);
                if (null == keywordsConfig) {
                    keywordsConfig = new KeywordsConfig();
                    Map<String, String> config = configDataComponent.getConfigData(siteId, CONFIG_CODE);
                    String value = config.get(CONFIG_KEYWORDS);
                    boolean blank = ConfigDataComponent.getBoolean(config.get(CONFIG_NEWWINDOW), true);
                    int max = ConfigDataComponent.getInt(config.get(CONFIG_MAX_COUNT), DEFAULT_MAX_COUNT);
                    if (CommonUtils.notEmpty(value)) {
                        String[] values = StringUtils.splitPreserveAllTokens(value, Constants.COMMA);
                        if (CommonUtils.notEmpty(values) && 0 == values.length % 2) {
                            int i = 0;
                            int j = 0;
                            String[] words = new String[values.length / 2];
                            String[] wordWithUrls = new String[values.length / 2];
                            for (String v : values) {
                                if (i++ % 2 == 0) {
                                    words[j] = v;
                                } else {
                                    try {
                                        URI url = new URI(v);
                                        if (blank) {
                                            wordWithUrls[j] = CommonUtils.joinString("<a href=\"", url.toString(),
                                                    "\" target=\"_blank\">", words[j], "</a>");
                                        } else {
                                            wordWithUrls[j] = CommonUtils.joinString("<a href=\"", url.toString(), "\">",
                                                    words[j], "</a>");
                                        }
                                    } catch (URISyntaxException e) {
                                        words[j] = null;
                                    }
                                    j++;
                                }
                            }
                            keywordsConfig.setMax(max);
                            keywordsConfig.setWords(words);
                            keywordsConfig.setWordWithUrls(wordWithUrls);
                        }
                    }
                    cache.put(siteId, keywordsConfig);
                }
            }
        }
        return keywordsConfig;
    }

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

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(
                new SysExtendField(CONFIG_KEYWORDS, INPUTTYPE_KEYWORDS, true, getMessage(locale, "page.keywords"), null, null));
        extendFieldList.add(new SysExtendField(CONFIG_NEWWINDOW, INPUTTYPE_BOOLEAN, true,
                getMessage(locale, "page.open_in_new_window"), null, Boolean.TRUE.toString()));
        extendFieldList.add(new SysExtendField(CONFIG_MAX_COUNT, INPUTTYPE_NUMBER, true, getMessage(locale, "page.total"), null,
                String.valueOf(DEFAULT_MAX_COUNT)));
        return extendFieldList;
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
        cache = cacheEntityFactory.createCacheEntity(CONFIG_KEYWORDS);
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear(false);
    }

    public static class KeywordsConfig {
        int max;
        String[] words;
        String[] wordWithUrls;

        /**
         * @return the max
         */
        public int getMax() {
            return max;
        }

        /**
         * @param max
         *            the max to set
         */
        public void setMax(int max) {
            this.max = max;
        }

        /**
         * @return the words
         */
        public String[] getWords() {
            return words;
        }

        /**
         * @param words
         *            the words to set
         */
        public void setWords(String[] words) {
            this.words = words;
        }

        /**
         * @return the wordWithUrls
         */
        public String[] getWordWithUrls() {
            return wordWithUrls;
        }

        /**
         * @param wordWithUrls
         *            the wordWithUrls to set
         */
        public void setWordWithUrls(String[] wordWithUrls) {
            this.wordWithUrls = wordWithUrls;
        }

    }
}
