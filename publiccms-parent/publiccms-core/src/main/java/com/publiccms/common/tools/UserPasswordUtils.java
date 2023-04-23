package com.publiccms.common.tools;

/**
 * 用户密码工具类
 * 
 * UserPasswordUtils
 *
 */
public class UserPasswordUtils {
    private static final int SALT_LENGTH = 10;
    public static final int PASSWORD_MAX_LENGTH = 256;
    public static final String ENCODE_SHA512 = "sha512";
    public static final String SALT_SPLIT = ".";

    public static String passwordEncode(String password, String salt, String encodePassword, String encode) {
        if (null == salt && null != encodePassword && encodePassword.contains(SALT_SPLIT)) {
            salt = encodePassword.substring(0, encodePassword.indexOf(SALT_SPLIT));
        }
        if (null != salt && SALT_LENGTH == salt.length()) {
            if (ENCODE_SHA512.equalsIgnoreCase(encode)) {
                return salt + SALT_SPLIT + VerificationUtils.sha512Encode(password + salt);
            } else {
                return salt + SALT_SPLIT + VerificationUtils.sha512Encode(VerificationUtils.sha512Encode(password) + salt);
            }
        } else {
            return VerificationUtils.md5Encode(password);
        }
    }

    public static boolean needUpdate(String encodePassword) {
        return null == encodePassword || !encodePassword.contains(SALT_SPLIT)
                || encodePassword.indexOf(SALT_SPLIT) != SALT_LENGTH;
    }

    public static String getSalt() {
        return VerificationUtils.getRandomNumber(SALT_LENGTH);
    }
}
