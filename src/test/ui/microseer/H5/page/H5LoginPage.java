package microseer.H5.page;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebHandler;
import com.automation.framework.utils.Log4jUtil;
import com.automation.microseer.common.H5Common;

/**
 * H5登录页
 * @Description: 
 * @ClassName: H5LoginPage
 * @author panyongjun
 * @date 2019年8月7日
 */
public class H5LoginPage extends H5Common{

	Log4jUtil logger = new Log4jUtil();
	
	public H5LoginPage(IWebHandler handler) {
		this.handler = handler;
	}
	
	By codeInputBy = By.xpath("//input[@type='tel']");//验证码输入框
	By codeSendBtnBy = By.xpath("//div[contains(@class, 'smscodebody')]");//发送验证码
	By nxtBtnBy = By.xpath("//div[text()='下一步']");
	
	/**
	 * h5页面打开并发送验证码
	 * author:panyongjun
	 * date:2019年8月7日
	 */
	public void h5UrlOpenAndCodeSend(String url) throws Throwable{
		navigateToUrl(url);
		Thread.sleep(1000);
		eleClickBy(codeSendBtnBy);
		Thread.sleep(3000);
	}
	
	/**
	 * h5页面输入验证码并点击下一步
	 * author:panyongjun
	 * date:2019年8月7日
	 */
	public void h5LoginWithCode(String code) throws Throwable{
		txtBoxSendValue(codeInputBy, code);
		eleClickBy(nxtBtnBy);
	}
	
	/**
	 * h5登录
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	public void h5Login(String url, String testEnv, String phone) throws Throwable{
		h5UrlOpenAndCodeSend(url);
		String code = codeGet(testEnv, phone);
		h5LoginWithCode(code);
		
	}
}
