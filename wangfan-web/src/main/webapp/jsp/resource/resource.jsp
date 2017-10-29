<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>资源管理</title>

<%@	include file="/jsp/inc/inc.jsp"%>
<script src="${ctx}/static/jslib/bootstrap-treeview/dist/bootstrap-treeview.min.js"></script>

</head>
<body>
	<div class="container-flush">
		<div class="row">
			<div class="col-md-4">
				<div id="tree"></div>
			</div>
			<div class="col-md-4">
			</div>
		</div>
	</div>

	<script type="text/javascript">
// 	$.post('${ctx}/user/userMenu', {}, function(data){
// 		console.info(data);
// 		$('#tree').treeview({
// //	         color: "#428bca",
// 	        //enableLinks: true,
// 	        text:'text',
// 	        data: data
// 	      });
// 	},'json');
	var tree = [
	            {
	              text: "Parent 1",
	              id:"11111",
	              tags:["BABY"],
	              nodes: [
	                {
	                	text: "Child 1",
	                  nodes: [
	                    {
	                    	text: "Grandchild 1"
	                    },
	                    {
	                    	text: "Grandchild 2"
	                    }
	                  ]
	                },
	                {
	                	text: "Child 2"
	                }
	              ]
	            },
	            {
	            	text: "Parent 2"
	            },
	            {
	            	text: "Parent 3"
	            },
	            {
	            	text: "Parent 4"
	            },
	            {
	            	text: "Parent 5"
	            }
	          ];
	
	//$('#tree').treeview(tree);
	$('#tree').treeview({
//         color: "#428bca",
        //enableLinks: true,
        showTags:true,
        data: tree
      });
	$('#tree').on('nodeSelected', function(event, data) {
		console.info(event);
		console.info(data);
      });
	</script>
</body>
</html>