<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>用户登陆</title>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	var ctx = '${ctx}';
	if(window.top != window.self){
		window.top.location = window.location;
	}
</script>

<!-- jQuery -->
<script src="${ctx}/static/jslib/jquery/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/jslib/font-awesome-4.7.0/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/user/index.css">
<script src="${ctx}/static/jslib/jquery-ui/jquery-ui-autocomplete.min.js"></script>
<script src="${ctx}/static/jslib/jquery-ui/jquery-ui-draggable.min.js"></script>
<script src="${ctx}/static/jslib/underscore/underscore-min.js"></script>
<script src="${ctx}/static/jslib/handlebars/handlebars.min.js"></script>
<script src="${ctx}/static/jslib/layer/layer.js"></script>
<link rel="stylesheet" href="${ctx}/static/css/user/index.css">
</head>
<body style="min-height:100%; overflow:hidden;">
<div id="dr-videoW">
	<video id="bg_video" data-height="1080" data-width="1920" autoplay loop>
		<source src="../static/images/mountain-scene.mp4" type="video/mp4">
	</video>
    <div id="bg_p"></div>
    <div id="bg_t"></div>
</div>

<div id="dr-login">
	<form id="flogin" method="post">
   	  <label for="username">手机号</label>
    	<input type="text" name="mobilePhone" id="username" value="15890699682">
      <label for="psw">密码</label>
        <input type="password" name="pwd" id="psw" value="123456">
        <button class="" id="floginsub" type="submit">登录</button>
        <button class="" id="forgotPwd" type="button" >找回密码</button>
    </form>
</div>

<div id="dr-login-logo"></div>

<script>
$(function(){
	function existy(n){try{return"undefined"!=typeof n}catch(n){}return!1}
	
	var $floginsub = $("#floginsub");
	$("#flogin").submit(function(e){
		
		e.preventDefault();
		if($("#flogin").data("state")==1) return;
		$("#flogin").data("state",1)       
		
		
		
		
		$.post(ctx+ '/user/loginByPc', $('#flogin').serialize() ,function(data){
			$("#flogin").data("state",0)
			
			if(data.success){
				setTimeout(function(){
					handleIajax("登录成功")
					$("#flogin").children(":not(button)").animate({opacity:"0"},500);
					$("#dr-login-logo").animate({opacity:"0"},200);
					$floginsub.addClass("fadeOut");
				},500)
				setTimeout(function(){
					location.href= ctx + "/user/index";
				},1300)
			} else {
				handleIajax(data.message, 1000)
			}
		},'json');
	})
	
	function handleIajax(text,time){
		if(!$floginsub.hasClass("ajax")) $floginsub.addClass("ajax");
		$floginsub.text(text);
		if(time){
			setTimeout(function () {
				$floginsub.text("提交")
				$floginsub.removeClass("ajax");
				$("#flogin").data("state",0)
			}, 2000);
		}
	}
	
	$(document).ajaxSend(function(evt, request, settings){
		handleIajax("提交中")
	}).ajaxError(function(event,request, settings){
		if(!existy(navigator.onLine)){
			handleIajax("未知错误",1000)
			return;
		}
		handleIajax("网络访问失败",1000)
		
	}).ajaxSuccess(function(event,request, settings){
		//handleIajax("提交成功",1000)
	});
	
	
	
	var videoSize = (function(){
		var $wrap=$("#dr-videoW");
		var $bg_video=$("#bg_video");
		var $bg_p=$("#bg_p");
		var vheight=Number($bg_video.attr("data-height"));
		var vwidth=Number($bg_video.attr("data-width"));
		var vRatio = vheight/vwidth; 
		
		$bg_video.bind("canplay",function(){
			$bg_p.fadeOut();
		})
		
		return function(){
			var sheight=$(window).height();
			var swidth=$(window).width();
			
			var sRatio = sheight/swidth;
			if(sRatio>vRatio){
				$bg_video.attr("height",sheight).attr("width",sheight/vRatio);
				$bg_video.addClass("h")
			}else{
				$bg_video.attr("height",swidth*vRatio).attr("width",swidth)
				$bg_video.removeClass("h")
			}
						
			$wrap.css({"height":sheight+"px","width":swidth+"px"});
			
		}
	})()
	var videoSizeControl = _.debounce(videoSize,200);
	$(window).resize(videoSizeControl);
	videoSize();
	
	/*
	想要的效果
	$("#floginsub").html("登录成功").addClass("ajax")
	setTimeout(function(){
		$("#flogin").children(":not(button)").animate({opacity:"0"},500);
		$("#floginsub").addClass("fadeOut");
	},500)
	*/
})
$('#submitBtn').click(function(){
	//console.info($('#myForm').serialize());
	
});	

$('#forgotPwd').click(function(){
	window.location.href = '${ctx}/user/forgetPage';
});
</script>
</body>
</html>