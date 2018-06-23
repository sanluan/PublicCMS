package com.publiccms.views.method.tools;

import java.util.List;

import com.publiccms.common.constants.CmsVersion;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

/**
 *
 * GetLicenseMethod
 * 
 */
@Component
public class GetLicenseMethod extends BaseMethod {

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        return CmsVersion.getLicense();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }
}
