package com.publiccms.common.tools;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.time.Instant;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import software.amazon.awssdk.auth.signer.internal.SignerConstant;
import software.amazon.awssdk.auth.signer.internal.SigningAlgorithm;
import software.amazon.awssdk.utils.DateUtils;

public class OSSUtils {
    private OSSUtils() {
    }

    public static String generatePostPolicy(Instant expiration, PolicyConditions conds) {
        String formatedExpiration = DateUtils.formatIso8601Date(expiration);
        StringBuilder postPolicy = new StringBuilder();
        postPolicy.append("{").append("\"expiration\":\"").append(formatedExpiration).append("\",").append(conds.jsonize())
                .append("}");
        return postPolicy.toString();
    }

    public static byte[] computeSignature(String stringToSign, byte[] signingKey) throws InvalidKeyException {
        return sign(stringToSign.getBytes(StandardCharsets.UTF_8), signingKey, SigningAlgorithm.HmacSHA256);
    }

    public static byte[] newSigningKey(String secretAccessKey, String dateStamp, String regionName) throws InvalidKeyException {
        byte[] kSecret = CommonUtils.joinString("AWS4", secretAccessKey).getBytes(StandardCharsets.UTF_8);
        byte[] kDate = sign(dateStamp, kSecret, SigningAlgorithm.HmacSHA256);
        byte[] kRegion = sign(regionName, kDate, SigningAlgorithm.HmacSHA256);
        byte[] kService = sign("s3", kRegion, SigningAlgorithm.HmacSHA256);
        return sign(SignerConstant.AWS4_TERMINATOR, kService, SigningAlgorithm.HmacSHA256);
    }

    private static byte[] sign(String stringData, byte[] key, SigningAlgorithm algorithm) throws InvalidKeyException {
        byte[] data = stringData.getBytes(StandardCharsets.UTF_8);
        return sign(data, key, algorithm);
    }

    private static byte[] sign(byte[] data, byte[] key, SigningAlgorithm algorithm) throws InvalidKeyException {
        Mac mac = algorithm.getMac();
        mac.init(new SecretKeySpec(key, algorithm.toString()));
        return mac.doFinal(data);
    }
}
