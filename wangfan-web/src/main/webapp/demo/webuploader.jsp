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



<style>
    .mk-uploader-list{
        zoom:1;
    }
    .mk-uploader-list:after{
        display: block;
        content: "";
        clear: both;
        
    }
    #picker .file-item {
        float: left;
        margin-right: 5px;
        position: relative;
        width: 100px;
        height: 100px;
    }

    #picker .file-item img {
        display: block;
        width: 100px;
        height: 100px;
        border: 0;
        padding: 0;
    }

    #picker div.file-panel {
        position: absolute;
        height: 0;
        filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0, startColorstr='#80000000', endColorstr='#80000000') \0;
        background: rgba(0, 0, 0, 0.5);
        width: 100%;
        top: 0;
        left: 0;
        overflow: hidden;
        z-index: 300;
        transition: height .3s ease-out;
        -moz-transition: height .3s ease-out;
        -webkit-transition: height .3s ease-out;
        -o-transition: height .3s ease-out;
    }

    #picker .file-item:hover div.file-panel {
        height: 30px;
    }

    #picker .file-item div.file-panel span {
        width: 24px;
        height: 24px;
        display: inline;
        float: right;
        text-indent: -9999px;
        overflow: hidden;
        background: url(${ctx}/static/images/icons.png) no-repeat;
        margin: 5px 1px 1px;
        cursor: pointer;
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    #picker .file-item div.file-panel span.cancel {
        background-position: -48px -24px;
    }

    #picker .file-item div.file-panel span.cancel:hover {
        background-position: -48px 0;
    }

    #picker .file-item .success {
        display: block;
        position: absolute;
        left: 0;
        bottom: 0;
        height: 40px;
        width: 100%;
        z-index: 200;
        background: url(${ctx}/static/images/success.png) no-repeat right bottom;
    }

    #picker .file-item .error {
        margin: 0;
        padding: 0;
        background: #f43838;
        color: #fff;
        position: absolute;
        bottom: 0;
        left: 0;
        height: 28px;
        line-height: 28px;
        width: 100%;
        z-index: 100;
        /* display: none; */
    }
</style>

</head>
<body>

	<div id="picker"></div>

	<script type="text/javascript">
		var kk = uploaderExt({
			//选择文件的按钮。
			pick : '#picker',
			inputName:'username'
		});
	</script>
</body>
</html>