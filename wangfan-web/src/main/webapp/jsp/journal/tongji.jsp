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
				日期<input id="dateInput" type="text" > 
				<button id="downloadAliJournal" class="btn btn-default">下载支付宝帐单</button> 
				<button id="downloadWechatJournal" class="btn btn-default">下载微信帐单</button>
				<button id="dateSearchBtn" class="btn btn-default">搜索</button>
				 <br>
				订单号<input id="orderNumber" type="text" >
				<button id="orderNumberBtn" class="btn btn-default">搜索</button>		
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div id="toolbar">
					<button id="addBtn" type="button" class="btn btn-success">添加</button>
					<button id="editBtn" type="button" class="btn btn-info">修改</button>
					<button id="deleteBtn" type="button" class="btn btn-danger">删除</button>
				</div>
				<table id="table"></table>
			</div>
		</div>
	</div>



	<%@	include file="/jsp/inc/inc-manager-js.jsp"%>

	<script type="text/javascript">
		var table = $('#table').bootstrapTable({
			url : ctx + "/journal/table",
			searchPlaceholder : "用户名称",
			search : true,
			toolbar : '#toolbar',
			columns : [ {
				checkbox : true
			}, {
				title : '付款方式',
				field : 'payType',
				align : 'left',
				valign : 'middle'
			}, {
				title : '交易类型',
				field : 'type',
				align : 'left',
				valign : 'middle'
			}, {
				title : '时间',
				field : 'modifyDateTime',
				align : 'left',
				valign : 'middle'
			}, {
				title : '创建人',
				field : 'username',
				align : 'left',
				valign : 'middle'
			}, {
				title : '金额',
				field : 'money',
				align : 'left',
				valign : 'middle'
			}, {
				title : '状态',
				field : 'status',
				align : 'left',
				valign : 'middle'
			}, {
				title : '订单号',
				field : 'orderNumber',
				align : 'left',
				valign : 'middle'
			} ]
		});
		
		$('#downloadAliJournal').click(function(e){
			$.post(ctx + '/journal/downloadAliJournal',{
				date:$('#dateInput').val()
			}, function(data){
				var str = data.obj;
				if(data.success){
					window.location.href = str; 
				} else {
					alert(data.message)
				}
			}, 'json');
		})
		
		$('#downloadWechatJournal').click(function(e){
			$.post(ctx + '/journal/downloadWechatJournal',{
				date:$('#dateInput').val()
			}, function(data){
				var str = data.obj;
				if(data.success){
					window.location.href = str; 
				} else {
					alert(data.message)
				}
			}, 'json');
		})
		
		$('#dateSearchBtn').click(function(){
			$('#table').bootstrapTable('refresh',{query: {
				searchDate:$('#dateInput').val()
			}});
		});
		
		$('#orderNumberBtn').click(function(){
			$('#table').bootstrapTable('refresh',{query: {
				orderNumber:$('#orderNumber').val()
			}});
		});

	</script>

</body>
</html>