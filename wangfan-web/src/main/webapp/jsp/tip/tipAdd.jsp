<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>提示管理</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-operator-css.jsp"%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<div class="form-group">
						<label for="tipName" class="col-sm-3 control-label">提示内容</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="tipName" placeholder="提示内容" name="tipName">
						</div>
					</div>
					<div class="form-group">
						<label for="tipDescription" class="col-sm-3 control-label">提示描述</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="tipDescription" name="tipDescription" placeholder="提示描述">
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<%@	include file="/jsp/inc/inc-operator-js.jsp"%>

	<script type="text/javascript">
		$('#myForm').validate({
			rules : {
				tipName : {
					required : true
				}
			},
			messages : {
				tipName : {
					required : "请输入提示内容"
				}
			},
			invalidHandler : function(event, validator) {//valid时如果有错误，调用的方法
				var errors = validator.numberOfInvalids();
				if (errors) {
				} else {
				}
			}
		});
	</script>
</body>
</html>