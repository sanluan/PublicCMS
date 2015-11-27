package com.publiccms.views.method;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.component.FileComponent;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetTemplateMetadataMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String path = getString(0, arguments);
        if (isNotEmpty(path)) {
            return fileComponent.getTemplateMetadata(path);
        }
        return null;
    }

    @Autowired
    private FileComponent fileComponent;
}
