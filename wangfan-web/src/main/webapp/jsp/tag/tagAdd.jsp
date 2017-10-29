<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>标签管理</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-operator-css.jsp"%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<div class="form-group">
						<label for="inputTagName" class="col-sm-2 control-label">名称</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="inputTagName" placeholder="标签名称" name="tagName">
						</div>
					</div>
					<div class="form-group">
						<label for="inputSeq" class="col-sm-2 control-label">排序</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="inputSeq" name="seq" placeholder="标签的顺序">
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
				tagName : {
					required : true
				},
				seq : {
					required : true,
					digits : true
				}
			},
			messages : {
				tagName : {
					required : "请输入标签名称"
				},
				seq : {
					required : "请输入序号",
					digits : "请输入一个整数"
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