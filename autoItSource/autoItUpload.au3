 #include <file.au3>
 ;$CmdLine[1] ;��һ������ (���������)
 ;$CmdLine[2] ;�ڶ������� (IP��ַ)

 Opt("TrayIconHide",1)
 $uploadFullPath1 = IniRead("D:\autoItUpload\uploadFileFullPath.ini", "uploadFilePath",  "uploadFilePath", "NotFound")

 Dim $title
 Dim $openBtn
 $openBtn="Button1"
 If $CmdLine[1]="ie" Then                        ; ����IE�����
   $title="ѡ��Ҫ���ص��ļ���ͨ��: "&$CmdLine[2]
   ;$title="ѡ��Ҫ���ص��ļ���ͨ��: 10.30.202.71"
   $openBtn="Button2"
 ElseIf $CmdLine[1]="chrome" Then               ; ����ȸ������
   $title="��"
 ElseIf $CmdLine[1]="firefox" Then             ; �����������
   $title="�ļ��ϴ�"
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
