package microseer.H5.page;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebHandler;
import com.automation.microseer.common.H5Common;

/**
 * h5添加银行卡
 * @Description: 
 * @ClassName: H5BankCardAdd
 * @author panyongjun
 * @date 2019年8月8日
 */
public class H5BankCardAddPage extends H5Common{
	
	public H5BankCardAddPage(IWebHandler handler){
		this.handler = handler;
	}
	
	public By pageTitleBy = By.xpath("//div[contains(@class, 'headerTitle')]");//添加银行卡页头
	By cardNumInputBy = By.xpath("//input[@placeholder='请填写您的银行卡号']");//银行卡号
	By phoneInputBy = By.xpath("//input[@placeholder='请输入银行预留手机号']");//预留手机号
	By codeInputBy = By.xpath("//input[@placeholder='请输入验证码']");//验证码
	By codeSendBtnBy = By.xpath("//div[text()='获取验证码']");//发送验证码按钮
	By confirmBtnBy = By.xpath("//div[text()='确认']");//确认
	public By h5ResultPageItemBy = By.xpath("//div[contains(@class, 'item-')]");//实名、绑卡、签约最终结果页栏目列表
	
	/**
	 * h5添加银行卡
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	public void bankCardAdd(String testEnv, String cardNum, String phone) throws Throwable{
		txtBoxSendValue(cardNumInputBy, cardNum);
		txtBoxSendValue(phoneInputBy, phone);
		eleClickBy(codeSendBtnBy);
		Thread.sleep(3000);
		String code = codeGet(testEnv, phone);
		txtBoxSendValue(codeInputBy, code);
		eleClickBy(confirmBtnBy);
	}
	
}
