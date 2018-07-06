package com.publiccms.views.method.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.service.cms.CmsContentAttributeService;

import freemarker.template.TemplateModelException;

/**
 *
 * GetContentAttributesMethod
 * 
 */
@Component
public class GetContentAttributesMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long[] ids = getLongArray(0, arguments);
        if (CommonUtils.notEmpty(ids)) {
            Map<String, Map<String, String>> resultMap = new HashMap<>();
            for (CmsContentAttribute entity : service.getEntitysWithoutText(ids)) {
                Map<String, String> map = ExtendUtils.getExtendMap(entity.getData());
                map.put("source", entity.getSource());
                map.put("sourceUrl", entity.getSourceUrl());
                map.put("wordCount", String.valueOf(entity.getWordCount()));
                resultMap.put(String.valueOf(entity.getContentId()), map);
            }
            return resultMap;
        }
        return null;
    }
    
    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Autowired
    private CmsContentAttributeService service;
}
