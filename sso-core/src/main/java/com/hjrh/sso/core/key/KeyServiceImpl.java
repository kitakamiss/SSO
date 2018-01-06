package com.hjrh.sso.core.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hjrh.sso.core.dao.fs.FileSystemDao;
import com.hjrh.sso.core.exception.ParamsNotInitiatedCorrectly;
import com.hjrh.sso.core.key.KeyService;
import com.hjrh.sso.core.key.SsoKey;

/**
 * 
 * 功能描述：默认的key管理实现类 从classpath:/keys.js文件中 读取key配置信息，是以json格式存储的。
 *
 * @author 吴俊明
 *
 *         <p>
 * 		修改历史：(修改人，修改时间，修改原因/内容)
 *         </p>
 */
public class KeyServiceImpl extends FileSystemDao implements KeyService {

	private static Logger logger = Logger.getLogger(KeyServiceImpl.class.getName());

	/**
	 * 默认的数据文件地址，在classpath下。
	 */
	public static final String DEFAULT_CLASSPATH_DATA = "keys.js";

	/** 指定公钥存放文件路径 ，默认是classPath */
	private static String PUBLIC_KEY_PATH = null;
	/** 指定公钥存放文件名 */
	private static String PUBLIC_KEY_FILE = null;
	/** 密钥长度，用来初始化 */
	private static final int KEYSIZE = 1024;
	/** 指定加密算法为RSA */
	private static final String ALGORITHM = "RSA";

	/**
	 * 秘钥映射表，key是keyId,value是Key对象。
	 */
	private Map<String, SsoKey> keyMap = null;

	/**
	 * 秘钥映射表，key是appId,value是Key对象。
	 */
	private Map<String, SsoKey> appIdMap = null;

	public KeyServiceImpl() {
		this.classPathData = DEFAULT_CLASSPATH_DATA;
		// 加载数据。
		loadAppData();
	}

	@Override
	protected void loadAppData() {
		try {
			String s = this.readDataFromFile();
			// 将读取的应用列表转换为应用map。
			List<SsoKey> keys = JSON.parseObject(s, new TypeReference<List<SsoKey>>() {
			});
			if (keys != null) {
				keyMap = new HashMap<String, SsoKey>(keys.size());
				appIdMap = new HashMap<String, SsoKey>(keys.size());
				for (SsoKey key : keys) {
					keyMap.put(key.getKeyId(), key);
					appIdMap.put(key.getAppId(), key);
				}
				keys = null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "load app data file error.", e);
		}
	}

	@Override
	public SsoKey findKeyByKeyId(String keyId) {
		SsoKey ssoKey = null;
		// loadAppData(); //重新加载数据
		if (this.keyMap != null) {
			ssoKey = this.keyMap.get(keyId);
			return ssoKey;
		}
		return null;
	}

	@Override
	public SsoKey findKeyByAppId(String appId) {
		SsoKey ssoKey = null;
		// loadAppData(); //重新加载数据
		if (this.appIdMap != null) {
			ssoKey = this.appIdMap.get(appId);
			return ssoKey;
		}
		return null;
	}

	/**
	 * 使用公钥将key加密
	 * 
	 * @param token
	 *            公钥文件标识
	 * @param keyValue
	 *            需要加密的key
	 * @return 加密后的key
	 */
	public String encryptKey(String token, String keyValue) throws ParamsNotInitiatedCorrectly, Exception {
		String encryptKey = null;
		if (checkKeyFileExistByToken(token)) { // 判断公钥文件是否存在
			Key publicKey = loadPublicKey(); // 加载公钥文件
			/** 得到Cipher对象来实现对源数据的RSA加密 */
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] bytes = keyValue.getBytes();
			/** 执行公钥加密操作 */
			byte[] encryptValue = cipher.doFinal(bytes);
			// 使用Base64加密
			BASE64Encoder encoder = new BASE64Encoder();
			encryptKey = encoder.encode(encryptValue);
		} else {
			throw new ParamsNotInitiatedCorrectly("The Public Key File Is Not Initiated !!!");
		}
		// 返回加密后的key
		return encryptKey;
	}

	/**
	 * 
	 * 功能描述：用Base64加密后的私钥文件
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午6:52:21</p>
	 *
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public Key loadPublicKey() {
		Key publicKey = null;
		ObjectInputStream ois = null;
		try {
			/** 将文件中的公钥对象读出 */
			ois = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
			publicKey = (Key) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return publicKey;
	}

	@Override
	public boolean checkKeyFileExistByToken(String token) {
		// TODO Auto-generated method stub
		// loadAppData();
		SsoKey ssoKey = appIdMap.get(token); // 获取当前运用的appId
		PUBLIC_KEY_PATH = ssoKey.getKeyPath(); //获取公钥文件存放路径
		PUBLIC_KEY_FILE = PUBLIC_KEY_PATH + token; //获取公钥文件名
		File keyFile = new File(PUBLIC_KEY_FILE);
		// 公钥文件是否存在
		return keyFile.exists();
	}

	@Override
	public Object generateKeyFile(String token) throws ParamsNotInitiatedCorrectly, Exception {
		// TODO Auto-generated method stub
		// 判断运用ID列表是否为空
		if (appIdMap == null || token == null || "".equals(token)) {
			throw new ParamsNotInitiatedCorrectly("appIdMap Parameter Is Initiated Incorrently !!");
		}

		/** RSA算法要求有一个可信任的随机数源 */
		SecureRandom secureRandom = new SecureRandom();
		/** 为RSA算法创建一个KeyPairGenerator对象 */
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		/** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
		keyPairGenerator.initialize(KEYSIZE, secureRandom);
		keyPairGenerator.initialize(KEYSIZE);
		/** 生成密匙对 */
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		/** 得到公钥 */
		Key publicKey = keyPair.getPublic();
		/** 得到私钥 */
		Key privateKey = keyPair.getPrivate();

		ObjectOutputStream publicOutPutStream = null;
		try {
			/** 用对象流将生成的公钥写入文件 */
			publicOutPutStream = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
			publicOutPutStream.writeObject(publicKey);
		} catch (Exception e) {
			throw e;
		} finally {
			/** 清空缓存，关闭文件输出流 */
			publicOutPutStream.close();
		}
		// 返回私钥文件
		return privateKey;
	}
}
