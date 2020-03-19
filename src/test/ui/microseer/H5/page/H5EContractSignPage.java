package microseer.H5.page;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebHandler;
import com.automation.microseer.common.H5Common;

/**
 * h5电子签约
 * @Description: 
 * @ClassName: H5SignEContract
 * @author panyongjun
 * @date 2019年8月8日
 */
public class H5EContractSignPage extends H5Common{
	public H5EContractSignPage(IWebHandler handler){
		this.handler = handler;
	}
	
	public By signBtnBy = By.xpath("//div[@class='signBtn']");//签约按钮
	By defaultFontBy = By.id("default");//默认签名
	By useBtnBy = By.xpath("//button/span[text()='使用']");
	By codeSendBtnBy = By.xpath("//button/span[text()='获取验证码']");//获取验证码
	By codeInputBy = By.xpath("//input[@placeholder='请输6位验证码']");//验证码输入框
	By submitBtnBy = By.xpath("//button/span[contains(text(),'提交')]");//提交
	
	/**
	 * 使用默认签名签约电子合同
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	public void eContractSign() throws Throwable{
		eleClickBy(signBtnBy);//签署
		eleClickBy(defaultFontBy);//默认签名
		eleClickBy(useBtnBy);//使用
		eleClickBy(signBtnBy);//确认签署
//		eleClickBy(codeSendBtnBy);//获取验证码
//		Thread.sleep(3000);//等待3秒
	}
	
	/**
	 * 合同签署校验并确认提交
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	public void eSignConfirm(String code) throws Throwable{
		txtBoxSendValue(codeInputBy, code);
		eleClickBy(submitBtnBy);
	}
}
