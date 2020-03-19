package microseer.H5.page;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebHandler;
import com.automation.framework.utils.Log4jUtil;
import com.automation.microseer.common.H5Common;

/**
 * 内部往来新增页
 * @author panyongjun
 * 2019年6月14日 下午3:36:40
 */
public class H5Page extends H5Common{

	Log4jUtil logger = new Log4jUtil();
	
	public H5Page(IWebHandler handler) {
		this.handler = handler;
	}
	
	By baiduSearchTxtBoxBy = By.id("index-kw");
	public By resultTitleListBy = By.xpath("//span[@class='c-title-text']");
	
	public void baiduSearch(String searchTxt) throws Throwable{
		navigateToUrl("http://m.baidu.com");
//		Cookie cookie = new Cookie("sign", "69101898bdc05f4043596968b51f1d69");
//		Cookie cookie1 = new Cookie("apikey", "0ffa3682a38b40878a58a09a67edd8b01602f154950002");
//		this.handler.getMetaDriver().manage().addCookie(cookie);
//		this.handler.getMetaDriver().manage().addCookie(cookie1);
		txtBoxSendValue(baiduSearchTxtBoxBy, searchTxt);
		txtBoxEnterPress(baiduSearchTxtBoxBy);
	}
}
