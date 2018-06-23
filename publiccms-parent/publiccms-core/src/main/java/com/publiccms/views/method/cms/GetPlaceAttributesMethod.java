package com.publiccms.views.method.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;

import freemarker.template.TemplateModelException;

/**
 *
 * GetPlaceAttributesMethod
 * 
 */
@Component
public class GetPlaceAttributesMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long[] ids = getLongArray(0, arguments);
        if (CommonUtils.notEmpty(ids)) {
            Map<String, Map<String, String>> resultMap = new HashMap<>();
            for (CmsPlaceAttribute entity : service.getEntitys(ids)) {
                Map<String, String> map = ExtendUtils.getExtendMap(entity.getData());
                resultMap.put(String.valueOf(entity.getPlaceId()), map);
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
    public int minParametersNumber() {
        return 1;
    }

    @Autowired
    private CmsPlaceAttributeService service;
    
}
