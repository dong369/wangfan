<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>反馈管理</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-operator-css.jsp"%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<input type="hidden" name="id">
					<div class="form-group">
						<label for="username" class="col-sm-3 control-label">用户名称</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="username" readonly="readonly" name="username">
						</div>
					</div>
					<div class="form-group">
						<label for="linkType" class="col-sm-3 control-label">联系方式</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="linkType" readonly="readonly" name="linkType">
						</div>
					</div>
					<div class="form-group">
						<label for="content" class="col-sm-3 control-label">反馈内容</label>
						<div class="col-sm-6">
							<textarea name="content" id="content" rows="10" cols="30" class="form-control" readonly="readonly"></textarea>
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