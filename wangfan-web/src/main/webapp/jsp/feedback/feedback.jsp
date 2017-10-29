<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>反馈管理</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-manager-css.jsp"%>

</head>
<body>
	<div class="container-flush">
		<div class="row">
			<div class="col-md-12">
				<div id="toolbar">
					<button id="editBtn" type="button" class="btn btn-info">查看详情</button>
				</div>
				<table id="table"></table>
			</div>
		</div>
	</div>

	<%@	include file="/jsp/inc/inc-manager-js.jsp"%>

	<script type="text/javascript">
		var table = $('#table').bootstrapTable({
			url : ctx + "/feedback/table",
			searchPlaceholder : "反馈内容",
			search : true,
			toolbar : '#toolbar',
			columns : [ {
				checkbox : true
			},
			{
                title: '用户名称',
                field: 'username',
                align: 'left',
                valign: 'middle',
                sortable:true
  		 	},        		
  		 	{
                title: '联系方式',
                field: 'linkType',
                align: 'left',
                valign: 'middle',
                sortable:true
  		 	},
  		 	{
               title: '是否已读',
               field: 'readStatus',
               align: 'left',
               valign: 'middle',
               sortable:true
 		     },
  		  	{
               title: '阅读时间',
               field: 'readDateTime',
               align: 'left',
               valign: 'middle',
               sortable:true
 		     },
     		 {
               title: '反馈内容',
               field: 'content',
               align: 'left',
               valign: 'middle',
               sortable:true
     		 },
  		 	{
               title: '创建时间',
               field: 'createDateTime',
               align: 'left',
               valign: 'middle',
               sortable:true
 		     }
			]
		});

		
		$('#editBtn').click(function() {
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
			window.top.layer.open({
				type : 2,
				title : '查看详情',
				maxmin : true,
				shadeClose : false, //点击遮罩关闭层
				area : [ '800px', '500px' ],
				content : ctx + '/feedback/editPage',
				btnAlign : 'c',
				btn : [ '确定', "关闭" ],
				yes : function(index, layero) {
					if(!layero.find('#layui-layer-iframe' + index).contents()[0].defaultView.$('#myForm').valid()){
						return ;
					}
					$.post(ctx + '/feedback/edit', layero.find('#layui-layer-iframe' + index).contents().find('#myForm').serialize(), function(data) {
						if (data.success) {
							window.top.layer.closeAll();
							table.bootstrapTable('refresh');
						} else {
							$.smkAlert({
								text : '修改失败:' + data.message,
								position : 'top-right',
								type : 'danger'
							});
						}
					}, 'json');
					return false;
				},
				btn2 : function(index, layero) {
					window.top.layer.closeAll();
					return false;
				},
				cancel : function() {

				},
				success : function(layero, index) {
					mk.load(layero.find('#layui-layer-iframe' + index).contents().find('#myForm'), ctx + '/feedback/get?id=' + st[0].id);
				}
			});
		});
	</script>

</body>
</html>