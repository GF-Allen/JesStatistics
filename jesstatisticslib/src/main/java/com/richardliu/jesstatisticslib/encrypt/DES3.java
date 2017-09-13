package com.richardliu.jesstatisticslib.encrypt;


import com.richardliu.jesstatisticslib.Constant;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * SecretUtils {3DES加密解密的工具类 }
 */
public class DES3 {

    // 定义加密算法，有DES、DESede(即3DES)、Blowfish
    private static final String Algorithm = "DESede";

    /**
     * 加密方法
     *
     * @param src 源数据的字节数组
     * @return
     */
    public static byte[] encryptMode(byte[] src) {
        try {
            SecretKey deskey;
            deskey = new SecretKeySpec(build3DesKey(Constant.PASSWORD_CRYPT_KEY), Algorithm); // 生成密钥
            Cipher c1 = Cipher.getInstance(Algorithm); // 实例化负责加密/解密的Cipher工具类
            c1.init(Cipher.ENCRYPT_MODE, deskey); // 初始化为加密模式
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param src 密文的字节数组
     * @return
     */
    public static byte[] decryptMode(byte[] src) {
        try {
            SecretKey deskey;
            deskey = new SecretKeySpec(build3DesKey(Constant.PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey); // 初始化为解密模式
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            System.out.println("解密错误" + e3.getMessage());
            e3.printStackTrace();
        }
        return null;
    }

    /*
     * 根据字符串生成密钥字节数组
     *
     * @param keyStr 密钥字符串
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        String string = MD5.GetMD5Code(keyStr).substring(0, 24);
        return string.getBytes("utf-8");
    }
}