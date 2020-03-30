package com.publiccms.common.copyright;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.publiccms.common.constants.CommonConstants;
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
    public boolean verify(String licenseFilePath) {
        return LicenseUtils.verifyLicense(CommonConstants.PUBLIC_KEY, getLicense(licenseFilePath));
    }

    @Override
    public boolean verify(String licenseFilePath, String domain) {
        License l = getLicense(licenseFilePath);
        return LicenseUtils.verifyLicense(CommonConstants.PUBLIC_KEY, l) && verifyDomain(domain, l.getDomain());
    }

    @Override
    public License getLicense(String licenseFilePath) {
        if (null != licenseFilePath) {
            File licenseFile = new File(licenseFilePath);
            if (null == license || lastModify != licenseFile.lastModified()) {
                try {
                    String licenseText = FileUtils.readFileToString(licenseFile, CommonConstants.DEFAULT_CHARSET_NAME);
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
                || domain.toLowerCase().contains(".dev.") || "localhost".equals(domain)) {
            return true;
        } else {
            String[] licenseDomains = StringUtils.split(licenseDomain, CommonConstants.COMMA_DELIMITED);
            int index;
            while (0 < (index = domain.indexOf(CommonConstants.DOT))) {
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
