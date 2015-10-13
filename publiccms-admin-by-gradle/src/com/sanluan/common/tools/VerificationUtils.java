package com.sanluan.common.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 
 * VerificationUtils 安全验证工具类
 *
 */
public class VerificationUtils {
    /**
     * @param length
     * @return
     */
    public static String getRandomNumber(int length) {
        String result = "";
        Random r = new Random();
        while (0 < length) {
            length -= 1;
            result += r.nextInt(9);
        }
        return result;
    }

    /**
     * @param input
     * @return
     */
    public static String encode(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());
            input = typeToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
        }
        return input;
    }

    /**
     * @param buffer
     * @return
     */
    private static String typeToHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }
}