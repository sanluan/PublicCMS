package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.tools.LicenseUtils;
import com.publiccms.common.tools.VerificationUtils;

import freemarker.template.TemplateModelException;

/**
 *
 * GetLicenseDateMethod
 * 
 */
@Component
public class GetLicenseDataMethod extends BaseMethod {

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        return VerificationUtils.base64Encode(LicenseUtils.getLicenseDate(CmsVersion.getLicense()));
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
