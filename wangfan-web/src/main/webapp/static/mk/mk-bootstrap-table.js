var bootStrapTableOpt = {
		method:'post',//post方式提交，中文参数没问题
		cache: false,//不缓存
		contentType: "application/x-www-form-urlencoded",//这点必须要设置，不然后台要以application/json方式接收
		pagination: true,//是否分页
		singleSelect:true,//是否单选
		pageSize: 10,//每页数据数
		pageList: [10, 20, 30, 40, 50],//分页列表的数量
		search: true,//是否有搜索
		searchPlaceholder:'',//搜索的占位符(框内显示的文字)
		showColumns: true,//是否可以选择性的显示某些列
		clickToSelect: true,//点击行的时候选中
		sidePagination:'server',//服务器端分页
		dataType : "json",//返回数据是json数据
		uniqueId: "id",//ID的字段名
		striped: true,//斑马线
		showRefresh:true//是否显示刷新
}

$.extend($.fn.bootstrapTable.defaults, bootStrapTableOpt);//修改Bootstrap Table的默认属性
