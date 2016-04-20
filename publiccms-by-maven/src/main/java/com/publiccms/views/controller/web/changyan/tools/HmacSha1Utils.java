package com.publiccms.views.controller.web.changyan.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.sanluan.common.base.Base;

/**
 * HMACSHA1签名算法
 */
public class HmacSha1Utils extends Base {
    private static final String HMACSHA1 = "HMACSHA1";
    private static Mac mac;
    static {
        try {
            mac = Mac.getInstance(HMACSHA1);
        } catch (NoSuchAlgorithmException e) {
            mac = null;
        }
    }

    /**
     * 计算HMACSHA1签名
     * 
     * @param input
     * @param secretKey
     * @return
     */
    private static String hmacSha1(String input, String secretKey) {
        try {
            if (notEmpty(mac)) {
                Key key = new SecretKeySpec(URLEncoder.encode(secretKey, DEFAULT_CHARSET).getBytes(DEFAULT_CHARSET), HMACSHA1);
                mac.init(key);
                byte[] hashValue = mac.doFinal(input.getBytes(DEFAULT_CHARSET));
                return new String(Base64.encodeBase64(hashValue));
            }
        } catch (InvalidKeyException | UnsupportedEncodingException e) {
            return BLANK;
        }
        return BLANK;
    }

    /**
     * @param appKey
     * @param imgUrl
     * @param nickname
     * @param profileUrl
     * @param userId
     * @return
     */
    public static String getSign(String appKey, String nickname, String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("user_id=" + userId);
        sb.append("&");
        sb.append("nickname=" + nickname);
        return hmacSha1(sb.toString(), appKey);
    }
}
