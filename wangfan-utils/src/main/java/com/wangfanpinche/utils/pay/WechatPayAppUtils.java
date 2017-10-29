package com.wangfanpinche.utils.pay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.utils.http.HttpUtils;
import com.wangfanpinche.utils.id.SnowFlakeIdGenerator;
import com.wangfanpinche.utils.other.EncryptUtils;

public class WechatPayAppUtils {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("utils/pay/wechatPay");

	/**
	 * 交易验证
	 * @throws Exception 
	 */
	public static Boolean valid(long orderId) throws Exception{
		
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";

		WechatPaySeatchRequest request = new WechatPaySeatchRequest();
		request.setAppid(getAppid());
		request.setOut_trade_no(orderId + "");
		request.setMch_id(getMch_id());
		request.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));

		String serializeObject = HttpUtils.serializeObject(request, true, false);
		String key = getKey();
		serializeObject = serializeObject + "&key=" + key;
		request.setSign(EncryptUtils.md5(serializeObject).toUpperCase());
		String postEntity = HttpUtils.postEntity(url, beanToXml(request));
		WechatPaySeatchResponse response = new XmlMapper().readValue(postEntity, WechatPaySeatchResponse.class);
		if(!"SUCCESS".equalsIgnoreCase(response.getReturn_code())){
			throw new RuntimeException(response.getReturn_msg());
		}
		if (!"SUCCESS".equalsIgnoreCase(response.getResult_code())) {
			throw new RuntimeException(response.getErr_code_des());
		}
		if (!"SUCCESS".equalsIgnoreCase(response.getTrade_state())) {
			throw new RuntimeException(response.getTrade_state_desc());
		}
		return true;		

	}

	/**
	 * 退款
	 * @return 
	 */
	public static String refound(long orderId, Integer total, Integer refund) {
		WechatPayRefundRequest request = new WechatPayRefundRequest();
		request.setAppid(getAppid());
		request.setMch_id(getMch_id());
		request.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));
		request.setOut_trade_no(orderId + "");
		request.setOut_refund_no(SnowFlakeIdGenerator.getInstance().generateLongId() + "");
		request.setTotal_fee(total);
		request.setRefund_fee(refund);
		request.setOp_user_id(getMch_id());

		String serializeObject = HttpUtils.serializeObject(request, true, false);
		String key = getKey();
		serializeObject = serializeObject + "&key=" + key;
		request.setSign(EncryptUtils.md5(serializeObject).toUpperCase());
		//String postEntity = HttpUtils.postEntity(url, beanToXml(request));
		return beanToXml(request);

	}

	public final static void main(String[] args) throws Exception {
		//退款
		
		/*
		String refound = refound(808495191525822464L, 1,1);
		String postStringEntity = HttpsUtil.postStringEntity("https://api.mch.weixin.qq.com/secapi/pay/refund", refound, "PKCS12", "G:/apiclient_cert.p12", "1411235902");
		System.out.println(postStringEntity);
		*/

		//下载对帐单
		downloadbill("20161213");
		
	}
	
	/**
	 * 微信下载对帐单
	 * @param date
	 * @return 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static byte[] downloadbill(String date) {
		String url = "https://api.mch.weixin.qq.com/pay/downloadbill";
		WechatDownloadBillRequest request = new WechatDownloadBillRequest();
		request.setAppid(getAppid());
		request.setMch_id(getMch_id());
		request.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));
		request.setBill_date(date);
		String serializeObject = HttpUtils.serializeObject(request, true, false);
		String key = getKey();
		serializeObject = serializeObject + "&key=" + key;
		request.setSign(EncryptUtils.md5(serializeObject).toUpperCase());
		String xml = beanToXml(request);
		byte[] postEntity = HttpUtils.postEntityGetByteArray(url, xml);
System.out.println(new String(postEntity));
		return postEntity;
//		FileUtils.writeByteArrayToFile(new File("D:/abc.gzip"), postEntity);
//		System.out.println(postEntity);
//		XmlMapper mapper = new XmlMapper();
//		WechatDownloadBillResponse response = mapper.readValue(postEntity, WechatDownloadBillResponse.class);
//System.out.println(JSON.toJSONString(response));
		
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static String preOrder(WechatPreOrder preOrder) {
		// 以下写在wechatpay配置文件里
		preOrder.setAppid(getAppid());
		preOrder.setMch_id(getMch_id());
		preOrder.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));
		preOrder.setTrade_type(getTrade_type());
		preOrder.setBody("往返拼车-" + preOrder.getBody());

		String serializeObject = HttpUtils.serializeObject(preOrder, true, false);
		String key = getKey();
		serializeObject = serializeObject + "&key=" + key;

		preOrder.setSign(EncryptUtils.md5(serializeObject).toUpperCase());

		// 转成xml
		String xml = beanToXml(preOrder);

		try {
			xml = new String(xml.toString().getBytes("UTF-8"), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		// 发送xml
		String postEntity = HttpUtils.postEntity(getUrl(), xml);

		return postEntity;
	}

	public static <T> String beanToXml(T bean) {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		String string;
		try {
			string = xmlMapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return string;
	}

	private static final String getAppid() {
		return bundle.getString("wechatPay.appid");
	}

	private static final String getMch_id() {
		return bundle.getString("wechatPay.mch_id");
	}

	private static final String getTrade_type() {
		return bundle.getString("wechatPay.trade_type");
	}

	public static final String getKey() {
		return bundle.getString("wechatPay.key");
	}

	private static final String getUrl() {
		return bundle.getString("wechatPay.url");
	}
}
