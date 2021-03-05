package com.micro.lcl.system.utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardWatchEventKinds;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1014:08
 */
public class PasswordUtil {
    /**
     * 定义迭代次数为1000次
     */
    private static final int ITERATIONCOUNT = 1000;

    /**
     * 定义使用的算法为:PBEWITHMD5andDES算法
     */
    public static final String ALGORITHM = "PBEWithMD5AndDES";//加密算法
    public static final String Salt = "63293188";//密钥

    /**
     *  加密明文字符串
     * @param plainText 待加密的明文字符串
     * @param password  生成密钥是使用的密码
     * @param salt  盐值
     * @return
     */
    public static String encrypt(String plainText, String password, String salt) {
        Key key = getPBEKey(password);
        byte[] encipheredData = null;
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt.getBytes(), ITERATIONCOUNT);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,key,parameterSpec);
            encipheredData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteToHexString(encipheredData);
    }

    /**
     *  将字节数组转换为十六进制字符串
     * @param src 字节数组
     * @return
     */
    private static String byteToHexString(byte[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
//            System.out.println("b: " + b + ", v: " + v + ", hv: " + hv);
            if (hv.length() < 2) {
                sb.append(0);
            }
            sb.append(hv);
//            System.out.println("sb: "+sb);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String encrypt = encrypt("admin", "123456", "RCGTeGiH");
        System.out.println(encrypt);
    }

    /**
     * 根据PBE密码生成一把密钥
     * @param password  生成密钥时所使用的密码
     * @return
     */
    private static Key getPBEKey(String password) {
        SecretKey secretKey = null;
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            secretKey = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    public static String MD5Encode(String origin, String charsetname) {
        String result = new String(origin);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                result = byteArrayToHexString(md.digest(origin.getBytes()));
            }else {
                result = byteArrayToHexString(md.digest(origin.getBytes(charsetname)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
}
