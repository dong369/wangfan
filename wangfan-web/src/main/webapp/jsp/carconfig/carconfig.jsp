<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>油费过路费</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-operator-css.jsp"%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="postage"> 油费 </label>
						
						<div class="col-sm-6">
							<input type="text" id="postage" placeholder="油费" name="postage" class="form-control" value="${c.postage}">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="toll"> 过路费 </label>

						<div class="col-sm-6">
							<input type="text" id="toll" placeholder="过路费" name="toll" class="form-control" value="${c.toll}">
						</div>
					</div>	
																													
					<div class="clearfix form-actions">
						<div class="col-md-offset-3 col-md-9">
							<button class="btn btn-info" type="button" id="submitBtn">
								<i class="ace-icon fa fa-check bigger-110"></i>
								提交
							</button>

							&nbsp; &nbsp; &nbsp;
							<button class="btn" type="reset">
								<i class="ace-icon fa fa-undo bigger-110"></i>
								重置
							</button>
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
				postage : {
					required : true
				},
				toll : {
					required : true
				}
			},
			messages : {
				postage : {
					required : "请输入油费"
				},
				toll : {
					required : "请输入过路费"
				}
			},
			invalidHandler : function(event, validator) {//valid时如果有错误，调用的方法
				var errors = validator.numberOfInvalids();
				if (errors) {
				} else {
				}
			}
		});
		
		$('#submitBtn').on('click', function(){
			$.post(ctx+ '/carconfig/add', $('#myForm').serialize(), function(data){
				if(data.success){
					window.location.href = ctx + '/carconfig/carconfig';
				} else {
					alert("提交失败:" + data.message);
				}
			}, 'json');
		});
		
	</script>
</body>
</html>