<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>你好</title>

<script src="${ctx}/static/jslib/jquery/jquery.min.js"></script>

<link rel="stylesheet" href="${ctx}/static/jslib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="${ctx}/static/jslib/bootstrap/css/bootstrap-theme.min.css" />
<script src="${ctx}/static/jslib/bootstrap/js/bootstrap.min.js"></script>

</head>
<body>
	
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<a href="${ctx}/user/loginByPc" class="btn btn-default">管理后台</a>
			</div>
			<div class="col-md-9">
				<h3>车主账号</h3>
				<ul>
					<li>张三：13611111111</li>
					<li>李四：13622222222</li>
				</ul>
				<h3>乘客帐号</h3>
				<ul>
					<li>王五：13633333333</li>
					<li>赵六：13644444444</li>
				</ul>
			</div>
		</div>
	</div>

</body>
</html>