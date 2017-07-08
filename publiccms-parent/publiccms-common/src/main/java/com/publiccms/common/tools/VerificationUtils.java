package com.publiccms.common.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.publiccms.common.base.Base;

/**
 * 安全验证工具类
 * 
 * VerificationUtils
 *
 */
public class VerificationUtils implements Base {

    /**
     * @param text
     * @param length
     * @return
     */
    public static String getRandomString(String text, int length) {
        StringBuilder sb = new StringBuilder();
        while (0 < length) {
            length -= 1;
            sb.append(text.charAt(r.nextInt(text.length())));
        }
        return sb.toString();
    }

    /**
     * @param length
     * @return
     */
    public static String getRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        while (0 < length) {
            length -= 1;
            sb.append(r.nextInt(9));
        }
        return sb.toString();
    }

    /**
     * @param input
     * @return
     */
    public static String encode(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());
            return typeToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return input;
        }
    }

    /**
     * @param buffer
     * @return
     */
    public static String typeToHex(byte buffer[]) {
        StringBuilder sb = new StringBuilder(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }
}