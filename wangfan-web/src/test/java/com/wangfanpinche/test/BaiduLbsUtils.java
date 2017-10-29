package com.wangfanpinche.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.wangfanpinche.utils.http.HttpUtils;

public class BaiduLbsUtils {

	private String entityAddUrl = "http://yingyan.baidu.com/api/v3/entity/add";
	private String entityListUrl = "http://api.map.baidu.com/trace/v2/entity/list";
	
	private String trackAddUrl = "http://api.map.baidu.com/trace/v2/track/addpoint";

	private String ak = "UQ4U7Gqka2n7OsEO1LeYPf4oCUDPhYen";
	private String service_id = "132228";

	public static void main(String[] args) throws Exception {
		BaiduLbsUtils b = new BaiduLbsUtils();
		
		String[] s = new String[]{
			"34.7431037471,113.7835011062",
			"34.7436677471,113.7803031062",	
			"34.7438014053,113.7778707590",	
			"34.7441874053,113.7747807590",
			"34.7441574053,113.7740977590"
		};
		for (int i = 0; i < s.length; i++) {
			b.trackAdd(s[i]);
			Thread.sleep(1000*20);
		}
		b.entityList();
	}
	
	 /* 将字符串转为时间戳 */
    public static String getTimeToStamp() {
        Date date = new Date();
        String tmptime = String.valueOf(date.getTime()).substring(0, 10);
        return tmptime;
    }

	public void entityAdd() {
		String entity_name = "aaaaaaaaaa";
		String entity_desc = "bbbbbbbbb";
		Map<String, String> params = new HashMap<>();
		params.put("ak", ak);
		params.put("entity_name", entity_name);
		params.put("entity_desc", entity_desc);
		params.put("service_id", service_id);
		String post = HttpUtils.post(entityAddUrl, params);
		System.out.println(post);
	}
	
	public void entityList() {
		String entity_name = "aaaaaaaaaa";
		Map<String, String> params = new HashMap<>();
		params.put("ak", ak);
		params.put("entity_name", entity_name);
		params.put("service_id", service_id);
		String post = HttpUtils.get(entityListUrl, params);
		System.out.println(post);
	}
	
	public void trackAdd(String point){
		String entity_name = "aaaaaaaaaa";
		Map<String, String> params = new HashMap<>();
		params.put("ak", ak);
		params.put("service_id", service_id);
		params.put("entity_name", entity_name);
		
		params.put("latitude", point.split(",")[0]);
		params.put("longitude", point.split(",")[1]);
		params.put("loc_time", getTimeToStamp());
		params.put("coord_type", "1");
		String post = HttpUtils.post(trackAddUrl, params);
		System.out.println(post);
	}
}
