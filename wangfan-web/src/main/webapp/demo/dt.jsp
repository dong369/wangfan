<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>你好</title>
<script type="text/javascript">
	var ctx = '${ctx}'
</script>
<link rel="stylesheet" href="${ctx}/static/jslib/webuploader/webuploader.css" />
<script src="${ctx}/static/jslib/jquery/jquery.min.js"></script>
<script src="${ctx}/static/jslib/webuploader/webuploader.js"></script>
<script src="${ctx}/static/mk/mk-webuploader.js"></script>
<script src="${ctx}/demo/CityList.js"></script>
<script src="${ctx}/static/js/baidu/CityData.js"></script>
<script src="${ctx}/static/js/baidu/mkSsxq.json"></script>
<script src="${ctx}/static/js/city/cityCodeList.js"></script>
</head>
<body>

	<div class="dw_header"></div>
	<div id="adLoc" class="Container">
		<label>区：</label> <select class="selectpicker filtersec" id="cityArea" data-style="btn-primary"></select> <label>商圈：</label> <select class="selectpicker filtersec" id="cityBusiness" data-container="body"></select>
	</div>
	<script type="text/JavaScript" src="http://api.map.baidu.com/api?v=2.0&ak=nAOmS8G3oSU5ivkL8fNSAEWx"></script>
	<script type="text/javascript">
		function getCitys(){
			var aabcc = new BMapLib.CityList()
			var treedata = new Array();
			
			$.each(sheng, function(i, n){
				$.each(n.nodes, function(ii, nn){
					$.each(cityCodeList, function(iii, nnn){
						if(nn.text.indexOf(nnn.name) > -1){
							aabcc.getSubAreaList(nnn.code, function(json) {
								var ar = new Array();
								for (i = 0; i < json.sub.length; i++) {
									var area = json.sub[i];
									//$('#cityArea').append("<option value="+area.area_code + ">" + area.area_name + "</option>");
									ar.push({
										text:area.area_name
									});
								}
								nn.nodes = ar;
							});
						}
					});
				});
			});
			
			
			/*
			$.each(sheng, function(i, n) {
				var nodes = new Array();
				var tags = [];
				$.each(n.cities, function(ii, nn){
					var cityNode = {
						text:nn.n
					};
					$.each(cityCodeList, function(iii, nnn){
						if(nnn.name.indexOf(nn.n) >= 0){
							cityNode.code = nnn.code;
							cityNode.text = nnn.name;
							var quNode = [];
							aabcc.getSubAreaList(nnn.code, function(json) {
								for (i = 0; i < json.sub.length; i++) {
									var area = json.sub[i];
									//$('#cityArea').append("<option value="+area.area_code + ">" + area.area_name + "</option>");
									quNode.push({
										text:area.area_name
									});
									console.info(area);
								}
								
							});
							cityNode.nodes = quNode;
						}
					})
					nodes.push(cityNode);
				})
				treedata.push({
					color : "green",
					backColor : "#FFFFFF",
					text : n.n,
					nodes:nodes,
					state:{
						expanded: false
					},
					tags : ["马凯"]
				});
			})
			*/
			return treedata;
		}
		var bbbb = getCitys();
		console.info(JSON.stringify(bbbb));
		/*
		var ipLocation = {
			"address" : "|None|CHINANET|None|None",
			"content" : {
				"address" : "",
				"address_detail" : {
					"city" : "郑州市",
					"city_code" : 268,
					"district" : "",
					"province" : "",
					"street" : "",
					"street_number" : ""
				}
			},
			"status" : 0
		};

		var bMapCityList;
		var curCityCode;
		$(function() {
			curCityCode = ipLocation.content.address_detail.city_code;
			bMapCityList = new BMapLib.CityList();

			setLoc();
		});

		var setLoc = function() {
			var areaCode;
			bMapCityList.getSubAreaList(curCityCode, function(json) {
				for (i = 0; i < json.sub.length; i++) {
					var area = json.sub[i];
					$('#cityArea').append("<option value="+area.area_code + ">" + area.area_name + "</option>");
				}
				areaCode = json.sub[0].area_code;
				bMapCityList.getSubBusinessList(areaCode, function(data) {
					for (i = 0; i < data.sub.length; i++) {
						var business = data.sub[i];
						$('#cityBusiness').append("<option value="+business.area_code + ">" + business.area_name + "</option>");
					}
				});
			});
		};
		$('#cityArea').change(function() {
			$('#cityBusiness').empty();
			bMapCityList.getSubBusinessList($(this).find("option:selected").val(), function(data) {
				for (i = 0; i < data.sub.length; i++) {
					var business = data.sub[i];
					$('#cityBusiness').append("<option value="+business.area_code + ">" + business.area_name + "</option>");
				}
			});
		});
		*/
	</script>
</body>
</body>
</html>