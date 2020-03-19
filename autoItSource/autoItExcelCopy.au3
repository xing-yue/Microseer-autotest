#include <Excel.au3>

; Create application object
Local $oExcel = _Excel_Open()

; Open Excel
Local $ePath = StringReplace(@ScriptDir, "lib", "")
Local $oWorkbook1 = _Excel_BookOpen($oExcel, $ePath & "testData\workflow\批量导入.xls")

; Copy A1:Y7
Local $oRange = $oWorkbook1.ActiveSheet.Range("A1:Y11")
_Excel_RangeCopyPaste($oWorkbook1.ActiveSheet, $oRange)

; Close excel by force
_Excel_Close($oExcel, False, True)
