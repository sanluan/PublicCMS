package com.publiccms.common.tools;

import java.util.regex.Pattern;

/**
 * 用户密码工具类
 * 
 * UserPasswordUtils
 *
 */
public class UserPasswordUtils {
    private static final int SALT_LENGTH = 10;
    private static final int WEAK_PASSWORD_LENGTH = 5;
    private static final Pattern WEAK_PASSWORD_PATTERN = Pattern.compile("\\d*|[a-z]*|[A-Z]*");

    public static String passwordEncode(String password, String salt) {
        if (null != salt && SALT_LENGTH == salt.length()) {
            return VerificationUtils.sha512Encode(VerificationUtils.sha512Encode(password) + salt);
        } else {
            return VerificationUtils.md5Encode(password);
        }
    }

    public static boolean needUpdate(String salt) {
        return null == salt || SALT_LENGTH != salt.length();
    }

    public static boolean isWeek(String username, String password) {
        if (null != password) {
            return WEAK_PASSWORD_LENGTH > password.length() || password.equalsIgnoreCase(username)
                    || WEAK_PASSWORD_PATTERN.matcher(password).matches();
        }
        return true;
    }

    public static String getSalt() {
        return VerificationUtils.getRandomNumber(SALT_LENGTH);
    }
}
