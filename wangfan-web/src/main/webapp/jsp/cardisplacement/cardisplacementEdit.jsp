<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>车辆排量</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-operator-css.jsp"%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form id="myForm" class="form-horizontal" role="form">
					<input type="hidden" name="id">
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="carBrand"> 车辆品牌 </label>
						<div class="col-sm-6">
							<select id="brandId" name="brandId"  class="form-control">							                   
			                   <c:forEach items="${cbList}" var="cb">							                   
			                   		<option value="${cb.id}">${cb.name}</option>	
			                   </c:forEach>
			                 </select>							                									
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="carSystem"> 车系名称 </label>

						<div class="col-sm-6">
							<select id="systemId" name="systemId" class="form-control">	
							 <c:forEach items="${csList}" var="cs">							                   
			                   		<option value="${cs.id}">${cs.name}</option>	
			                   </c:forEach>						                  
			                 </select>
						</div>
					</div>	
																													
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="carYear"> 年代款 </label>

						<div class="col-sm-6">
							<select id="yearId" name="yearId" class="form-control">
							<c:forEach items="${cyList}" var="cy">							                   
			                   		<option value="${cy.id}">${cy.name}</option>	
			                   </c:forEach>								                  
			                 </select>
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-3 control-label">排量</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="name" placeholder="排量" name="name">
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
				name : {
					required : true
				}
			},
			messages : {
				name : {
					required : "请输入排量"
				}
			},
			invalidHandler : function(event, validator) {//valid时如果有错误，调用的方法
				var errors = validator.numberOfInvalids();
				if (errors) {
				} else {
				}
			}
		});
		
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
		
		$('#brandId').on('changed.bs.select	', function(e){
			$('#systemId').empty();
			$('#yearId').empty();
			if($('#brandId').val().length > 0){
				$.post(ctx + '/carsystem/listByBrandId', 'brandId=' + $('#brandId').val(), function(data){
					for (var i = 0; i < data.length; i++) {
						$('#systemId').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
					}
					$('#systemId').selectpicker('refresh');
					$('#yearId').selectpicker('refresh');
				},'JSON');
			}
			
		})
		
		$('#systemId').on('changed.bs.select ', function(e){
			$('#yearId').empty();
			if($('#systemId').val().length > 0){
				$.post(ctx + '/caryear/listBySystemId', 'systemId=' + $('#systemId').val(), function(data){
					for (var i = 0; i < data.length; i++) {
						$('#yearId').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
					}
					$('#yearId').selectpicker('refresh');
				},'JSON');
			}
		})
	</script>
</body>
</html>