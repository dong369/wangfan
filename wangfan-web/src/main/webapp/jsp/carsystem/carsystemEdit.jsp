<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>车辆车系</title>
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
						<label class="col-sm-3 control-label no-padding-right" for="brand"> 车辆品牌 </label>
						<div class="col-sm-6">
							<select name="brandId" id="brandId" class="form-control">							                   
			                   <c:forEach items="${cbList}" var="cb">							                   
			                   		<option value="${cb.id}">${cb.name}</option>	
			                   </c:forEach>
			                 </select>							                									
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-3 control-label">车系名称</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="name" placeholder="车系名称" name="name">
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
					required : "请输入车系名称"
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
	</script>
</body>
</html>