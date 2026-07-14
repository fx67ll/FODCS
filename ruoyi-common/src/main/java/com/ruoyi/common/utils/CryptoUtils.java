package com.ruoyi.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES-GCM 加解密工具类（服务端密钥持有，永不下发前端）
 *
 * 算法：AES/GCM/NoPadding，256 位密钥，随机 12 字节 IV（GCM 推荐 12 字节），IV 拼接在密文前。
 * 输出格式：Base64( IV(12B) || 密文 || GCM认证标签(16B) )。
 *
 * 密钥不在此类绑定，由调用方按场景传入：
 * - dbEncKey：加密数据库中存储的 secret_value
 * - appSecretKey：加密下发给前端的第三方凭据
 *
 * @author fx67ll
 */
public class CryptoUtils
{
    /** AES-GCM 算法 */
    private static final String ALGORITHM = "AES/GCM/NoPadding";

    /** GCM IV 长度（字节），GCM 规范推荐 12 字节 */
    private static final int IV_LENGTH = 12;

    /** GCM 认证标签长度（位），128 位 */
    private static final int TAG_LENGTH_BITS = 128;

    /** 密钥长度（字节），256 位 */
    private static final int KEY_LENGTH = 32;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 加密
     *
     * @param plainText 明文
     * @param key       Base64 编码的密钥（解码后须为 32 字节 / 256 位）
     * @return Base64 编码的密文（含前置 IV）
     */
    public static String encrypt(String plainText, String key)
    {
        if (plainText == null || key == null)
        {
            throw new IllegalArgumentException("明文与密钥均不可为空");
        }
        try
        {
            byte[] keyBytes = base64Decode(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            byte[] iv = new byte[IV_LENGTH];
            SECURE_RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            // IV 拼接在密文前，解密时取前 12 字节作 IV
            byte[] output = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, output, 0, iv.length);
            System.arraycopy(cipherText, 0, output, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(output);
        }
        catch (Exception e)
        {
            throw new RuntimeException("AES-GCM 加密失败", e);
        }
    }

    /**
     * 解密
     *
     * @param cipherTextBase64 Base64 编码的密文（含前置 IV）
     * @param key              Base64 编码的密钥（解码后须为 32 字节 / 256 位）
     * @return 明文
     */
    public static String decrypt(String cipherTextBase64, String key)
    {
        if (cipherTextBase64 == null || key == null)
        {
            throw new IllegalArgumentException("密文与密钥均不可为空");
        }
        try
        {
            byte[] keyBytes = base64Decode(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            byte[] input = base64Decode(cipherTextBase64);
            byte[] iv = new byte[IV_LENGTH];
            byte[] cipherText = new byte[input.length - IV_LENGTH];
            System.arraycopy(input, 0, iv, 0, IV_LENGTH);
            System.arraycopy(input, IV_LENGTH, cipherText, 0, cipherText.length);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            throw new RuntimeException("AES-GCM 解密失败", e);
        }
    }

    /**
     * 生成新的 256 位密钥（Base64 编码）
     *
     * 用于首次生成 dbEncKey / appSecretKey，生成后写入 application.yml。
     *
     * @return Base64 编码的密钥
     */
    public static String generateKey()
    {
        byte[] key = new byte[KEY_LENGTH];
        SECURE_RANDOM.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * 校验密钥是否为合法的 256 位 AES 密钥（Base64 解码后 32 字节）
     *
     * @param key Base64 编码的密钥
     * @return true 合法
     */
    public static boolean isValidKey(String key)
    {
        if (key == null)
        {
            return false;
        }
        try
        {
            byte[] keyBytes = base64Decode(key);
            return keyBytes.length == KEY_LENGTH;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private static byte[] base64Decode(String base64)
    {
        return Base64.getDecoder().decode(base64);
    }
}
