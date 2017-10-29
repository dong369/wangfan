<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>车辆品牌</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-operator-css.jsp"%>
<link rel="stylesheet" href="${ctx}/static/jslib/webuploader/webuploader.css" />
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<div class="form-group">
						<label for="name" class="col-sm-3 control-label">品牌名称</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="name" placeholder="品牌名称" name="name">
						</div>
					</div>
					<div class="form-group">
						<label for="initial" class="col-sm-3 control-label">首字母</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="initial" name="initial" placeholder="首字母">
						</div>
					</div>
					<div class="form-group">
						<label for="picUrl" class="col-sm-3 control-label">品牌图片</label>
						<div class="col-sm-6">
							<div id="picker"></div>	
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<%@	include file="/jsp/inc/inc-operator-js.jsp"%>
	
	<script src="${ctx}/static/jslib/webuploader/webuploader.js"></script>
	<script src="${ctx}/static/mk/mk-webuploader.js"></script>

	<script type="text/javascript">
		$('#myForm').validate({
			rules : {
				name : {
					required : true
				},
				initial : {
					required : true
				}
			},
			messages : {
				name : {
					required : "请输入品牌名称"
				},
				initial : {
					required : "请输入首字母"
				}
			},
			invalidHandler : function(event, validator) {//valid时如果有错误，调用的方法
				var errors = validator.numberOfInvalids();
				if (errors) {
				} else {
				}
			}
		});
		
		var kk = uploaderExt({
			//选择文件的按钮。
			pick : '#picker',
			inputName:'picUrl'
		});
	</script>
</body>
</html>