<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>车辆信息</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-operator-css.jsp"%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="carBrand"> 车辆品牌 </label>
						
						<div class="col-sm-6">
							<select id="brandId" name="brandId"  class="form-control" title="--请选择品牌--">
			                   <c:forEach items="${cbList}" var="cb">
			                   		<option value="${cb.id}">${cb.name}</option>	
			                   </c:forEach>
			                 </select>							                									
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="carSystem"> 车系名称 </label>

						<div class="col-sm-6">
							<select id="systemId" name="systemId" class="form-control">																                 
			                 </select>
						</div>
					</div>	
																													
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="carYear"> 年代款 </label>

						<div class="col-sm-6">
							<select id="yearId" name="yearId" class="form-control">							                  
			                 </select>
						</div>
					</div>	
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="carDisplacement"> 排量 </label>

						<div class="col-sm-6">
							<select id="displacementId" name="displacementId" class="form-control">							                  
			                 </select>
						</div>
					</div>	
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="carmodel"> 车型 </label>

						<div class="col-sm-6">
							<input type="text" id="carmodel" placeholder="车型" name="carmodel" class="form-control">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="seat"> 座位数(个) </label>

						<div class="col-sm-6">
							<input type="text" id="seat" placeholder="座位数" name="seat" class="form-control">
						</div>
					</div>	
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="fuel"> 工信部综合油耗(L/100km) </label>

						<div class="col-sm-6">
							<input type="text" id="fuel" placeholder="工信部油量" name="fuel" class="form-control">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="actfuel"> 实测油耗(L/100km) </label>

						<div class="col-sm-6">
							<input type="text" id="actfuel" placeholder="实测油量" name="actfuel" class="form-control">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" for="outsideColor"> 外观颜色 </label>

						<div class="col-sm-6">
							<input type="text" id="outsideColor" placeholder="外观颜色" name="outsideColor" class="form-control">
						</div>
					</div>					
				</form>
			</div>
		</div>
	</div>

	<%@	include file="/jsp/inc/inc-operator-js.jsp"%>

	<script type="text/javascript">
		$('#myForm').validate({
			rules : {
				carmodel : {
					required : true
				},
				seat : {
					required : true,
					digits : true
				},
				fuel : {
					required : true
				},
				actfuel : {
					required : true
				},
				outsideColor : {
					required : true
				}
			},
			messages : {
				carmodel : {
					required : "请输入车型"
				},
				seat : {
					required : "请输入座位数",
					digits : "请输入一个整数"
				},
				fuel : {
					required : "请输入工信部综合油耗"
				},
				actfuel : {
					required : "请输入实测油耗"
				},
				outsideColor : {
					required : "请输入外观颜色"
				}
			},
			invalidHandler : function(event, validator) {//valid时如果有错误，调用的方法
				var errors = validator.numberOfInvalids();
				if (errors) {
				} else {
				}
			}
		});
		
		$('#systemId').empty().attr('title', '--请选择车系--');
		$('#yearId').empty().attr('title', '--请选择年代款--');
		$('#displacementId').empty().attr('title', '--请选择排量--');		
		
		$('#brandId').selectpicker({
			liveSearch: true,
			liveSearchNormalize: true,
		    showTick:true
		});
		$('#systemId').selectpicker({
			liveSearch: true,
			liveSearchNormalize: true,
		    showTick:true
		});
		$('#yearId').selectpicker({
			liveSearch: true,
			liveSearchNormalize: true,
		    showTick:true
		});
		$('#displacementId').selectpicker({
			liveSearch: true,
			liveSearchNormalize: true,
		    showTick:true
		});
		
		$('#brandId').on('changed.bs.select ', function(e){
			$('#systemId').empty();
			$('#yearId').empty();
			$('#displacementId').empty();
			if($('#brandId').val().length > 0){
				$.post(ctx + '/carsystem/listByBrandId', 'brandId=' + $('#brandId').val(), function(data){
					for (var i = 0; i < data.length; i++) {
						$('#systemId').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
					}
					$('#systemId').selectpicker('refresh');
					$('#yearId').selectpicker('refresh');
					$('#displacementId').selectpicker('refresh');
				},'JSON');
			}
			
		})
		
		$('#systemId').on('changed.bs.select ', function(e){
			$('#yearId').empty();
			$('#displacementId').empty();
			if($('#systemId').val().length > 0){
				$.post(ctx + '/caryear/listBySystemId', 'systemId=' +  $('#systemId').val(), function(data){
					for (var i = 0; i < data.length; i++) {
						$('#yearId').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
					}
					$('#yearId').selectpicker('refresh');
					$('#displacementId').selectpicker('refresh');
				},'JSON');
			}
		})
		
		
		$('#yearId').on('changed.bs.select ', function(e){
			$('#displacementId').empty();
			if($('#yearId').val().length > 0){
				$.post(ctx + '/cardisplacement/listByYearId', 'yearId=' + $('#yearId').val(), function(data){
					for (var i = 0; i < data.length; i++) {
						$('#displacementId').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
					}
					$('#displacementId').selectpicker('refresh');
				},'JSON');
			}
		})
		
		$('#displacementId').on('changed.bs.select ', function(data){
			$('#carmodel').val($('#systemId option').eq($('#systemId')[0].selectedIndex).text() 
					+' ' +$('#yearId option').eq($('#yearId')[0].selectedIndex).text() 
					+'款 ' +$('#displacementId option').eq($('#displacementId')[0].selectedIndex).text());
		});
	</script>
</body>
</html>