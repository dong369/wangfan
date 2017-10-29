package com.wangfanpinche.utils.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wangfanpinche.utils.http.HttpUtils;
import com.wangfanpinche.utils.other.DateUtils;

/**
 * 支付宝APP支付工具类
 * 
 * @author Kevin
 * @date 2016年11月18日
 */
public class AlipayAppUtils {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("utils/pay/aliPay");

	/**
	 * 构建支付信息
	 * 
	 * @return
	 */
	public static String buildOrderInfo(AliPayBizContent content, String notifyUrl) {
		StringBuilder sb = new StringBuilder();
		AliPayRequest request = new AliPayRequest();
		request.setApp_id(getAppId());
		request.setCharset(getCharset());
		request.setMethod(getMethod());
		request.setSign_type(getSignType());
		request.setVersion(getVersion());
		request.setBiz_content(JSON.toJSONString(content));
		request.setNotify_url(notifyUrl);
		request.setTimestamp(DateUtils.toString(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
		sb.append(HttpUtils.serializeObject(request));
		sb.append("&sign=" + sign(request, getRsaPrivate()));
		return sb.toString();
	}

	/**
	 * 退款
	 * 
	 * @param out_trade_no
	 * @param refund_amount
	 */
	public static void refund(String out_trade_no, String refund_amount) {
		AlipayClient alipayClient = new DefaultAlipayClient(getGateway(), getAppId(), getRsaPrivate(), "json", getCharset(), getAliPublicKey());
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();// 创建API对应的request类
		Map<String, String> params = new HashMap<>();
		params.put("out_trade_no", out_trade_no);
		params.put("refund_amount", refund_amount);
		request.setBizContent(JSON.toJSONString(params));
		try {
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			System.out.println(JSON.toJSONString(response));
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 通过alipayClient调用API，获得对应的response类
		// TODO 根据response中的结果继续业务逻辑处理
	}

	public static void main(String[] args) throws Exception {
		// refund("123", "0.01");
		downloadJournal("2016-11");
	}

	public static AlipayDataDataserviceBillDownloadurlQueryResponse downloadJournal(String date) throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(getGateway(), getAppId(), getRsaPrivate(), "json", getCharset(), getAliPublicKey());
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		Map<String, String> biz = new HashMap<>();
		biz.put("bill_type", "trade");
		biz.put("bill_date", date);
		request.setBizContent(JSON.toJSONString(biz));
		AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
		return response;
	}

	/**
	 * 通过rsa加密
	 */
	private static String sign(AliPayRequest target, String rsaKey) {
		String oriSign = SignUtils.sign(HttpUtils.serializeObject(target, true, false), rsaKey);
		String encodedSign = "";
		try {
			encodedSign = URLEncoder.encode(oriSign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedSign;
	}

	/**
	 * 获取乘客车单的支付信息
	 */
	public String get2() {
		return null;
	}

	private static final String getAppId() {
		return bundle.getString("alipay.appid");
	}

	private static final String getCharset() {
		return bundle.getString("alipay.charset");
	}

	private static final String getMethod() {
		return bundle.getString("alipay.method");
	}

	private static final String getSignType() {
		return bundle.getString("alipay.sign_type");
	}

	private static final String getVersion() {
		return bundle.getString("alipay.version");
	}

	public static final String getAliPublicKey() {
		return bundle.getString("alipay.public.key");
	}

	private static final String getRsaPrivate() {
		return bundle.getString("alipay.rsa.private");
	}

	private static final String getGateway() {
		return bundle.getString("alipay.gateway");
	}

}
