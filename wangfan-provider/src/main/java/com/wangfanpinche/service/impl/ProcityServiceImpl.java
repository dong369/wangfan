package com.wangfanpinche.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.agent.Procity;
import com.wangfanpinche.dto.agent.Procity.Type;
import com.wangfanpinche.service.ProcityService;
import com.wangfanpinche.vo.agent.ProcityVo;

@Service
public class ProcityServiceImpl implements ProcityService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public List<ProcityVo> find() {
		String hql = " select new com.wangfanpinche.vo.agent.ProcityVo(p.id, p.name) from Procity p where p.deleted = ? and p.type = ? ";
		List<ProcityVo> pList = baseDao.find(ProcityVo.class, hql, false, Type.CITY);
		return pList;
	}

	@Override
	public void export() {
		List<Procity> l = baseDao.find(Procity.class, " from Procity order by seq");
		JSONArray sheng = new JSONArray();
		
		//拿出所有的省
		for (int i = 0; i < l.size(); ) {
			Procity p = l.get(i);
			if(p.getProcity() != null){
				i++;
				continue;
			}
			JSONObject j = new JSONObject();
			j.put("id", p.getId());
			j.put("text", p.getName());
			sheng.add(j);
			l.remove(i);
		}
		Map<String, List<Procity>> map = new HashMap<>();
		for (int i = 0; i < l.size(); i++) {
			List<Procity> list = map.get(l.get(i).getProcity().getId());
			if(list == null || list.isEmpty()){
				list = new ArrayList<>();
			}
			list.add(l.get(i));
			map.put(l.get(i).getProcity().getId(), list);
		}
		
		
		//所有的市
		sheng.forEach( j1 -> {
			JSONObject j = (JSONObject) j1;
			JSONArray shiArr = new JSONArray();
			List<Procity> list = map.get(j.getString("id"));
			for (int i = 0; i < list.size(); i++) {
				Procity p = list.get(i);
				
				JSONObject shi = new JSONObject();
				shi.put("id", p.getId());
				shi.put("text", p.getName());
				shiArr.add(shi);
			}
			j.put("nodes", shiArr);
			
			for (int i = 0; i < shiArr.size(); i++) {
				JSONObject s = shiArr.getJSONObject(i);
				List<Procity> xianList = map.get(s.getString("id"));
				JSONArray xianArr = new JSONArray();
				if(xianList  == null || xianList.isEmpty()){
					continue;
				}
				for (int jj = 0; jj < xianList.size(); jj++) {
					Procity pp = xianList.get(jj);
					
					JSONObject xian = new JSONObject();
					xian.put("id", pp.getId());
					xian.put("text", pp.getName());
					xianArr.add(xian);
				}
				s.put("nodes", xianArr);
			}
		});
		
		
		Logger log = Logger.getGlobal();
		log.info(JSON.toJSONString(sheng));
	}
	
}
