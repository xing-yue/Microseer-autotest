package com.automation.microseer.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebElement;
import com.automation.framework.base.WebAbstractCaseBase;
import com.automation.framework.utils.AutoItUtil;
import com.automation.framework.utils.Dom4jUtil;
import com.automation.framework.utils.ExcelUtil;
import com.automation.framework.utils.FileUtil;
import com.automation.framework.utils.IdCardGen;
import com.automation.framework.utils.Log4jUtil;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

/**
 * @Description:scm公共类 
 * @ClassName: ScmAPICommon
 * @author panyongjun
 * @date 2019年8月1日
 */
public class MicroseerCommon extends WebAbstractCaseBase {
	
	protected int GRID_LOADING_TIME = 30;
	Log4jUtil logger = new Log4jUtil();
	AutoItUtil autoIt = new AutoItUtil();
	ExcelUtil excel = new ExcelUtil();
	FileUtil file = new FileUtil();
	IdCardGen idGen = new IdCardGen();
	protected static int LOADING_TIME = 20;
	
	//解绑信息
	public String memberHost = "http://120.55.114.86:8086";
	public String memberUser = "zhengpy";
	public String memberPwd = "654321";
	
	//oss参数
	public String topic = "ms-app-meta-2017";
	
	/**
	 * 获取指定环境及手机的验证码
	 * author:panyongjun
	 * date:2019年8月8日
	 */
	public String codeGet(String testEnv, String phone) throws Throwable{
		String ossHost = ossUrlGet(testEnv);
		String code = phoneCodeGet(ossHost, phone);
		return code;
	}
	
	/**
	 * oss地址获取
	 * author:panyongjun
	 * date:2019年8月7日
	 */
	public String ossUrlGet(String testEnv) throws Throwable{
		String ossHost = "";
		String webXmlFile = System.getProperty("user.dir") + 
				"\\src\\test\\xml\\webUrl.xml";
		Dom4jUtil xmlUtil = new Dom4jUtil(webXmlFile);
		switch(testEnv.toLowerCase()){
			case "test":ossHost = xmlUtil.getNodeText("/webUrl/OSS/testUrl");break;
			case "pre":ossHost = xmlUtil.getNodeText("/webUrl/OSS/preUrl");break;
			case "pro":ossHost = xmlUtil.getNodeText("/webUrl/OSS/proUrl");break;
			default:break;	}	
		return ossHost;
	}

    /**
     * 手机验证码获取
     * @author panyongjun
     * 2018年11月23日 上午10:48:24
     * @param phone
     * @return
     */
    public String phoneCodeGet(String testFor, String phone){
    	String code = "";
    	Response response = ossMessageQuery(testFor);
    	List<String> msgidList = response.jsonPath().getList("data.msgId");
    	for(String msgid:msgidList){
    		response = ossSMSMessageBody(testFor, msgid);
    		String messageBody = response.jsonPath().get("data.messageView.messageBody");
//    		System.out.println(messageBody);
    		if(messageBody.contains(phone)){
    			code = messageBody.split("\"text\":\"")[1].split("，")[0].replace("【工薪记】验证码为", "");
//    			System.out.println(code);
    			break;
    		}
    	}
    	return code;
    }
	
	/**
	 * 从OSS消息池中获取message
	 * @author panyongjun
	 * 2018年11月23日 上午9:34:19
	 * @return
	 */
	public Response ossMessageQuery(String ossHost){
		Calendar cal = Calendar.getInstance();
		long endTime = cal.getTimeInMillis();
		System.out.print(endTime);
		cal.add(Calendar.DATE, -1);
		long fromTime = cal.getTimeInMillis();
		System.out.println(fromTime);
		String url = "/message/queryMessageByTopic.query";
		Response response = given()
				.log().all()
//				.cookie("Hm_lvt_034ca97c86b52f15a7374fb509a5f8a5", ossLoginCookie)
				.queryParam("begin", fromTime)
				.queryParam("end", endTime)
				.queryParam("topic", topic)
				.get(ossHost + url);
		response.prettyPrint();
		return response;
	}
	
