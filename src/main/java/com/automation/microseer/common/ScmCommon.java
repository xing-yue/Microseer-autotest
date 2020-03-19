package com.automation.microseer.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebElement;
import com.automation.framework.action.IWebHandler;
import com.automation.framework.utils.AutoItUtil;
import com.automation.framework.utils.DBUtil;
import com.automation.framework.utils.ExcelUtil;
import com.automation.framework.utils.FileUtil;
import com.automation.framework.utils.IdCardGen;
import com.automation.framework.utils.Log4jUtil;
import com.automation.framework.utils.DBUtil.DBType;

/**
 * @Description:scm公共类 
 * @ClassName: ScmAPICommon
 * @author panyongjun
 * @date 2019年8月1日
 */
public class ScmCommon extends MicroseerCommon {
	
	public ScmCommon(IWebHandler handler){
		this.handler = handler;
	}
	
	protected int GRID_LOADING_TIME = 30;
	Log4jUtil logger = new Log4jUtil();
	AutoItUtil autoIt = new AutoItUtil();
	ExcelUtil excel = new ExcelUtil();
	FileUtil file = new FileUtil();
	IdCardGen idGen = new IdCardGen();
	protected static int LOADING_TIME = 20;

	
	/**
	 * 获取可报增的截止日期
	 * @return
	 * @throws Throwable
	 */
	public String operateEndDayGet() throws Throwable{
		DBUtil dbUtil = new DBUtil(DBType.SQLServer);
		dbUtil.connect(dbUrl, dbUser, dbPwd);//连接数据库
		//获取“自动化测试成本中心”（5295）可报增减的最后截止日
		int sqlResult = dbUtil.queryForInt("SELECT operateEnd FROM [ms_scm].[dbo].[schemeSsPf] where costCenterId='5295'");
		dbUtil.close();
		return String.valueOf(sqlResult);
	}
	
	/**
	 * 获取汇缴月返回值，2018-09
	 * @param theEndDay
	 * @return
	 * @throws Throwable
	 */
	public String payForMonth(String theEndDay) throws Throwable{
		String payMonth = null;
		GregorianCalendar gCal = new GregorianCalendar();
		//汇缴日大于截止日，报增时，汇缴月为下月
		System.out.println(gCal.get(Calendar.DAY_OF_MONTH));
		if(gCal.get(Calendar.DAY_OF_MONTH) > Integer.valueOf(theEndDay)){
			gCal.add(Calendar.MONTH, 1);
		}
		//获取汇缴月返回值
		System.out.println(gCal.get(Calendar.MONTH) + 1);
		if((gCal.get(Calendar.MONTH)+1) > 10){
			payMonth = gCal.get(Calendar.YEAR) + "-" + (gCal.get(Calendar.MONTH) + 1);
		}else{
			payMonth = gCal.get(Calendar.YEAR) + "-0" + (gCal.get(Calendar.MONTH) + 1);
		}
		return payMonth;
	}
	
	/**
	 * 批量增减员，从数据库ms_microseer中的operationFlow表中比对当前流程状态
	 * @param statusCode，状态码
	 * @return
	 * @throws Throwable
	 */
	public String excelPath = System.getProperty("user.dir") + "\\testData\\workflow\\批量导入.xls";
	public boolean operateFlowStatusChkFromDB(int statusCode) throws Throwable{
		boolean statusInDB = false;
		ExcelUtil excel = new ExcelUtil();
		List<Object> excelCardCodeList = excel.readExcelByColumn(excelPath, 5);//读取批量导入excel表中的证件信息
		excelCardCodeList.remove(0);//去头
		DBUtil dbUtil = new DBUtil(DBType.SQLServer);
		dbUtil.connect(dbUrl, dbUser, dbPwd);//连接数据库，通过数据库表[operationFlow]的status判断当前雇员状态
		for(Object excelCardCode:excelCardCodeList){
			int sqlResult = dbUtil.queryForInt("SELECT [status] FROM [ms_microseer].[dbo].[operationFlow] where empId = ("
					+ "SELECT [id] FROM [ms_microseer].[dbo].[employee] where cardCode='" + excelCardCode + "') "
					+ "order by id desc");
			if(sqlResult==statusCode){//状态码比对
				statusInDB = true;
				continue;
			}else{
				statusInDB = false;
				break;
			}
		}
		dbUtil.close();
		return statusInDB;
	}
	
	/**
	 * 批量增减员，从数据库ms_microseer中的operationFlow表中比对当前流程状态
	 * @param statusCode
	 * @return
	 * @throws Throwable
	 */
	public boolean employeeStatusChkFromDB(int statusCode) throws Throwable{
		boolean statusInDB = false;
		ExcelUtil excel = new ExcelUtil();
		List<Object> excelCardCodeList = excel.readExcelByColumn(excelPath, 5);//读取批量导入excel表中的证件信息
		excelCardCodeList.remove(0);//去头
		DBUtil dbUtil = new DBUtil(DBType.SQLServer);
		dbUtil.connect(dbUrl, dbUser, dbPwd);//连接数据库，通过数据库表[operationFlow]的status判断当前雇员状态
		for(Object excelCardCode:excelCardCodeList){
			int sqlResult = dbUtil.queryForInt("SELECT [ssStatus] FROM [ms_ehr].[dbo].[employee] where cardCode = '" + excelCardCode + "'");
			if(sqlResult==statusCode){//状态码比对
				statusInDB = true;
				continue;
			}else{
				statusInDB = false;
				break;
			}
		}
		dbUtil.close();
		return statusInDB;
	}
	
