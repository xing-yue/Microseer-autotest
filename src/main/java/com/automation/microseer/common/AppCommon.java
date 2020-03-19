package com.automation.microseer.common;

import org.openqa.selenium.By;

import com.automation.framework.base.AppAbstractCaseBase;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class AppCommon extends AppAbstractCaseBase{

	public AppCommon(AppiumDriver<MobileElement> handler) {
		this.appHandler = handler;
	}
	
	public void appiumLogcat(String udid, String logPath){
		excuteAdbShell("adb -s " + udid + " >" + logPath);
	}
	
	/**
	 * 执行adb命令
	 * author:panyongjun
	 * date:2019年12月27日
	 */
	private void excuteAdbShell(String s) {
        Runtime runtime=Runtime.getRuntime();
        try{
            runtime.exec(s);
        }catch(Exception e){
            System.out.println("执行命令:"+s+"出错");
        }
    }
	
	/**
	 * 通过app输入法回车
	 * author:panyongjun
	 * date:2020年1月3日
	 */
	public void imeEnterPress(String udid) throws Throwable{
		excuteAdbShell("adb -s " + udid + " shell input keyevent KEYCODE_ENTER");
		Thread.sleep(1000);
	}
	
	/**
	 * 输入法切换
	 * author:panyongjun
	 * date:2020年1月3日
	 */
	public void imeSwith(String ime, String udid) throws Throwable{
		excuteAdbShell("adb -s " + udid + " shell ime set " + ime);
		Thread.sleep(5000);
	}
	
	/*------------------------------------------------菠萝秘书app公用方法---------------------------------------------*/
	
	/**
	 * 菠萝秘书登录
	 * author:panyongjun
	 * date:2019年12月23日
	 */
	public void crmLogin(String user, String pwd) throws Throwable{
		eleClickById("layout_login_pwd");
		txtBoxSendValue(appHandler.findElementById("edit_phone"), user);
		Thread.sleep(1000);
		txtBoxSendValue(appHandler.findElementById("edit_password"), pwd);
		eleClickById("btn_login");
		Thread.sleep(5000);
//		int tryTimes = 0;
//		while(eleIsDisplay(By.id("dialog_view")) && tryTimes<3){
//			tryTimes++;
//			Thread.sleep(2000);
//		}
	}
	
	/*---------------------------------------------------------------------------------------------------------*/	
	
	/*------------------------------------------------工薪计app公用方法---------------------------------------------*/
	
	/**
	 * 我的
	 * author:panyongjun
	 * date:2019年8月15日
	 */
	public void toMine() throws Throwable{
		eleClickById("radio_mine");
	}
	
	/**
	 * 钱袋
	 * author:panyongjun
	 * date:2019年8月15日
	 */
	public void toMoney() throws Throwable{
		eleClickById("radio_mine");
	}
	
	/**
	 * 任务
	 * author:panyongjun
	 * date:2019年8月15日
	 */
	public void toTask() throws Throwable{
		eleClickById("radio_money");
	}
	
	/**
	 * 接单（home）
	 * author:panyongjun
	 * date:2019年8月15日
	 */
	public void toHome() throws Throwable{
		eleClickById("radio_home");
	}
	
	public void gxjLogin(String user, String pwd) throws Throwable{
		eleClickById("layout_login_pwd");
		txtBoxSendValue(appHandler.findElementById("edit_phone"), user);
		Thread.sleep(1000);
		txtBoxSendValue(appHandler.findElementById("edit_password"), pwd);
		eleClickById("btn_login");
	}
	
	public void gxjResetLogin(String user, String pwd) throws Throwable{
		swipeLeft(4);
		eleClickById("btn_go");
		eleClickById("layout_login_pwd");
		txtBoxSendValue(appHandler.findElementById("edit_phone"), user);
		Thread.sleep(1000);
		txtBoxSendValue(appHandler.findElementById("edit_password"), pwd);
		eleClickById("btn_login");
	}
	
	/*---------------------------------------------------------------------------------------------------------*/
}