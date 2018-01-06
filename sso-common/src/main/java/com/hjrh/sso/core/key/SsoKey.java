package com.hjrh.sso.core.key;

import java.io.Serializable;
import java.security.Key;

import com.hjrh.sso.common.DESCoder;

public class SsoKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8673821624657924001L;

	/**
	 * 秘钥ID
	 */
	private String keyId;
	/**
	 * 应用ID
	 */
	private String appId;
	/**
	 * 秘钥值。
	 */
	private String value;
	/**
	 * 私钥文件存放路径
	 */
	private String keyPath;

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	/**
	 * 将本对象转换为对应的key对象。
	 * 
	 * @return Key对象。
	 * @throws Exception
	 */
	public Key toSecurityKey() throws Exception {
		if (this.getValue() != null) {
			return DESCoder.initSecretKey(this.getValue());
		}
		return null;
	}

	@Override
	public String toString() {
		return "ssoKey [keyId=" + keyId + ", appId=" + appId + ", value=" + value + ",keyPath=" + keyPath + "]";
	}
}
