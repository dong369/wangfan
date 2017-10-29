<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>代理商统计</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-manager-css.jsp"%>

<!-- BootStrap smoke -->
<link rel="stylesheet" href="${ctx}/static/jslib/bootstrap-datepicker/css/bootstrap-datepicker3.css">

<%@	include file="/jsp/inc/inc-operator-js.jsp"%>
<%@	include file="/jsp/inc/inc-manager-js.jsp"%>
<script src="${ctx}/static/jslib/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
<script src="${ctx}/static/jslib/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>

<script src="${ctx}/static/jslib/echarts/echarts.js"></script>


</head>
<body>
	<div class="container-fluid">
		<!-- Nav tabs -->
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active"><a href="#home" role="tab" data-toggle="tab">订单统计</a></li>
			<li role="presentation"><a href="#profile" role="tab" data-toggle="tab">用户注册量统计</a></li>
			<li role="presentation"><a href="#messages" role="tab" data-toggle="tab">金额统计</a></li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="home">
				<%@include file="statisticsOrder.jsp" %>
			</div>
			<div role="tabpanel" class="tab-pane" id="profile">
				<%@include file="statisticsUserRegist.jsp" %>
			</div>
			<div role="tabpanel" class="tab-pane" id="messages">
				<%@include file="statisticsOrderMoney.jsp" %>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$.fn.datepicker.defaults.language = "zh-CN";
		$('.datepicker').datepicker({
			format : 'yyyy-mm-dd',
			endDate : '0d',
			autoclose : true
		});
		
	</script>

</body>
</html>