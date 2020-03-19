package microseer.GXJ.script;

import java.util.List;

import org.testng.annotations.Test;

import com.automation.framework.base.WebTestNGCaseBaseUI;
import com.automation.framework.exception.FrameworkException;
import com.automation.framework.verification.ValidationUtil;
import com.automation.microseer.common.GXJCommon;

import microseer.GXJ.page.loginPage;
import microseer.GXJ.page.newProjectPage;

/**
 * 工薪计 demo
 * @author panyongjun
 *
 */
public class TestCaseOfGXJ extends WebTestNGCaseBaseUI{
	
	/**
	 * 工薪计登录
	 * panyongjun
	 * 2019年8月1日
	 */
	@Test(alwaysRun=true, groups={"microseer.GXJ.login.test"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfGxjDemo()throws FrameworkException{
		try{
			caseName = "工薪计 demo";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			GXJCommon gxj = new GXJCommon(handler);
			gxj.login("13321872737", "123456");
			gxj.upgradeBoxDismiss();
			
			newProjectPage pageAction = new newProjectPage(handler);
			pageAction.newProject();
			List<String> topTabList = eleTxtListsGet(pageAction.topTabBy);
			result = strIsIncludedInList("项目", topTabList) && 
					strIsIncludedInList("用户", topTabList) && 
					strIsIncludedInList("账单", topTabList);
			
			ValidationUtil.verifyTrue(result, caseName, handler);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
}