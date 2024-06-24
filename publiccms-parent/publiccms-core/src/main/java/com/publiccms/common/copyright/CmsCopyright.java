package com.publiccms.common.copyright;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.IpUtils;
import com.publiccms.common.tools.LicenseUtils;

/**
 *
 * CmsCopyright
 *
 */
public class CmsCopyright implements Copyright {
    private long lastModify = 0L;
    private License license;

    @Override
    public boolean verify(License license) {
        return LicenseUtils.verifyLicense(CommonConstants.PUBLIC_KEY, license);
    }

    @Override
    public boolean verify(License license, String domain) {
        return LicenseUtils.verifyLicense(CommonConstants.PUBLIC_KEY, license) && verifyDomain(domain, license.getDomain());
    }

    @Override
    public License getLicense(String licenseFilePath) {
        if (null != licenseFilePath) {
            File licenseFile = new File(licenseFilePath);
            if (null == license || lastModify != licenseFile.lastModified()) {
                try {
                    String licenseText = FileUtils.readFileToString(licenseFile, Constants.DEFAULT_CHARSET_NAME);
                    license = LicenseUtils.readLicense(licenseText);
                    lastModify = licenseFile.lastModified();
                } catch (IOException e) {
                }
            }
        }
        return license;
    }

    private static boolean verifyDomain(String domain, String licenseDomain) {
        if ("*".equals(licenseDomain) || IpUtils.isIp(domain) || domain.toLowerCase().startsWith("dev.")
                || domain.toLowerCase().contains(".dev.") || domain.toLowerCase().startsWith("test.")
                || domain.toLowerCase().contains(".test.") || "localhost".equals(domain)) {
            return true;
        } else {
            String[] licenseDomains = StringUtils.split(licenseDomain, Constants.COMMA);
            int index;
            while (0 < (index = domain.indexOf(Constants.DOT))) {
                if (ArrayUtils.contains(licenseDomains, domain)) {
                    return true;
                } else {
                    domain = domain.substring(index + 1);
                }
            }
        }
        return false;
    }
}
