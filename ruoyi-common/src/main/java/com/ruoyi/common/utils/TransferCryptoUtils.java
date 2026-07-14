package com.ruoyi.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 前后端传输加解密工具类（AES-CBC，阶段四·4.17）
 *
 * 与数据库加密工具 CryptoUtils（AES-GCM）刻意采用不同算法，做算法隔离：
 * 即便 transferKey 泄露，攻击者拿到的是 CBC 密文，而数据库存的是 GCM 密文，互解不开；反之亦然。
 *
 * 算法：AES/CBC/PKCS5Padding，随机 16 字节 IV 拼接在密文前，输出 Base64( IV(16B) || 密文 )。
 *
 * transferKey 由后端按需签发（5 分钟短时效），前端存内存，仅用于管理端秘钥查看/修改的传输加密，
 * 绝不用于数据库加密，绝不与 dbEncKey 混用。
 *
 * @author fx67ll
 */
public class TransferCryptoUtils
{
    /** AES-CBC 算法 */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /** CBC IV 长度（字节），固定 16 */
    private static final int IV_LENGTH = 16;

    /** 密钥长度（字节），256 位 */
    private static final int KEY_LENGTH = 32;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 加密
     *
     * @param plainText   明文
     * @param transferKey Base64 编码的传输密钥（解码后须为 32 字节 / 256 位）
     * @return Base64 编码的密文（含前置 IV）
     */
    public static String encrypt(String plainText, String transferKey)
    {
        if (plainText == null || transferKey == null)
        {
            throw new IllegalArgumentException("明文与密钥均不可为空");
        }
        try
        {
            byte[] keyBytes = Base64.getDecoder().decode(transferKey);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            byte[] iv = new byte[IV_LENGTH];
            SECURE_RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            // IV 拼接在密文前
            byte[] output = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, output, 0, iv.length);
            System.arraycopy(cipherText, 0, output, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(output);
        }
        catch (Exception e)
        {
            throw new RuntimeException("AES-CBC 传输加密失败", e);
        }
    }

    /**
     * 解密
     *
     * @param cipherTextBase64 Base64 编码的密文（含前置 IV）
     * @param transferKey      Base64 编码的传输密钥
     * @return 明文
     */
    public static String decrypt(String cipherTextBase64, String transferKey)
    {
        if (cipherTextBase64 == null || transferKey == null)
        {
            throw new IllegalArgumentException("密文与密钥均不可为空");
        }
        try
        {
            byte[] keyBytes = Base64.getDecoder().decode(transferKey);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            byte[] input = Base64.getDecoder().decode(cipherTextBase64);
            byte[] iv = new byte[IV_LENGTH];
            byte[] cipherText = new byte[input.length - IV_LENGTH];
            System.arraycopy(input, 0, iv, 0, IV_LENGTH);
            System.arraycopy(input, IV_LENGTH, cipherText, 0, cipherText.length);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            throw new RuntimeException("AES-CBC 传输解密失败", e);
        }
    }

    /**
     * 生成新的 256 位传输密钥（Base64 编码）
     *
     * @return Base64 编码的密钥
     */
    public static String generateKey()
    {
        byte[] key = new byte[KEY_LENGTH];
        SECURE_RANDOM.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
