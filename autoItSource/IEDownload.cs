using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Automation;
using System.Threading;
using System.IO;

namespace IEDownload
{
        class IEDownload
        {
            //需要传一个IE窗口标题的参数, 如上海清算所招聘后台管理系统
            static void Main(string[] args)
            {
                AutomationElement desktop = AutomationElement.RootElement;
                Thread.Sleep(3000);    
                try
                {
                    String title = null;
                    if (args.Length == 1)
                        title = args[0] + " - Internet Explorer"; 
                    //IE弹出窗口
                    var downloadWin = desktop.FindFirst(TreeScope.Children,
                        new AndCondition(
                        new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.Window),
                        new PropertyCondition(AutomationElement.NameProperty, title)     //"上海清算所招聘后台管理系统 - Internet Explorer"
                         ));

                    //下拉箭头
                    Condition dropDownCondition = new AndCondition(
                    new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.SplitButton),
                    new PropertyCondition(AutomationElement.NameProperty, ""));

                    var dropDownBtn = downloadWin.FindFirst(TreeScope.Descendants, dropDownCondition);
                    InvokePattern invoke = (InvokePattern)dropDownBtn.GetCurrentPattern(InvokePattern.Pattern);
                    invoke.Invoke();

                    Thread.Sleep(2000);
                    //选择另存为
                    Condition savaAsCondition = new AndCondition(
                    new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.MenuItem),
                    new PropertyCondition(AutomationElement.NameProperty, "另存为(A)"));
                    var savaAsMenu = desktop.FindFirst(TreeScope.Descendants, savaAsCondition);
                    ((InvokePattern)savaAsMenu.GetCurrentPattern(InvokePattern.Pattern)).Invoke();
                    Thread.Sleep(3000);

                    //另存为对话框
                    var savaAsWin = desktop.FindFirst(TreeScope.Children,
                        new AndCondition(
                        new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.Window),
                        new PropertyCondition(AutomationElement.NameProperty, "另存为")));

                    Condition fileNameCondition = new AndCondition(
                        new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.Edit),
                        new PropertyCondition(AutomationElement.NameProperty, "文件名:"));

                    var fileNameTxt = savaAsWin.FindFirst(TreeScope.Descendants, fileNameCondition);
                    TextPattern valuePattern = (TextPattern)fileNameTxt.GetCurrentPattern(TextPattern.Pattern);
                    string fileName = valuePattern.DocumentRange.GetText(-1); //取文件名

                    String fullPath = @"D:\autoItDownload\" + fileName;
                    String iniFile = @"D:\autoItDownload\downloadFileFullPath.ini";

                    if (File.Exists(fullPath))
                    {
                        File.Delete(fullPath);
                    }

                    try
                    {                        
                        using (StreamWriter sw = new StreamWriter(iniFile))
                        {
                            sw.Write(fullPath);
                            sw.Flush();
                        }
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine(e);
                    }

                    ValuePattern name = (ValuePattern)fileNameTxt.GetCurrentPattern(ValuePattern.Pattern);
                    name.SetValue(fullPath);

                    //save
                    Condition saveCondition = new AndCondition(
                        new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.Button),
                        new PropertyCondition(AutomationElement.NameProperty, "保存(S)"));

                    var saveBtn = savaAsWin.FindFirst(TreeScope.Descendants, saveCondition);
                    ((InvokePattern)saveBtn.GetCurrentPattern(InvokePattern.Pattern)).Invoke();
                    Thread.Sleep(5000);

                    //close the window
                    Condition closeCondition = new AndCondition(
                        new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.Button),
                        new PropertyCondition(AutomationElement.NameProperty, "关闭"));
                    var closeBtn = downloadWin.FindFirst(TreeScope.Descendants, closeCondition);
                    ((InvokePattern)closeBtn.GetCurrentPattern(InvokePattern.Pattern)).Invoke();
                }
                catch(Exception ex)
                {
                    Console.WriteLine("下载文件异常");
                    Console.WriteLine(ex.Message);
                }          
            
        }
    }
}
