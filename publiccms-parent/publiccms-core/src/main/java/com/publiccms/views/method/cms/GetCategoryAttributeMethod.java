package com.publiccms.views.method.cms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;

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
        if (CommonUtils.notEmpty(id)) {
            CmsCategoryAttribute entity = service.getEntity(id);
            if (null != entity) {
                Map<String, String> map = ExtendUtils.getExtendMap(entity.getData());
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
    public int minParametersNumber() {
        return 1;
    }

    @Autowired
    private CmsCategoryAttributeService service;

}
