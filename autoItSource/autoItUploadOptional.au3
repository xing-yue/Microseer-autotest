#include <file.au3>
;$CmdLine[1] ;第一个参数 (脚本名称后面)

Opt("TrayIconHide",1)

$var = IniRead("D:\autoItUpload\uploadFileFullPath.ini", "uploadFilePath", "uploadFilePath", "NotFound")
 Dim $title
 If $CmdLine[1]="ie" Then                        ; 代表IE浏览器
   $title="选择要加载的文件"
 ElseIf $CmdLine[1]="chrome" Then               ; 代表谷歌浏览器
   $title="打开"
 ElseIf $CmdLine[1]="firefox" Then             ; 代表火狐浏览器
   $title="文件上传"
EndIf

;ControlFocus("title","text",controlID) Edit1=Edit instance 1
ControlFocus($title, "","Edit1")

; Wait 10 seconds for the Upload window to appear
  WinWait("[CLASS:#32770]","",5)

; Set the File name text on the Edit field

  ;ControlSend("打开", "", "Edit1", $var)
  ControlSetText($title, "", "Edit1", $var)

  Sleep(2000)

; Click on the Open button

  ControlClick($title, "","Button1");
