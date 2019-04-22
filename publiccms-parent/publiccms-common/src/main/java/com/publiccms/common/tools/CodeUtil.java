package com.publiccms.common.tools;

import java.util.Calendar;
import java.util.Random;

/**
 *  
 *
 * @author : suyuyuan
 * @version 1.0 
 * @date ：2016年6月3日 下午4:50:42 
 */
public class CodeUtil {

    public static String passWord(String id) {
        String result = "";
        int[] b = new int[id.length()];
        for (int i = 0; i < id.length(); i++) {
            char q = id.charAt(i);
            b[i] = (int) (q - '0');// 字符数字-字符0就是实际的数字值，赋值给数字数组
        }
        int[] w = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        int[] c = { 1, 0, 'x', 9, 8, 7, 6, 5, 4, 3, 2 };
        int sum = 0;
        for (int j = 0; j < id.length(); j++) {
            sum = sum + b[j] * w[j];
        }
        int r = sum % 11;
        int res = c[r];
        // if (res == 120) {
        // result = "x";
        // } else {
        // result = String.valueOf(res);
        // }
        if (res == 120) {
            result = "9";
        } else {
            result = String.valueOf(res);
        }
        return result;
    }

    public static String createRandom(int ws) {
        String cs = "0";
        for (int i = 0; i < ws - 1; i++) {
            cs = cs + "0";
        }
        cs = "1" + cs;
        int stochastic = new Double(Math.random() * Integer.valueOf(cs)).intValue();
        String temp = "%0" + ws + "d";
        String stocha = String.format(temp, stochastic);
        return stocha;
    }

    public static String createCode() {

        StringBuffer sBuffer = new StringBuffer();
        //时分秒
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int mm = cal.get(Calendar.MILLISECOND);

        sBuffer.append(hour).append(minute).append(second).append(mm);

        // 随机数7位
        String random = createRandom(7);
        // System.out.println(random);
        sBuffer.append(random);

        // 校验码
        String check = CodeUtil.passWord(random);
        // System.out.println(check);
        sBuffer.append(check);

        String str = sBuffer.toString();
        return str;

    }

    public static String createRandomChar() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 8; i++) {
            int choice = 97; // 65大写
            code += (char) (choice + random.nextInt(26));
        }
        return code;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            String str = CodeUtil.createCode();
            System.out.println(str);
            System.out.println(str.length());

            System.out.println(CodeUtil.createRandomChar());

        }

    }

}