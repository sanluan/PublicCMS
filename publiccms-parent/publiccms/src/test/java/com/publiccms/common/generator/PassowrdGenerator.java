package com.publiccms.common.generator;

import java.util.Scanner;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.VerificationUtils;

public class PassowrdGenerator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Please input a command:[1 to encrypt password,2 to decrypt password,other to exit]");
            switch (sc.nextLine()) {
            case "1":
                System.out.println("Please input your password:");
                String password = sc.nextLine(); // 这里输入加密前的密码
                System.out.println(
                        VerificationUtils.base64Encode(VerificationUtils.encrypt(password, CommonConstants.ENCRYPT_KEY)));
                break;
            case "2":
                System.out.println("Please input your encrypted password:");
                String encryptPassword = sc.nextLine(); // 这里输入加密后的密码
                System.out.println(
                        VerificationUtils.decrypt(VerificationUtils.base64Decode(encryptPassword), CommonConstants.ENCRYPT_KEY));
                break;
            default:
                sc.close();
                System.exit(0);
            }
        }
    }
}