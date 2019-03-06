package com.publiccms.common.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.views.pojo.entities.ExtendData;

/**
 *
 * ExtendUtils
 * 
 */
public class ExtendUtils {

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
                String value = extendFieldMap.getOrDefault(extend.getId().getCode(), extend.getDefaultValue());
                if (null != value) {
                    map.put(extend.getId().getCode(), value);
                }
            }
        }
        return map;
    }

    /**
     * @param extendData
     * @param extendFieldList
     * @return extent data map
     */
    public static List<ExtendData> getDefaultExtentDataList(Map<String, String> extendData,
            List<SysExtendField> extendFieldList) {
        List<ExtendData> extendDataList = new ArrayList<>();
        if (CommonUtils.notEmpty(extendFieldList)) {
            for (SysExtendField extend : extendFieldList) {
                String value = extendData.getOrDefault(extend.getId().getCode(), extend.getDefaultValue());
                if (null != value) {
                    extendDataList.add(new ExtendData(extend.getId().getCode(), value));
                }
            }
        }
        return extendDataList;
    }

    /**
     * @param extendDataList
     * @param extendFieldList
     * @return extent data map
     */
    public static Map<String, String> getExtentDataMap(List<ExtendData> extendDataList, List<SysExtendField> extendFieldList) {
        Map<String, String> map = new LinkedHashMap<>();
        if (CommonUtils.notEmpty(extendDataList)) {
            Map<String, String> extendFieldMap = new LinkedHashMap<>();
            for (ExtendData extend : extendDataList) {
                extendFieldMap.put(extend.getName(), extend.getValue());
            }
            for (SysExtendField extend : extendFieldList) {
                String value = extendFieldMap.getOrDefault(extend.getId().getCode(), extend.getDefaultValue());
                if (null != value) {
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
     * @return extend string
     */
    public static String getExtendString(Map<String, String> map) {
        try {
            return CommonConstants.objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            return null;
        }
    }
}