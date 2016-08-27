package com.publiccms.common.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.views.pojo.ExtendData;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

public class ExtendUtils extends Base {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> getSysExtentDataMap(List<ExtendData> extendDataList,
            List<SysExtendField> sysExtendFieldList) {
        Map<String, String> map = new HashMap<String, String>();
        if (notEmpty(extendDataList)) {
            Map<String, String> extendFieldMap = new HashMap<String, String>();
            for (ExtendData extend : extendDataList) {
                extendFieldMap.put(extend.getName(), extend.getValue());
            }
            for (SysExtendField extend : sysExtendFieldList) {
                String value = extendFieldMap.get(extend.getCode());
                if (notEmpty(value)) {
                    map.put(extend.getCode(), value);
                } else {
                    map.put(extend.getCode(), extend.getDefaultValue());
                }
            }
        }
        return map;
    }
    
    public static Map<String, String> getExtentDataMap(List<ExtendData> extendDataList,
            List<ExtendField> extendFieldList) {
        Map<String, String> map = new HashMap<String, String>();
        if (notEmpty(extendDataList)) {
            Map<String, String> extendFieldMap = new HashMap<String, String>();
            for (ExtendData extend : extendDataList) {
                extendFieldMap.put(extend.getName(), extend.getValue());
            }
            for (ExtendField extend : extendFieldList) {
                String value = extendFieldMap.get(extend.getCode());
                if (notEmpty(value)) {
                    map.put(extend.getCode(), value);
                } else {
                    map.put(extend.getCode(), extend.getDefaultValue());
                }
            }
        }
        return map;
    }

    public static Map<String, String> getExtendMap(String data) {
        if (notEmpty(data)) {
            try {
                return objectMapper.readValue(data, new TypeReference<Map<String, String>>() {
                });
            } catch (IOException | ClassCastException e) {
                return new HashMap<String, String>();
            }
        }
        return new HashMap<String, String>();

    }

    public static String getExtendString(Map<String, String> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            return null;
        }
    }
}