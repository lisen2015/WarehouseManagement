<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>404</title>
<style type="text/css">
*{margin:0;padding:0}
body{background-color:#71cef1;}
#container{background:url(../images/bg404.jpg) repeat-x;height:642px;}
#container div.info_container{width:700px;margin:0 auto;}
#container .info_container div.error_info{background:url(<%=basePath%>images/error_bg.jpg) no-repeat scroll bottom;width:674px;height:268px;margin:0 auto;padding-top:90px;}
#container .info_container div.error_relfect{background:url(<%=basePath%>images/reflect_bg.jpg) no-repeat;width:674px;height:131px; margin:0 auto;}
div.info{padding-top:70px;padding-left:60px;}
div.ico{float:left;margin-right:25px;}
div.link{float:left;}
div.link p{line-height:32px;font-weight:bold;font-family: 微软雅黑;}
div.link p.special{font-size:12px;}
#container .info_container div.footer{text-align:center;width:700px;margin:26px auto 0px auto;}
#container .info_container div.footer p{font-size:12px;color:#ffffff;}
</style>
</head>
<body>
	<div id="container">
		<div class="info_container">
			<div class="error_info">
				<div class="info">
					<div class="ico">
						<img src="<%=basePath%>images/error_ico.gif" />
					</div>
					<div class="link">
						<p>对不起，您的页面暂时无法找到！</p>
						<p class="special">返回<a href="<%=basePath%>mLogin">首页</a></p>
					</div>
				</div>
			</div>
			<div class="error_relfect"></div>
			<div class="footer">
			</div>
		</div>
	</div>
</body>
</html>