	/**
	 * 手机验证码获取
	 * @author panyongjun
	 * 2018年11月23日 上午9:40:12
	 * @param msgId
	 * @return
	 */
	public Response ossSMSMessageBody(String ossHost, String msgId){
		String url = "/message/viewMessage.query";
		Response response = given()
				.log().all()
//				.cookie("cookie_token", ossLoginCookie)
				.queryParam("msgId", msgId)
				.queryParam("topic", topic)
				.get(ossHost + url);
		response.prettyPrint();
		return response;
	}

//	/**
//	 * 公共组件登陆接口
//	 * @author panyongjun
//	 * 2018年11月28日 下午6:00:34
//	 * @param user
//	 * @param pwd
//	 * @return
//	 */
//	public Response auth(String user, String pwd){
//		String url = "/auth";
//		String bodyJson = "{ \"appId\": \"1601\", \"codeKey\": \"\", \"password\": \"" + pwd + "\", \"rememberMe\": false, \"userName\": \"" + user + "\", \"verifyCode\": \"\"}";
//		Response response = given()
//				.log().all()
//				.contentType("application/json")
//				.body(bodyJson)
//				.post(authHost + url);
//		response.prettyPeek();
//		return response;
//	}
	
	/**
	 * 会员域后台登陆
	 * @author panyongjun
	 * 2018年12月1日 下午2:58:01
	 * @param userName
	 * @param password
	 * @return
	 */
	public Response certAuth(String userName, String password){
		String url = "/mobilegw/auth";
		Response response = given()
				.log().all()
				.contentType("application/json")
				.queryParam("userName", userName)
				.queryParam("password", password)
				.post(memberHost + url);
		response.prettyPrint();
		return response;
	}
	
	/**
	 * 根据身份证检索实名记录
	 * @author panyongjun
	 * 2018年12月1日 下午4:50:44
	 * @param authToken
	 * @param certNo
	 * @return
	 * @throws Throwable 
	 */
	public Response certSearch(String authToken, String certNo) throws Throwable{
		String url = "/mobilegw/backend";
		String requestData = "{\"acqId\":\"01\", "
				+ "\"versionNo\":\"1.00\", "
				+ "\"reqId\":\"20DE8979495D8A2F4B63F1807ED0258F8DD8\", "
				+ "\"sendTime\":\"" + systemCurrentDate("yyyyMMddHHmmss") + "\", "
				+ "\"startPage\":1, "
				+ "\"limit\":20, "
				+ "\"certNo\":\"" + certNo + "\"}";
		Response response = given()
				.log().all()
				.queryParam("requestData", requestData)
				.queryParam("operationType", "eigpay.sp.customer.user.list")
				.queryParam("merchId", "9000100")
				.queryParam("instId", "9000100")
				.queryParam("token", authToken)
				.post(memberHost + url);
		response.prettyPeek();
		return response;
	}
	
	/**
	 * 会员域实名解绑
	 * @author panyongjun
	 * 2018年12月1日 下午2:58:26
	 * @param certCode
	 * @return
	 * @throws Throwable 
	 */
	public Response certUnbind(String authToken, String userId) throws Throwable{
		String url = "/mobilegw/backend";
		String requestData = "{\"acqId\":\"01\", "
				+ "\"versionNo\":\"1.00\", "
				+ "\"reqId\":\"NHdcDARQTEF0mfpYVTegvr2CKiBWkyQ7tKGk\", "
				+ "\"sendTime\":\"" + systemCurrentDate("yyyyMMddHHmmss") + "\", "
				+ "\"userId\":\"" + userId + "\"}";
		System.out.print(requestData);
		Response response = given()
				.log().all()
				.header("token", authToken)
				.contentType("application/json")
				.queryParam("requestData", requestData)
				.queryParam("operationType", "eigpay.sp.customer.auth.realname.cancel")
				.queryParam("merchId", "9000100")
				.queryParam("instId", "9000100")
				.queryParam("token", authToken)
				.post(memberHost + url);
		response.prettyPeek();
		return response;
	}	
	
	/**
	 * 财务中心出入账使用的combo组件公用方法
	 * @author 上海微知
	 * 2019年6月14日 下午4:57:03
	 * @param label
	 * @param option
	 * @throws Throwable
	 */
	public void comboValueSet(String label, String option) throws Throwable {
		String comboId = labelForGet(label);
		eleClickBy(By.id(comboId));
		Thread.sleep(1000);
		txtBoxSendValue(By.xpath("//input[@id='" + comboId + "']"), option);
		Thread.sleep(1000);	
		eleClickByText(By.xpath("//ul[@role='listbox']/li"), option);	
	}
	
