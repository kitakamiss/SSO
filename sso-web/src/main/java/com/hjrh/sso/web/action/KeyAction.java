package com.hjrh.sso.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.hjrh.common.utils.md5.TripleDes;
import com.hjrh.sso.core.key.KeyService;
import com.hjrh.sso.core.key.RSASecurityUtil;
import com.hjrh.sso.core.key.SsoKey;

import net.sf.json.JSONObject;

/**
 * 
 * 功能描述：与秘钥相关的web请求处理类，处理查询应用的秘钥等信息
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
@Controller
public class KeyAction {
	/**
	 * 秘钥服务。
	 */
	@Autowired
	private KeyService keyService;
	
	public void setKeyService(KeyService keyService) {
		this.keyService = keyService;
	}

	/**
	 * 
	 * 功能描述：根据应用ID，查询对应的秘钥信息，默认的实现是不加密的，未实现认证
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午6:37:04</p>
	 *
	 * @param appId  应用ID
	 * @return    对应的秘钥
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	@RequestMapping("/fetchKey")
	@ResponseBody
	public String fetchKey(String appId){
		SsoKey ssokey = keyService.findKeyByAppId(appId);
		try {
			return TripleDes.encrypt(JSONObject.fromObject(ssokey).toString(),null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
