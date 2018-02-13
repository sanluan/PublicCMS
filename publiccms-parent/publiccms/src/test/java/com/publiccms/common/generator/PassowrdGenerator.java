package com.publiccms.common.generator;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.VerificationUtils;

public class PassowrdGenerator {
    public static void main(String[] args) {
        String password = ""; // 这里输入加密前的密码
        System.out.println(VerificationUtils.base64Encode(VerificationUtils.encrypt(password, CommonConstants.ENCRYPT_KEY)));
        // String encryptPassword = ""; // 这里输入加密后的密码
        // System.out.println(VerificationUtils.decrypt(VerificationUtils.base64Decode(encryptPassword),
        // CommonConstants.ENCRYPT_KEY));
    }
}