	/**
	 * 根据指定value选择下拉框
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void comboValueSet(By locator, String value) throws Throwable{
		List<IWebElement> itemList = eleListsGet(locator);
		eleClickByValue(itemList, value);
	}
	
	/**
	 * 根据指定text选择下拉框
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void comboTextSet(By locator, String value) throws Throwable{
		List<IWebElement> itemList = eleListsGet(locator);
		eleClickByText(itemList, value);
	}
	
	/**
	 * 登录后跳转至内部往来默认页
	 * @author 上海微知
	 * 2019年6月14日 下午3:41:00
	 * @throws Throwable
	 */
	public void toURL(String testEnv, String testName, String user, String pwd) throws Throwable{
		//登陆
		newLogin(user, pwd);
//		notifyBoxClose();
		loginPopUpBoxWithCompany("288572");
		//页面跳转至内部往来
		String testUrl = testUrlGet(testEnv, testName);
		navigateToUrl(testUrl);
		Thread.sleep(2000);
	}
	
	public String testUrlGet(String testEnv, String testName) throws Throwable{
		String testUrl = "";
		String webXmlFile = System.getProperty("user.dir") + 
				"\\src\\test\\xml\\webUrl.xml";
		Dom4jUtil xmlUtil = new Dom4jUtil(webXmlFile);
		switch(testEnv.toLowerCase()){
		case "test":testUrl = xmlUtil.getNodeText("/webUrl/" + testName + "/testUrl");break;
		case "pre":testUrl = xmlUtil.getNodeText("/webUrl/" + testName + "/preUrl");break;
		case "pro":testUrl = xmlUtil.getNodeText("/webUrl/" + testName + "/proUrl");break;
		default:break;	}
		return testUrl;
	}
	
