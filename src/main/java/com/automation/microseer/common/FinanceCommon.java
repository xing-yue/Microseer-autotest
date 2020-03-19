package com.automation.microseer.common;

import java.util.List;

import org.openqa.selenium.By;

import com.automation.framework.action.IWebElement;
import com.automation.framework.action.IWebHandler;
import com.automation.framework.utils.Log4jUtil;
import com.automation.microseer.common.ScmCommon;

/**
 * @Description: 财务中心公共类
 * @ClassName: FinanceCommon
 * @author panyongjun
 * @date 2019年8月1日
 */
public class FinanceCommon extends MicroseerCommon {
	
	public FinanceCommon(IWebHandler handler){
		this.handler = handler;
	}
	
	ScmCommon scmComm = new ScmCommon(this.handler);
	Log4jUtil logUtil = new Log4jUtil();
	
	/**
	 * 左侧导航栏父节点展开
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	By menuBy = By.xpath("//ul[@role='menu']/li");//左侧菜单栏
	public void menuExpand(String menuName) throws Throwable{
		List<IWebElement> eleList = eleListsGet(menuBy);
		eleClickByText(eleList, menuName);
	}
	
	/**
	 * 左侧导航栏勾选
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void menuItemClick(String menuName, String subName) throws Throwable{
		List<IWebElement> eleList = eleListsGet(By.xpath("//ul[@id='"+menuName+"$Menu']/li"));
		eleClickByText(eleList, subName);
	}
	
	/**
	 * 日期选择
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void dateSet(String dateLabel, String start, String end) throws Throwable{
		if(scmComm.dateCompare(start, end)){
			String labelFor = scmComm.labelForGet(dateLabel);
			eleClickBy(By.id(labelFor));
			String sysDate = systemCurrentDate("yyyy-MM-dd");
			
			//左侧日期
			int diffYear = scmComm.dateDiffByYear(start, sysDate);
			int diffMonth = scmComm.dateDiffByMonth(start, sysDate);
			int dateDay = scmComm.dateDayGet(start);
			datePickup(true, diffYear, diffMonth, dateDay);
			//右侧日期
			int diffYearR = scmComm.dateDiffByYear(start, sysDate);
			int diffMonthR = scmComm.dateDiffByMonth(start, sysDate);
			int dateDayR = scmComm.dateDayGet(start);
			datePickup(false, diffYearR, diffMonthR, dateDayR);
			
		}else{
			logUtil.info("结束日期小于起始日期");
			return;
		}
	}
	
	/**
	 * 日期筛选
	 * author:panyongjun
	 * date:2019年8月1日
	 */
	public void datePickup(boolean isLeft, int diffYear, int diffMonth, int dateDay) throws Throwable{
		By yearPrevBtnBy = null;
		By monthPrevBtnBy = null;
		By yearNextBtnBy = null;
		By monthNextBtnBy = null;
		By dayBtnBy = null;
		if(isLeft){
			yearPrevBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-left']//a[@class='ant-calendar-next-year-btn']");
			monthPrevBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-left']//a[@class='ant-calendar-next-month-btn']");
			yearNextBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-left']//a[@class='ant-calendar-prev-year-btn']");
			monthNextBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-left']//a[@class='ant-calendar-prev-month-btn']");
			dayBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-left']//td[not(class, 'ant-calendar-cell ant-calendar-next-month-btn-day'])/div");
		}else{
			yearPrevBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-right']//a[@class='ant-calendar-next-year-btn']");
			monthPrevBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-right']//a[@class='ant-calendar-next-month-btn']");
			yearNextBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-right']//a[@class='ant-calendar-prev-year-btn']");
			monthNextBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-right']//a[@class='ant-calendar-prev-month-btn']");	
			dayBtnBy = By.xpath("//div[@clas='ant-calendar-range-part ant-calendar-range-right']//td[not(class, 'ant-calendar-cell ant-calendar-next-month-btn-day'])/div");
		}		
		if(diffYear >= 0){
			for(int i=0; i<diffYear; i++){
				eleClickBy(yearNextBtnBy);
			}				
		}else{
			for(int i=0; i<Math.abs(diffYear); i++){
				eleClickBy(yearPrevBtnBy);
			}				
		}
		if(diffMonth >= 0){
			for(int i=0; i<diffMonth; i++){
				eleClickBy(monthNextBtnBy);
			}	
		}else{
			for(int i=0; i<Math.abs(diffMonth); i++){
				eleClickBy(monthPrevBtnBy);
			}				
		}
		List<IWebElement> dayList = eleListsGet(dayBtnBy);
		eleClickByText(dayList, String.valueOf(dateDay));
	}
}
