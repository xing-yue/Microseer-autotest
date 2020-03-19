package com.automation.microseer.common;

import java.util.List;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebElement;
import com.automation.framework.action.IWebHandler;


/**
 * @Description: 工薪计公共类
 * @ClassName: GongXinJiCommon
 * @author panyongjun
 * @date 2019年8月1日
 */
public class GXJCommon extends MicroseerCommon{

	public GXJCommon(IWebHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * 微知端运营中心tab按钮选择
	 */
	By tabListBy = By.xpath("//div[@role='tablist']//div[@role='tab']");//运营中心tab按钮
	public void tabSelect(String tabName) throws Throwable{
		List<IWebElement> tabList = eleListsGet(tabListBy);
		eleClickByText(tabList, tabName);
	}
	
	/**
	 * 微知端勾选指定的应用中心（用户中心、运营中心、财务中心、账单中心、订单中心）
	 */
	By applicationBy = By.xpath("//ul[@role='menu']/li[4]");//微知端应用按钮
	By applicationItemBy = By.xpath("//ul[@id='item_1$Menu']/li");//应用列表
	public void applicationSelect(String applicationName) throws Throwable{
		eleHoverAndWait(applicationBy);
		List<IWebElement> itemList = eleListsGet(applicationItemBy);
		eleClickByText(itemList, applicationName);
	}
	
	/**
	 * dismiss工薪计企业端登录后的升级通知窗口
	 */
	By dialogBoxBy = By.xpath("//div[@role='document']");//系统升级公告窗口
	By iKnowBtnBy = By.xpath("//div[@class='ant-modal-footer']/button");//我知道了按钮
	public void upgradeBoxDismiss() throws Throwable{
		if(eleDisplayChk(dialogBoxBy)){
			eleClickBy(iKnowBtnBy);
			Thread.sleep(2000);
		}
	}
	
	/**
	 * 工薪计登录
	 */
	By userBy = By.id("userName");
	By pwdBy = By.id("password");
	By loginBtnBy = By.xpath("//button[@class='ant-btn login-btn login-form-button ant-btn-primary']");
	By comboItemBy = By.xpath("//ul[@role='listbox']/li");//下拉框选项
	By comboArrowBy = By.xpath("//span[@class='ant-select-arrow']");//下拉框中向下小箭头
	By confirmBtnBy = By.xpath("//div[@class='ant-modal-footer']/div/button");//确定按钮
	public void login(String user, String pwd) throws Throwable{
		navigateToUrl(url);
		txtBoxSendValue(userBy, user);
		txtBoxSendValue(pwdBy, pwd);
		eleClickBy(loginBtnBy);
		
		if(eleDisplayChk(dialogBoxBy)){
			eleClickBy(comboArrowBy);
			comboTextSet(comboItemBy, "微知（上海）服务外包有限公司");
			eleClickBy(confirmBtnBy);
			Thread.sleep(2000);
		}
	}
}
