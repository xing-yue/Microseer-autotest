 ControlFocus("另存为", "","Edit1")
 $fileName = ControlGetText ("另存为", "", "Edit1")
 Sleep(1000)
 $sFilePath = "D:\autoItDownload\downloadFileFullPath.ini"
 FileDelete($sFilePath)
 $downloadPath1 = "D:\autoItDownload\"
 $downloadFullPath1 = "D:\autoItDownload\"&$fileName
 FileWrite($sFilePath, $downloadFullPath1)
 If FileExists($downloadFullPath1) Then
    ;MsgBox($MB_SYSTEMMODAL, "", "The file exists." & @CRLF & "FileExist returned: " & $downloadFullPath1)
    FileDelete($downloadFullPath1)
 EndIf
 ControlSetText("另存为", "", "Edit1", $downloadPath1)
 Sleep(1000)
 $downloadPath2 = ControlGetText ("另存为", "", "Edit1")
 $tryCount = 0
 while $downloadPath2 <> $downloadPath1 And $tryCount < 5
	ControlSetText("另存为", "", "Edit1", "")
	Sleep(1000)
	ControlSetText("另存为", "", "Edit1", $downloadPath1)
 	Sleep(1000)
 	$downloadPath2 = ControlGetText ("另存为", "", "Edit1")
	$tryCount = $tryCount + 1
 WEnd
 ControlClick ("另存为", "", "Edit1")
 Sleep(1000)
 ControlClick ("另存为", "保存(&S)", "[CLASS:Button; INSTANCE:1]" )
 Sleep(1000)
 ControlSetText("另存为", "", "Edit1", $fileName)
 ControlClick ("另存为", "保存(&S)", "[CLASS:Button; INSTANCE:1]" )
 Sleep(1000)
 Sleep(10000)

