package com.jenkinsreport.jenkinsreportpage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import com.automation.framework.action.IWebHandler;
import com.automation.framework.utils.CharUtil;
import com.automation.framework.utils.CommonUtil;
import com.automation.framework.utils.ExcelUtil;
import com.automation.framework.utils.FileUtil;
import com.automation.microseer.common.MicroseerCommon;
import com.automation.framework.utils.Dom4jUtil;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class JenkinsReportPage extends MicroseerCommon{

	public JenkinsReportPage(IWebHandler handler){
		this.handler = handler;
	}
	
	public int jenkinsTotalCaseNumber = 0;
	public int jenkinsPassCaseNumber = 0;
	public int jenkinsFailCaseNumber = 0;
	public int jenkinsSkipCaseNumber = 0;
	public Double jenkinsPassPercent = 0.0000d;
	public String tomcatPath = "";
	public String jobName = "";
	public String jenkinsHostIP = null;
//	public Object[] subJobArray = null;
	public String resultFolder = null;
	public String rootJobName = null;
//	public String rootJobResult = null;
	
	FileUtil fileUtil = new FileUtil();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	CommonUtil commUtil = new CommonUtil();
	ExcelUtil excelUtil = new ExcelUtil();
	
	By writeMailLinkBy = By.linkText("写邮件");//写邮件
	By mailToBy = By.name("to");//收件人
	By subjectBy = By.name("subject");//邮件主题
	By sendMailBtnBy = By.name("bt_sendmail2");//邮件发送按钮
	By attachFileBy = By.xpath("//html/body/table[4]/tbody/tr/td[3]/table/tbody[4]/tr[2]/td[2]/a[1]");//增加附件
	By fileBrowserBy = By.name("userfile1");//选择文件
	By attachUploadBy = By.name("upbtn");//附件上传按钮
	By attachBoxCloseBtnBy = By.name("close");//附件窗口关闭按钮
	By bodyBy = By.name("body");//邮件内容
	By pageNumBy = By.xpath("//html/body/table[4]/tbody/tr/td[3]/table/tbody/tr[4]/td[2]/font[2]");//收件箱共几页页数
	public Object[] testModuleArray = null;
	
	
	public String caseNumRead(String module, Element passTable) throws Throwable {
		String caseNum = "";
		try{
			if(passTable != null){
				if(module.contains("总用例")){
					caseNum = passTable.getElementsByTag("td").get(1).text();
				}
//				if(module.contains("大宗商品")){
//					caseNum = String.valueOf(Integer.valueOf(passTable.getElementsByTag("td").get(2).text()) + 
//							Integer.valueOf(passTable.getElementsByTag("td").get(3).text()) + 
//							Integer.valueOf(passTable.getElementsByTag("td").get(4).text()));
//				}
			}else{
				caseNum = "0";
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return caseNum;
	}
	
	/**
	 * 收集最近一段时间用例执行情况
	 * @param dateList
	 * @param module
	 * @return
	 * @throws Throwable
	 */
	public String caseNumGet(List<String> dateList, String module) throws Throwable {
		String passCaseNum = "";
		try{
			Element passTable = null;
			for(String date:dateList){
				if(systemCurrentDate("yyyyMMdd").equals(date)){
					passCaseNum += jenkinsPassCaseNumber;
				}else{
//					reportPath = tomcatPath + "\\webapps\\" + date + "\\index.html";
					File f = new File(tomcatPath + "\\webapps\\" + date + "\\" + resultFolder + "\\index.html");
					if(f.exists()){
						try{
							Document doc = Jsoup.parse(f, "UTF-8", "");//("http://" + jenkinsHostIP + ":8080/" + date + "/" + resultFolder + "/index.html").get();				
							passTable = doc.getElementsByAttributeValue("class", "bg2").get(0);
						}catch(Throwable e){
							e.printStackTrace();
						}
						passCaseNum += caseNumRead(module, passTable) + ",";
					}else{
						passCaseNum += "0" + ",";
					}
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return passCaseNum;
	}
	
	public String caseTrend(Object[] moduleArray) throws Throwable {
		String trendDivStr = "";
		try{
			trendDivStr = moduleTrendDiv("totalTrend", "总用例");
//			if(strIsIncludedInArray("大宗商品", moduleArray)){
//				trendDivStr += moduleTrendDiv("wareTrend", "大宗商品");
//			}
//			
//			trendDivStr += moduleTrendDiv("careerTrend", "招聘");
//			trendDivStr += moduleTrendDiv("counterTrend", "债券柜台");
//			trendDivStr += moduleTrendDiv("cdTrend", "大额存单");
//			trendDivStr += moduleTrendDiv("searchTrend", "网站检索");
//			trendDivStr += moduleTrendDiv("infoTrend", "信息披露");
//			trendDivStr += moduleTrendDiv("rateTrend", "利率互换");
//			trendDivStr += moduleTrendDiv("valuationTrend", "估值");
//			trendDivStr += moduleTrendDiv("priceTrend", "报价");
//			trendDivStr += moduleTrendDiv("bjClearingTrend", "北金所");
//			trendDivStr += moduleTrendDiv("rcmTrend", "风控");
//			trendDivStr += moduleTrendDiv("trainTrend", "培训报名");
//			trendDivStr += moduleTrendDiv("nonClearingTrend", "非清");
//			trendDivStr += moduleTrendDiv("memberTrend", "会员服务");
//			trendDivStr += moduleTrendDiv("englishTrend", "英文");
//			trendDivStr += moduleTrendDiv("dataOVTrend", "数据概览");
//			trendDivStr += moduleTrendDiv("securityTrend", "漏洞安全");
//			trendDivStr += moduleTrendDiv("salonTrend", "沙龙");
//			trendDivStr += moduleTrendDiv("lineTrend", "专线");
//			trendDivStr += moduleTrendDiv("breachTrend", "违约");
//			trendDivStr += moduleTrendDiv("standBondTrend", "标准债");
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return trendDivStr;
	}
	
	/**
	 * 根据当前日期+\-获取格式为"yyyyMMdd"的日期
	 * @param days
	 * @return
	 * @throws Throwable
	 */
	public String dateStrGet(int days) throws Throwable {
		String dateStr = "";
		try{
			List<String> oneMonthDateList = dateListGet("yyyyMMdd", days);
			for(String date:oneMonthDateList){
				if(date.equals(oneMonthDateList.get(oneMonthDateList.size() - 1))){
					dateStr += "'" + date + "'";
				}else{
					dateStr += "'" + date + "', ";
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return dateStr;
	}
	
	/**
	 * 饼图生成
	 * @param fail
	 * @param pass
	 * @param skip
	 * @return
	 * @throws Throwable
	 */
	public String totalCaseResultPie(String fail, String pass, String skip) throws Throwable {
		String pieDivStr = "";
		try{
			String failColor = "RGBA(255,69,0,1)";
			String passColor = "RGBA(100,205,50,1)";
			String skipColor = "RGBA(255,215,0,1)";
			if(jenkinsPassPercent < 0.9){
				passColor = "RGBA(255,255,0,1)";
			}
			pieDivStr = "<div id=\"my_container\" style=\"height:400px; width:auto;\" >	\r"
					+ "	<!-- ECharts单文件引入 --> \r"
					+ "	<script src=\"js/echarts.js\"></script> \r"
					+ "	<script type=\"text/javascript\"> \r"
					+ "		var myChart = echarts.init(document.getElementById(\"my_container\")); \r"
					+ "		var option = { \r"
					+ "			series : [ \r"
					+ "			{ \r"
					+ "				name: '访问来源', \r"
					+ "				type: 'pie', \r"
					+ "				radius : '85%', \r"
					+ "				data:[ \r"
					+ "					{value:" + fail + ", name:'fail'}, \r"
					+ "					{value:" + pass + ", name:'pass'}, \r"
					+ "					{value:" + skip + ", name:'skip'},], \r"
					+ "				itemStyle: { \r"
					+ "					normal: {label: {textStyle: { \r"
					+ "						// 用 itemStyle.normal.label.textStyle.fontSize 來更改饼图字体大小  \r"
					+ "						fontSize: 30 \r"
					+ "					} \r"
					+ "				} \r"
					+ "			}}}], \r"
					+ "			color: ['" + failColor + "','" + passColor + "','" + skipColor + "'] \r"
					+ "		}; \r"
					+ "		myChart.setOption(option); \r"
					+ "	</script> \r"
					+ "</div> \r";
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return pieDivStr;
	}
	
	/**
	 * 生成case执行走势图
	 * @param divId
	 * @param trendName
	 * @return
	 * @throws Throwable
	 */
	public String moduleTrendDiv(String divId, String trendName) throws Throwable {
		String divStr = "";
		try{			
			divStr = "<div id=\"" + divId + "\" style=\"height:200px; width:auto;\" > \r"
					+ "	<!-- ECharts单文件引入 --> \r"
					+ "	<script src=\"js/echarts.js\"></script> \r"
					+ "	<script type=\"text/javascript\"> \r"
					+ "	var myChart = echarts.init(document.getElementById(\"" + divId + "\")); \r"
					+ "	var option = { \r"
					+ "		title :  \r"
					+ "			{text: \'" + trendName + "执行走势\',subtext: ''}, \r"
					+ "		tooltip : {trigger: 'axis'}, \r"
					+ "		legend: {data:['','']}, \r"
					+ "		toolbox: {"
					+ "			show : false,"
					+ "			feature : {mark : {show: true}, \r"
					+ "			dataView : {show: true, readOnly: false}, \r"
					+ "			magicType : {show: true, type: ['line', 'bar']}, \r"
					+ "			restore : {show: true}, \r"
					+ "			saveAsImage : {show: true} \r"
					+ "		} \r"
					+ "	}, \r"
					+ "	calculable : false, \r"
					+ "	xAxis : [ \r"
					+ "	{ \r"
					+ "		type : 'category', \r"
					+ "		boundaryGap : false, \r"
					+ "		data : [" + dateStrGet(-30) + "] \r"
					+ "	}], \r"
					+ "	yAxis : [ \r"
					+ "	{ \r"
					+ "		type : 'value', \r"
					+ "		axisLabel : { \r"
					+ "		formatter: '{value} ' \r"
					+ "		} \r"
					+ "	}], \r"
					+ "	series : [ \r"
					+ "	{ \r"
					+ "		name:'', \r"
					+ "		type:'line', \r"
					+ "		data:[" + caseNumGet(dateListGet("yyyyMMdd", -30), trendName) + "], \r"
					+ "		itemStyle : { normal: {label : {show: true}}}, \r"
					+ "	}] \r"
					+ "	}; \r"
					+ "	myChart.setOption(option); \r"
					+ "	myChart.on('click', function (param){  \r"
					+ "		var name=param.name;  \r";
			String myChartClickEventStr = "";
			for(String date:dateListGet("yyyyMMdd", -30)){
				myChartClickEventStr += myChartClickEventSet(date);
			}
			divStr += myChartClickEventStr + "	});  \r"
					+ "	myChart.on('click',eConsole);  \r"
					+ "	</script> \r"
					+ "</div> \r";
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return divStr;
	}
	
	/**
	 * 用例执行趋势图点击链接生成
	 * @param date
	 * @return
	 * @throws Throwable
	 */
	public String myChartClickEventSet(String date) throws Throwable {
		String myChartClickEventStr = "";
		try{
			if(systemCurrentDate("yyyyMMdd").equals(date)){
				myChartClickEventStr = 
						  "		if(name==\"" + date + "\"){  \r"
						+ "			window.location.href=\"http://" + jenkinsHostIP + ":8080/" + resultFolder + "/index.html\";  \r"
						+ "		} \r";
			}else{
				myChartClickEventStr = 
						  "		if(name==\"" + date + "\"){  \r"
						+ "			window.location.href=\"http://" + jenkinsHostIP + ":8080/" + date + "/" + resultFolder + "/index.html\";  \r"
						+ "		} \r";
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return myChartClickEventStr;
	}
	
//	/**
//	 * 添加测试报告截图附件
//	 * @param attachFile
//	 * @throws Throwable
//	 */
//	public void addTestReport(String resultScreenShot) throws Throwable{
//		try {
//			String oriHandle = this.handler.getWindowHandle();
//			eleClickBy(attachFileBy);
//			Thread.sleep(2000);
//			this.handler.switchToNewWindowFromDefault(oriHandle);
//			eleClickBy(fileBrowserBy);
//			fileUpload(resultScreenShot);
//			eleClickBy(attachUploadBy);
//			Thread.sleep(2000);
//			eleClickBy(attachBoxCloseBtnBy);
//			Thread.sleep(1000);
//		} catch (Exception e) {
//			exceptionErrorHandle(e);
//		}
//	}
	
//	/**
//	 * 测试结果发送
//	 * @param mailTo
//	 * @param mailSubject
//	 * @throws Throwable
//	 */
//	public void sendReport(String mailTo, String resultScreenShot) throws Throwable{
//		try {
//			if(winMailNeedClear()){
//				winMailBoxClear();
//			}
//			String oriHandle = this.handler.getWindowHandle();
//			eleClickBy(writeMailLinkBy);//点击收件箱
//			Thread.sleep(2000);		
//			txtBoxSendValue(mailToBy, mailTo);
//			txtBoxSendValue(subjectBy, systemCurrentDate("yyyy/MM/dd") + "自动化测试报告");
//			addTestReport(resultScreenShot);//添加附件（测试报告压缩包）
//			this.handler.switchToWindowByHandle(oriHandle);
//			String mailDes = "附件为测试报告截图，详情请查看链接："
//					+ "测试报告：http://" + jenkinsHostIP + ":8080/testResultReport/index.html "
//					+ "Job详情：http://" + jenkinsHostIP + ":8080/jenkins/";
//			txtBoxSendValue(bodyBy, mailDes);
//			eleClickBy(sendMailBtnBy);
//			Thread.sleep(2000);
//		} catch (Exception e) {
//			exceptionErrorHandle(e);
//		}
//	}
	
	/**
	 * 备份测试结果到指定的文件夹下
	 * @param testResultFolderName
	 * @throws Throwable
	 */
	public void backUpYesterdayTestResult(String testResultFolderName) throws Throwable {
		String testResultFolderPath = tomcatPath + "\\webapps\\" + testResultFolderName;
		String resultBackupPath = tomcatPath + "\\webapps\\" + systemCurrentDate(-1).replaceAll("-", "");
		fileUtil.folderCreate(resultBackupPath);
		fileUtil.copyAll(testResultFolderPath, resultBackupPath);
	}
	
	/**
	 * 删除指定天数之外的测试报告
	 * @throws Throwable
	 */
	public void deleteNotInRecentDaysResult(String testResultFolder, int days) throws Throwable {
		List<String> excludedTestResultFolderList = dateListGet("yyyyMMdd", days);
		File[] tomcatWebAppsFolderFileList = fileUtil.fileListGet(tomcatPath + "\\webapps\\" + testResultFolder);
		for(File tomcatWebAppsFolderFile:tomcatWebAppsFolderFileList){
			boolean isFolder = tomcatWebAppsFolderFile.isDirectory();
			String folderName = tomcatWebAppsFolderFile.getName();
			String folderRegex = "^\\d{4}\\d{1,2}\\d{1,2}";
			boolean isDateFolder = Pattern.compile(folderRegex).matcher(folderName).matches();
			if(isFolder && isDateFolder && !strIsIncludedInList(folderName, excludedTestResultFolderList)){
				fileUtil.deleteAll(tomcatWebAppsFolderFile.getPath());
			}
		}
	}
	
	/**
	 * 新报告生成前，备份上一次的测试报告
	 * @param testResultFolderName
	 * @throws Throwable
	 */
	public void oldResultBackup(String testResultFolderName) throws Throwable {
		deleteNotInRecentDaysResult(testResultFolderName, -30);
		backUpYesterdayTestResult(testResultFolderName);
	}
	
	/**
	 * 测试job获取
	 * @param multiJobName
	 * @return
	 * @throws Throwable
	 */
	public Object[] testModuleArrayGet(String multiJobName) throws Throwable {
		jobName = multiJobName;
		String jenkinsHome = System.getenv("JENKINS_HOME");
//		jenkinsHome = "\\\\10.30.92.109\\Jenkins\\jenkinsJob";
		String configXml = jenkinsHome + "\\jobs\\" + multiJobName + "\\config.xml";
		Dom4jUtil xmlUtil = new Dom4jUtil(configXml);
		List<String> testModuleList = xmlUtil.xmlAttributeRead("jobName");
		List<String> testModuleStatusList = xmlUtil.xmlAttributeRead("disableJob");
		List<String> testModuleListNew = new ArrayList<String>();
		for(int i=0; i<testModuleStatusList.size(); i++){
			if(testModuleStatusList.get(i).equalsIgnoreCase("false")){
//				testModuleList.remove(i);
				testModuleListNew.add(testModuleList.get(i));
			}
		}
		return testModuleListNew.toArray();
	}
	
	/**
	 * 测试报告生成
	 * @param testModuleArray
	 * @throws Throwable
	 */
	public void testReportGenerate(String htmlPath, String jsPath, Object[] testModuleArray) throws Throwable {
		for(Object testModule:testModuleArray){
			jenkinsTotalCaseNumber += Integer.valueOf(testModuleResultGet(testModule.toString()).get(0));
			jenkinsFailCaseNumber += Integer.valueOf(testModuleResultGet(testModule.toString()).get(2));
			jenkinsSkipCaseNumber += Integer.valueOf(testModuleResultGet(testModule.toString()).get(3));
		}
		jenkinsPassCaseNumber = jenkinsTotalCaseNumber - jenkinsFailCaseNumber - 
				jenkinsSkipCaseNumber;
		BigDecimal jenkinsPass = new BigDecimal(jenkinsPassCaseNumber);
		BigDecimal jenkinsToal = new BigDecimal(jenkinsTotalCaseNumber);
		jenkinsPassPercent = jenkinsPass.divide(jenkinsToal, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
//		reportJSGenerate(jenkinsPassCaseNumber, jenkinsFailCaseNumber, jenkinsSkipCaseNumber, jsPath);
		reportHtmlGenerate(htmlPath, testModuleArray);
	}
	
	public List<String> failCaseListGet(String xmlPath, String xmlNodeName) throws Throwable {
		List<String> failCaseList = new ArrayList<String>();
		try{
			Dom4jUtil xmlUtil = new Dom4jUtil(xmlPath);
			List<String> studoutList = xmlUtil.xmlAttributeRead(xmlNodeName);
			for(String studout:studoutList){
				System.out.println(studout);
				String failCaseName = studout.split("com\\.shclearing\\.automation\\.framework\\.base\\.AbstractWebBaseAction\\.whichCaseIsRun")[1];
				failCaseName = failCaseName.split("is start")[0];
				failCaseName = failCaseName.split("case")[1];
				if(!strIsIncludedInList(failCaseName, failCaseList)){
					failCaseList.add(failCaseName);
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return failCaseList;
	}
	
	/**
	 * 获取用例执行总数
	 * @param xmlPath
	 * @param xmlNodeName
	 * @return
	 * @throws Throwable
	 */
	public String getTotalNum(String xmlPath, String xmlNodeName) throws Throwable {
		String totalNum = "";
		try{
			Dom4jUtil xmlUtil = new Dom4jUtil(xmlPath);
			List<String> mumberList = xmlUtil.xmlAttributeRead(xmlNodeName);
			int total = 0;
			for(String number:mumberList){
				total += Integer.valueOf(number);
			}
			totalNum = String.valueOf(total);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return totalNum;
	}
	
	/**
	 * 获取相应job的测试结果，pass，fail，skip，total
	 * @param moduleName
	 * @return
	 * @throws Throwable
	 */
	public List<String> testModuleResultGet(String moduleName) throws Throwable {
		List<String> testResultList = new ArrayList<String>();
		String jenkinsHome = System.getenv("JENKINS_HOME");
//		jenkinsHome = "\\\\10.30.92.109\\Jenkins\\jenkinsJob";
		String buildNo = theLatestBuildNo(jenkinsHome + "\\jobs\\" + moduleName + "\\builds");
		String fail = getTotalNum(jenkinsHome + "\\jobs\\" + moduleName + "\\builds\\" + buildNo + "\\build.xml", "failCount");
		String skip = getTotalNum(jenkinsHome + "\\jobs\\" + moduleName + "\\builds\\" + buildNo + "\\build.xml", "skipCount");
		String total = getTotalNum(jenkinsHome + "\\jobs\\" + moduleName + "\\builds\\" + buildNo + "\\build.xml", "totalCount");

		if(Integer.valueOf(total) !=0 && !total.equalsIgnoreCase("") && 
				!fail.equalsIgnoreCase("") && 
				!skip.equalsIgnoreCase("")){
			int failCaseNumber = Integer.valueOf(fail);
			int skipCaseNumber = Integer.valueOf(skip);
			int totalCaseNumber = Integer.valueOf(total);
			int passCaseNumber = totalCaseNumber - failCaseNumber - skipCaseNumber;
			BigDecimal passCase = new BigDecimal(passCaseNumber);
			BigDecimal totalCase = new BigDecimal(totalCaseNumber);
			BigDecimal passRate = passCase.divide(totalCase, 5, BigDecimal.ROUND_HALF_DOWN);
			String pass = String.valueOf(passCaseNumber);
			String rate = passRate.toString();
			testResultList.add(total);
			testResultList.add(pass);
			testResultList.add(fail);
			testResultList.add(skip);
			testResultList.add(rate);
			testResultList.add(buildNo);
		}else{
			testResultList.add("0");
			testResultList.add("0");
			testResultList.add("0");
			testResultList.add("0");
			testResultList.add("0");
			testResultList.add("");
		}

		return testResultList;
	}
	
	/**
	 * jenkins构建号获取
	 * @param sPath
	 * @return
	 * @throws Throwable
	 */
	public String theLatestBuildNo(String sPath) throws Throwable {
		String buildNo = "";
		try{
		    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
		    if (!sPath.endsWith(File.separator)) {  
		        sPath = sPath + File.separator;  
		    }  
		    File dirFile = new File(sPath);  
		    File[] files = dirFile.listFiles();
		    List<String> modifyTimeList = new ArrayList<String>();
		    List<String> folderList = new ArrayList<String>();
		    for(File buildFile:files){
		    	if(buildFile.isDirectory()){
			    	long fileModifyTime = buildFile.lastModified();
			    	if(!(fileModifyTime == 0)){
			            Date modifyDate = new Date(fileModifyTime);
			            modifyTimeList.add(sdf.format(modifyDate));
			            folderList.add(buildFile.getName());
			    	}
		    	}
		    }
		    String theLatestDate = theLatestDate(modifyTimeList);
		    int index = strIndexGetFromList(modifyTimeList, theLatestDate);
		    buildNo = folderList.get(index);
		}catch (Exception ex) {
			exceptionErrorHandle(ex);
		}
		return buildNo;
	} 
	
	/**
	 * 获取最近一次的测试结果
	 * @param modifyTimeList
	 * @return
	 * @throws Throwable
	 */
	public String theLatestDate(List<String> modifyTimeList) throws Throwable {
		String theLastModifyTime = "";
		try{
			long theLastest = 0l;
            Calendar cal = Calendar.getInstance();
            long dateNow = cal.getTimeInMillis();
            long diffTime = 0l;
			for(int i = 0; i < modifyTimeList.size(); i++){
				long fileModifyTime = sdf.parse(modifyTimeList.get(i)).getTime();
				long roundDiffTime = (dateNow - fileModifyTime);
				if(i == 0){
					theLastest = fileModifyTime;
					diffTime = roundDiffTime;
				}else{
					if(roundDiffTime < diffTime){
						theLastest = fileModifyTime;
						diffTime = roundDiffTime;
					}
				}
			}
			theLastModifyTime = sdf.format(new Date(theLastest));
		}catch (Exception ex) {
			exceptionErrorHandle(ex);
		}
		return theLastModifyTime;
	}
	
	/**
	 * 失败用例中文名链接生成
	 * @param testModule
	 * @param htmlName
	 * @throws Throwable
	 */
	public void failCaseHtmlGenerate(Object testModule, String htmlName) throws Throwable {
		try{
			String jenkinsHome = System.getenv("JENKINS_HOME");
//			jenkinsHome = "\\\\10.30.92.109\\Jenkins\\jenkinsJob";
			String buildNo = theLatestBuildNo(jenkinsHome + "\\jobs\\" + testModule + "\\builds");
//			String dateNow = systemCurrentDate("yyyy-MM-dd");
			String moduleFolder = tomcatPath + "\\webapps\\fail\\" + systemCurrentDate("yyyy-MM-dd");
			String htmlFolder = moduleFolder + "\\" + testModule;
			fileUtil.folderCreate(moduleFolder);
			fileUtil.folderCreate(htmlFolder);
			String htmlPath = htmlFolder + "\\" + htmlName;
			String htmlStr = "<html> \r"
					+ "<head> \r"
					+ "	<meta charset=utf-8> \r"
					+ "	<title>" + testModule + "失败用例</title> \r"
					+ "</head> \r"
					+ "<body> \r";
			String failCaseStr = "";
			
			Dom4jUtil xml = new Dom4jUtil(jenkinsHome + "\\jobs\\" + testModule + "\\builds\\" + buildNo + "\\build.xml");
			List<String> subJobList = xml.getNodeTextList("/com.tikal.jenkins.plugins.multijob.MultiJobBuild/subBuilds/"
					+ "com.tikal.jenkins.plugins.multijob.MultiJobBuild_-SubBuild/jobName");
			List<String> failCaseListTotal = new ArrayList<String>();
			if(subJobList.size()>0){
				for(String subJob:subJobList){
					String subJobBuildNo = theLatestBuildNo(jenkinsHome + "\\jobs\\" + subJob + "\\builds");
					failCaseListTotal = failCaseListGet(jenkinsHome + "\\jobs\\" + subJob + 
							"\\modules\\com.shclearing.auto$shclearing-auto\\builds\\" + 
							subJobBuildNo + "\\junitResult.xml", "stdout");
					for(String failCase:failCaseListTotal){
						failCaseStr += "	<br>" + subJob + "-" + failCase + "</br> \r";
					}
				}
			}else{
				failCaseListTotal = failCaseListGet(jenkinsHome + "\\jobs\\" + testModule + 
						"\\modules\\com.shclearing.auto$shclearing-auto\\builds\\" + 
						buildNo + "\\junitResult.xml", "stdout");
				for(String failCase:failCaseListTotal){
					failCaseStr += "	<br>" + failCase + "</br> \r";
				}
			}

			htmlStr = htmlStr + failCaseStr
					+ "</body> \r"
					+ "</html> \r";
			contentWrite(htmlPath, htmlStr);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 根据指定的job数组，生成测试报告html文件
	 * @param htmlPath
	 * @param testModuleArray
	 * @throws Throwable
	 */
	public void reportHtmlGenerate(String htmlPath, Object[] testModuleArray) throws Throwable {
		try{
			String dateNow = systemCurrentDate("yyyy-MM-dd");
			System.out.println(htmlPath);
			fileUtil.deleteFile(htmlPath);
			
			String htmlStr = "<html> \r"
					+ "<style> \r"
					+ "		body{ text-align:center}.div{ margin:0 auto;} \r"
					+ "</style> \r"
					+ "<head> \r"
					+ "	<meta charset=\"utf-8\"> \r"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/new_file.css\"> \r"
					+ "	<script src=\"js/pie.js\"> \r"
					+ "	</script> \r"
					+ "	<title>网站自动化测试报告</title> \r"
					+ "</head> \r"
					+ "<body> \r"
					+ "	<div id=\"header\"> \r"
					+ "		<div id=\"logo\"> \r"
//					+ "			<p>"
//					+ "				<a href=\"http://" + jenkinsHostIP + ":8080/" + systemCurrentDate(-1).replaceAll("-", "") + "//" + testResultFolder + "//index.html\" title=\"跳转到昨天的测试结果\"><font color=blue>" + systemCurrentDate(-1) + "</font></a> \r"
//					+"				<a href=\"http://" + jenkinsHostIP + ":8080/" + testResultJump + "/index.html\" title=\"跳转到" + moduleName + "\"><font color=blue>" + moduleName + "</font></a> \r"
//					+ "			</p>"
					+ "			<h1>"					
					+ "				<a href=\"http://" + jenkinsHostIP + ":8080/testResultReport/index.html\" title=\"跳转到最新的自动化测试报告页\">自动化测试报告（ " + dateNow + "）</a> \r"
					+ "			</h1> \r"
//					+ "		  <a href=\"http://" + jenkinsHostIP + ":8080/" + systemCurrentDate(-1).replaceAll("-", "") + "/" + testResultFolder + "/index.html\" title=\"跳转到前一天生成的测试报告\"><font color=blue>前一天</font></a> \r"
//					+ "		  <a href=\"http://" + jenkinsHostIP + ":8080/" + systemCurrentDate(+1).replaceAll("-", "") + "/" + testResultFolder + "/index.html\" title=\"跳转到后一天生成的测试报告\"><font color=blue>后一天</font></a> \r"
					+ "		</div> \r"
					+ "	</div> \r"
					+ totalCaseResultPie(String.valueOf(jenkinsFailCaseNumber), 
							String.valueOf(jenkinsPassCaseNumber), 
							String.valueOf(jenkinsSkipCaseNumber))
					+ caseTrend(testModuleArray)
					+ "<div class=\"tab_width\" style=\"margin:auto; width:auto;\"> \r"
					+ "	<b class=\"t1\"></b> \r"
					+ "	<b class=\"t2\"></b> \r"
					+ "	<b class=\"t3\"></b> \r"
					+ "	<b class=\"t4\"></b> \r"
					+ "	<div class=\"tab\"> \r"
					+ "		<table align=\"center\"> \r"
					+ "			<tr> \r"
					+ "				<td>模块</td> \r"
					+ "				<td>case总计</td> \r";
					for(Object testModule:testModuleArray){
//						<a href="http://" + jenkinsHostIP + ":8080/jenkins/job/大宗商品(Fcp-Sheaf-Beaf)" title="跳转到大宗商品(Fcp-Sheaf-Beaf)jenkins详情页">
						htmlStr += "				<td> \r";
						if(strIsIncludedInList(testModule.toString(), multiJobList)){
							htmlStr += "					<a href=\"http://" + jenkinsHostIP + ":8080/" + zhToPinyinAbbr(testModule.toString()) + "/index.html\" title=\"" + "跳转到" + testModule.toString() + "详情\"> \r";
						}else{
							htmlStr += "					<a href=\"http://" + jenkinsHostIP + ":8080/jenkins/job/" + testModule.toString() + "/" + testModuleResultGet(testModule.toString()).get(5) + "\" title=\"" + "跳转到" + testModule.toString() + "\"> \r";
						}
						
//						if(strIsIncludedInArray(testModule.toString(), subJobArray)){
//							htmlStr += "					<a href=\"http://" + jenkinsHostIP + ":8080/" + testResultFolder + "/index.html\" title=\"" + "跳转到" + testModule.toString() + "详情\"> \r";
//						}else{
//							htmlStr += "					<a href=\"http://" + jenkinsHostIP + ":8080/jenkins/job/" + testModule.toString() + "/" + testModuleResultGet(testModule.toString()).get(5) + "\" title=\"" + "跳转到" + testModule.toString() + "\"> \r";
//						}
						
						htmlStr += "					" + testModule.toString();
						htmlStr += "					</a> \r";
						htmlStr += "				</td> \r";
					}
					htmlStr += "			</tr> \r"
					+ "			<tr class=\"bg1\"> \r"
					+ "				<td>Total</td> \r"
					+ "				<td>" + jenkinsTotalCaseNumber + "</td> \r";
					for(Object testModule:testModuleArray){
						htmlStr += "				<td>" + Integer.valueOf(testModuleResultGet(testModule.toString()).get(0)) + "</td> \r";
					}
					htmlStr += "			</tr> \r"
					+ "			<tr class=\"bg2\"> \r"
					+ "				<td>Pass</td> \r"
					+ "				<td>" + jenkinsPassCaseNumber + "</td> \r";
					for(Object testModule:testModuleArray){
						htmlStr += "				<td>" + Integer.valueOf(testModuleResultGet(testModule.toString()).get(1)) + "</td> \r";
					}
					htmlStr += "			</tr> \r"
					+ "			<tr class=\"bg1\"> \r"
					+ "				<td>Fail</td> \r"
					+ "				<td>" + jenkinsFailCaseNumber + "</td> \r";
					for(Object testModule:testModuleArray){
						if(Integer.valueOf(testModuleResultGet(testModule.toString()).get(2))==0){
							htmlStr += "				<td>" + Integer.valueOf(testModuleResultGet(testModule.toString()).get(2)) + "</td> \r";
						}else{
							String failCaseHtml = System.currentTimeMillis() + ".html";
							failCaseHtmlGenerate(testModule, failCaseHtml);
							htmlStr += "				<td><a href=\"http://" + jenkinsHostIP + ":8080/fail/" + systemCurrentDate("yyyy-MM-dd") + "/" + testModule + "/" + failCaseHtml + "\">" + 
							Integer.valueOf(testModuleResultGet(testModule.toString()).get(2)) + "</a></td> \r";
						}
					}
					htmlStr += "			</tr> \r"
					+ "			<tr class=\"bg2\"> \r"
					+ "				<td>Skip</td> \r"
					+ "				<td>" + jenkinsSkipCaseNumber + "</td> \r";
					for(Object testModule:testModuleArray){
						htmlStr += "				<td>" + Integer.valueOf(testModuleResultGet(testModule.toString()).get(3)) + "</td> \r";
					}
					htmlStr += "			</tr> \r"
					+ "			<tr class=\"bg1\"> \r"
					+ "				<td>通过率</td> \r";
					int totalPass = Integer.valueOf(doubleToPercentage(jenkinsPassPercent).split("\\.")[0]);
					if(totalPass < 60){
						htmlStr += "				<td bgcolor=\"red\">" + doubleToPercentage(jenkinsPassPercent) + "</td> \r";
					}
					if((totalPass >= 60) && (totalPass < 90)){
						htmlStr += "				<td bgcolor=\"yellow\">" + doubleToPercentage(jenkinsPassPercent) + "</td> \r";
					}
					if(totalPass >= 90){
						htmlStr += "				<td>" + doubleToPercentage(jenkinsPassPercent) + "</td> \r";
					}
					for(Object testModule:testModuleArray){
						int aa = Integer.valueOf(doubleToPercentage(Double.valueOf(testModuleResultGet(testModule.toString()).get(4))).split("\\.")[0]);
						if(aa < 60){
							htmlStr += "				<td bgcolor=\"red\">" + doubleToPercentage(Double.valueOf(testModuleResultGet(testModule.toString()).get(4))) + "</td> \r";
						}
						if((aa >= 60) && (aa < 90)){
							htmlStr += "				<td bgcolor=\"yellow\">" + doubleToPercentage(Double.valueOf(testModuleResultGet(testModule.toString()).get(4))) + "</td> \r";
						}
						if(aa >= 90){
							htmlStr += "				<td>" + doubleToPercentage(Double.valueOf(testModuleResultGet(testModule.toString()).get(4))) + "</td> \r";
						}
					}
					htmlStr += "			</tr> \r"
					+ "		</table> \r"
					+ "	</div> \r"
					+ "	<b class=\"b4\"></b> \r"
					+ "	<b class=\"b3\"></b> \r"
					+ "	<b class=\"b2\"></b> \r"
					+ "	<b class=\"b1\"></b> \r"
					+ "</div> \r"
					+ "<div style=\"text-align:center;\"> \r"
					+ "	<p>注释：通过率在90%以上，默认显示；60%~90%之间，黄色显示；<60%， 红色显示；</p> \r"
					+ "</div> \r"
					+ "</body> \r"
					+ "</html> \r";
			contentWrite(htmlPath, htmlStr);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 生成测试通过率
	 * @param passPercent
	 * @return
	 * @throws Throwable
	 */
	public String doubleToPercentage(Double passPercent) throws Throwable {
		String passRate = "";
		try{
			if(passPercent == 0.0){
				passRate = "0.0%";
			}else{
				if(Double.toString(passPercent * 100).length() > 6){
					passRate = Double.toString(passPercent * 100).substring(0, 5) + "%";
				}else{
					passRate = Double.toString(passPercent * 100) + "%";
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return passRate;
	}
	
	/**
	 * 把指定的js/html内容写入js/html
	 * @param filePath
	 * @param content
	 * @throws Throwable
	 */
	public void contentWrite(String filePath, String content) throws Throwable {  
        try {  
            String oriStr = ""; //原有txt内容  
            String newStr = "";//内容更新  
            File f = new File(filePath);  
            if (f.exists()) {  
            	f.delete();
            }  
            f.createNewFile();// 不存在则创建  
            if(filePath.contains("pie.js")){
                File oriFile = new File(filePath.replace("pie.js", "ori_pie.txt"));
                BufferedReader input = new BufferedReader(new FileReader(oriFile));  
                while ((oriStr = input.readLine()) != null) {  
                	newStr += oriStr + "\n";  
                }  
                input.close();  
            }

            newStr += content;  
  
            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
            BufferedWriter output = new BufferedWriter(writerStream);  
            output.write(newStr);  
            output.close();  
        } catch (Exception ex) {  
        	exceptionErrorHandle(ex);
        }  
    }
	
	/**
	 * 生成测试报告使用的js文件
	 * @param passNumber
	 * @param failNumber
	 * @param skipNumber
	 * @param jsPath
	 * @throws Throwable
	 */
	public void reportJSGenerate(int passNumber, int failNumber, int skipNumber, String jsPath) throws Throwable {
		try{
			fileUtil.deleteFile(jsPath);
			System.out.println(jsPath);
			String windowOnloadFunctionStr = ""
					+ "window.onload = function() {\r"
					+ "		var canvas = document.getElementById(\"pie_canvas\");\r" 
					+ "		var seriesData = [\r"
					+ "		{name: \"Pass\", value: " + passNumber + ", color: \"RGBA(100,205,50,1)\"}, \r" 
					+ "		{name: \"Fail\", value: " + failNumber + ", color: \"RGBA(255,69,0,1)\"}, \r" 
					+ "		{name: \"Skip\", value: " + skipNumber + ", color: \"RGBA(255,215,0,1)\"}] \r" 
					+ "		var config = {\r"
					+ "		width: 600, \r"
					+ "		height: 400, \r"
					+ "		series: seriesData, \r"
					+ "		canvas: canvas, \r"
					+ "		unit: \"\", \r"
					+ "		title: \"\", \r" 
					+ "		tooltips: { \r"
					+ "			enable: true \r"
					+ "		}, \r" 
					+ "		animation: { \r"
					+ "			enable: false \r" 
					+ "		}, \r" 
					+ "		legend: { \r"
					+ "			enable: false \r"
					+ "		}, \r" 
					+ "		text: { \r"
					+ "			enable: true \r"
					+ "		}, \r"
					+ "	}; \r"
					+ "	pieChart.initSettings(config); \r"
					+ "	pieChart.render(); \r"
					+ "}";
			contentWrite(jsPath, windowOnloadFunctionStr);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 转换jenkins上的任务名，中文->拼音
	 * @param chinese
	 * @return
	 * @throws Throwable
	 */
	public String zhToPinyinAbbr(String chinese) throws Throwable{
		StringBuffer pinYin = new StringBuffer();
		CharUtil charUtil = new CharUtil();
		if(charUtil.isChinese(chinese)){
			char[] zhCharArray = chinese.toCharArray();
			HanyuPinyinOutputFormat pinFormat = new HanyuPinyinOutputFormat();
			pinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			pinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			for(int i=0; i<zhCharArray.length; i++){
				if(zhCharArray[i] > 128){
					try{
						String[] strs = PinyinHelper.toHanyuPinyinStringArray(zhCharArray[i], pinFormat);
						pinYin.append(strs[0]);
	 				}catch(Throwable e){
	 					e.printStackTrace();
					}
				}else{
					pinYin.append(zhCharArray[i]);
				}
			}
			return pinYin.toString();
		}else{
			return chinese;
		}
	}
}
