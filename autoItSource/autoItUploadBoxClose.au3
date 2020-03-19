
 ;$CmdLine[1] ;第一个参数 (浏览器类型)
 ;$CmdLine[2] ;第二个参数 (IP地址)

 Dim $title
 $cancelBtn="Button2"
 If $CmdLine[1]="ie" Then                        ; 代表IE浏览器
   $title="选择要上载的文件，通过: "&$CmdLine[2]
   $cancelBtn="Button3"
 ElseIf $CmdLine[1]="chrome" Then               ; 代表谷歌浏览器
   $title="打开"
 ElseIf $CmdLine[1]="firefox" Then             ; 代表火狐浏览器
   $title="文件上传"
 EndIf

$var = WinList($title)
While $var[0][0] > 0
  WinActive($title)
  ControlGetFocus($title)
  ControlFocus ($title, "", "" )
  ControlClick($title, "取消", $cancelBtn);
  $var=WinList($title)
WEnd