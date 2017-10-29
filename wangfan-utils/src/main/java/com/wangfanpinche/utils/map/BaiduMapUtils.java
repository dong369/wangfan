package com.wangfanpinche.utils.map;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.wangfanpinche.utils.http.HttpUtils;

/**
 * 百度地图工具类
 * @author Thanatos
 *
 */
public class BaiduMapUtils {
	
	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("utils/map/baiduMap");
		
	/**
	 * 路线规划距离和行驶时间
	 * @return
	 */
	public static String routematrix(String source, String target){
		Map<String, String> map = new HashMap<>();
		map.put("output", getOutput());
		map.put("origins", source);
		map.put("destinations", target);
		map.put("coord_type", getCoordtype());	
		map.put("tactics", getTactics());		
		map.put("ak", getAk());
		String string = HttpUtils.get(getRoutematrixUrl(), map);		
		return string;
	}
	
	/**
	 * 坐标转换
	 * GPS设备获取的坐标--转换成--百度坐标
	 * @return
	 */
	public static String geoconvRevers(String location){
		Map<String, String> map = new HashMap<>();
		map.put("output", getOutput());
		map.put("coords", location);
		map.put("from", getFrom());
		map.put("to", getTo());
		map.put("ak", getAk());
		String string = HttpUtils.get(getGeoconvUrl(), map);	
		return string;
	}

	/**
	 * 从坐标查询详细信息
	 */
	public static String geocoderRevers(String location){		
		Map<String, String> map = new HashMap<>();
		map.put("output", getOutput());
		map.put("coordtype", getCoordtype());
		map.put("location", location);
		map.put("pois", getPois());
		map.put("ak", getAk());		
		String string = HttpUtils.get(getGeocoderUrl(), map);	
		return string;
	}
	
	private static final String getOutput(){
		return bundle.getString("output");
	}
	private static final String getAk(){
		return bundle.getString("ak");
	}	
	private static final String getTactics(){
		return bundle.getString("tactics");
	}
	private static final String getFrom(){
		return bundle.getString("from");
	}
	private static final String getTo(){
		return bundle.getString("to");
	}
	private static final String getCoordtype(){
		return bundle.getString("coordtype");
	}
	private static final String getPois(){
		return bundle.getString("pois");
	}		
	private static final String getRoutematrixUrl(){
		return bundle.getString("routematrixUrl");
	}	
	private static final String getGeoconvUrl(){
		return bundle.getString("geoconvUrl");
	}
	private static final String getGeocoderUrl(){
		return bundle.getString("geocoderUrl");
	}
	
}
