package microseer.GXJ.page;

import org.openqa.selenium.By;
import com.automation.framework.action.IWebHandler;
import com.automation.microseer.common.GXJCommon;


public class newProjectPage extends GXJCommon{

	public newProjectPage(IWebHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	public By topTabBy = By.xpath("//header[@class='ms-header ant-layout-header']//ul/li[contains(@class, 'ant-menu-item')]");
	
	/**
	 * 新建项目
	 * author:panyongjun
	 * date:2019年8月13日
	 */
	public void newProject() throws Throwable{
		eleClickBy(By.xpath("//div[@class='ant-row']//button"));
		txtBoxSendValue(By.xpath("//div[@role='textbox']"), "bbbbbbbbbbbbbbbbbbbbb");
//		comboTextSet(By.xpath(""), "text");
	}
}