	/**
	 * 进度条判断及输出
	 */
	By progressBarTxtBy = By.xpath("//div[@role='dialog']//"
			+ "div[contains(@class, 'x-progress-text')]");
	By progressBarBy = By.xpath("//div[@role='dialog']//"
			+ "div[@role='progressbar']");
	public void progressLogger() throws Throwable {
		try{
			while(eleDisplayChk(progressBarBy)){
				try{
					logger.info(eleTxtGet(By.xpath("//div[@role='dialog']")));
					String progressTxt = eleTxtGet(progressBarTxtBy);
					if(progressTxt.contains("/")){
						int total = Integer.valueOf(progressTxt.split("/")[1]);
						logger.info("总数为："  + total);
						if(Integer.valueOf(progressTxt.split("/")[0])!=total){
							Thread.sleep(1000);
							progressTxt = eleTxtGet(progressBarTxtBy);
							logger.info("进度："  + progressTxt);
						}else{
							break;
						}
					}
					if(progressTxt.equals("撤销完成")){
						break;
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			if(!eleDisplayChk(progressBarBy)){
				logger.info("进度条已消失，完成100%");
			}else{
				Thread.sleep(5000);//某些进度条中会出现统计，比如反馈、审核等场景，对这种场景做等待
				logger.info("进度："  + eleTxtGet(By.xpath("//div[@role='dialog']")));
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 批量增员数据生成
	 * @param excelPath
	 * @throws Throwable
	 */
	public void employeeExcelGen(String costProvince, String costCity, String costSuite,  
			boolean cardTypeIsEmpty, boolean cardCodeIsEmpty, boolean userNameIsEmpty, boolean mobileIsEmpty, 
			String costBase, boolean startDateIsEmpty, boolean trueDateIsEmpty, boolean accountTypeIsEmpty, 
			String excelPath) throws Throwable {
		try{
			File excelFile = new File(excelPath);
			file.fileCopy(excelPath, excelFile.getParent() + "/批量导入数据备份/" + systemCurrentDate("yyyyMMdd") + "_" + 
					Calendar.getInstance().getTimeInMillis() + "_" + excelFile.getName());
			String oriExcelPath = System.getProperty("user.dir") + "\\testData\\workflow\\other_model.xlsx";
			file.deleteFile(excelPath);
			ArrayList<Object> excelHeaderList = excel.readExcelByRow(oriExcelPath, 1);//从“other_model”表中读取表头
			
			//批量导入表内容写入
			String defaultDate = payForMonth(operateEndDayGet()) + "-" + systemCurrentDate("yyyy-MM-dd").split("-")[2];
			ArrayList<ArrayList<Object>> excelDoubleArrayList = new ArrayList<ArrayList<Object>>();
			excelDoubleArrayList.add(excelHeaderList);
			for(int i=0; i<10; i++){
				ArrayList<Object> excelContentList = new ArrayList<Object>();
				excelContentList.add(costProvince);
				excelContentList.add(costCity);
				excelContentList.add(costSuite);
				if(cardTypeIsEmpty){
					excelContentList.add("");
				}else{
					excelContentList.add("护照");
				}
				if(cardCodeIsEmpty){
					excelContentList.add("");
				}else{
					excelContentList.add("auto_" + idGen.generate().substring(5));
				}
				if(userNameIsEmpty){
					excelContentList.add("");
				}else{
					excelContentList.add(userNameGet());
				}
				if(mobileIsEmpty){
					excelContentList.add("");
				}else{
					excelContentList.add(phoneNumberGet());
				}
				excelContentList.add(costBase);
				if(startDateIsEmpty){
					excelContentList.add("");
				}else{
					excelContentList.add(defaultDate);
				}
				if(trueDateIsEmpty){
					excelContentList.add("");
				}else{
					excelContentList.add(defaultDate);
				}
				if(accountTypeIsEmpty){
					excelContentList.add("");
				}else{
					excelContentList.add("新开户");
				}
				excelDoubleArrayList.add(excelContentList);
			}
			excel.writeExcel(excelDoubleArrayList, excelPath);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 批量增员数据生成
	 * @param excelPath
	 * @throws Throwable
	 */
	public void employeeExcelGen(String excelPath) throws Throwable {
		try{
			employeeExcelGen(province, city, suite, false, false, 
					false, false, base, false, false, false, excelPath);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
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
	 * 批量增员excel表复制
	 * @throws Throwable
	 */
	public void employeeExcelCopy(String excelPath, String autoItExePath) throws Throwable{
		try{
			autoIt.runAu3(autoItExePath, autoItExePath.split("/")[autoItExePath.split("/").length-1]);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 输出比对结果
	 * @param result
	 * @param message
	 */
	public void loggerInfo(boolean result, String message) {
		if(result){
			logger.info(message + "比对结果为true");
		}else{
			logger.info(message + "比对结果为false");
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
	 * 接单中心提示接单窗口关闭
	 */
	public void orderNofifyClose() throws Throwable{
		try{
			eleClickBy(By.partialLinkText("稍后再说"));
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 根据检索框placeholder的值，输入userNameOrUserId，进行检索
	 * @param placeholderTxt
	 * @param userNameOrUserId
	 * @throws Throwable
	 */
	public void queryWithIdOrName(String placeholderTxt, String userNameOrUserId) throws Throwable {
		try{
			String inputBoxId = eleAttrValueGet(By.xpath("//input[@placeholder='" + placeholderTxt + "']"), "id");
			txtBoxSendValue(By.id(inputBoxId), userNameOrUserId);
			eleClickBy(By.id(inputBoxId.replace("inputEl", "trigger-search")));
//			gridContentLoading(GRID_CONTENT_LOADING_TIME);
			pageLoadingWait(LOADING_TIME);
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
	 * 对话框栏目中，根据表头获取相应栏目的id
	 * @param columnName
	 * @return
	 * @throws Throwable
	 */
	public String dialogGridColumnIdGet(String columnName) throws Throwable{
		String columnId = null;
		try{
			List<IWebElement> gridColumnHeaderList = eleListsGet(By.xpath("//div[@role='dialog']//"
					+ "div[@role='columnheader']"));
			for(IWebElement gridColumnHeader:gridColumnHeaderList){
				if(gridColumnHeader.getText().equals(columnName)){
					columnId = gridColumnHeader.getAttribute("data-componentid");
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return columnId;
	}
	
	/**
	 * 表格头栏目对应id获取
	 * @param columnName
	 * @return
	 * @throws Throwable
	 */
	public String gridColumnIdGet(String columnName) throws Throwable{
		String columnId = null;
		try{
			List<IWebElement> columnList = eleListsGet(By.xpath("//div[@role='rowgroup']/div[@role='row']/div/div"));
			for(IWebElement column:columnList){
				String columnTxt = column.getText();
				if(columnTxt.equals(columnName)){
					columnId = column.getAttribute("data-componentid");
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return columnId;
	}
	
	/**
	 * 参保月份选择
	 * @param locator
	 * @throws Throwable
	 */
	public void payDateSet(By locator) throws Throwable{
		try{
			eleClickBy(locator);
			eleClickBy(By.partialLinkText("确定"));
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
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
	 * 展开指定的父级菜单栏目名称及其子菜单栏名称
	 * @param rootMenuTxt
	 * @param subMenuTxt
	 * @throws Throwable
	 */
	public void menuOpen(String rootMenuTxt, String subMenuTxt) throws Throwable{
		try{
			if(!rootMenuTxt.equals("")){
				String menuId = rootMenuOpen(rootMenuTxt);
				if(!subMenuTxt.equals("")){
					List<IWebElement> subMenuList = eleListsGet(By.xpath("//li[@id='" + menuId + "']/ul[@class='x-treelist-container']/li"));
					for(IWebElement subMenu:subMenuList){
						String eleTxt = subMenu.getText().replace(" ", "");
						if(eleTxt.equals(subMenuTxt)){
							subMenu.click();
							logger.info("点击子菜单'" + eleTxt + "'");
							Thread.sleep(1000);
							break;
						}
					}
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 展开指定的菜单栏
	 */
	By menuBtnBy = By.xpath("//ul[@class='x-treelist-root-container']/li");
	public String rootMenuOpen(String menuTxt) throws Throwable {
		String menuBtnId = "";
		try{
			List<IWebElement> menuList = eleListsGet(menuBtnBy);
			for(IWebElement menu:menuList){
				String eleTxt = menu.getText().replace(" ", "");
				if(eleTxt.equals(menuTxt)){
					menuBtnId = menu.getAttribute("id");
					menu.click();
					logger.info("点击菜单'" + eleTxt + "'");
					waitPageLoad(30);
					break;
				}
			}
			if(menuTxt.equals("参保审核") || 
					menuTxt.equals("进度查询") || 
					menuTxt.equals("基数采集") || 
					menuTxt.equals("账单管理") || 
					menuTxt.equals("工单中心") || 
//					menuTxt.equals("参保进度") || 
					menuTxt.equals("雇员管理") || 
					menuTxt.equals("反馈中心") || 
					menuTxt.equals("参保名册")){
				gridContentLoading(LOADING_TIME);
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return menuBtnId;
	}
	
	/**
	 * 展开指定的子菜单
	 */
	By subMenuBtnBy = By.xpath("//ul[@class='x-treelist-container']/li");
	public void subMenuOpen(String subTxt) throws Throwable {
		try{
			List<IWebElement> subMenuList = eleListsGet(subMenuBtnBy);
			for(IWebElement subMenu:subMenuList){
				String eleTxt = subMenu.getText().replace(" ", "");
				if(eleTxt.equals(subTxt)){
					subMenu.click();
					logger.info("点击子菜单'" + eleTxt + "'");
					Thread.sleep(1000);
					break;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
}
