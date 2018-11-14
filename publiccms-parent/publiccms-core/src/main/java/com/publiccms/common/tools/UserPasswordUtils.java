package com.publiccms.common.tools;

/**
 * 用户密码工具类
 * 
 * UserPasswordUtils
 *
 */
public class UserPasswordUtils {
    private static final int SALT_LENGTH = 10;

    public static String passwordEncode(String password, String salt) {
        if (null != salt && SALT_LENGTH == salt.length()) {
            return VerificationUtils.sha2Encode(VerificationUtils.sha2Encode(password) + salt);
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
