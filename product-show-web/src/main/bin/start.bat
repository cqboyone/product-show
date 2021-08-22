@ECHO OFF
%1 start mshta vbscript:createobject("wscript.shell").run("""%~0"" ::",0)(window.close)&&exit
set path=D:\bnspa_new\java\jre8\bin;%path%
java -jar ../lib/product-show-web.jar