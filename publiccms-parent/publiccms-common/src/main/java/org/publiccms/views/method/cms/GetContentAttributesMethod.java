package org.publiccms.views.method.cms;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.entities.cms.CmsContentAttribute;
import org.publiccms.logic.service.cms.CmsContentAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

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
        if (notEmpty(ids)) {
            Map<String, Map<String, String>> resultMap = new HashMap<>();
            for (CmsContentAttribute entity : service.getEntitysWithoutText(ids)) {
                Map<String, String> map = getExtendMap(entity.getData());
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
        return true;
    }

    @Override
    public int minParamtersNumber() {
        return 1;
    }

    @Autowired
    private CmsContentAttributeService service;
}
