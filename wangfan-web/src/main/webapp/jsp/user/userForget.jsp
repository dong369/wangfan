<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>用户管理</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-manager-css.jsp"%>

</head>
<body>

	<div class="container">
		<div class="rows">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="mobilePhone">手机号</label>

						<div class="col-sm-6">
							<input type="text" id="mobilePhone" placeholder="手机号" name="mobilePhone" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="code">验证码</label>

						<div class="col-sm-6">
							<input type="text" id="code" placeholder="验证码" name="code" class="form-control">
							<button type="button" id="sendMsg" class="btn btn-default">点击获取验证码</button>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="pwd">新密码</label>

						<div class="col-sm-6">
							<input type="text" id="pwd" placeholder="新密码" name="pwd" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="pwd2">重复新密码</label>

						<div class="col-sm-6">
							<input type="text" id="pwd2" placeholder="重复新密码" name="pwd2" class="form-control">
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button id="submitBtn" type="button" class="btn btn-default">提交</button>
						</div>
					</div>
				</form>

			</div>
		</div>
	</div>

	<%@	include file="/jsp/inc/inc-manager-js.jsp"%>
	<script type="text/javascript">
		$('#sendMsg').click(function() {
			$('#sendMsg').prop('disabled', true);
			$('#sendMsg').text('验证码获取中...');

			$.post(ctx + '/user/sendVerify', {
				mobilePhone : $('#mobilePhone').val()
			}, function(data) {
				if (data.success == true) {
					$('#sendMsg').text('验证码获取中...');

					//setTimeout
					//$('#sendMsg').prop('disabled', false);
				} else {
					alert(data.message);
				}
			}, 'json');
		})
		
		$('#submitBtn').click(function(){
			
			$.post(ctx + '/user/userForget', $('#myForm').serialize(), function(data) {
				if(data.success){
					alert('密码重置成功');
				} else {
					alert('密码重置失败');
				}
			}, 'json');
		});
	</script>

</body>
</html>