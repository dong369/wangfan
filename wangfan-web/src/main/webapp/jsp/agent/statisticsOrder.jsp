<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-12">
		<form class="form-horizontal" id="myForm" role="form">
			<div class="form-group">
				<label for="procity" class="col-sm-2 control-label">代理区域</label>
				<div class="col-sm-6">
					<select class="form-control" name="id">
						<c:forEach items="${agent}" var="a">
							<option value="${a.procities.id }">${a.procities.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="day" class="col-sm-2 control-label">查询最近几天的</label>
				<div class="col-sm-6">
					<select class="form-control" name="zuijin">
						<option value="">不查</option>
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>

					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">开始日期</label>
				<div class="col-sm-6">
					<div class="input-group date">
						<input type="text" class="form-control datepicker" name="startDate">
						<div class="input-group-addon">
							<span class="glyphicon glyphicon-th"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">结束日期</label>
				<div class="col-sm-6">
					<div class="input-group date">
						<input type="text" class="form-control datepicker" name="endDate">
						<div class="input-group-addon">
							<span class="glyphicon glyphicon-th"></span>
						</div>
					</div>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button id="orderFindBtn" type="button" class="btn btn-info">搜索</button>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="row">
	<div id="orderCharts" class="col-md-12" style="height: 500px"></div>
</div>

<script>
	option = {
		title : {
			text : '订单量统计'
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '订单量' ]
		},
		grid : {
			left : '3%',
			right : '4%',
			bottom : '3%',
			containLabel : true
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'category',
			boundaryGap : false,
			data : []
		},
		yAxis : {
			type : 'value'
		},
		series : []
	};

	$('#orderFindBtn').click(function(e) {
		$.post(ctx + '/agent/statisticsOrder', $('#myForm').serialize(), function(data) {
			var timeArr = [];
			var orderTotal = {
				name : '订单量',
				stack : '总量',
				type : 'line',
				data : []
			}
			$.each(data, function(i, n) {
				timeArr.push(n.datetime);
				orderTotal.data.push(n.total);
			});
			//console.info(orderTotal)

			option.xAxis.data = timeArr;
			option.series = [];
			option.series.push(orderTotal);
			myChart.setOption(option);

		}, 'json');

	});

	// 基于准备好的dom，初始化echarts实例  // 使用刚指定的配置项和数据显示图表。
	var myChart = echarts.init($('#orderCharts')[0]);
</script>