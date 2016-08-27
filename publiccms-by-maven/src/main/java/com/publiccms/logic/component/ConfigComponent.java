package com.publiccms.logic.component;

import static com.publiccms.common.tools.ExtendUtils.getExtendMap;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Cacheable;
import com.publiccms.common.spi.Configable;
import com.publiccms.entities.sys.SysConfig;
import com.publiccms.logic.service.sys.SysConfigService;
import com.publiccms.views.pojo.ConfigItem;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

/**
 * 
 * ConfigComponent 文件操作组件
 *
 */
@Component
public class ConfigComponent extends Base implements Cacheable {
    private static List<String> cachedlist = synchronizedList(new ArrayList<String>());
    private static Map<String, Map<String, String>> cachedMap = synchronizedMap(new HashMap<String, Map<String, String>>());

    @Autowired
    private SysConfigService service;
    @Autowired
    private List<Configable> configableList;
    private Map<String, List<Configable>> configableMap;

    public Set<String> getConfigCodeList() {
        Set<String> configItemSet = new LinkedHashSet<String>();
        for (Configable configable : configableList) {
            configItemSet.add(configable.getCode());
        }
        return configItemSet;
    }

    public Map<String, List<Configable>> getConfigableMap() {
        if (empty(configableMap)) {
            configableMap = new HashMap<String, List<Configable>>();
            for (Configable configable : configableList) {
                List<Configable> configableList = configableMap.get(configable.getCode());
                if (empty(configableList)) {
                    configableList = new ArrayList<Configable>();
                }
                configableList.add(configable);
                configableMap.put(configable.getCode(), configableList);
            }
        }
        return configableMap;
    }

    public List<ConfigItem> getConfigItemList(int siteId, String code, Locale locale) {
        List<ConfigItem> configItemList = new ArrayList<ConfigItem>();
        List<Configable> configableList = getConfigableMap().get(code);
        if (notEmpty(configableList)) {
            for (Configable configable : configableList) {
                for (String subcode : configable.getSubcode(siteId)) {
                    ConfigItem configItem = new ConfigItem(subcode, configable.getSubcodeDescription(subcode, locale));
                    configItemList.add(configItem);
                }
            }
        }
        return configItemList;
    }

    public List<ExtendField> getExtendFieldList(int siteId, String code, String subcode, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        List<Configable> configableList = getConfigableMap().get(code);
        if (notEmpty(configableList)) {
            for (Configable configable : configableList) {
                if (configable.getSubcode(siteId).contains(subcode)) {
                    extendFieldList = configable.getExtendFieldList(subcode, locale);
                    break;
                }
            }
        }
        return extendFieldList;
    }

    public Map<String, String> getConfigData(int siteId, String code, String subCode) {
        String key = getKey(siteId, code, subCode);
        Map<String, String> map = cachedMap.get(key);
        if (empty(map)) {
            SysConfig entity = service.getEntity(siteId, code, subCode);
            if (notEmpty(entity) && notEmpty(entity.getData())) {
                map = getExtendMap(entity.getData());
            } else {
                map = new HashMap<String, String>();
            }
            clearCache(300);
            cachedlist.add(key);
            cachedMap.put(key, map);
        }
        return map;
    }

    public void removeCache(int siteId, String code, String subCode) {
        int index = cachedlist.indexOf(getKey(siteId, code, subCode));
        if (0 < index) {
            cachedMap.remove(cachedlist.remove(index));
        }
    }

    private void clearCache(int size) {
        if (size < cachedlist.size()) {
            for (int i = 0; i < size / 10; i++) {
                cachedMap.remove(cachedlist.remove(0));
            }
        }
    }

    private String getKey(int siteId, String code, String subCode) {
        return siteId + code + subCode;
    }

    @Override
    public void clear() {
    }
}
