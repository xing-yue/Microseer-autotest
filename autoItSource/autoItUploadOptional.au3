#include <file.au3>
;$CmdLine[1] ;��һ������ (�ű����ƺ���)

Opt("TrayIconHide",1)

$var = IniRead("D:\autoItUpload\uploadFileFullPath.ini", "uploadFilePath", "uploadFilePath", "NotFound")
 Dim $title
 If $CmdLine[1]="ie" Then                        ; ����IE�����
   $title="ѡ��Ҫ���ص��ļ�"
 ElseIf $CmdLine[1]="chrome" Then               ; ����ȸ������
   $title="��"
 ElseIf $CmdLine[1]="firefox" Then             ; �����������
   $title="�ļ��ϴ�"
EndIf

;ControlFocus("title","text",controlID) Edit1=Edit instance 1
ControlFocus($title, "","Edit1")

; Wait 10 seconds for the Upload window to appear
  WinWait("[CLASS:#32770]","",5)

; Set the File name text on the Edit field

  ;ControlSend("��", "", "Edit1", $var)
  ControlSetText($title, "", "Edit1", $var)

  Sleep(2000)

; Click on the Open button

  ControlClick($title, "","Button1");
