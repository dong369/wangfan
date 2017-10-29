package com.wangfanpinche.utils.pay;

/**
 * 支付宝  订单请求 商品内容
 * @author Administrator
 *
 */
public class AliPayBizContent {

	private static String timeout_express = "15m";//最晚付款时间，逾期将关闭交易
	private static String product_code = "QUICK_MSECURITY_PAY";//销售产品码(QUICK_MSECURITY_PAY)
	private String total_amount;//订单总金额，单位为元，精确到小数点后两位
	private String subject;//标题
	private String body;//商品描述
	private String out_trade_no;//商户网站唯一订单号(收入表id)
	
	
	
	public static String getTimeout_express() {
		return timeout_express;
	}
	public void setTimeout_express(String timeout_express) {
		this.timeout_express = timeout_express;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	
}
