package com.sz.blockchain.util;

import com.sz.blockchain.app.Wallet;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CryptoUtils {

    public static String getSHA256(String content){
        byte[] sha256 = getSHA256(content.getBytes(StandardCharsets.UTF_8));
        return byte2Hex(sha256);
    }

    public static byte[] getSHA256(byte[] bytes){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

    public static String getTwiceSHA256(String content){
        String sha256 = getSHA256(content);
        return getSHA256(sha256);
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString();
    }


    /**
     * RIPEMD(RACE Intergrity Primitives Evaluation Message Digest):即RACE原始完整性校验消息摘要,是比利时鲁汶大学COSIC研究小组开发的Hash函数算法.
     * RIPEMD使用MD4的设计原理,并针对MD4的算法缺陷进行改进,1996年首次发布RIPEMD-128版本,它在性能上与SHA-1相类似.
     * @param publicKey
     * @return
     */
    public static byte[] ripeMD160Hash(byte[] publicKey) {
        byte[] sha256 = CryptoUtils.getSHA256(publicKey);
        RIPEMD160Digest ripemd160 = new RIPEMD160Digest();
        ripemd160.update(sha256, 0, sha256.length);
        byte[] output = new byte[ripemd160.getDigestSize()];
        ripemd160.doFinal(output, 0);
        return output;
    }

    public static byte[] checkSum(byte[] bytes){
        byte[] twiceSHA256 = getSHA256(getSHA256(bytes));
        return Arrays.copyOfRange(twiceSHA256, 0, Wallet.ADDRESS_CHECKSUM_LENGTH);
    }
}
