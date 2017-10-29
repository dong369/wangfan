<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>添加代理商</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-manager-css.jsp"%>
<style type="text/css">
body {
	margin-top: 20px;
	margin-bottom: 20px;
}
</style>

</head>
<body>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4">
				<div id="tree"></div>
			</div>
			<div class="col-md-8">
				<form id="aform" >
					<input type="hidden" name="userId">
					<input type="hidden" name="procityId">
				</form>
				<form class="form-horizontal" id="myForm" role="form">
					
					<div class="form-group">
						<label for="phone" class="col-sm-2 control-label">手机号</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="phone" placeholder="手机号">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button id="findBtn" type="button" class="btn btn-info">搜索</button>
						</div>
					</div>
					<div class="form-group">
						<label for="username" class="col-sm-2 control-label">用户名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="username" placeholder="用户名" name="username">
						</div>
					</div>
					<div class="form-group">
						<label for="mobilePhone" class="col-sm-2 control-label">手机号</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="mobilePhone" placeholder="手机号" name="mobilePhone">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button id="bindBtn" type="button" class="btn btn-danger">绑定</button>
							<button id="unbindBtn" type="button" class="btn btn-success">解绑</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<%@	include file="/jsp/inc/inc-manager-js.jsp"%>
	<script src="${ctx}/static/jslib/bootstrap-treeview/dist/bootstrap-treeview.min.js"></script>
	<script src="${ctx}/static/js/baidu/CityData.js"></script>
	<script src="${ctx}/static/js/baidu/mkSsxq.js"></script>
	<script type="text/javascript">
		function getTree() {
			$.each(ssxq, function(i, n) {
				n.state = {
					expanded : false
				}
			});
			/*
			$.each(ssxq, function(i, n) {
				var nodes = new Array();
				var tags = [];
				$.each(n.cities, function(ii, nn){
					nodes.push({
						text:nn.n
					});
				})
				treedata.push({
					color : "green",
					backColor : "#FFFFFF",
					text : n.n,
					nodes:nodes,
					state:{
						expanded: false
					},
					tags : ["马凯"]
				});
			})
			 */
			return ssxq;
		}

		$('#tree').treeview({
			data : getTree(),
			showTags : true,
			onNodeSelected: function(event, data){
				$('#aform')[0].reset();
				$('#myForm')[0].reset();
				$('#aform input[name=procityId]').val(data.id);
				$.post(ctx + '/agent/bingUser', $('#aform').serialize(), function(data) {
					
					$('#myForm').show();
					$('#aform input[name=userId]').val(data.userId);
					$('#myForm input[name=username]').val(data.username);
					$('#myForm input[name=mobilePhone]').val(data.mobilePhone);
					
					return;
				}, 'json');
			},
			nodeUnselected: function(event, node){
				$('#myForm').hide();
			}
		});

		$('#findBtn').click(function() {
			mk.load($('#myForm'), ctx + '/agent/info?mobilePhone=' + $('#phone').val(), function(data) {
				if (!data.id) {
					$('#aform input[name=userId]').val('');
					alert("没有查询到用户信息~");
					return;
				}
				$('#aform input[name=userId]').val(data.id);
			})
		});
		
		$('#bindBtn').click(function(){
			$.post(ctx + '/agent/bind', $('#aform').serialize(), function(data){
				if(data.success){					
					$.smkAlert({
						text : '绑定成功',
						position : 'top-right',
						type : 'success'
					});
				} else {
					$.smkAlert({
						text : '绑定失败:' + data.message,
						position : 'top-right',
						type : 'danger'
					});
				}
			}, 'json');
		});
		
		$('#unbindBtn').click(function(){
			$.post(ctx + '/agent/unbind', $('#aform').serialize(), function(data){
				if(data.success){					
					$.smkAlert({
						text : '解绑成功',
						position : 'top-right',
						type : 'success'
					});
				} else {
					$.smkAlert({
						text : '解绑失败:' + data.message,
						position : 'top-right',
						type : 'danger'
					});
				}
			}, 'json');
		});
	</script>
</body>
</html>