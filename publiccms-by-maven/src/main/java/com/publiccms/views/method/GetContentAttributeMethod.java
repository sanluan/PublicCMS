package com.publiccms.views.method;

import static com.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetContentAttributeMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (notEmpty(id)) {
            CmsContentAttribute entity = service.getEntity(id);
            if (notEmpty(entity)) {
                Map<String, String> map = getExtendMap(entity.getData());
                map.put("text", entity.getText());
                map.put("source", entity.getSource());
                map.put("sourceUrl", entity.getSourceUrl());
                map.put("wordCount", String.valueOf(entity.getWordCount()));
                return map;
            }
        }
        return null;
    }

    @Autowired
    private CmsContentAttributeService service;
}
