package com.publiccms.common.tools;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.publiccms.common.base.Base;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.views.pojo.entities.ExtendData;
import com.publiccms.views.pojo.entities.ExtendField;

/**
 *
 * ExtendUtils
 * 
 */
public class ExtendUtils implements Base {

    /**
     * @param extendDataList
     * @param sysExtendFieldList
     * @return extent data map
     */
    public static Map<String, String> getSysExtentDataMap(List<ExtendData> extendDataList,
            List<SysExtendField> sysExtendFieldList) {
        Map<String, String> map = new LinkedHashMap<>();
        if (CommonUtils.notEmpty(extendDataList)) {
            Map<String, String> extendFieldMap = new LinkedHashMap<>();
            for (ExtendData extend : extendDataList) {
                extendFieldMap.put(extend.getName(), extend.getValue());
            }
            for (SysExtendField extend : sysExtendFieldList) {
                String value = extendFieldMap.get(extend.getId().getCode());
                if (null == value) {
                    map.put(extend.getId().getCode(), extend.getDefaultValue());
                } else {
                    map.put(extend.getId().getCode(), value);
                }
            }
        }
        return map;
    }

    /**
     * @param extendDataList
     * @param extendFieldList
     * @return extent data map
     */
    public static Map<String, String> getExtentDataMap(List<ExtendData> extendDataList, List<ExtendField> extendFieldList) {
        Map<String, String> map = new LinkedHashMap<>();
        if (CommonUtils.notEmpty(extendDataList)) {
            Map<String, String> extendFieldMap = new LinkedHashMap<>();
            for (ExtendData extend : extendDataList) {
                extendFieldMap.put(extend.getName(), extend.getValue());
            }
            for (ExtendField extend : extendFieldList) {
                String value = extendFieldMap.get(extend.getId().getCode());
                if (null == value) {
                    map.put(extend.getId().getCode(), extend.getDefaultValue());
                } else {
                    map.put(extend.getId().getCode(), value);
                }
            }
        }
        return map;
    }

    /**
     * @param data
     * @return extent map
     */
    public static Map<String, String> getExtendMap(String data) {
        if (CommonUtils.notEmpty(data)) {
            try {
                return objectMapper.readValue(data, new TypeReference<Map<String, String>>() {

                });
            } catch (IOException | ClassCastException e) {
                return new LinkedHashMap<>();
            }
        }
        return new LinkedHashMap<>();

    }

    /**
     * @param map
     * @return extend string
     */
    public static String getExtendString(Map<String, String> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            return null;
        }
    }
}