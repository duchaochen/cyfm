package com.ppcxy.common.utils.security;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * 基础加密组件
 *
 * @version 1.0
 * @since 1.0
 */
public abstract class Coder {
    public static final String KEY_SHA = "SHA";
    
    
    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64ToStr(String key) throws Exception {
        return new String(Base64.getDecoder().decode(key), "UTF-8");
    }
    
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.getDecoder().decode(key);
    }
    
    /**
     * BASE64加密
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] bytes) throws Exception {
        return new String(Base64.getEncoder().encode(bytes));
    }
    
    public static String encryptStrToBASE64(String str) throws Exception {
        return new String(Base64.getEncoder().encode(str.getBytes("UTF-8")),"UTF-8");
    }
    
    public static String encryptMD5(String str) {
        return Md5Utils.hash(str);
    }
    
    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        
        return sha.digest();
        
    }
    
    /**
     * Turns array of bytes into string
     *
     * @param buf Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        
        return strbuf.toString();
    }
}
