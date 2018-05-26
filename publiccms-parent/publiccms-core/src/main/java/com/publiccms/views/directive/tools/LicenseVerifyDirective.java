package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.copyright.License;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LicenseUtils;
import com.publiccms.common.tools.VerificationUtils;

/**
 * 
 * VersionDirective 技术框架版本指令
 *
 */
@Component
public class LicenseVerifyDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String licenseData = handler.getString("licenseData");
        handler.put("result", false);
        if (CommonUtils.notEmpty(licenseData)) {
            License license = LicenseUtils.readLicense(VerificationUtils.base64Decode(licenseData));
            handler.put("result", LicenseUtils.verifyLicenseDate(license));
        }
        handler.render();
    }

}
