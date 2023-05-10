package com.publiccms.common.tools;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.Constants;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.logic.component.config.ContentConfigComponent.KeywordsConfig;

/**
 *
 * ExtendUtils
 * 
 */
public class ExtendUtils {
    private ExtendUtils() {
    }

    public static final Pattern HTML_PATTERN = Pattern.compile(">([^<]+)</[^a|A]");

    /**
     * @param attribute
     * @return extent map
     */
    public static Map<String, String> getAttributeMap(CmsCategoryAttribute attribute) {
        if (null == attribute) {
            return Collections.emptyMap();
        } else {
            Map<String, String> map = getExtendMap(attribute.getData());
            map.put("title", attribute.getTitle());
            map.put("keywords", attribute.getKeywords());
            map.put("description", attribute.getDescription());
            return map;
        }
    }

    /**
     * @param attribute
     * @param keywordsConfig
     * @return extent map
     */
    public static Map<String, String> getAttributeMap(CmsContentAttribute attribute, KeywordsConfig keywordsConfig) {
        if (null == attribute) {
            return Collections.emptyMap();
        } else {
            Map<String, String> map = getExtendMap(attribute.getData());
            map.put("text", replaceText(attribute.getText(), keywordsConfig));
            map.put("source", attribute.getSource());
            map.put("sourceUrl", attribute.getSourceUrl());
            map.put("wordCount", String.valueOf(attribute.getWordCount()));
            map.put("minPrice", String.valueOf(attribute.getMinPrice()));
            map.put("maxPrice", String.valueOf(attribute.getMaxPrice()));
            return map;
        }
    }

    public static String replaceText(String html, KeywordsConfig keywordsConfig) {
        if (null != keywordsConfig && CommonUtils.notEmpty(html) && CommonUtils.notEmpty(keywordsConfig.getWords())) {
            Matcher matcher = HTML_PATTERN.matcher(html);
            StringBuilder sb = new StringBuilder();
            int end = 0;
            while (matcher.find()) {
                String temp = matcher.group();
                sb.append(html.substring(end, matcher.start())).append(">");
                sb.append(StringUtils.replaceEach(matcher.group(1), keywordsConfig.getWords(), keywordsConfig.getWordWithUrls()));
                sb.append(temp.substring(temp.length() - 3, temp.length()));
                end = matcher.end();
            }
            if (end < html.length()) {
                sb.append(html.substring(end, html.length()));
            }
            return sb.toString();
        } else {
            return html;
        }
    }

    /**
     * @param attribute
     * @return extent map
     */
    public static Map<String, String> getAttributeMap(CmsPlaceAttribute attribute) {
        if (null == attribute) {
            return Collections.emptyMap();
        } else {
            return getExtendMap(attribute.getData());
        }
    }

    /**
     * @param data
     * @return extent map
     */
    public static Map<String, String> getExtendMap(String data) {
        if (CommonUtils.notEmpty(data)) {
            try {
                return Constants.objectMapper.readValue(data, Constants.objectMapper.getTypeFactory()
                        .constructMapLikeType(LinkedHashMap.class, String.class, String.class));
            } catch (IOException | ClassCastException e) {
                return new LinkedHashMap<>();
            }
        }
        return new LinkedHashMap<>();
    }

    /**
     * @param map
     * @param sitePath
     * @param extendFieldListArrays
     * @return extend string
     */
    @SafeVarargs
    public static String getExtendString(Map<String, String> map, String sitePath,
            List<SysExtendField>... extendFieldListArrays) {
        return getExtendString(map, sitePath, null, extendFieldListArrays);
    }

    /**
     * @param map
     * @param sitePath
     * @param searchableConsumer
     * @param extendFieldListArrays
     * @return extend string
     */
    @SafeVarargs
    public static String getExtendString(Map<String, String> map, String sitePath,
            BiConsumer<SysExtendField, String> searchableConsumer, List<SysExtendField>... extendFieldListArrays) {
        if (CommonUtils.notEmpty(extendFieldListArrays) && null != map) {
            Set<String> notSafeKeys = new HashSet<>();
            notSafeKeys.addAll(map.keySet());
            for (List<SysExtendField> extendFieldList : extendFieldListArrays) {
                if (CommonUtils.notEmpty(extendFieldList)) {
                    for (SysExtendField extend : extendFieldList) {
                        notSafeKeys.remove(extend.getId().getCode());
                        String value = map.get(extend.getId().getCode());
                        if (null == value) {
                            if (null != extend.getDefaultValue()) {
                                map.put(extend.getId().getCode(), value);
                            }
                        } else if (null != extend.getMaxlength()) {
                            if (ArrayUtils.contains(Config.INPUT_TYPE_EDITORS, extend.getInputType())) {
                                map.put(extend.getId().getCode(),
                                        HtmlUtils.cleanUnsafeHtml(HtmlUtils.keep(value, extend.getMaxlength()), sitePath));
                            } else {
                                map.put(extend.getId().getCode(), CommonUtils.keep(value, extend.getMaxlength(), null));
                            }
                        } else {
                            if (ArrayUtils.contains(Config.INPUT_TYPE_EDITORS, extend.getInputType())) {
                                map.put(extend.getId().getCode(), HtmlUtils.cleanUnsafeHtml(value, sitePath));
                            }
                        }
                        if (extend.isSearchable() && null != value) {
                            searchableConsumer.accept(extend, value);
                        }
                    }
                }
            }
            if (!notSafeKeys.isEmpty()) {
                for (String key : notSafeKeys) {
                    map.remove(key);
                }
            }
            try {
                return Constants.objectMapper.writeValueAsString(map);
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }
}