<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>推送管理</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-operator-css.jsp"%>

<link rel="stylesheet" href="${ctx}/static/jslib/webuploader/webuploader.css" />
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<input type="hidden" name="id">
					<div class="form-group">
						<label for="title" class="col-sm-3 control-label">标题</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="title" placeholder="标题" name="title">
						</div>
					</div>
					<div class="form-group">
						<label for="bannerUrl" class="col-sm-3 control-label">Banner图片</label>
						<div class="col-sm-6">
							<div id="banner"></div>							
						</div>												
					</div>
					<div class="form-group">
						<label for="startDateTime" class="col-sm-3 control-label">开始时间</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="startDateTime" name="startDateTime" placeholder="开始时间">
						</div>
					</div>
					<div class="form-group">
						<label for="endDateTime" class="col-sm-3 control-label">结束时间</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="endDateTime" name="endDateTime" placeholder="结束时间">
						</div>
					</div>
					<div class="form-group">
						<label for="pageUrl" class="col-sm-3 control-label">H5页面地址</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="pageUrl" name="pageUrl" placeholder="页面链接">
						</div>
					</div>
					<div class="form-group">
						<label for="bizNumber" class="col-sm-3 control-label">推送类型</label>
						<div class="col-sm-6">
							<input type="radio" name="bizNumber" value="PASSENGER">乘客
							<input type="radio" name="bizNumber" value="OWNER">车主
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
				}
			},
			messages : {
				title : {
					required : "请输入标题"
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
			pick : '#banner',
			inputName:'bannerUrl'
		});
	</script>
</body>
</html>