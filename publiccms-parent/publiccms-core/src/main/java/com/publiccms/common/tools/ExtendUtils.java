package com.publiccms.common.tools;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.sys.SysExtendField;

/**
 *
 * ExtendUtils
 * 
 */
public class ExtendUtils {

    /**
     * @param data
     * @return extent map
     */
    public static Map<String, String> getExtendMap(String data) {
        if (CommonUtils.notEmpty(data)) {
            try {
                return CommonConstants.objectMapper.readValue(data, CommonConstants.objectMapper.getTypeFactory()
                        .constructMapLikeType(LinkedHashMap.class, String.class, String.class));
            } catch (IOException | ClassCastException e) {
                return new LinkedHashMap<>();
            }
        }
        return new LinkedHashMap<>();

    }

    /**
     * @param map
     * @param extendFieldListArrays
     * @return extend string
     */
    @SafeVarargs
    public static String getExtendString(Map<String, String> map, List<SysExtendField>... extendFieldListArrays) {
        if (null == map) {
            return null;
        } else {
            if (CommonUtils.notEmpty(extendFieldListArrays)) {
                for (List<SysExtendField> extendFieldList : extendFieldListArrays) {
                    if (CommonUtils.notEmpty(extendFieldList)) {
                        for (SysExtendField extend : extendFieldList) {
                            String value = map.get(extend.getId().getCode());
                            if (null == value && null != extend.getDefaultValue()) {
                                map.put(extend.getId().getCode(), value);
                            }
                        }
                    }
                }
            }
            try {
                return CommonConstants.objectMapper.writeValueAsString(map);
            } catch (IOException e) {
                return null;
            }
        }
    }
}