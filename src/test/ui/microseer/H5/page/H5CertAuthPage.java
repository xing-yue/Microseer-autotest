package microseer.H5.page;

import java.io.File;
import org.openqa.selenium.By;
import com.automation.framework.action.IWebHandler;
import com.automation.framework.utils.Log4jUtil;
import com.automation.microseer.common.H5Common;


/**
 * h5实名认证
 * @Description: 
 * @ClassName: H5CertAuthPage
 * @author panyongjun
 * @date 2019年8月8日
 */
public class H5CertAuthPage extends H5Common{

	Log4jUtil logger = new Log4jUtil();
	
	public H5CertAuthPage(IWebHandler handler) {
		this.handler = handler;
	}
	
	By nameInputBy = By.xpath("//input[@placeholder='请输入真实姓名']");//姓名输入框
	By certNoInputBy = By.xpath("//input[@placeholder='请输入身份证号']");//身份证输入框
	public By submitBy = By.xpath("//div[text()='提交认证']");
	By fstCertImageBy = By.xpath("//div[contains(@class, 'imgIdCard')]/div[1]/input");//身份证正面
	By secCertImageBy = By.xpath("//div[contains(@class, 'imgIdCard')]/div[2]/input");//身份证正面
	
	/**
	 * 实名认证
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	public void realNameCert(String name, String certNo) throws Throwable{
		txtBoxSendValue(nameInputBy, name);
		txtBoxSendValue(certNoInputBy, certNo);
		eleClickBy(submitBy);
		Thread.sleep(5000);
	}
	
	/**
	 * 身份证正反面上传
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	public void certImageUpload(String h5Url) throws Throwable{
		eleClickBy(fstCertImageBy);
		fileUpload(System.getProperty("user.dir") + File.separator + "testData" + File.separator + "h5" + File.separator + "身份证正面.jpg");
		eleClickBy(secCertImageBy);
		fileUpload(System.getProperty("user.dir") + File.separator + "testData" + File.separator + "h5" + File.separator + "身份证背面.jpg");
	}
}
