package microseer.H5.script;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.automation.framework.base.WebTestNGCaseBaseUI;
import com.automation.framework.exception.FrameworkException;
import com.automation.framework.verification.ValidationUtil;

import microseer.H5.page.H5BankCardAddPage;
import microseer.H5.page.H5CertAuthPage;
import microseer.H5.page.H5EContractSignPage;
import microseer.H5.page.H5LoginPage;

/**
 * 工薪计H5（实名、签约、绑卡、校验）
 * @author panyongjun
 */
public class TestCaseOfH5 extends WebTestNGCaseBaseUI{

	String realName = "潘永军";//姓名
	String certNo = "34220119791028361X";//身份证号码
	String phone = "13402080128";//登录手机号
	String cardPhone = "13321872737";//银行卡绑定手机号
	String cardNum = "6225882103028327";//银行卡卡号
	String h5Url = "https://testweb.viphrm.com/h5dk/#/mobilevalid?openId=154414661.eyJhbGciOiJIUzI1NiJ9.eyJhcHBpZCI6IjE2MDMiLCJleHAiOjE1NzIxNTQ2MjcsInVzZXJpZCI6IjE1NDQxNDY2MSJ9.L1NhwYyE6Fr9lLaoyv5Ps_rutwJqglkWwMren8yszlM&clientId=260173098";//h5链接
	
	/**
	 * H5登录
	 * author:panyongjun
	 * date:2019年8月7日
	 */
	@Test(priority=0, alwaysRun=true, groups={"microseer.H5.test"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfH5Login()throws FrameworkException{
		try{
			caseName = "H5登录";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			H5LoginPage pageAction = new H5LoginPage(handler);
			pageAction.h5Login(h5Url, testOn, phone);
			
			//判断是否跳转至认证页面
			H5CertAuthPage certPageAction = new H5CertAuthPage(handler);
			result = eleTxtGet(certPageAction.submitBy).equals("提交认证");
			
			ValidationUtil.verifyTrue(result, caseName, handler);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
	
	/**
	 * h5实名认证
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	@Test(priority=1, alwaysRun=true, groups={"microseer.H5.test"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfH5CertAuth()throws FrameworkException{
		try{
			caseName = "h5实名认证";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			navigateToUrl(h5Url);
			Thread.sleep(3000);
			H5CertAuthPage pageAction = new H5CertAuthPage(handler);
			pageAction.certImageUpload(h5Url);//上传身份证照片
			pageAction.realNameCert(realName, certNo);
			
			//验证是否实名成功并跳转至电子签约页
			H5EContractSignPage signPageAction = new H5EContractSignPage(handler);
			result = eleTxtGet(signPageAction.signBtnBy).equals("签署");
			
			ValidationUtil.verifyTrue(result, caseName, handler);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * h5电子签约
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	//@Test(priority=2, alwaysRun=true, groups={"microseer.H5.test"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfH5EContractSign()throws FrameworkException{
		try{
			caseName = "h5电子签约";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			navigateToUrl(h5Url);
			Thread.sleep(3000);
			H5EContractSignPage pageAction = new H5EContractSignPage(handler);
			pageAction.eContractSign();//签署合同
//			String code = codeGet(testOn, phone);//验证码获取
			pageAction.eSignConfirm("123456");//签署校验并确认提交
			result = eleTxtGet(pageAction.signBtnBy).equals("签署");
			
			//验证是否实名成功并跳转至电子签约页
			H5EContractSignPage signPageAction = new H5EContractSignPage(handler);
			result = eleTxtGet(signPageAction.signBtnBy).equals("签署");
			
			ValidationUtil.verifyTrue(result, caseName, handler);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
	
	/**
	 * h5添加银行卡
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	//@Test(priority=3, alwaysRun=true, groups={"microseer.H5.test"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfH5BankCardAdd()throws FrameworkException{
		try{
			caseName = "h5添加银行卡";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			navigateToUrl(h5Url);
			Thread.sleep(3000);
			H5BankCardAddPage pageAction = new H5BankCardAddPage(handler);
			pageAction.bankCardAdd(testOn, cardNum, cardPhone);
			
			//验证绑卡是否成功
			List<String> itemList = eleTxtListsGet(pageAction.h5ResultPageItemBy);
			result = strIsIncludedInList("实名认证", itemList) && 
					strIsIncludedInList("电子签约", itemList) && 
					strIsIncludedInList("绑定手机号", itemList) && 
					strIsIncludedInList("银行卡列表", itemList) && 
					strIsIncludedInList("联系客服", itemList);
			
			ValidationUtil.verifyTrue(result, caseName, handler);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
}