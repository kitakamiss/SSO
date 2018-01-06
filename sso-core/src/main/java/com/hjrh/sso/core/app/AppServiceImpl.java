package com.hjrh.sso.core.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hjrh.sso.core.app.App;
import com.hjrh.sso.core.dao.fs.FileSystemDao;

/**
 * 
 * 功能描述：默认的应用管理服务实现类，默认将应用信息存储在
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class AppServiceImpl extends FileSystemDao implements AppService {
	
	private static Logger logger = Logger.getLogger(AppServiceImpl.class.getName());
	
	/**
	 * 外部数据文件地址，优先级更高。
	 */
	public static final String  DEFAULT_EXTERNAL_DATA =  "D:\\workspace\\sso\\sso-core\\src\\main\\resources\\apps.js";
	
	/**
	 * 默认的数据文件地址，在classpath下。
	 */
	public static final String DEFAULT_CLASSPATH_DATA = "apps.js";
	
	/**
	 * 应用的映射表，key是appId，value是应用对象信息。
	 */
	private Map<String, App> appMap = null;
	
	/**
	 * sso服务器本身的应用配置信息。
	 */
	private App ssoServerApp = null;
	
	
	/**
	 * 构造方法。
	 */
	public AppServiceImpl(){
		this.externalData = DEFAULT_EXTERNAL_DATA;
		this.classPathData = DEFAULT_CLASSPATH_DATA;
		//加载数据。
		loadAppData();
	}
	

	protected void loadAppData(){
		try{
			//先清空原来的数据文件。
			if(appMap!=null){
				appMap.clear();
			}
			appMap = null;
			//读取数据文件。
			String s = this.readDataFromFile();
			//将读取的应用列表转换为应用map。
			List<App> apps = JSON.parseObject(s, new TypeReference<List<App>>(){});
			//为主机增加反斜线地址。
			appendSlashToHost(apps);
			if(apps!=null){
				appMap = new HashMap<String, App>(apps.size());
				for(App app:apps){
					appMap.put(app.getAppId(), app);
					//设置sso应用服务器。
					if(ssoServerApp==null){
						if(app.isssoServer()){
							this.ssoServerApp = app;
						}
					}
				}
				apps = null;
			}
		}catch (Exception e) {
			logger.log(Level.SEVERE, "load app data file error.", e);
		}
	}
	
	/**
	 * 为主机地址最后增加一个斜线，即"/"。若没有的话则追加一个，若有的话则不追加。
	 * @param apps 应用列表。
	 */
	private void appendSlashToHost(List<App> apps){
		if(apps!=null && apps.size()>0){
			for(App app:apps){
				//若应用的主机地址不为空，且不以斜线结尾，则追加一个斜线。
				if(app.getHost()!=null && app.getHost().length()>0 && !app.getHost().endsWith("/")){
					app.setHost(app.getHost()+"/");
				}
				//否则不做处理。
			}
		}
	}
	

	public App findAppById(String appId) {
		if(appMap!=null){
			return appMap.get(appId);
		}
		return null;
	}
	
	public App findssoServerApp() {
		return this.ssoServerApp;
	}

	public App findAppByHost(String host) {
		if(StringUtils.isEmpty(host)){
			return null;
		}
		Collection<App> apps = appMap.values();
		//先按照原始地址查找一遍。
		App app = findAppByUrl(apps, host); 
		//若没有找到，再按照增加一个斜线"/"之后再查找一遍。
		if(app==null){
			app = findAppByUrl(apps, host+"/"); 
		}
		return app;
	}
	
	private App findAppByUrl(Collection<App> apps, String url){
		if(url==null || url.length()==0){
			return null;
		}
		
		for(App app: apps){
			if(!StringUtils.isEmpty(app.getHost()) && url.startsWith(app.getHost())){
				return app;
			}
		}
		return null;
	}
}
