package com.publiccms.views.method.cms;

import static com.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetPlaceAttributesMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long[] ids = getLongArray(0, arguments);
        if (notEmpty(ids)) {
            Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
            for (CmsPlaceAttribute entity : service.getEntitys(ids)) {
                Map<String, String> map = getExtendMap(entity.getData());
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
    public int minParamtersNumber() {
        return 1;
    }

    @Autowired
    private CmsPlaceAttributeService service;
}
