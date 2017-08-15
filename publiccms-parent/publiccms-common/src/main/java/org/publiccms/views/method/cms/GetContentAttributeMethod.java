package org.publiccms.views.method.cms;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.publiccms.common.tools.ExtendUtils.getExtendMap;

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
 * GetContentAttributeMethod
 * 
 */
@Component
public class GetContentAttributeMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (notEmpty(id)) {
            CmsContentAttribute entity = service.getEntity(id);
            if (null != entity) {
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
