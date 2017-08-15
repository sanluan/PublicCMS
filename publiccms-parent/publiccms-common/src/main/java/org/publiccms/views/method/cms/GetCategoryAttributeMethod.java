package org.publiccms.views.method.cms;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.util.List;
import java.util.Map;

import org.publiccms.entities.cms.CmsCategoryAttribute;
import org.publiccms.logic.service.cms.CmsCategoryAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

/**
 *
 * GetCategoryAttributeMethod
 * 
 */
@Component
public class GetCategoryAttributeMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Integer id = getInteger(0, arguments);
        if (notEmpty(id)) {
            CmsCategoryAttribute entity = service.getEntity(id);
            if (null != entity) {
                Map<String, String> map = getExtendMap(entity.getData());
                map.put("title", entity.getTitle());
                map.put("keywords", entity.getKeywords());
                map.put("description", entity.getDescription());
                return map;
            }
        }
        return null;
    }
    
    @Override
    public boolean needAppToken() {
        return false;
    }
    
    @Override
    public int minParamtersNumber() {
        return 1;
    }

    @Autowired
    private CmsCategoryAttributeService service;

}
