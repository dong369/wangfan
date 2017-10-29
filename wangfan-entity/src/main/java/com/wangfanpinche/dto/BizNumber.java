package com.wangfanpinche.dto;

public enum BizNumber {

	
	OP_001,//乘客申请加入车主车单
	
	OP_002,//乘客同意
	
	OP_003,//乘客支付
	
	OP_004,//乘客提醒车主出发

	OO_001,//车主邀请乘客加入
	
	OO_002,//车主同意
	
	OO_003,//车主出发去接乘客
	
	OO_004,//车主提醒乘客支付
	
	PASSENGER,//乘客的活动
	
	OWNER,//车主的活动
	
	MATCHOWNER,//匹配的车主
	
	MATCHPASSENGER//匹配的乘客
}
