<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<meta name="description" content="">
<meta name="author" content="ThemeBucket">
<link rel="shortcut icon" href="#" type="image/png">

<title>404 Page</title>
<!-- bootstrap��ʽ -->
<link href="../../css/bootstrap/style.css" rel="stylesheet">
<link href="../../css/bootstrap/style-responsive.css" rel="stylesheet">
<!-- bootstrap JS  -->
<script src="../../js/jquery-2.0.3.js"></script>
<script src="../../js/bootstrap/bootstrap.min.js"></script>
<script src="../../js/bootstrap/modernizr.min.js"></script>
<!-- IE9 ����������ļ���JS -->
<script src="../../js/bootstrap/html5shiv.js"></script>
<script src="../../js/bootstrap/respond.min.js"></script>
</head>

<body class="error-page">

	<section>
	<div class="container ">

	<section class="error-wrapper text-center">
		<h1>
			<img alt="" src="../../img/error/404-error.png">
		</h1>
		<h2>δ �� �� ҳ ��</h2>
		<h3>�� �� �� �� ʧ ��</h3>
		<a class="back-btn" href="../../login.jsp"> �� �� �� ¼</a>
	 </section>

	</div>
	</section>



</body>
</html>

