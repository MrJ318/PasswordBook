package com.jevon.passwordbook.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/8 12:17
 */
public class AesEncryptionUtils {

    /**
     * 生成AES密钥
     *
     * @param password 指定随机源的种子
     * @return 字符串密钥
     */
    public static String createKey(String password) {
        try {
            //指定加密算法的名称为AES,
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            //初始化密钥生成器，指定密钥的长度(单位:bit),
            //SecureRandom是生成安全随机数序列
            SecureRandom secureRandom = new SecureRandom(password.getBytes());
            keyGenerator.init(128, secureRandom);
            //生成原始对称密钥
            SecretKey secretKey = keyGenerator.generateKey();
            //返回编码格式的密钥
            byte[] enCodeFormat = secretKey.getEncoded();
            //将编码格式的密钥进行Base64编码
            return Base64.encodeToString(enCodeFormat, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 明文加密
     *
     * @param strKey  string密钥
     * @param content 加密前的原内容
     * @return 密文
     */
    public static String encrypt(String strKey, String content) {
        try {
            //生成密钥
            SecretKeySpec key = new SecretKeySpec(Base64.decode(strKey, Base64.DEFAULT), "AES");
            //根据指定算法生成密码器
            @SuppressLint("GetInstance") Cipher ciper = Cipher.getInstance("AES");
            //初始化密码器，
            // 第一个参数为密码的操作模式：加密(ENCRYPT_MODE),解密(DECRYPT_MODE)
            //第二个参数为AES密钥
            ciper.init(Cipher.ENCRYPT_MODE, key);
            //获取加密内容的字节数组
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            byte[] aesBytes = ciper.doFinal(contentBytes);
            //为了避免解密时数据丢失，将加密后的内容进行Base64编码后再返回
            //将加密后的密文转为字符串返回
            return Base64.encodeToString(aesBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 密文解密
     *
     * @param strKey  string密钥
     * @param content 加密后的内容
     * @return 明文
     */
    public static String decrypt(String strKey, String content) {
        try {
            //生成密钥
            SecretKeySpec key = new SecretKeySpec(Base64.decode(strKey, Base64.DEFAULT), "AES");
            //根据指定算法生成密码器
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES");
            //初始化密码器，
            // 第一个参数为密码的操作模式：加密(ENCRYPT_MODE),解密(DECRYPT_MODE)
            //第二个参数为AES密钥
            cipher.init(Cipher.DECRYPT_MODE, key);
            //先将密文进行Base64解码
            byte[] aesBytes = Base64.decode(content, Base64.DEFAULT);
            //将密文进行解密
            byte[] contentBytes = cipher.doFinal(aesBytes);
            //将解密后的内容转成字符串并返回
            return new String(contentBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
