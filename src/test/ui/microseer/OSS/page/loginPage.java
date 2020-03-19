package microseer.OSS.page;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebHandler;
import com.automation.microseer.common.GXJCommon;


public class loginPage extends GXJCommon{

	public loginPage(IWebHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	public By topTabBy = By.xpath("//header[@class='ms-header ant-layout-header']//ul/li[contains(@class, 'ant-menu-item')]");
}
