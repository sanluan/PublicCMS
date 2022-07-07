package com.publiccms.common.tools;

/**
 * 用户密码工具类
 * 
 * UserPasswordUtils
 *
 */
public class UserPasswordUtils {
    private static final int SALT_LENGTH = 10;
    public static final String ENCODE_SHA512 = "sha512";

    public static String passwordEncode(String password, String salt, String encode) {
        if (null != salt && SALT_LENGTH == salt.length()) {
            if (ENCODE_SHA512.equalsIgnoreCase(encode)) {
                return VerificationUtils.sha512Encode(password + salt);
            } else {
                return VerificationUtils.sha512Encode(VerificationUtils.sha512Encode(password) + salt);
            }
        } else {
            return VerificationUtils.md5Encode(password);
        }
    }

    public static boolean needUpdate(String salt) {
        return null == salt || SALT_LENGTH != salt.length();
    }

    public static String getSalt() {
        return VerificationUtils.getRandomNumber(SALT_LENGTH);
    }
}
