package org.publiccms.views.method.cms;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.util.List;

import org.publiccms.entities.cms.CmsPlaceAttribute;
import org.publiccms.logic.service.cms.CmsPlaceAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

/**
 *
 * GetPlaceAttributeMethod
 * 
 */
@Component
public class GetPlaceAttributeMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (notEmpty(id)) {
            CmsPlaceAttribute entity = service.getEntity(id);
            if (null != entity) {
                return getExtendMap(entity.getData());
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
    private CmsPlaceAttributeService service;
    
}
