package com.publiccms.logic.component;

import static com.sanluan.common.tools.RequestUtils.getValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.logging.LogFactory.getLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsExtend;
import com.publiccms.logic.service.cms.CmsExtendService;

@Component
public class ExtendComponent {
    public static final int ITEM_TYPE_MODEL = 1, ITEM_TYPE_CATEGORY = 2, EXTEND_TYPE_CONTENT = 1, EXTEND_TYPE_CATEGORY = 2;
    private static final String PARAMETER_PREFIX = "ex_";
    private final Log log = getLog(getClass());

    @Autowired
    private CmsExtendService extendService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public String dealCategoryExtent(Integer categoryId, Map<String, String[]> parameterMap) {
        if (null != categoryId) {
            List<CmsExtend> modelExtendList = (List<CmsExtend>) extendService.getPage(ITEM_TYPE_CATEGORY, categoryId,
                    EXTEND_TYPE_CATEGORY, true, null, null).getList();
            Map<String, String> map = dealExtent(modelExtendList, parameterMap);
            try {
                return objectMapper.writeValueAsString(map);
            } catch (JsonProcessingException e) {
                log.debug(e.getMessage());
            }
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getContentExtent(CmsContentAttribute cmsContentAttribute) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (null != cmsContentAttribute) {
            if (null != cmsContentAttribute.getText()) {
                try {
                    return (List<Map<String, String>>) objectMapper.readValue(cmsContentAttribute.getText(), List.class);
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            }
        }
        return list;
    }

    public String contentImages(CmsContentAttribute cmsContentAttribute, Map<String, String[]> parameterMap) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (null != cmsContentAttribute) {
            try {
                Set<String> parameterSet = parameterMap.keySet();
                for (String paramterName : parameterSet) {
                    if (paramterName.startsWith("images_") && paramterName.endsWith("_image")
                            && isNotBlank(getValue(parameterMap, paramterName))) {
                        String pre = paramterName.substring(0, paramterName.length() - 6);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("image", getValue(parameterMap, paramterName));
                        map.put("description", getValue(parameterMap, pre + "_description"));
                        list.add(map);
                    }
                }
                return objectMapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                log.debug(e.getMessage());
            }
        }
        return "[]";
    }

    public String contentExtent(CmsContentAttribute cmsContentAttribute, Map<String, String[]> parameterMap) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (null != cmsContentAttribute) {
            try {
                Set<String> parameterSet = parameterMap.keySet();
                for (String paramterName : parameterSet) {
                    if (paramterName.startsWith("ex_c_") && paramterName.endsWith("_name")
                            && isNotBlank(getValue(parameterMap, paramterName))) {
                        String pre = paramterName.substring(0, paramterName.length() - 5);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", getValue(parameterMap, paramterName));
                        map.put("text", getValue(parameterMap, pre + "_text"));
                        list.add(map);
                    }
                }
                return objectMapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                log.debug(e.getMessage());
            }
        }
        return "[]";
    }

    @SuppressWarnings("unchecked")
    public String dealExtent(int extendType, Integer categoryId, Integer modelId, Map<String, String[]> parameterMap) {
        if (null != categoryId && null != modelId) {
            List<CmsExtend> modelExtendList = (List<CmsExtend>) extendService.getPage(ITEM_TYPE_MODEL, modelId, extendType, true,
                    null, null).getList();
            Map<String, String> map = dealExtent(modelExtendList, parameterMap);
            List<CmsExtend> categoryExtendList = (List<CmsExtend>) extendService.getPage(ITEM_TYPE_CATEGORY, categoryId,
                    extendType, true, null, null).getList();
            map.putAll(dealExtent(categoryExtendList, parameterMap));
            try {
                return objectMapper.writeValueAsString(map);
            } catch (JsonProcessingException e) {
                log.debug(e.getMessage());
            }
        }
        return "";
    }

    public void saveExtend(int itemType, Integer itemId, List<String> systemExtendList, Map<String, String[]> parameterMap) {
        Set<String> parameterSet = parameterMap.keySet();
        for (String paramterName : parameterSet) {
            if (paramterName.startsWith("ex_new_") && paramterName.endsWith("_code")
                    && isNotBlank(getValue(parameterMap, paramterName))) {
                String pre = paramterName.substring(0, paramterName.length() - 5);
                CmsExtend extend = new CmsExtend();
                extend.setItemType(itemType);
                extend.setItemId(itemId);
                if (0 < paramterName.indexOf("_c_"))
                    extend.setExtendType(EXTEND_TYPE_CONTENT);
                else
                    extend.setExtendType(EXTEND_TYPE_CATEGORY);
                extend.setName(getValue(parameterMap, pre + "_name"));
                extend.setCode(getValue(parameterMap, paramterName));
                extend.setInputType(getValue(parameterMap, pre + "_inputType"));
                extend.setDefaultValue(getValue(parameterMap, pre + "_defaultValue"));
                extend.setDescription(getValue(parameterMap, pre + "_description"));
                if (!systemExtendList.contains(extend.getCode())) {
                    extend.setIsCustom(true);
                }
                if (isNotBlank(getValue(parameterMap, pre + "_required"))) {
                    extend.setIsRequired(true);
                }
                extendService.save(extend);
            }
        }
    }

    public void updateExtend(int itemType, Integer itemId, int extendType, List<String> systemExtendList,
            Map<String, String[]> parameterMap) {
        if (null != itemId) {
            @SuppressWarnings("unchecked")
            List<CmsExtend> extendList = (List<CmsExtend>) extendService.getPage(itemType, itemId, extendType, null, null, null)
                    .getList();
            for (CmsExtend extend : extendList) {
                String pre;
                if (EXTEND_TYPE_CONTENT == extendType) {
                    pre = "ex_c_" + extend.getId();
                } else {
                    pre = "ex_ca_" + extend.getId();
                }
                String code = getValue(parameterMap, pre + "_code");
                if (isNotBlank(code)) {
                    extend.setName(getValue(parameterMap, pre + "_name"));
                    extend.setCode(code);
                    extend.setInputType(getValue(parameterMap, pre + "_inputType"));
                    extend.setDefaultValue(getValue(parameterMap, pre + "_defaultValue"));
                    extend.setDescription(getValue(parameterMap, pre + "_description"));
                    if (!systemExtendList.contains(extend.getCode())) {
                        extend.setIsCustom(true);
                    } else {
                        extend.setIsCustom(false);
                    }
                    if (isNotBlank(getValue(parameterMap, pre + "_required"))) {
                        extend.setIsRequired(true);
                    } else {
                        extend.setIsRequired(false);
                    }
                    extendService.update(extend.getId(), extend, new String[] { "id", "itemType", "itemId", "extendType" });
                } else {
                    extendService.delete(extend.getId());
                }
            }
        }
    }

    private Map<String, String> dealExtent(List<CmsExtend> extendList, Map<String, String[]> parameterMap) {
        Map<String, String> map = new HashMap<String, String>();
        for (CmsExtend extend : extendList) {
            String value = getValue(parameterMap, PARAMETER_PREFIX + extend.getCode());
            if (null != value) {
                map.put(extend.getCode(), value);
            }
        }
        return map;
    }
}
