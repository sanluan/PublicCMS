package com.publiccms.common.tools;

/**
 * 用户密码工具类
 * 
 * UserPasswordUtils
 *
 */
public class UserPasswordUtils {
    private static final int OLD_SHA512_SALT_LENGTH = 10;
    private static final int SALT_LENGTH = 20;
    public static final int PASSWORD_MAX_LENGTH = 256;
    public static final String ENCODE_SHA512 = "sha512";
    public static final String SALT_SPLIT = ".";
    private static final String SALT_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

    /**
     * @param encodePassword
     * @return
     */
    public static boolean needUpdate(String encodePassword) {
        return null == encodePassword || !encodePassword.contains(SALT_SPLIT)
                || encodePassword.indexOf(SALT_SPLIT) != SALT_LENGTH;
    }

    /**
     * @param password
     * @param encodePassword
     * @param passwordEncode
     * @return
     */
    public static String passwordEncode(String password, String encodePassword, String passwordEncode) {
        String salt = null;
        if (null != encodePassword) {
            if (encodePassword.contains(SALT_SPLIT)) {
                salt = encodePassword.substring(0, encodePassword.indexOf(SALT_SPLIT));
            }
        } else {
            salt = getSalt();
        }
        if (null != salt && (SALT_LENGTH == salt.length() || OLD_SHA512_SALT_LENGTH == salt.length())) {
            if (ENCODE_SHA512.equalsIgnoreCase(passwordEncode)) {
                return CommonUtils.joinString(salt, SALT_SPLIT,
                        VerificationUtils.sha512Encode(CommonUtils.joinString(password, salt)));
            } else {
                return CommonUtils.joinString(salt, SALT_SPLIT,
                        VerificationUtils.sha512Encode(CommonUtils.joinString(VerificationUtils.sha512Encode(password), salt)));
            }
        } else {
            return VerificationUtils.md5Encode(password);
        }
    }

    private static String getSalt() {
        return VerificationUtils.getRandomString(SALT_CHARACTERS, SALT_LENGTH);
    }
}
