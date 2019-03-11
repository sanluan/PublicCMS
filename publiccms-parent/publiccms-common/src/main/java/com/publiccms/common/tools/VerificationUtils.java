package com.publiccms.common.tools;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.publiccms.common.constants.Constants;

/**
 * 安全验证工具类
 *
 * VerificationUtils
 *
 */
public class VerificationUtils {

    /**
     * @param text
     * @param length
     * @return random string
     */
    public static String getRandomString(String text, int length) {
        StringBuilder sb = new StringBuilder();
        while (0 < length) {
            length -= 1;
            sb.append(text.charAt(Constants.random.nextInt(text.length())));
        }
        return sb.toString();
    }

    /**
     * @param length
     * @return random number
     */
    public static String getRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        while (0 < length) {
            length -= 1;
            sb.append(Constants.random.nextInt(9));
        }
        return sb.toString();
    }

    /**
     * 生成公钥私钥对
     *
     * @param keySize
     * @param secrand
     * @return key pair
     */
    public static KeyPair generateKeyPair(int keySize, SecureRandom secrand) {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            if (null == secrand) {
                keygen.initialize(keySize);
            } else {
                keygen.initialize(keySize, secrand);
            }
            return keygen.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 获取公钥对象
     *
     * @param publicKey
     * @return public key
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKey(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    /**
     * 获取私钥对象
     *
     * @param privateKey
     * @return private key
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getPrivateKey(byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec privateKeyPKCS8 = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        return keyf.generatePrivate(privateKeyPKCS8);
    }

    /**
     * 公钥验证签名
     *
     * @param publicKey
     * @param data
     * @param sign
     * @return whether public key verify is passed
     */
    public static boolean publicKeyVerify(byte[] publicKey, byte[] data, byte[] sign) {
        try {
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(getPublicKey(publicKey));
            signature.update(data);
            return signature.verify(sign);
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 私钥签名
     *
     * @param privateKey
     * @param data
     * @return private key sign
     */
    public static byte[] privateKeySign(byte[] privateKey, byte[] data) {
        try {
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(getPrivateKey(privateKey));
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param publicKey
     * @param input
     * @return public key encode result
     */
    public static byte[] publicKeyEncode(byte[] publicKey, byte[] input) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            return cipher.doFinal(input);
        } catch (Exception e) {
            return input;
        }
    }

    /**
     * 公钥解密
     *
     * @param publicKey
     * @param input
     * @return public key decode result
     */
    public static byte[] publicKeyDecode(byte[] publicKey, byte[] input) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, getPublicKey(publicKey));
            return cipher.doFinal(input);
        } catch (Exception e) {
            return input;
        }
    }

    /**
     * 私钥加密
     *
     * @param privateKey
     * @param input
     * @return private key decode result
     */
    public static byte[] privateKeyDecode(byte[] privateKey, byte[] input) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(privateKey));
            return cipher.doFinal(input);
        } catch (Exception e) {
            return input;
        }
    }

    /**
     * 私钥解密
     *
     * @param privateKey
     * @param input
     * @return private key encode result
     */
    public static byte[] privateKeyEncode(byte[] privateKey, byte[] input) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            return cipher.doFinal(input);
        } catch (Exception e) {
            return input;
        }
    }

    /**
     * md5加密
     *
     * @param input
     * @return md5 encode result
     */
    public static String md5Encode(String input) {
        return encode(input, "MD5");
    }

    /**
     * sha1加密
     *
     * @param input
     * @return sha1 encode result
     */
    public static String sha1Encode(String input) {
        return encode(input, "SHA-1");
    }

    /**
     * base64加密
     *
     * @param input
     * @return base64 encode result
     */
    public static String base64Encode(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    private static String encode(String input, String type) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(input.getBytes());
            return bypeToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return input;
        }
    }

    /**
     * base64解密
     *
     * @param input
     * @return base64 decode result
     */
    public static byte[] base64Decode(String input) {
        return Base64.decodeBase64(input);
    }

    /**
     * sha512加密
     *
     * @param input
     * @return sha2 encode result
     */
    public static String sha512Encode(String input) {
        return encode(input, "SHA-512");
    }
    
    /**
     * sha256加密
     *
     * @param input
     * @return sha2 encode result
     */
    public static String sha256Encode(String input) {
        return encode(input, "SHA-256");
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
            byte[] sha1Key = sha1Encode(key).getBytes(Constants.DEFAULT_CHARSET);
            DESedeKeySpec dks = new DESedeKeySpec(sha1Key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(dks));
            return cipher.doFinal(input.getBytes(Constants.DEFAULT_CHARSET));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * 3-DES解密
     *
     * @param input
     * @param key
     * @return 3-DES decode result
     */
    public static String decrypt(byte[] input, String key) {
        try {
            byte[] sha1Key = sha1Encode(key).getBytes(Constants.DEFAULT_CHARSET);
            DESedeKeySpec dks = new DESedeKeySpec(sha1Key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey sKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.DECRYPT_MODE, sKey);
            byte ciphertext[] = cipher.doFinal(input);
            return new String(ciphertext, Constants.DEFAULT_CHARSET);
        } catch (Exception e) {
            return Constants.BLANK;
        }
    }

    /**
     * @param buffer
     * @return hex
     */
    public static String bypeToHex(byte buffer[]) {
        StringBuilder sb = new StringBuilder(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }
}