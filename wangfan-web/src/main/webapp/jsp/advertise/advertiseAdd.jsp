<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>广告管理</title>
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
						<label for="picUrl" class="col-sm-3 control-label">广告图片</label>
						<div class="col-sm-6">
							<div id="picker"></div>							
						</div>
					</div>
					<div class="form-group">
						<label for="title" class="col-sm-3 control-label">广告标题</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="title" name="title" placeholder="广告标题">
						</div>
					</div>
					<div class="form-group">
						<label for="adLocation" class="col-sm-3 control-label">广告类型</label>
						<div class="col-sm-6">
							<input type="radio" id="adLocation" name="adLocation" value="LOGIN" checked="checked">登录广告
							<input type="radio" id="adLocation" name="adLocation" value="LOGOUT">退出广告
						</div>
					</div>
					<div class="form-group">
						<label for="second" class="col-sm-3 control-label">显示秒数</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="second" name="second" placeholder="显示秒数">
						</div>
					</div>
					<div class="form-group">
						<label for="h5url" class="col-sm-3 control-label">h5的链接地址</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="h5url" name="h5url" placeholder="h5的链接地址">
						</div>
					</div>
					<div class="form-group">
						<label for="descript" class="col-sm-3 control-label">广告描述</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="descript" name="descript" placeholder="广告描述">
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
				title : {
					required : true
				},
				second : {
					required : true
				},
				h5url : {
					required : true
				}
			},
			messages : {
				title : {
					required : "请输入广告标题"
				},
				second : {
					required : "请输入显示秒数"
				},
				h5url : {
					required : "请输入h5的链接地址"
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