package org.publiccms.views.method.home;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.entities.home.HomeGroupPostContent;
import org.publiccms.logic.service.home.HomeGroupPostContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

/**
 *
 * GetHomeGroupPostContentsMethod
 * 
 */
@Component
public class GetHomeGroupPostContentsMethod extends BaseMethod {
    
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long[] ids = getLongArray(0, arguments);
        if (notEmpty(ids)) {
            Map<String, String> resultMap = new HashMap<>();
            for (HomeGroupPostContent entity : service.getEntitys(ids)) {
                resultMap.put(String.valueOf(entity.getPostId()), entity.getContent());
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
    private HomeGroupPostContentService service;
    
}
