package com.wangfanpinche.utils.sms;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import com.alibaba.fastjson.JSON;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.wangfanpinche.utils.other.StringUtils;

/**
 * 短信工具类
 * @author Kevin
 * @date 2016年11月18日
 */
public class AliSmsUtils {
	
	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("utils/sms/aliSms");
	
	/**
	 * 发送验证码
	 */
	public static void sendValidCode(String phone, String code){
		
		TaobaoClient client = new DefaultTaobaoClient(getUrl(), getAppKey(), getAppSecret());
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType( getSmsType());
		req.setSmsFreeSignName(StringUtils.ascConveterUTF8(getSmsFreeSignName()));
		Map<String, String> params = new HashMap<>();
		params.put("code", code);
		params.put("product", StringUtils.ascConveterUTF8(getProduct()));
		req.setSmsParamString(JSON.toJSONString(params));
		req.setRecNum(phone);
		req.setSmsTemplateCode( getSmsTemplateCode());
		AlibabaAliqinFcSmsNumSendResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		if(rsp.getErrorCode() != null){
			throw new RuntimeException("短信发送失败：" + rsp.getSubMsg());
		}
		
	}
	
	private static final String getUrl(){
		return bundle.getString("sms.url");
	}
	
	private static final String getAppKey(){
		return bundle.getString("sms.appKey");
	}
	
	private static final String getAppSecret(){
		return bundle.getString("sms.appSecret");
	}
	private static final String getSmsType(){
		return bundle.getString("smsType");
	}
	private static final String getSmsFreeSignName(){
		return bundle.getString("smsFreeSignName");
	}
	private static final String getProduct(){
		return bundle.getString("product");
	}
	private static final String getSmsTemplateCode(){
		return bundle.getString("smsTemplateCode");
	}
	
	

}
