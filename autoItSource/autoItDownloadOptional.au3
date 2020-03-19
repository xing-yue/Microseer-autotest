;ControlFocus("title","text",controlID) Edit1=Edit instance 1
 Dim $title
 $title="Áí´æÎª"
 ControlFocus($title, "","Edit1")
 $fileName = ControlGetText ($title, "", "Edit1")
 Sleep(1000)
 $sFilePath = "D:\autoItDownload\downloadFileFullPath.ini"
 FileDelete($sFilePath)
 $downloadFullPath1 = "D:\autoItDownload\"&$fileName
 FileWrite($sFilePath, $downloadFullPath1)
 ControlSetText($title, "", "Edit1", $downloadFullPath1)
 Sleep(1000)
 $downloadFullPath2 = ControlGetText ($title, "", "Edit1")
 $tryCount = 0
 while $downloadFullPath2 <> $downloadFullPath1 And $tryCount < 5
	ControlSetText($title, "", "Edit1", "")
	Sleep(1000)
	ControlSetText($title, "", "Edit1", $downloadFullPath1)
 	Sleep(1000)
 	$downloadFullPath2 = ControlGetText ($title, "", "Edit1")
	$tryCount = $tryCount + 1
 WEnd
 ControlClick ($title, "", "Edit1")
 Sleep(1000)
 ControlClick ($title, "±£´æ(&S)", "[CLASS:Button; INSTANCE:1]" )
 Sleep(10000)


