package com.publiccms.common.generator;

import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.lang3.time.DateUtils;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.copyright.License;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.LicenseUtils;
import com.publiccms.common.tools.VerificationUtils;

/**
 *
 * LicenseGenerator
 * 
 */
public class LicenseGenerator {

    /**
     * @param arg
     * @throws Throwable
     */
    public static void main(String[] arg) throws Throwable {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input a password:");
        SecureRandom secrand = new SecureRandom();
        secrand.setSeed(VerificationUtils.sha512Encode(sc.nextLine()).getBytes(CommonConstants.DEFAULT_CHARSET)); // 初始化随机产生器
        KeyPair keyPair = VerificationUtils.generateKeyPair(1024, secrand);
        String publicKey = VerificationUtils.base64Encode(keyPair.getPublic().getEncoded());
        if (CommonConstants.PUBLIC_KEY.equals(publicKey)) {
            License license = new License();
            license.setVersion("1.0");
            license.setAuthorization("免费体验");
            license.setOrganization("所有用户");
            license.setIssue("天津黑核科技有限公司");
            license.setDomain("*");
            license.setStartDate(DateFormatUtils.getDateFormat(LicenseUtils.DATE_FORMAT_STRING).format(new Date()));
            license.setEndDate(
                    DateFormatUtils.getDateFormat(LicenseUtils.DATE_FORMAT_STRING).format(DateUtils.addMonths(new Date(), 3)));
            license.setSignaturer(LicenseUtils.generateSignaturer(keyPair.getPrivate().getEncoded(), license));
            String s2 = LicenseUtils.generateSignaturer(keyPair.getPrivate().getEncoded(), license);
            String licenseText = LicenseUtils.writeLicense(license);
            License l = LicenseUtils.readLicense(licenseText);
            if (license.getSignaturer().equals(s2) && LicenseUtils.verifyLicense(CommonConstants.PUBLIC_KEY, l)) {
                System.out.println("----------PublicCMS License-----------");
                System.out.print(licenseText);
                System.out.println("----------PublicCMS License-----------");
            }
        } else {
            System.out.println(publicKey);
        }
        sc.close();
    }
}
