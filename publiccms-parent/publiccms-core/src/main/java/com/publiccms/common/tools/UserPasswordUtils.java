package com.publiccms.common.tools;

import com.publiccms.common.constants.Constants;

/**
 * 用户密码工具类
 * 
 * UserPasswordUtils
 *
 */
public class UserPasswordUtils {
    private static final int SALT_LENGTH = 32;
    public static final int PASSWORD_MAX_LENGTH = 256;
    public static final String ENCODE_SHA512 = "sha512";
    public static final String SALT_SPLIT = ".";
    private static final String SALT_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

    public static String passwordEncode(String password, String salt, String encodePassword, String encode) {
        if (null == salt && null != encodePassword && encodePassword.contains(SALT_SPLIT)) {
            salt = encodePassword.substring(0, encodePassword.indexOf(SALT_SPLIT));
        }
        if (null == salt || salt.length() != SALT_LENGTH) {
            salt = getSalt();
        }
        if (ENCODE_SHA512.equalsIgnoreCase(encode)) {
            return CommonUtils.joinString(salt, SALT_SPLIT, VerificationUtils.sha512Encode(CommonUtils.joinString(password, salt)));
        } else {
            return CommonUtils.joinString(salt, SALT_SPLIT,
                    VerificationUtils.sha512Encode(CommonUtils.joinString(VerificationUtils.sha512Encode(password), salt)));
        }
    }

    public static boolean needUpdate(String encodePassword) {
        return null == encodePassword || !encodePassword.contains(SALT_SPLIT)
                || encodePassword.indexOf(SALT_SPLIT) != SALT_LENGTH;
    }

    public static String getSalt() {
        StringBuilder salt = new StringBuilder(SALT_LENGTH);
        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = Constants.random.nextInt(SALT_CHARACTERS.length());
            salt.append(SALT_CHARACTERS.charAt(index));
        }
        return salt.toString();
    }
}
