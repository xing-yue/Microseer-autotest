package com.automation.microseer.common;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebElement;
import com.automation.framework.action.IWebHandler;

/**
 * @Description:收入发放公共类 
 * @ClassName: SalaryCommon
 * @author panyongjun
 * @date 2019年8月1日
 */
public class SalaryCommon extends MicroseerCommon {

	public SalaryCommon(IWebHandler handler) {
		this.handler = handler;
	}
	
	By loadingBy = By.xpath("//span[@class='ant-spin-dot ant-spin-dot-spin']");//页面加载圆圈
	
	/**
	 * 收入发放左侧菜单点击
	 * author:panyongjun
	 * date:2019年8月1日
	 */
	public void menuOpen(String menuTxt) throws Throwable{
		eleClickBy(By.partialLinkText(menuTxt));
	}
	
	/**
	 * 根据指定combo标签及选项，选择对应下拉选项
	 * author:panyongjun
	 * date:2019年8月1日
	 */
	public void comboSet(String labelTxt, String itemTxt) throws Throwable{
		String labelFor = labelForGet(labelTxt);
		String comboItemDivId = eleAttrValueGet(By.xpath("//div[@id='" + labelFor + "']"), "aria-controls");
		eleClickBy(By.id(labelFor));
		List<IWebElement> itemList = new ArrayList<IWebElement>();
		itemList = eleListsGet(By.xpath("//div[@id='" + comboItemDivId + "']/ul/li"));
		eleClickByText(itemList, itemTxt);
	}
}
