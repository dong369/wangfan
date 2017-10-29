<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>提现管理</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-manager-css.jsp"%>

</head>
<body>
	<div class="container-flush">
		<div class="row">
			<div class="col-md-12">
				<div id="toolbar">
					<button id="addBtn" type="button" class="btn btn-success">审核通过</button>
				</div>
				<table id="table"></table>
			</div>
		</div>
	</div>



	<%@	include file="/jsp/inc/inc-manager-js.jsp"%>

	<script type="text/javascript">
		var table = $('#table').bootstrapTable({
			url : ctx + "/user/cashouttable",
			searchPlaceholder : "内容",
			search : true,
			toolbar : '#toolbar',
			columns : [ {
				checkbox : true
			}, {
				title : '提现人',
				field : 'username',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
				title : '提现金额',
				field : 'money',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
				title : '审核人',
				field : 'reviewername',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
				title : '审核时间',
				field : 'reviewDateTime',
				align : 'left',
				valign : 'middle',
				sortable:true
			}, {
                title: '状态',
                field: 'status',
                align: 'left',
                valign: 'middle'
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
			var status = st[0].status;
			if (status == "完成") {
				$.smkAlert({
					text : '已经审核通过，请选择其他提现数据!',
					position : 'top-right',
					type : 'warning'
				});
				return ;
			}			
			parent.$.smkConfirm({
				text : '是否审核通过?',
				accept : '是',
				cancel : '否'
			}, function(res) {
				if (res) {
					$.post(ctx + '/user/cashOutSucc', {id:st[0].id}, function(data){
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