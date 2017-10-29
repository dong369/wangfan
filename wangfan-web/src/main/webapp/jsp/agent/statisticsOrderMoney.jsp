<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-12">
		<form class="form-horizontal" id="myOrderMoneyForm" role="form">
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
					<button id="orderMoneyFindBtn" type="button" class="btn btn-info">搜索</button>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="row">
	<div class="col-md-12" >
		<div id="orderMoneyCharts" style="height: 500px;width: 1600px;"  ></div>
	</div>	
</div>

<script>
	orderMoneyOption = {
		title : {
			text : '金额统计'
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '金额' ]
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

	$('#orderMoneyFindBtn').click(function(e) {
		$.post(ctx + '/agent/statisticsOrderMoney', $('#myOrderMoneyForm').serialize(), function(data) {
			var timeorderMoneyArr = [];
			var orderMoneyTotal = {
				name : '金额',
				stack : '总量',
				type : 'line',
				data : []
			}
			$.each(data, function(i, n) {
				timeorderMoneyArr.push(n.datetime);
				orderMoneyTotal.data.push(n.total);
			});
			console.info(orderMoneyTotal)

			orderMoneyOption.xAxis.data = timeorderMoneyArr;
			orderMoneyOption.series = [];
			orderMoneyOption.series.push(orderMoneyTotal);
			orderMoneyChart.setOption(orderMoneyOption);

		}, 'json');

	});

	var orderMoneyChart = echarts.init($('#orderMoneyCharts')[0]);
</script>