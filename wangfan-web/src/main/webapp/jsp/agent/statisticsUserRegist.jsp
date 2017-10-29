<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-12">
		<form class="form-horizontal" id="myUserRegForm" role="form">
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
					<button id="userRegFindBtn" type="button" class="btn btn-info">搜索</button>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="row">
	<div class="col-md-12" >
		<div id="userRegCharts" style="height: 500px;width: 1600px;"  ></div>
	</div>
</div>

<script>
	userRegOption = {
		title : {
			text : '用户注册量统计'
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '用户注册量' ]
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

	$('#userRegFindBtn').click(function(e) {
		$.post(ctx + '/agent/statisticsUserRegist', $('#myUserRegForm').serialize(), function(data) {
			var timeuserRegArr = [];
			var userRegTotal = {
				name : '用户注册量',
				stack : '总量',
				type : 'line',
				data : []
			}
			$.each(data, function(i, n) {
				timeuserRegArr.push(n.datetime);
				userRegTotal.data.push(n.total);
			});
			console.info(userRegTotal)

			userRegOption.xAxis.data = timeuserRegArr;
			userRegOption.series = [];
			userRegOption.series.push(userRegTotal);
			userRegChart.setOption(userRegOption);

		}, 'json');

	});

	var userRegChart = echarts.init($('#userRegCharts')[0]);
</script>