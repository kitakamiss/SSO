package com.hjrh.sso.web.util;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

import com.hjrh.common.utils.cache.redis.RedisUtils;

/**
 * Utils - RSA加密解密
 */
public final class RSAUtils {

	/** 安全服务提供者 */
	private static final Provider PROVIDER = new BouncyCastleProvider();

	/** 密钥大小 */
	private static final int KEY_SIZE = 1024;
	
	private static final String KEY_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM   = "SHA1withRSA";

	/**
	 * 不可实例化
	 */
	private RSAUtils() {
	}

	/**
	 * 生成密钥对
	 * 
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", PROVIDER);
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		Assert.notNull(publicKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA", PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param text
	 *            字符串
	 * 
	 * @return Base64编码字符串
	 */
	public static String encrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey);
		Assert.notNull(text);
		byte[] data = encrypt(publicKey, text.getBytes());
		return data != null ? new String(Base64.encodeBase64(data)) : null;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		Assert.notNull(privateKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String decrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey);
		Assert.notNull(text);
		byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
		return data != null ? new String(data) : null;
	}

	
	public static PublicKey getPublicKey(String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{  
          
		// 解密由base64编码的公钥     
        byte[] keyBytes = Base64.decodeBase64(key.getBytes());     
        // 构造X509EncodedKeySpec对象     
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);     
        // KEY_ALGORITHM 指定的加密算法     
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);     
        // 取公钥匙对象     
        PublicKey pubKey = keyFactory.generatePublic(keySpec);  
          
        return pubKey;  
    }
	
	public static boolean verify(byte[] data, String publicKey, String sign)     
            throws Exception {     
        // 解密由base64编码的公钥     
        byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());     
        // 构造X509EncodedKeySpec对象     
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);     
        // KEY_ALGORITHM 指定的加密算法     
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);     
        // 取公钥匙对象     
        PublicKey pubKey = keyFactory.generatePublic(keySpec);     
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);     
        signature.initVerify(pubKey);     
        signature.update(data);     
        // 验证签名是否正常     
        return signature.verify(Base64.decodeBase64(sign.getBytes()));     
    } 
	/**
	 * 
	 * 功能描述：
	 *
	 * @author  游文魁
	 * <p>创建日期 ：2017-8-15 下午2:40:49</p>
	 *
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public static Map<String,Object> getRsa(){
		Map<String,Object> map = new HashMap<String,Object>();
		RSAPublicKey publicKey = RSALoginService.generateKey();
		byte[] modulus = publicKey.getModulus().toByteArray();
		byte[] exponent = publicKey.getPublicExponent().toByteArray();
		map.put("modulus",Base64.encodeBase64String(modulus));
		map.put("exponent", Base64.encodeBase64String(exponent));
		RedisUtils.save("modulus", map.get("modulus"), 1800);//半小时过期
		RedisUtils.save("exponent", map.get("exponent"), 1800);
		return map;
	}
	
	
}