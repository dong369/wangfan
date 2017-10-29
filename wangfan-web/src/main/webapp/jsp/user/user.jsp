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
	<div class="container-flush">
		<div class="row">
			<div class="col-md-12">
				<div id="toolbar">
					<button id="addBtn" type="button" class="btn btn-success">启用</button>
					<button id="deleteBtn" type="button" class="btn btn-danger">禁用</button>
				</div>
				<table id="table"></table>
			</div>
		</div>
	</div>



	<%@	include file="/jsp/inc/inc-manager-js.jsp"%>

	<script type="text/javascript">
		var table = $('#table').bootstrapTable({
			url : ctx + "/user/table",
			searchPlaceholder : "用户名称",
			search : true,
			toolbar : '#toolbar',
			columns : [ {
				checkbox : true
			}, {
				title : '用户名称',
				field : 'username',
				align : 'left',
				valign : 'middle'
			}, {
				title : '手机号',
				field : 'mobilePhone',
				align : 'left',
				valign : 'middle'
			}, {
				title : '实名认证',
				field : 'status',
				align : 'left',
				valign : 'middle'
			}, {
				title : '车主认证',
				field : 'ostatus',
				align : 'left',
				valign : 'middle'
			}, {
				title : '余额',
				field : 'balance',
				align : 'left',
				valign : 'middle'
			}, 			 
			{
                title: '状态',
                field: 'deleted',
                align: 'left',
                valign: 'middle',
                formatter:function(data,row,c){
                	if(data){
                		return "禁用";
                	}
              		return "启用";
                }
  		     }]
		});

		$('#addBtn').click(function() {
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
			var deleted = st[0].deleted;
			if (deleted == false) {
				$.smkAlert({
					text : '该用户已经启用!',
					position : 'top-right',
					type : 'warning'
				});
				return ;
			}
			parent.$.smkConfirm({
				text : '是否启用?',
				accept : '是',
				cancel : '否'
			}, function(res) {
				if (res) {
					$.post(ctx + '/user/able', {id:st[0].id}, function(data){
						if(data.success){
							table.bootstrapTable('refresh');
							$.smkAlert({
								text : '启用成功',
								position : 'top-right',
								type : 'success'
							});
						} else {
							$.smkAlert({
								text : '启用失败:' + data.message,
								position : 'top-right',
								type : 'danger'
							});
						}
					}, 'json');
				}
			});

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
			var deleted = st[0].deleted;
			if (deleted == true) {
				$.smkAlert({
					text : '该用户已经禁用!',
					position : 'top-right',
					type : 'warning'
				});
				return ;
			}
			parent.$.smkConfirm({
				text : '是否禁用?',
				accept : '是',
				cancel : '否'
			}, function(res) {
				if (res) {
					$.post(ctx + '/user/enable', {id:st[0].id}, function(data){
						if(data.success){
							table.bootstrapTable('refresh');
							$.smkAlert({
								text : '禁用成功',
								position : 'top-right',
								type : 'success'
							});
						} else {
							$.smkAlert({
								text : '禁用失败:' + data.message,
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