	/**
	 * 弹出窗窗口确认
	 */
	By dialogBoxBy = By.xpath("//div[@role='alertdialog' and @aria-hidden='false']");//弹出窗窗口
	By dialogConfrimBy = By.partialLinkText("确定");//弹出窗确定按钮
	public void messageConfirm() throws Throwable{
		try{
			logger.info(eleTxtGet(dialogBoxBy));
			eleClickBy(dialogConfrimBy);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 弹出窗口确认
	 * @param btnTxt
	 * @throws Throwable
	 */
	public void messageConfirm(String btnTxt) throws Throwable{
		try{
			logger.info(eleTxtGet(dialogBoxBy));
			eleClickBy(By.linkText(btnTxt));
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 根据combo元素默认value，选择指定栏目
	 * @param defaultTxt
	 * @param itemTxt
	 * @throws Throwable
	 */
	public void comboQueryWithValue(String defaultTxt, String itemTxt) throws Throwable {
		try{
			String inputId = "";
			List<IWebElement> inputList = eleListsGet(By.xpath("//input[@role='combobox']"));
			for(IWebElement input:inputList){
				String inputValue = input.getAttribute("value");
				if(inputValue.equals(defaultTxt)){
					inputId = input.getAttribute("id");
					break;
				}
			}
			eleClickBy(By.id(inputId.replace("inputEl", "trigger-picker")));
			comboInputByTxt(By.id(inputId), itemTxt);
			pageLoadingWait(LOADING_TIME);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 根据指定combo的placeholder的值，选择指定的下拉菜单
	 * @param placeholderTxt
	 * @param itemTxt
	 * @throws Throwable
	 */
	public void comboQueryWithTxt(String placeholderTxt, String itemTxt) throws Throwable {
		try{
			comboInputByTxt(By.xpath("//input[@placeholder='" + placeholderTxt + "']"), itemTxt);
			gridContentLoading(LOADING_TIME);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 二级密码输入
	 * @param secPwd
	 * @throws Throwable
	 */
	public void secLevelPwdInput(String secPwd) throws Throwable {
		try{
//			eleClickBy(By.id("password"));
			txtBoxSendValue(By.id("password"), secPwd);//输入二级密码
			eleClickBy(By.xpath("//button[@onclick='login()']"));//确认
			waitPageLoad(LOADING_TIME);//检查页面加载状态
			pageLoadingWait(LOADING_TIME);//检查加载元素
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 元素加载等待time秒
	 * @param time
	 * @throws Throwable
	 */
	By gridContentBy = By.xpath("//div[@role='grid']//div[@class='x-grid-item-container']/table"); //表内容
	By gridHeaderBy = By.xpath("//div[@role='columnheader']");//表头
	public void gridContentLoading(int time) throws Throwable {
		try{
			if(!eleDisplayChk(gridContentBy) && eleDisplayChk(gridHeaderBy)){
				int i=0;
				do{
					logger.info("内容未完成加载,请稍后...");
					i++;
				}while(!eleDisplayChk(gridContentBy) && eleDisplayChk(gridHeaderBy) && i<time);
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * dialog窗口关闭
	 */
	By boxCloseBtnBy = By.xpath("//div[@role='dialog']//div[@class='x-tool-img x-tool-close']"); 
	public void winBoxClose() throws Throwable{
		try{
			List<String> closeEleIdList = new ArrayList<String>();
			List<IWebElement> closeList = eleListsGet(boxCloseBtnBy);
			for(IWebElement close:closeList){
				closeEleIdList.add(close.getAttribute("id"));
			}
			Collections.sort(closeEleIdList);
			Collections.reverse(closeEleIdList);
			for(String closeEleId:closeEleIdList){
				eleClickBy(By.id(closeEleId));
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 业务接口页面加载等待（派单中心，客服中心等）
	 * @param time，单位秒
	 * @throws Throwable
	 */
	By buziPageLoadingMsgTxtBy = By.xpath("//body[not(@role)]//div[@id='loadingText']");//页面加载提示
	public void buziPageLoadingWait(int time) throws Throwable{
		try{
//			int tryTimes = (int) (time/MAX_ELE_DISPLAYTIME);
			if(eleDisplayChk(buziPageLoadingMsgTxtBy)){
				int i=0;
				do{
					logger.info(eleTxtGet(buziPageLoadingMsgTxtBy));
					Thread.sleep(1000);
					i++;
				}while(eleDisplayChk(buziPageLoadingMsgTxtBy) && i<time);
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 页面加载
	 */
	By loadingMsgTxtBy = By.xpath("//div[(@role='status' or @role='dialog' or @role='tabpanel' or @class='x-mask-msg') and @aria-hidden='false']//"
			+ "div[@class='x-mask-msg-text']");//页面加载提示
	public void pageLoadingWait(int time) throws Throwable{
		try{
			if(eleDisplayChk(loadingMsgTxtBy)){
				int i=0;
				do{
					logger.info(eleTxtGet(loadingMsgTxtBy));
					Thread.sleep(1000);
					i++;
				}while(eleDisplayChk(loadingMsgTxtBy) && i<time);
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 阿拉伯数字月份转换为中文月份
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public String monthTransferToZh(String monthNum) throws Throwable {
		String zhMonth = "";
		try{
			switch(monthNum){
				case "01": zhMonth = "一月"; break;
				case "02": zhMonth = "二月"; break;
				case "03": zhMonth = "三月"; break;
				case "04": zhMonth = "四月"; break;
				case "05": zhMonth = "五月"; break;
				case "06": zhMonth = "六月"; break;
				case "07": zhMonth = "七月"; break;
				case "08": zhMonth = "八月"; break;
				case "09": zhMonth = "九月"; break;
				case "10": zhMonth = "十月"; break;
				case "11": zhMonth = "十一月"; break;
				case "12": zhMonth = "十二月"; break;
				default:break;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return zhMonth;
	}
	
	/**
	 * 根据输入检索选择下拉框选择第一个栏目
	 * @param locator
	 * @throws Throwable
	 */
	public void comboSearchByFst(By locator) throws Throwable{
		try{
			List<IWebElement> itemList = eleListsGet(locator);
			itemList.get(0).click();
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 根据输入检索选择下拉框选择第一个栏目
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void comboSearchByFst() throws Throwable{
		try{
			List<IWebElement> itemList = eleListsGet(By.xpath("//ul[@aria-hidden='false']/li"));
			itemList.get(0).click();
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 下拉框选择第一个栏目
	 * @param locator
	 * @throws Throwable
	 */
	public void comboInputByFst(By locator) throws Throwable{
		try{
			eleClickBy(locator);
			List<IWebElement> itemList = eleListsGet(comboItemBy);
			itemList.get(0).click();
			logger.info("下拉框栏目'" + itemList.get(0).getAttribute("value") + "'被选中");
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 下拉框选择指定的栏目
	 * @param locator
	 * @param itemTxt
	 * @throws Throwable
	 */
	By comboDivItemBy = By.xpath("//ul[@aria-hidden='false']/div");//下拉框栏目
	public void comboInputByDivTxt(By locator, String itemTxt) throws Throwable{
		try{
			eleClickBy(locator);
			List<IWebElement> itemList = eleListsGet(comboDivItemBy);
			for(IWebElement item:itemList){
				String txt = item.getText();
				if(txt.contains(itemTxt)){
					item.click();
					logger.info("下拉框栏目'" + txt + "'被选中");
					pageLoadingWait(LOADING_TIME);
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 下拉框选择指定的栏目
	 * @param locator
	 * @param itemTxt
	 * @throws Throwable
	 */
	By comboItemBy = By.xpath("//ul[@aria-hidden='false']/li");//下拉框栏目
	public void comboInputByTxt(By locator, String itemTxt) throws Throwable{
		try{
			eleClickBy(locator);
			List<IWebElement> itemList = eleListsGet(comboItemBy);
			for(IWebElement item:itemList){
				String txt = item.getText();
				if(txt.contains(itemTxt)){
					item.click();
					logger.info("下拉框栏目'" + txt + "'被选中");
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 根据combo标签及栏目进行下拉选择
	 * @param labelTxt
	 * @param itemTxt
	 * @throws Throwable
	 */
	public void comboLabelByTxt(String labelTxt, String itemTxt) throws Throwable{
		try{
			eleClickBy(By.id(labelForGet(labelTxt).replace("inputEl", "trigger-picker")));
			List<IWebElement> itemList = eleListsGet(comboItemBy);
			for(IWebElement item:itemList){
				String txt = item.getText();
				if(txt.contains(itemTxt)){
					item.click();
					logger.info("下拉框栏目'" + txt + "'被选中");
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 根据combo标签及栏目进行下拉选择
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void comboLabelByTxt(By locator, String labelTxt, String itemTxt) throws Throwable{
		try{
			eleClickBy(By.id(labelForGet(locator, labelTxt).replace("inputEl", "trigger-picker")));
			List<IWebElement> itemList = eleListsGet(comboItemBy);
			for(IWebElement item:itemList){
				String txt = item.getText();
				if(txt.contains(itemTxt)){
					item.click();
					logger.info("下拉框栏目'" + txt + "'被选中");
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 获取指定标签的id
	 */
	By labelTagBy = By.tagName("label");
	public String labelForGet(String labelTxt) throws Throwable{
		String labelId = null;
		try{
			List<IWebElement> labelList = eleListsGet(labelTagBy);
			for(IWebElement label:labelList){
				String txt = label.getText();
				if(txt.contains(labelTxt)){
					labelId = label.getAttribute("for");
					logger.info("标签'" + labelTxt + "'的属性'for'的值为" + labelId);
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return labelId;
	}
	
	/**
	 * label属性获取
	 * @author panyongjun
	 * 2019年6月20日 下午1:40:24
	 * @param labelTxt
	 * @param attr
	 * @return
	 * @throws Throwable
	 */
	public String labelAttrGet(String labelTxt, String attr) throws Throwable{
		String labelId = null;
		try{
			List<IWebElement> labelList = eleListsGet(labelTagBy);
			for(IWebElement label:labelList){
				String txt = label.getText();
				if(txt.contains(labelTxt)){
					labelId = label.getAttribute(attr);
					logger.info("标签'" + labelTxt + "'的属性'"+ attr + "'的值为" + labelId);
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return labelId;
	}
	
	/**
	 * label for属性获取
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public String labelForGet(By locator, String labelTxt) throws Throwable{
		String labelId = null;
		try{
			List<IWebElement> labelList = eleListsGet(locator);
			for(IWebElement label:labelList){
				if(label.getText().contains(labelTxt)){
					labelId = label.getAttribute("for");
					logger.info("标签'" + labelTxt + "'的属性'for'的值为" + labelId);
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return labelId;
	}
	
	/**
	 * 使用指定账户登陆，打开指定模块
	 * @param loginUser
	 * @param loginPwd
	 * @param bizModule
	 * @throws Throwable
	 */
	public void loginAndBizModuleOpen(String loginUser, String loginPwd, String bizModule) throws Throwable {
		try{
			this.handler.deleteAllCookies();
			newLogin(loginUser, loginPwd);
			loginPopUpBoxClose();//公司选择窗口，直接点击确认登录
//			login(loginUser, loginPwd);
//			accountActiveBoxClose();
//			notifyBoxClose();
			String oriHandler = this.handler.getWindowHandle();
			buziOpen(bizModule);
			this.handler.close();
			this.handler.switchToNewWindowFromDefault(oriHandler);
			if(bizModule.equals("供应链") || bizModule.equals("福利政策")){
				secLevelPwdInput(microseerSecPwd);
			}
			waitPageLoad(LOADING_TIME);//检查页面加载状态
			buziPageLoadingWait(LOADING_TIME);//检查加载元素
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}

	/**
	 * moa平台登陆
	 * @param userName
	 * @param pwd
	 * @throws Throwable
	 */
	By userNameBy = By.id("txtAccount");//登录名输入框
	By userPwdBy = By.id("txtPassword");//登陆密码输入框
	By loginBtnBy = By.id("btnLogin");//登陆按钮
	public void login(String userName, String pwd) throws Throwable{
		try{
			navigateToUrl(url);
			txtBoxSendValue(userNameBy, userName);
			txtBoxSendValue(userPwdBy, pwd);
			eleClickBy(loginBtnBy);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * moa平台登陆
	 * @param userName
	 * @param pwd
	 * @throws Throwable
	 */
	By userNameBy1 = By.name("userName");//登录名输入框
	By userPwdBy1 = By.name("password");//登陆密码输入框
	By loginBtnBy1 = By.xpath("//button[@class='btn btn-block btn-primary']");//登陆按钮
	public void newLogin(String userName, String pwd) throws Throwable{
		try{
			navigateToUrl(url);
			txtBoxSendValue(userNameBy1, userName);
			txtBoxSendValue(userPwdBy1, pwd);
			eleClickBy(loginBtnBy1);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 打开制定的主界面业务模块
	 * @param buziName
	 * @throws Throwable
	 */
	public void buziOpen(String buziName) throws Throwable{
		try{
//			List<IWebElement> menuList = eleListsGet(By.xpath("//div[@class='ant-row production']/div/div/div[contains(text(), '" + buziName + "')]"));
			eleClickBy(By.xpath("//div[@class='ant-row production']/div/div/div[contains(text(), '" + buziName + "')]"));
			waitPageLoad(LOADING_TIME);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 通知窗口关闭
	 * @throws Throwable
	 */
	By notifyBoxCloseBtnBy = By.xpath("//div[@class='y-modal-add-email fade modal in' or "
			+ "@class='modal y-modal-add-email fade in']//button[@class='close']");
	public void notifyBoxClose() throws Throwable{
		while(eleDisplayChk(notifyBoxCloseBtnBy)){
			eleClickBy(notifyBoxCloseBtnBy);
		}
	}
	
	/**
	 * 加入多个公司的登录账户，登录时需手动选择进入的公司的窗口（不选择，直接确认关闭窗口）
	 */
	By popupBoxConfirmBy = By.xpath("//div[@class='popup popup-confirm']/div[@class='popup-footer']/button");//确认按钮
	public void loginPopUpBoxClose() throws Throwable{
		while(eleDisplayChk(popupBoxConfirmBy)){
			eleClickBy(popupBoxConfirmBy);
		}
	}
	
	public void loginPopUpBoxWithCompany(String cid) throws Throwable{
		if(eleDisplayChk(popupBoxConfirmBy)){
			eleClickBy(By.name("companyId"));
			comboValueSet(By.xpath("//select[@name='companyId']/option"), cid);
			eleClickBy(popupBoxConfirmBy);
		}
	}
	
	/**
	 * 账户激活窗口关闭
	 * @throws Throwable
	 */
	By accountActiveBoxCloseBtnBy = By.xpath("//div[@class='modal y-modal-add-email fade in']//button[@class='close']");
	public void accountActiveBoxClose() throws Throwable{
		if(eleDisplayChk(accountActiveBoxCloseBtnBy)){
			eleClickBy(accountActiveBoxCloseBtnBy);
		}
	}
}
