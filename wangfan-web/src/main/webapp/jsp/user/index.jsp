<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>首页</title>

<%@	include file="/jsp/inc/inc.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/static/jslib/font-awesome-4.7.0/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/user/index.css">
<script src="${ctx}/static/jslib/jquery-ui/jquery-ui-autocomplete.min.js"></script>
<script src="${ctx}/static/jslib/jquery-ui/jquery-ui-draggable.min.js"></script>
<script src="${ctx}/static/jslib/underscore/underscore-min.js"></script>
<script src="${ctx}/static/jslib/handlebars/handlebars.min.js"></script>
<script src="${ctx}/static/jslib/layer/layer.js"></script>
</head>

<body>
<div id="dr-h">
    <div id="dr-header">
    	<div id="logo">往返拼车</div>
        <a id="sign-out" href="${ctx}/user/logoutByPc"><i class="fa fa-sign-out"></i>退出</a>
    </div>    
</div>
<div id="dr-d"></div>

<div id="dr-framenav">
	<div class="ui-widget" id="dr-menu-search">
      <input id="tags" placeholder="导航菜单快捷搜索">
    </div>
    <!--菜单容器-->
	<div class="dr-framenav-c"></div>
</div>
<div id="dr-right">
    <div id="dr-tabwrapper">
    	<div id="dr-tabwrapper-swtich" title="点击折叠菜单"><i class="fa fa-arrow-circle-o-left"></i></div>
        <div id="dr-tabwrapper-menu" title="点击管理选项卡"><i class="fa fa-th"></i></div>
        <!--选项卡-->
        <div id="dr-ul-wrap"><div><ul></ul></div></div>
    </div>
    <!--iframe容器-->
    <div id="fr-iframebox"></div>
</div>

<!--fa-unlock-alt minus-->
<div id="dr-menu-management"><div class="dr-unfold"><ul class="dr-menu-management-item"></ul></div><div class="dr-user-unfold"><ul class="dr-menu-management-item"></ul></div><div class="dr-tab-list"><ul class="dr-menu-management-item"></ul></div></div>

<script>


	
	/**
	 * 默认打开的页面
	 * 标题
	 * 链接
	 * 是否固定 1:表示固定 0:不固定
	 * */
	var unfoldPage = [
		["首页","0.html",1]
	]
	/**
	 * 用户固定打开的页面
	 * 标题
	 * 链接
	 * 是否固定 2:表示固定 0:不固定
	 * 默认是空数组，目前是h5本地储存
	 * */
	var userfoldPage = [];
</script>

<!--index.js置于最底部-->
<script src="${ctx}/static/js/user/index.js"></script>

<script type="text/javascript">
	$(function(){
		$.post('${ctx}/user/userMenu', {}, function(data){
			var menuArray = new Array();
			for(var i = 0; i < data.length; i++){
				if(data[i].url.substr(0,4) != 'http'){
					data[i].url = ctx + data[i].url;
				}
				var menuObj = {
						text : data[i].name,
						icon : data[i].icon,
						href : data[i].url,
						children : new Array()
					};
				
				menuArray.push(menuObj);
				if(data[i].resources.length == 0){
					continue;
				}

				for(var j = 0; j < data[i].resources.length; j++){
					if(!data[i].resources[j].hasOwnProperty('url')){
						data[i].resources[j].url = '#';
					}
					if(data[i].resources[j].url.substr(0,4) != 'http'){
						data[i].resources[j].url = ctx + data[i].resources[j].url;
					}
					menuObj.children.push({
						text : data[i].resources[j].name,
						icon : data[i].resources[j].icon,
						href : data[i].resources[j].url
					});
				}
				
				
			}
			//左侧菜单生成
			//$("#dr-framenav .dr-framenav-c").append($(navtemplate(menuArray)));
			indexInit(menuArray)
		},'json');
	})
	
	

</script>
</body>
</html>
