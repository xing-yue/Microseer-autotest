$var = WinList("另存为")
While $var[0][0] > 0
  WinActive("另存为")
  ControlGetFocus("另存为")
  ControlFocus ("另存为", "", "" ) 
  ControlClick ("另存为", "取消", "[CLASS:Button; INSTANCE:2]" ) 
  $var=WinList("另存为")
WEnd