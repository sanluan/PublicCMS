package org.publiccms.views.method.home;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.entities.home.HomeArticleContent;
import org.publiccms.logic.service.home.HomeArticleContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

/**
 *
 * GetHomeArticleContentsMethod
 * 
 */
@Component
public class GetHomeArticleContentsMethod extends BaseMethod {
    
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long[] ids = getLongArray(0, arguments);
        if (notEmpty(ids)) {
            Map<String, String> resultMap = new HashMap<>();
            for (HomeArticleContent entity : service.getEntitys(ids)) {
                resultMap.put(String.valueOf(entity.getArticleId()), entity.getContent());
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
    private HomeArticleContentService service;
    
}
