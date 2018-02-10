package com.publiccms.views.method.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.home.HomeGroupPostContent;
import com.publiccms.logic.service.home.HomeGroupPostContentService;

import freemarker.template.TemplateModelException;

/**
 *
 * GetGroupPostContentMethod
 * 
 */
@Component
public class GetGroupPostContentMethod extends BaseMethod {
    
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (CommonUtils.notEmpty(id)) {
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
