package com.publiccms.common.tools;

import java.security.GeneralSecurityException;
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
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.publiccms.common.constants.Constants;

/**
 * 安全验证工具类
 *
 * VerificationUtils
 *
 */
public class VerificationUtils {

    private VerificationUtils() {
    }

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
        } catch (GeneralSecurityException e) {
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
        } catch (GeneralSecurityException e) {
            return Constants.EMPTY_BYTE_ARRAY;
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
            return byteToHex(messageDigest.digest());
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

    private static byte[] iv = "1245656789012334".getBytes(Constants.DEFAULT_CHARSET);

    /**
     * AES加密
     *
     * @param input
     * @param key
     * @return AES encode result
     */
    public static byte[] encryptAES(String input, String key) {
        try {
            byte[] keybyte = CommonUtils.keep(key, 16, null).getBytes(Constants.DEFAULT_CHARSET);
            if (keybyte.length < 16) {
                int lenght = keybyte.length;
                keybyte = Arrays.copyOf(keybyte, 16);
                System.arraycopy(iv, 0, keybyte, lenght, 16 - lenght);
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(keybyte, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new GCMParameterSpec(128, iv));
            return cipher.doFinal(input.getBytes(Constants.DEFAULT_CHARSET));
        } catch (GeneralSecurityException e) {
            return Constants.EMPTY_BYTE_ARRAY;
        }
    }

    /**
     *
     * AES解密
     *
     * @param input
     * @param key
     * @return 3-DES decode result
     */
    public static String decryptAES(byte[] input, String key) {
        try {
            byte[] keybyte = CommonUtils.keep(key, 16, null).getBytes(Constants.DEFAULT_CHARSET);
            if (keybyte.length < 16) {
                int lenght = keybyte.length;
                keybyte = Arrays.copyOf(keybyte, 16);
                System.arraycopy(iv, 0, keybyte, lenght, 16 - lenght);
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(keybyte, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new GCMParameterSpec(128, iv));
            byte[] ciphertext = cipher.doFinal(input);
            return new String(ciphertext, Constants.DEFAULT_CHARSET);
        } catch (GeneralSecurityException e) {
            return Constants.BLANK;
        }
    }

    /**
     *
     * 3-DES解密
     *
     * @deprecated 
     * @param input
     * @param key
     * @return 3-DES decode result
     */
    @Deprecated
    public static String decrypt3DES(byte[] input, String key) {
        try {
            byte[] sha1Key = sha1Encode(key).getBytes(Constants.DEFAULT_CHARSET);
            DESedeKeySpec dks = new DESedeKeySpec(sha1Key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey sKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.DECRYPT_MODE, sKey);
            byte[] ciphertext = cipher.doFinal(input);
            return new String(ciphertext, Constants.DEFAULT_CHARSET);
        } catch (GeneralSecurityException e) {
            return Constants.BLANK;
        }
    }

    /**
     * @param buffer
     * @return hex
     */
    public static String byteToHex(byte[] buffer) {
        StringBuilder sb = new StringBuilder(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }
}
