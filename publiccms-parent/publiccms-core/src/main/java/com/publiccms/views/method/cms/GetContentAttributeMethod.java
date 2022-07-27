package com.publiccms.views.method.cms;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.service.cms.CmsContentAttributeService;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetContentAttributeMethod
 * 
 */
@Component
public class GetContentAttributeMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (CommonUtils.notEmpty(id)) {
            CmsContentAttribute entity = service.getEntity(id);
            if (null != entity) {
                Map<String, String> map = ExtendUtils.getExtendMap(entity.getData());
                map.put("text", entity.getText());
                map.put("source", entity.getSource());
                map.put("sourceUrl", entity.getSourceUrl());
                map.put("wordCount", String.valueOf(entity.getWordCount()));
                map.put("minPrice", String.valueOf(entity.getMinPrice()));
                map.put("maxPrice", String.valueOf(entity.getMaxPrice()));
                return map;
            }
        }
        return null;
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Resource
    private CmsContentAttributeService service;
    
}
