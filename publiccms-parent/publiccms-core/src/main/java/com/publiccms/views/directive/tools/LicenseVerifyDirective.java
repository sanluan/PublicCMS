package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
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
        String signaturer = handler.getString("signaturer");
        handler.put("result", false);
        if (CommonUtils.notEmpty(signaturer) && CommonUtils.notEmpty(licenseData)) {
            handler.put("result", VerificationUtils.publicKeyVerify(VerificationUtils.base64Decode(CommonConstants.PUBLIC_KEY),
                    VerificationUtils.base64Decode(licenseData), VerificationUtils.base64Decode(signaturer)));
        }
        handler.render();
    }

}
