package com.publiccms.views.method.cms;

import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetPlaceAttributeMethod
 * 
 */
@Component
public class GetPlaceAttributeMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (CommonUtils.notEmpty(id)) {
            CmsPlaceAttribute entity = service.getEntity(id);
            if (null != entity) {
                return ExtendUtils.getExtendMap(entity.getData());
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
    private CmsPlaceAttributeService service;
    
}
