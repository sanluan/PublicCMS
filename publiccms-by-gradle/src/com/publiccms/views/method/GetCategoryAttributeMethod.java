package com.publiccms.views.method;

import static com.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetCategoryAttributeMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Integer id = getInteger(0, arguments);
        if (notEmpty(id)) {
            CmsCategoryAttribute entity = service.getEntity(id);
            if (notEmpty(entity)) {
                Map<String, String> map = getExtendMap(entity.getData());
                map.put("title", entity.getTitle());
                map.put("keywords", entity.getKeywords());
                map.put("description", entity.getDescription());
                return map;
            }
        }
        return null;
    }

    @Autowired
    private CmsCategoryAttributeService service;
}
