package com.publiccms.views.method.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.home.HomeArticleContent;
import com.publiccms.logic.service.home.HomeArticleContentService;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetHomeArticleContentMethod extends BaseMethod {
    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (notEmpty(id)) {
            HomeArticleContent entity = service.getEntity(id);
            if(null!=entity){
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
    private HomeArticleContentService service;
}
