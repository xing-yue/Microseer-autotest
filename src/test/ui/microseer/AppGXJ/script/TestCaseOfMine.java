package microseer.AppGXJ.script;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.automation.framework.base.AppTestNGCaseBaseUI;
import com.automation.framework.exception.FrameworkException;
import com.automation.microseer.common.AppCommon;

public class TestCaseOfMine extends AppTestNGCaseBaseUI{
	
	@Test(alwaysRun=true, groups={"microseer.AppGXJ.login.test"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfGxjDemo()throws FrameworkException{
		try{
			caseName = "工薪计 demo";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			AppCommon app = new AppCommon(appHandler);
			app.toMine();//我的
			
			assertEquals(result, true);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
}