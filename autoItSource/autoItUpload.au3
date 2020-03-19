 #include <file.au3>
 ;$CmdLine[1] ;第一个参数 (浏览器类型)
 ;$CmdLine[2] ;第二个参数 (IP地址)

 Opt("TrayIconHide",1)
 $uploadFullPath1 = IniRead("D:\autoItUpload\uploadFileFullPath.ini", "uploadFilePath",  "uploadFilePath", "NotFound")

 Dim $title
 Dim $openBtn
 $openBtn="Button1"
 If $CmdLine[1]="ie" Then                        ; 代表IE浏览器
   $title="选择要上载的文件，通过: "&$CmdLine[2]
   ;$title="选择要上载的文件，通过: 10.30.202.71"
   $openBtn="Button2"
 ElseIf $CmdLine[1]="chrome" Then               ; 代表谷歌浏览器
   $title="打开"
 ElseIf $CmdLine[1]="firefox" Then             ; 代表火狐浏览器
   $title="文件上传"
 EndIf

 ControlFocus($title, "","Edit1")
 Sleep(1000)
 ControlSetText($title, "", "Edit1", $uploadFullPath1)
 Sleep(1000)
 $uploadFullPath2 = ControlGetText ($title, "", "Edit1")
 $tryCount = 0
 while $uploadFullPath2 <> $uploadFullPath1 And $tryCount < 5
	ControlSetText($title, "", "Edit1", "")
	Sleep(1000)
	ControlSetText($title, "", "Edit1", $uploadFullPath1)
 	Sleep(1000)
 	$uploadFullPath2 = ControlGetText ($title, "", "Edit1")
	$tryCount = $tryCount + 1
 WEnd
 ;WinWait("[CLASS:#32770]","",10)
 ;ControlSend($title, "", "Edit1", $var)
 Sleep(2000)
 ControlClick($title, "", $openBtn)
