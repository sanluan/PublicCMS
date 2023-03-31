package com.publiccms.common.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.ArrayUtils;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.Constants;
import com.publiccms.entities.sys.SysExtendField;

/**
 *
 * ExtendUtils
 * 
 */
public class ExtendUtils {

    private ExtendUtils() {
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