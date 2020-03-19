package microseer.GXJ.page;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebHandler;
import com.automation.microseer.common.GXJCommon;


public class loginPage extends GXJCommon{

	public loginPage(IWebHandler handler) {
		super(handler);
	}

	public By topTabBy = By.xpath("//header[@class='ms-header ant-layout-header']//ul/li[contains(@class, 'ant-menu-item')]");
	
	public void newProject() throws Throwable{
//		eleClickBy(By.xpath("//span[text()='创建项目']"));
		handler.getMetaDriver().findElement(By.xpath("//button"));
		Thread.sleep(1000);
	}
}
