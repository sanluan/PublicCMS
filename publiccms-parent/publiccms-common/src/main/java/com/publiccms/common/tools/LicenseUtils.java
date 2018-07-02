package com.publiccms.common.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.copyright.License;

public class LicenseUtils {
    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";

    public static String writeLicense(License license) {
        try {
            StringWriter sw = new StringWriter();
            BufferedWriter bw = new BufferedWriter(sw);
            for (Entry<String, String> entry : license.entrySet()) {
                bw.append(entry.getKey()).append("=").append(entry.getValue());
                bw.newLine();
            }
            bw.close();
            return sw.getBuffer().toString();
        } catch (IOException e) {
        }
        return null;
    }

    public static License readLicense(String licenseText) {
        License license = new License();
        if (null != licenseText) {
            try {
                BufferedReader br = new BufferedReader(new StringReader(licenseText));
                String temp = null;
                while (null != (temp = br.readLine())) {
                    String[] values = StringUtils.split(temp, "=", 2);
                    if (values.length == 2) {
                        license.put(values[0], values[1]);
                    }
                }
                br.close();
            } catch (IOException e) {
            }
        }
        return license;
    }

    public static License readLicense(byte[] licenseData) {
        License license = new License();
        if (null != licenseData) {
            String licenseText = new String(licenseData, Constants.DEFAULT_CHARSET);
            String[] licenseItem = StringUtils.split(licenseText, ";");
            for (String item : licenseItem) {
                String[] values = StringUtils.split(item, "=", 2);
                if (values.length == 2) {
                    license.put(values[0], values[1]);
                }
            }
        }
        return license;
    }

    public static boolean verifyLicense(String publicKey, License license) {
        if (null != license && VerificationUtils.publicKeyVerify(VerificationUtils.base64Decode(publicKey),
                getLicenseDate(license), VerificationUtils.base64Decode(license.getSignaturer()))) {
            return verifyLicenseDate(license);
        }
        return false;
    }

    public static boolean verifyLicenseDate(License license) {
        if (null != license && null != license.getStartDate() && null != license.getEndDate()) {
            Date now = new Date();
            try {
                if (now.after(
                        DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING).parse(license.getStartDate()))
                        && (DateFormatUtils.SHORT_DATE_LENGTH != license.getEndDate().length() || now
                                .before(DateUtils.addDays(DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING)
                                        .parse(license.getEndDate()), 1)))) {
                    return true;
                }
            } catch (ParseException e) {
            }
        }
        return false;
    }

    public static String generateSignaturer(byte[] privateKey, License license) {
        return VerificationUtils.base64Encode(VerificationUtils.privateKeySign(privateKey, getLicenseDate(license)));
    }

    public static byte[] getLicenseDate(License license) {
        StringBuilder sb = new StringBuilder();
        if (null != license) {
            List<String> list = new ArrayList<>(license.keySet());
            Collections.sort(list);
            for (String key : list) {
                if (!License.KEY_SIGNATURER.equals(key)) {
                    sb.append(key).append("=").append(license.get(key)).append(";");
                }
            }
        }
        return sb.toString().getBytes(Constants.DEFAULT_CHARSET);
    }
}