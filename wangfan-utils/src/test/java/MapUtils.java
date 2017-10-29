import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangfanpinche.utils.map.BaiduMapUtils;

public class MapUtils {
	

	@Test
	public void test() throws Exception{
		List<Map<String, Object>> l = new ArrayList<>();
		
		String jsonStr = FileUtils.readFileToString(new File("f:/city.txt"), "UTF-8");
		JSONObject j = JSON.parseObject(jsonStr);
		JSONArray shengArray = j.getJSONArray("provinces");
		shengArray.forEach(e -> {
			JSONObject o = (JSONObject) e;
			String point = o.getString("g").substring(0, o.getString("g").indexOf("|"));
			String[] split = point.split(",");
			String geocoderRevers = BaiduMapUtils.geocoderRevers(split[1] + "," + split[0]);
			String string = JSON.parseObject(geocoderRevers).getJSONObject("result").getJSONObject("addressComponent").getString("province");
			Map<String, Object> map = new HashMap<>();
			map.put("text", string);
			l.add(map);
			JSONArray cities = o.getJSONArray("cities");
			List<Map<String, Object>> cit = new ArrayList<>();
			
			cities.forEach(c -> {
				JSONObject cityJ = (JSONObject) c;
				String point1 = cityJ.getString("g").substring(0, o.getString("g").indexOf("|"));
				String[] split1 = point1.split(",");
				String geocoderRevers1 = BaiduMapUtils.geocoderRevers(split1[1] + "," + split1[0]);
				String string1 = JSON.parseObject(geocoderRevers1).getJSONObject("result").getJSONObject("addressComponent").getString("city");
				Map<String, Object> mmm = new HashMap<>();
				mmm.put("text", string1);
				cit.add(mmm);
			});
			map.put("nodes", cit);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		System.out.println(JSON.toJSONString(l));
	}

}
