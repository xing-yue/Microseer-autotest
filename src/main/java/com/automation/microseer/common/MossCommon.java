package com.automation.microseer.common;

import com.automation.framework.action.IWebHandler;

/**
 * @Description:moss权限管理公共类 
 * @ClassName: MossCommon
 * @author panyongjun
 * @date 2019年8月1日
 */
public class MossCommon extends MicroseerCommon{
	public MossCommon(IWebHandler handler){
		this.handler = handler;
	}
	
	@Override
	/**
	 * 登录后跳转至内部往来默认页
	 * @author 上海微知
	 * 2019年6月14日 下午3:41:00
	 * @throws Throwable
	 */
	public void toURL(String testEnv, String testName, String user, String pwd) throws Throwable{
		//登陆
		newLogin(user, pwd);
//		notifyBoxClose();
		loginPopUpBoxWithCompany("288572");
		String homeUrl = handler.getCurrentUrl();
		navigateToUrl(homeUrl + "/permission");
		Thread.sleep(2000);
	}
}
