package org.publiccms.views.method.home;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.List;

import org.publiccms.entities.home.HomeGroupPostContent;
import org.publiccms.logic.service.home.HomeGroupPostContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

/**
 *
 * GetHomeGroupPostContentMethod
 * 
 */
@Component
public class GetHomeGroupPostContentMethod extends BaseMethod {
    
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (notEmpty(id)) {
            HomeGroupPostContent entity = service.getEntity(id);
            if (null != entity) {
                return entity.getContent();
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
    private HomeGroupPostContentService service;
    
}
