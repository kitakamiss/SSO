package com.hjrh.sso.web.util;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hjrh.common.utils.cache.redis.RedisUtils;
import com.hjrh.common.utils.string.StringUtil;



/*******RSA登录工具**********/
public class RSALoginService {

	private static final String PRIVATE_KEY_NAME = "privateKey";
	
	private static final int RSA_OVERTIME= 1800;

	/*****生成密钥对，返回公钥、私钥放session********/
	public RSAPublicKey generateKey(HttpServletRequest request) {
		KeyPair keyPair = RSAUtils.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		HttpSession session = request.getSession();
		session.setAttribute(PRIVATE_KEY_NAME, privateKey);
		return publicKey;
	}
	
	/*****生成密钥对，返回公钥、私钥放redis缓存********/
	public static RSAPublicKey generateKey() {
		KeyPair keyPair = RSAUtils.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RedisUtils.save(PRIVATE_KEY_NAME, privateKey, RSA_OVERTIME);
		return publicKey;
	}
	
	/*******从session中清除私钥*************/
	public void removePrivateKey(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(PRIVATE_KEY_NAME);
	}

	/*********解密字符串*****************/
	public static String decryptParameter(String parameter) {
		if (parameter != null) {
			RSAPrivateKey privateKey = (RSAPrivateKey) RedisUtils.get(PRIVATE_KEY_NAME);
			if (privateKey != null && StringUtil.isNotNull(parameter)) {
				return RSAUtils.decrypt(privateKey, parameter);
			}
		}
		return null;
	}
	
}