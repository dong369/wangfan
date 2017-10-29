<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>车主认证</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-manager-css.jsp"%>

</head>
<body>
	<div class="container-flush">
		<div class="row">
			<div class="col-md-12">
				<div id="toolbar">
					<button id="deleteBtn" type="button" class="btn btn-success">审核通过</button>					
				</div>
				<table id="table"></table>
			</div>
		</div>
	</div>



	<%@	include file="/jsp/inc/inc-manager-js.jsp"%>

	<script type="text/javascript">
		var table = $('#table').bootstrapTable({
			url : ctx + "/ownerapprove/table",
			searchPlaceholder : "用户名称",
			search : true,
			toolbar : '#toolbar',
			columns : [ {
				checkbox : true
			}, {
				title : '认证状态',
				field : 'status',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
				title : '车主姓名',
				field : 'ownerName',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
				title : '身份证号',
				field : 'ownerIdNumber',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
				title : '证件照',
				field : 'idenPhoto',
				align : 'left',
				valign : 'middle',
				sortable:true,
				formatter:function(data,row,c){
                	return '<a href="' + data + '" target="_blank">点击查看</a>'
                }
			}, {
				title : '驾驶证',
				field : 'drivPhoto',
				align : 'left',
				valign : 'middle',
				sortable:true,
				formatter:function(data,row,c){
                	return '<a href="' + data + '" target="_blank">点击查看</a>'
                }
			}, {
				title : '车型',
				field : 'carmodel',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
				title : '车牌号',
				field : 'carNumber',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
				title : '颜色',
				field : 'carColor',
				align : 'left',
				valign : 'middle',
				sortable:true
			} ]
		});

		$('#deleteBtn').click(function() {
			var st = $('#table').bootstrapTable('getSelections');
			if(st <= 0){
				$.smkAlert({
					text : '请选中一条数据再操作',
					position : 'top-right',
					type : 'warning'
				});
				return ;
			}
			var seleted = st[0];
			parent.$.smkConfirm({
				text : '是否审核通过?',
				accept : '是',
				cancel : '否'
			}, function(res) {
				if (res) {
					$.post(ctx + '/ownerapprove/ownerapprovesucc', {id:st[0].userId}, function(data){
						if(data.success){
							table.bootstrapTable('refresh');
							$.smkAlert({
								text : '审核成功',
								position : 'top-right',
								type : 'success'
							});
						} else {
							$.smkAlert({
								text : '审核失败:' + data.message,
								position : 'top-right',
								type : 'danger'
							});
						}
					}, 'json');
				}
			});

		});
		
	</script>

</body>
</html>