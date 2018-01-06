package com.hjrh.sso.core.authentication;

import com.hjrh.sso.core.model.EncryCredentialInfo;

/**
 * 
 * 功能描述：加密凭据的管理器，包括对加密凭据的加密和解密等操作
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface EncryCredentialManager {

	/**
	 * 
	 * 功能描述：对编码的凭据信息进行解码，解码后为一个凭据对象
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午5:58:53</p>
	 *
	 * @param encryCredential  加密和编码后的凭据信息
	 * @return  解密和解码后的凭据信息
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public EncryCredentialInfo decrypt(EncryCredential encryCredential);
	
	
	/**
	 * 
	 * 功能描述：对凭据信息进行加密和编码处理，加密和编码之后的一个加密凭据信息字符串
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午5:58:35</p>
	 *
	 * @param encryCredentialInfo  加密前的用于凭据信息
	 * @return  加密后的用户字符串
	 * 
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public String encrypt(EncryCredentialInfo encryCredentialInfo);
	
	/**
	 * 
	 * 功能描述：检查用户凭据信息的合法性，凭据是否合法，凭据是否过期和有效等
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午5:58:16</p>
	 *
	 * @param encryCredentialInfo 用户凭据信息
	 * @return 凭据信息是否有效，true-表示有效，false-表示无效
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public boolean checkEncryCredentialInfo(EncryCredentialInfo encryCredentialInfo);
	
}
