<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>全民代理</title>
    <style>
        *{
            margin:0;
            padding: 0;
        }
        html,body{
            margin:0;
            padding: 0;
            background-color:#f5f5f5;
        }
        #wrap{
        	max-width: 460px;
        	margin: 0 auto;
        }
        #imgA{
            background:url('data:image/jpeg;base64,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') top center no-repeat;
            background-size:cover;
            position: relative;
        }
        #imgA-zw{
            display: block;
            width: 100%;
            height: auto;
            margin: 0;
        }
        #imgA-img{
            position: absolute;
            left:0;
            top:0;
            width: 100%;
            height: 100%;
        }
        .item{
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            width: 300px;
            margin: 0 auto 10px;
            display: -moz-box;
            display: -webkit-box;
            display: box;
            height: 49px;
            padding: 2px;
            border:1px solid #999999;
        }
        .item input{
            display: block;
            -moz-box-flex: 1;
            -webkit-box-flex: 1;
            box-flex: 1;
            height: 43px;
            line-height: 43px;
            border:0;
            background:transparent;
            margin-right: 2px;
            padding-left: 5px;
        }
        .item div{
            width: 60px;
            text-align: center;
            height: 43px;
            line-height: 43px;
            background-color:#999999;
            border-radius: 2px;
            color:#fff;
        }
        .item div.active{
            background-color:#FE8B2E;
        }
        #zhuce.active{
            background-color:#FE8B2E;
        }
        #zhuce{
            display: block;
            margin: 0 auto;
            width: 302px;
            text-align: center;
            height: 47px;
            line-height: 47px;
            background-color:#999999;
            color:#fff;
            text-decoration: none;
        }
        #iAjax{
            position:fixed;
            left: 50%;
            bottom:40%;
            height:0;
            width: 0;
            z-index:999;
            display: none;
            opacity: 1 !important;
        }
        #iAjax div.w{
            position:absolute;
            left: 0;
            bottom:0;
            margin: auto;
            padding: 10px;
            min-width: 100px;
            transform: translateX(-50%);
            -ms-transform: translateX(-50%);
            -moz-transform: translateX(-50%);
            -webkit-transform: translateX(-50%);
            -o-transform: translateX(-50%);
            text-align: center;
            line-height: 22px;
            border-radius: 2px;
            color: #fff;
            background-color: rgba(0,0,0,.5);
        }
    </style>
</head>
<body>
    <div id="wrap">
        <div id="imgA">
            <img id="imgA-zw" src="${ctx}/static/images/h5/qmtg/banner-zw.png" />
            <img id="imgA-img" src="${ctx}/static/images/h5/qmtg/banner.jpg" />
        </div>
        <div style="color:#999999; font-size:15px; text-align: center; margin-bottom: 10px;">全民代理，你的第二份工作</div>
        <div class="item">
            <input id="js-tel" type="tel" placeholder="请输入您的手机号" />
            <div id="js-getcode">验证</div>
        </div>
        <div class="item">
            <input id="js-yanzhengma" type="tel" placeholder="请输入验证码" maxlength="4" />
        </div>
        <a id="zhuce" href="">下载往返</a>
        <p style="color: #999999; font-size: 9px; padding-top: 20px; text-align: center;">©Copyright Reserved 2014-2016 @往返拼车（www.wangfanpinche.com）豫ICP备16013336号-3</p>
    </div>
<script src="${ctx}/static/jslib/jquery/jquery3.1.1-min.js"></script>
<script>
    function existy(variableName) {
        try {
            if (typeof(variableName) == "undefined") {
                return false;
            } else {
                return true;
            }
        } catch(e) {}
        return false;
    }
        var ajaxSetting = function(headers){
            var $iAjax;
            if (!$("#iAjax").length){
                $( "body" ).append($('<div id="iAjax"><div class="w"></div></div>'))
                $iAjax = $( "body" ).find("#iAjax");
            }else{
                $iAjax = $( "body" ).find("#iAjax");
            }


            //信息弹出层
            var handleIAjax = function(tip,time,error){
                var tip = tip || "提交中";
                var time = time || 1500;
                //console.log($iAjax);
                $iAjax.find(".w").html(tip)

                $iAjax.show(0);
                if(existy(time)){
                    setTimeout(function () {
                        $iAjax.hide(0);
                    }, time);
                }
            };
            var hide=function(time){
                var time = time || 0;
                $iAjax.hide();
            }


            var defaultAjaxSetting = {
                //请求方式 默认为"GET"
                type: "POST",
                //(默认: true) 默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
                async: true,
                //发送到服务器的数据
                data: {},
                //默认： 同域请求为false
                crossDomain: false,
                //设置Ajax相关回调函数的上下文
                context: null,
                //发送信息至服务器时内容编码类型
                contentType:"application/x-www-form-urlencoded",
                //设置请求超时时间（毫秒）
                timeout:20000,
                headers:headers || {},
                dataType: "json",
                //在发送请求之前调用，并且传入一个XMLHttpRequest作为参数。
                beforeSend:function (xhr) {
                    //handleIAjax("传输中",200000000000);
                },
                //error 在请求出错时调用。传入XMLHttpRequest对象，描述错误类型的字符串以及一个异常对象（如果有的话）
                error:function (xhr) {
//                    if(substr(xhr.status,0,2)!=20){
//                        var text = [xhr.status,"-",xhr.statusText].join("");
//                        warn(text);
//                        //handleIAjax(text);
//                        if(substr(xhr.status,0,3)==500){
//                            //location.href = "jiadenglu.html"
//                        }
//                    }
                },
                complete:function(){
                    //handleIAjax("传输中",0);
                    console.log("complete")
                }
            };
            var doAjax = function(userAjaxSetting){
                var ajaxSetting = $.extend(defaultAjaxSetting,userAjaxSetting || {})
                $.ajax(ajaxSetting);
            };

            return {
                tip:handleIAjax,
                ajaxState:false,
                ajax: doAjax
            };
        }
        //返回正则后的数组
        function strMatch(str,re){
            return str.match(re);
        }
        //验证手机号
        function inputCheckPhone(value){
            if((/^1[34578]\d{9}$/.test(value))){
                return true;
            }
            return false;
        }
    </script>
