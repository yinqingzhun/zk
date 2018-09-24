<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>demo</title>
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
    <script src="/lodop/LodopFuncs.js" type="text/javascript"></script>
    <object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
        <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="/lodop/install_lodop32.exe"></embed>
    </object>
</head>
<body>
<textarea rows="15"  cols="80" style="display: none;" id="textarea01">
<table   border="1" width="360" height="220" style="border-collapse:collapse;border:solid 1px" bordercolor="#000000">
  <tr>
    <td width="100%" height="240">
      <p align="center"> 
      <font face="隶书" size="5" style="letter-spacing: 10px">郭德强</font>
      <p align="center"><font face="宋体" size="3">科学家</font></p>
      <p align="left"><font face="宋体" size="3">　地址：中国北京社会科学院附近东大街西胡同</font></p>
      <p align="left"><font face="宋体" size="3">　电话：010-88811888</font></p>
      <p><br>      　
      </p>
    </td>
  </tr>
</table>
</textarea>
<input type="button"  value="打印凭证" id="kkk"/>
 
<script type="text/javascript">
    $(function(){
        $("#kkk").click(function(){
            var LODOP=getLodop();
            LODOP.PRINT_INIT("test套打");
            LODOP.ADD_PRINT_HTM(0,0,"100%","100%",document.getElementById("textarea01").value);
            /*  LODOP.SET_PRINT_PAGESIZE(1,'200mm','152.4mm','');    //设置纸张高度

           LODOP.ADD_PRINT_TEXT(10,250,157,20,'客户公司名称');            
           LODOP.ADD_PRINT_TEXT(432,149,157,20,'联系电话');   */

            //LODOP.PRINT();
            LODOP.PREVIEW();
        })
    })
  
</script>
</body>
</html>