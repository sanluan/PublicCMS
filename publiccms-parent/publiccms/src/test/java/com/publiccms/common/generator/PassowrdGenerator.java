package com.publiccms.common.generator;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.VerificationUtils;

public class PassowrdGenerator {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Please input a command:[1 to encrypt password,2 to decrypt password,3 to encrypt password in old version,other to exit]");
            switch (sc.nextLine()) {
            case "1":
                System.out.println("Please input your password:");
                String password = sc.nextLine(); // 这里输入加密前的密码
                System.out.println(
                        VerificationUtils.base64Encode(VerificationUtils.encryptAES(password, CommonConstants.ENCRYPT_KEY)));
                break;
            case "3":
                System.out.println("Please input your password:");
                String pass = sc.nextLine(); // 这里输入加密前的密码
                System.out.println(
                        VerificationUtils.base64Encode(encrypt(pass, CommonConstants.ENCRYPT_KEY)));
                break;
            case "2":
                System.out.println("Please input your encrypted password:");
                String encryptPassword = sc.nextLine(); // 这里输入加密后的密码
                String wd = VerificationUtils.decryptAES(VerificationUtils.base64Decode(encryptPassword),
                        CommonConstants.ENCRYPT_KEY);
                if (CommonUtils.empty(wd)) {
                    wd = VerificationUtils.decrypt3DES(VerificationUtils.base64Decode(encryptPassword), CommonConstants.ENCRYPT_KEY);
                }
                System.out.println(wd);
                break;
            default:
                sc.close();
                System.exit(0);
            }
        }
    }
    
    /**
     * 3-DES加密
     *
     * @param input
     * @param key
     * @return 3-DES encode result
     */

    public static byte[] encrypt(String input, String key) {
        try {
            byte[] sha1Key = VerificationUtils.sha1Encode(key).getBytes(StandardCharsets.UTF_8);
            DESedeKeySpec dks = new DESedeKeySpec(sha1Key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(dks));
            return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return null;
        }
    }
}