<script>
    var handleAjax = ajaxSetting();
    $(function(){
        //计时器
        var jishiState;
        //用于储存手机号
        var mobilePhone;
        //用于储存验证码
        var yanzhengma;
        
        var ctx = '${ctx}';
    	var phone = '${phone}';

        var $zhuce = $("#zhuce")
        //getCodeObj
        var $getCodeObj =  $("#js-getcode");

        $getCodeObj.click(function(){
            if($getCodeObj.hasClass("active")){
                getCode(mobilePhone);
            }

        })



        //验证码输入
        $("#js-yanzhengma").bind("input",function(){
            var $this = $(this);
            var val = $this.val();
            $this.val(strMatch(val,/\d*/)[0]);
            zhuceContro();
            yanzhengma=$this.val()
        })

        //手机号输入
        $("#js-tel").bind("input",function(){
            var $this = $(this);
            var val = $this.val();
            $this.val(strMatch(val,/\d*/)[0]);
            zhuceContro()

            if(inputCheckPhone( $this.val())){
                $getCodeObj.addClass("active");
                mobilePhone = $this.val();
            }else{
                $getCodeObj.removeClass("active");
            }
        })

        //注册亮还是不亮？
        function zhuceContro(){
            if($("#js-yanzhengma").val().length === 4 && inputCheckPhone($("#js-tel").val())){
                $zhuce.addClass("active");
            }else{
                $zhuce.removeClass("active");
            }
        }

        //获取验证码计时器
        function jishi(){
            console.log("获取验证码倒计时: 开始")
            jishiState = true;
            var init = 60;

            $getCodeObj.removeClass("active")
            $getCodeObj.html(init+"秒后重发");
            var setContro = setInterval(function(){
                init = init -1;
                if(init<=0){
                    $getCodeObj.addClass("active");
                    $getCodeObj.html("重新发送");
                    clearInterval(setContro);
                    jishiState = false;
                    console.log("获取验证码倒计时: 结束")
                    return;
                }
                $getCodeObj.html(init+"秒后重发");
            },1000)
        }
        //获取验证码
        function getCode(tel){
            console.log(tel)
            if(jishiState || handleAjax.ajaxState) return;
            handleAjax.tip("传输中",150000);
            handleAjax.ajaxState = true;
            handleAjax.ajax({
                data: {mobilePhone:tel},
                url: "https://app.wangfanpinche.com:8443/user/sendVerify",
                success: function (data) {
                    handleAjax.tip("传输中",0);

                    if(data.success){
                        $("#js-yanzhengma").focus();
                    }
                },
                complete:function(){
                    handleAjax.ajaxState = false;
                    jishi()
                },
            })
        }

        $zhuce.click(function(){
            if($zhuce.hasClass("active")){
                var data ={
                    toPhone: mobilePhone,
                    code: yanzhengma
                }
                zhuce(data);
            }
            return false;
        })
        //注册
        function zhuce(data){
            if(handleAjax.ajaxState) return;
            handleAjax.tip("传输中",150000);
            handleAjax.ajaxState = true;
            handleAjax.ajax({
                data: data,
                url: "https://app.wangfanpinche.com:8443/baseController/recommendWeb/" + phone,
                success: function (data) {
                    if(data.success){
                        location.href = "/"
                    }
                },
                complete:function(){
                    handleAjax.ajaxState = false;
                },
            })
        }

    })
</script>
</body>
</html>