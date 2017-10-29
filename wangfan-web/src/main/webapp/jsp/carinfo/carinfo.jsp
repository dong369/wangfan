<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/jsp/inc/inc-jstl.jsp"%>

<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>车辆信息</title>
<%@	include file="/jsp/inc/inc-meta.jsp"%>
<%@	include file="/jsp/inc/inc-manager-css.jsp"%>

</head>
<body>
	<div class="container-flush">
		<div class="row">
			<div class="col-md-12">
				<div id="toolbar">
					<button id="addBtn" type="button" class="btn btn-success">添加</button>
					<button id="editBtn" type="button" class="btn btn-info">修改</button>
					<button id="deleteBtn" type="button" class="btn btn-danger">删除</button>
					<form id="myForm" style="display: inline;">
						按<b style="color: #5bc0de;">品牌拼音首字母</b>查找：<a id="A" onclick="binitial(A)">A</a>&nbsp;<a id="B" onclick="binitial(B)">B</a>&nbsp;<a id="C" onclick="binitial(C)">C</a>&nbsp;<a id="D" onclick="binitial(D)">D</a>&nbsp;
						<a id="F" onclick="binitial(F)">F</a>&nbsp;<a id="H" onclick="binitial(H)">H</a>&nbsp;<a id="J" onclick="binitial(J)">J</a>&nbsp;<a id="L" onclick="binitial(L)">L</a>
						<a id="M" onclick="binitial(M)">M</a>&nbsp;<a id="O" onclick="binitial(O)">O</a>&nbsp;<a id="Q" onclick="binitial(Q)">Q</a>&nbsp;<a id="R" onclick="binitial(R)">R</a>&nbsp;<a id="S" onclick="binitial(S)">S</a>
						<a id="T" onclick="binitial(T)">T</a>&nbsp;<a id="W" onclick="binitial(W)">W</a>&nbsp;<a id="X" onclick="binitial(X)">X</a>&nbsp;<a id="Y" onclick="binitial(Y)">Y</a>&nbsp;<a id="Z" onclick="binitial(Z)">Z</a>
						
						<input type ="hidden" id="ini" name="ini" />
						<select name="brandId" id="brandSelect"></select>
						<select name="systemId" id="systemSelect"></select>
						<select name="yearId" id="yearSelect"></select>
						<select name="displacementId" id="displacementSelect"></select>
					</form>
				</div>
				<table id="table"></table>
			</div>
		</div>
	</div>

	<%@	include file="/jsp/inc/inc-manager-js.jsp"%>
	
	<script type="text/javascript">
		var postage = '${postage}';
		var toll = '${toll}';
	</script>
	<script type="text/javascript">		
		var table = $('#table').bootstrapTable({
			url : ctx + "/carinfo/table",
			searchPlaceholder : "车型",
			search : true,
			toolbar : '#toolbar',
			queryParams:function(params){
       	    	params.initial =  $('#ini').val();	 
		    	params.brandId = $('#brandSelect').val();
		    	params.systemId = $('#systemSelect').val();
		    	params.yearId = $('#yearSelect').val();
		    	params.displacementId = $('#displacementSelect').val();
		    	//alert("params.initial-------" + params.initial + "---------params.brandId------" + params.brandId);
	        	return params;
	        },
			columns : [ {
				checkbox : true
			},
			{
                title: '品牌',
                field: 'brandName',
                align: 'left',
                valign: 'middle',
                sortable:true
	  		 }, 
	  		 {
	                title: '车系',
	                field: 'systemName',
	                align: 'left',
	                valign: 'middle',
	                sortable:true
	  		 },
	  		 {
	               title: '年代款',
	               field: 'yearName',
	               align: 'left',
	               valign: 'middle',
	               sortable:true
 			 },
 			{
               title: '排量',
               field: 'displacementName',
               align: 'left',
               valign: 'middle',
               sortable:true
 			 }, 			
 		 	{
              title: '车型',
              field: 'carmodel',
              align: 'left',
              valign: 'middle',
              sortable:true
			 },
			 {
	               title: '座位数(个)',
	               field: 'seat',
	               align: 'left',
	               valign: 'middle',
	               sortable:true,
	               editable: {
	                   type: 'text',
	                 	 title: '座位数',
						 validate: function (v) {
							 if (isNaN(v)) return '座位数必须是数字';
		                     var fuel = parseInt(v);
		                     if (fuel <= 1) return '座位数必须是正整数，且大于1';
						 }
	               		}
		 		 	},
				{
	               title: '工信部综合油耗(L/100km)',
	               field: 'fuel',
	               align: 'left',
	               valign: 'middle',
	               sortable:true,
	               editable: {
	                   type: 'text',
	                 	 title: '工信部油耗',
						 validate: function (v) {
							 if (isNaN(v)) return '油耗必须是数字';
		                     var fuel = parseInt(v);
		                     if (fuel <= 0) return '油耗必须是正整数';
						 }
	               }
				 
	 			 },
	 			{
	               title: '实测油耗(L/100km)',
	               field: 'actfuel',
	               align: 'left',
	               valign: 'middle',
	               sortable:true
	 			 },
       			 {
                     title: '位置费用',
                     field: 'aabbccdee',
                     align: 'left',
                     valign: 'middle',
                     formatter:function(data,row,c){
                    	if (isNaN(row.fuel)) {
	                      	return 0;
						}else{
							//(油耗 * 油费 + 过路费 ）* 1.2 / 座位数
							// 0.33 
							var youfei = 1*row.fuel/100*postage;
							var guolufei = 1*toll;
	                    	var seat = row.seat-1;
	                      	return (guolufei + youfei) * 1.2 / seat;	
						}
                     }
       			 }	 			
			 ],
			 onEditableSave: function (field, row, oldValue, $el) {
		    	   var params = {};
		    	   params[field] = row[field]
		    	   params['id'] = row.id
		    	  $.post(ctx + '/carinfo/updatefuel',params, function(resp){
		    		  if(!resp.success){
		    			  alert('修改失败，请刷新页面');
		    		  }
		    	  }, 'JSON');
	           }
		});

		$('#deleteBtn').click(function() {
			var st = $('#table').bootstrapTable('getSelections');
			if(st <= 0){
				$.smkAlert({
					text : '请选中一条数据再操作',
					position : 'top-right',
					type : 'warning'
				});
				return ;
			}
			var seleted = st[0];
			parent.$.smkConfirm({
				text : '是否删除?',
				accept : '是',
				cancel : '否'
			}, function(res) {
				if (res) {
					$.post(ctx + '/carinfo/delete', {id:st[0].id}, function(data){
						if(data.success){
							table.bootstrapTable('refresh');
							$.smkAlert({
								text : '删除成功',
								position : 'top-right',
								type : 'success'
							});
						} else {
							$.smkAlert({
								text : '删除失败:' + data.message,
								position : 'top-right',
								type : 'danger'
							});
						}
					}, 'json');
				}
			});

		});
		$('#addBtn').click(function() {
			window.top.layer.open({
				type : 2,
				title : '车辆信息添加',
				maxmin : true,
				shadeClose : false, //点击遮罩关闭层
				area : [ '800px', '780px' ],
				content : ctx + '/carinfo/addPage',
				btnAlign : 'c',
				btn : [ '确定', "关闭" ],
				yes : function(index, layero) {
					if(!layero.find('#layui-layer-iframe' + index).contents()[0].defaultView.$('#myForm').valid()){
						return ;
					}
					$.post(ctx + '/carinfo/add', layero.find('#layui-layer-iframe' + index).contents().find('#myForm').serialize(), function(data) {
						if (data.success) {
							window.top.layer.closeAll();
							table.bootstrapTable('refresh');
						} else {
							$.smkAlert({
								text : '添加失败:' + data.message,
								position : 'top-right',
								type : 'danger'
							});
						}
					}, 'json');
					return false;
				},
				btn2 : function(index, layero) {
					window.top.layer.closeAll();
					return false;
				},
				cancel : function() {

				}
			});
		});
		$('#editBtn').click(function() {
			var st = $('#table').bootstrapTable('getSelections');
			if(st <= 0){
				$.smkAlert({
					text : '请选中一条数据再操作',
					position : 'top-right',
					type : 'warning'
				});
				return ;
			}
			var seleted = st[0];
			window.top.layer.open({
				type : 2,
				title : '车辆信息修改',
				maxmin : true,
				shadeClose : false, //点击遮罩关闭层
				area : [ '800px', '780px' ],
				content : ctx + '/carinfo/editPage?id=' + st[0].id,
				btnAlign : 'c',
				btn : [ '确定', "关闭" ],
				yes : function(index, layero) {
					if(!layero.find('#layui-layer-iframe' + index).contents()[0].defaultView.$('#myForm').valid()){
						return ;
					}
					$.post(ctx + '/carinfo/edit', layero.find('#layui-layer-iframe' + index).contents().find('#myForm').serialize(), function(data) {
						if (data.success) {
							window.top.layer.closeAll();
							table.bootstrapTable('refresh');
						} else {
							$.smkAlert({
								text : '修改失败:' + data.message,
								position : 'top-right',
								type : 'danger'
							});
						}
					}, 'json');
					return false;
				},
				btn2 : function(index, layero) {
					window.top.layer.closeAll();
					return false;
				},
				cancel : function() {

				},
				success : function(layero, index) {
					mk.load(layero.find('#layui-layer-iframe' + index).contents().find('#myForm'), ctx + '/carinfo/get?id=' + st[0].id);
				}
			});
		});
		
		
		/* var oto2=document.getElementsByTagName('a');
		for(var y=0;y<oto2.length;y++){
	       oto2[y].index=y;	
	       oto2[y].style = "color: #5bc0de;";
	       oto2[y].onclick=function(){
	    	   //alert(" $('#ini').val()-----"+ $('#ini').val());
	    	   if ($('#ini').val() != "") {			
				   document.getElementById($('#ini').val()).style = "color: #5bc0de;";
			   }		    	   
	    	   oto2[this.index].style = "color:#F25141;";		    	      	  
	        }
	    };  */
	    
	    
		function binitial(str){
			//alert("str--------" + str.innerHTML);
			$('#brandSelect').empty().append('<option value="">--请选择--</option>');
			$.post(ctx + '/carbrand/listByInitial','initial=' + str.innerHTML, function(data){
				for (var i = 0; i < data.length; i++) {
					$('#brandSelect').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
				}

				$('#brandSelect').on('change', function(e){
					brandSelectChange();
					$('#table').bootstrapTable('refresh');
				});
				$('#systemSelect').on('change', function(e){
					systemSelectChange();
					$('#table').bootstrapTable('refresh');
				});
				$('#yearSelect').on('change', function(e){
					yearSelectChange();
					$('#table').bootstrapTable('refresh');
				});
				$('#displacementSelect').on('change', function(e){
					$('#table').bootstrapTable('refresh');
				});
			},'JSON');
		
		    $('#ini').val(str.innerHTML);
			$('#table').bootstrapTable('refresh');
		}
		
	    
		//模糊查询	
		$('#brandSelect').empty().append('<option value="">--请选择--</option>');
		 $.post(ctx + '/carbrand/list',{}, function(data){
			for (var i = 0; i < data.length; i++) {
				$('#brandSelect').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
			}

			$('#brandSelect').on('change', function(e){
				brandSelectChange();
				$('#table').bootstrapTable('refresh');
			});
			$('#systemSelect').on('change', function(e){
				systemSelectChange();
				$('#table').bootstrapTable('refresh');
			});
			$('#yearSelect').on('change', function(e){
				yearSelectChange();
				$('#table').bootstrapTable('refresh');
			});
			$('#displacementSelect').on('change', function(e){
				$('#table').bootstrapTable('refresh');
			});
		},'JSON');
		
		//品牌更改
		function brandSelectChange(){
			var id = $('#brandSelect').val();
			$('#systemSelect').empty().append('<option value="">--请选择--</option>');
			$('#yearSelect').empty().append('<option value="">--请选择--</option>');
			$('#displacementSelect').empty().append('<option value="">--请选择--</option>');
			if(id.length > 0) {
				$.post(ctx + '/carsystem/listByBrandId', 'brandId=' + id, function(data){
					$('#systemSelect').empty();
					$('#systemSelect').append('<option value="">--请选择--</option>');
					for (var i = 0; i < data.length; i++) {
						$('#systemSelect').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
					}
				},'JSON');
			}
		}
		
		//车型更改
		function systemSelectChange(){
			var id = $('#systemSelect').val();
			$('#yearSelect').empty().append('<option value="">--请选择--</option>');
			$('#displacementSelect').empty().append('<option value="">--请选择--</option>');
			if(id.length > 0){
				$.post(ctx + '/caryear/listBySystemId', 'systemId=' + id, function(data){
					$('#yearSelect').empty().append('<option value="">--请选择--</option>');
					for (var i = 0; i < data.length; i++) {
						$('#yearSelect').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
					}
				},'JSON');
			}
		}
		
		//年代更改
		function yearSelectChange(){
			var id = $('#yearSelect').val();
			$('#displacementSelect').empty().append('<option value="">--请选择--</option>');
			if(id.length > 0) {
				$.post(ctx + '/cardisplacement/listByYearId', 'yearId=' + id, function(data){
					$('#displacementSelect').empty().append('<option value="">--请选择--</option>');
					for (var i = 0; i < data.length; i++) {
						$('#displacementSelect').append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
					}
				},'JSON');
			}
		}	
				
		$('#systemSelect').append('<option value="">--请选择--</option>');
		$('#yearSelect').append('<option value="">--请选择--</option>');
		$('#displacementSelect').append('<option value="">--请选择--</option>');
	</script>

</body>
</html>