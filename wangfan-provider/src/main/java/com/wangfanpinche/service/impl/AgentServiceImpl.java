package com.wangfanpinche.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Role;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.agent.Agent;
import com.wangfanpinche.dto.agent.Procity;
import com.wangfanpinche.dto.agent.Procity.Type;
import com.wangfanpinche.service.AgentService;
import com.wangfanpinche.utils.other.DateUtils;
import com.wangfanpinche.vo.UserVo;
import com.wangfanpinche.vo.agent.AgentVo;

@Service
public class AgentServiceImpl implements AgentService {

	@Autowired
	private BaseDao baseDao;

	@Override
	public UserVo getUserByPhone(UserVo vo){
		String hql = " select new com.wangfanpinche.vo.UserVo( u.id, u.deleted, u.username, u.mobilePhone) from User u where u.mobilePhone = ? ";
		UserVo uvo = baseDao.get(UserVo.class, hql, vo.getMobilePhone());
		if(uvo == null){
			return new UserVo();
		}
		List<Agent> agents = baseDao.find(Agent.class, " select j from Agent j where j.user.id = ? ", uvo.getId());
		
		return uvo;
	}

	@Override
	public void bind(AgentVo vo) {
		String hqlp = " from Procity where deleted = ? and id = ? ";
		Procity pro = baseDao.get(Procity.class, hqlp, false, vo.getProcityId());
		if (pro.getType().equals(Type.PROVINCE)) {
			throw new RuntimeException("不能绑定省,请选择市区进行绑定!");
		}
		
		//判断是否绑定过Agent表，如果没有绑定过，则赋给其代理商权限（agentRole）
		String hqlc = " select count(id) from Agent where deleted = ? and user.id = ? ";
		Long userCount = baseDao.count(hqlc, false, vo.getUserId());
		if (userCount == 0) {
			User u = baseDao.getById(User.class, vo.getUserId());
			if (!StringUtils.hasText(u.getPwd())) {
				u.setPwd("123456");
			}
			Role r = new Role();
			r.setId("agentRole");
			u.getRoles().add(r);			
		}
		
		String hql = " select count(id) from Agent where deleted = ? and user.id = ? and procities.id = ? ";
		Long count = baseDao.count(hql, false, vo.getUserId(), vo.getProcityId());
		if (count > 0) {
			throw new RuntimeException("这个人已经绑定过这个区域!");
		}
		Agent agent = new Agent();
		User u = new User();
		u.setId(vo.getUserId());
		Procity p = new Procity();
		p.setId(vo.getProcityId());
		
		agent.setUser(u);
		agent.setProcities(p);
		agent.setStartDateTime(LocalDateTime.now());
		baseDao.save(agent);
	}

	@Override
	public void unbind(AgentVo vo) {
		String hql = " from Agent where deleted = ? and user.id = ? and procities.id = ? ";
		Agent a = baseDao.get(Agent.class, hql, false, vo.getUserId(), vo.getProcityId());
		a.setDeleted(true);
		//解绑之后，判断是否还有代理商  ，如果没有，删除这个人的agentRole权限。
		String hqlc = " select count(id) from Agent where deleted = ? and user.id = ? ";
		Long userCount = baseDao.count(hqlc, false, vo.getUserId());
		if (userCount == 0) {					
			String sqld = " delete from t_user_role where USER_ID = :userId and ROLE_ID = :roleId ";
			Map<String, Object> map = new HashMap<>();
			map.put("userId", vo.getUserId());
			map.put("roleId", "agentRole");
			baseDao.executeSql(sqld, map);			
		}
	}

	@Override
	public AgentVo getUserByProcityId(AgentVo vo) {
		String hql = " select new com.wangfanpinche.vo.agent.AgentVo( u.id, u.username, u.mobilePhone) from Agent a left join a.user u where a.deleted = ? and a.procities.id = ? ";
		AgentVo avo = baseDao.get(AgentVo.class, hql, false, vo.getProcityId());
		return avo;
	}

	@Override
	public List<Agent> procity(AgentVo vo) {
		String hql = " from Agent a left join fetch a.procities p where a.deleted = ? and a.user.id = ? ";
		List<Agent> agent = baseDao.find(Agent.class, hql, false, vo.getUserId());
		return agent;
	}
	
	@Override
	public List<Map<String, Object>> statisticsOrder(LocalDate start, LocalDate end, String id) {
		Procity p = baseDao.getById(Procity.class, id);
		String a = "district";
		if (p.getType().equals(Type.CITY)) {
			a = "city";
		}
		String sql = "select DATE_FORMAT(oo.departDateTime, '%Y-%m-%d') datetime, count(oo.id) total from t_ownerorder oo " +
					 " left join t_ownerlocation olf on oo.fromLocation_ID = olf.ID" + 
					 " left join t_ownerlocation olt on oo.toLocation_ID = olt.ID" + 
					 " where olf." + a + " in (" + 
					 "	select ttp.name from t_procity ttp where id  = :id " + 
					 " )" + 
					 " and oo.`status` = 3" + 
					 " and DATE_FORMAT(oo.departDateTime, '%Y-%m-%d') >= :startDate" + 
					 " and DATE_FORMAT(oo.departDateTime, '%Y-%m-%d') <= :endDate" + 
					 " group by datetime"; 
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("startDate", DateUtils.toString(start, "yyyy-MM-dd"));
		map.put("endDate", DateUtils.toString(end, "yyyy-MM-dd"));
		
		List<Map<String, Object>> result = baseDao.findBySql(sql, map);
		return result;
	}

	@Override
	public List<Map<String, Object>> statisticsOrderMoney(LocalDate start, LocalDate end, String id) {
		Procity p = baseDao.getById(Procity.class, id);
		String a = "district";
		if (p.getType().equals(Type.CITY)) {
			a = "city";
		}
		String sql = "select DATE_FORMAT(oo.departDateTime, '%Y-%m-%d') datetime, sum(oof.allfare) total from t_ownerorder oo " +
				 " left join t_ownerlocation olf on oo.fromLocation_ID = olf.ID" + 
				 " left join t_ownerlocation olt on oo.toLocation_ID = olt.ID" + 
				 " left join t_ownerorderfinish oof on oo.id = oof.ownerOrder_ID" +
				 " where olf." + a + " in (" + 
				 "	select ttp.name from t_procity ttp where id  = :id " + 
				 " )" + 
				 " and oo.`status` = 3" + 
				 " and DATE_FORMAT(oo.departDateTime, '%Y-%m-%d') >= :startDate" + 
				 " and DATE_FORMAT(oo.departDateTime, '%Y-%m-%d') <= :endDate" + 
				 " group by datetime"; 
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("startDate", DateUtils.toString(start, "yyyy-MM-dd"));
		map.put("endDate", DateUtils.toString(end, "yyyy-MM-dd"));
		
		List<Map<String, Object>> result = baseDao.findBySql(sql, map);
		return result;
	}

	@Override
	public List<Map<String, Object>> statisticsUserRegist(LocalDate start, LocalDate end, String id) {
		Procity p = baseDao.getById(Procity.class, id);
		String a = "district";
		if (p.getType().equals(Type.CITY)) {
			a = "city";
		}
		String sql = "select DATE_FORMAT(oo.CREATEDATETIME, '%Y-%m-%d') datetime, count(oo.id) total from t_user oo " +
				 " left join t_ownerlocation olf on oo.fromLocation_ID = olf.ID" + 
				 " where olf." + a + " in (" + 
				 "	select ttp.name from t_procity ttp where id  = :id " + 
				 " )" + 
				 " and DATE_FORMAT(oo.CREATEDATETIME, '%Y-%m-%d') >= :startDate" + 
				 " and DATE_FORMAT(oo.CREATEDATETIME, '%Y-%m-%d') <= :endDate" + 
				 " group by datetime"; 
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("startDate", DateUtils.toString(start, "yyyy-MM-dd"));
		map.put("endDate", DateUtils.toString(end, "yyyy-MM-dd"));
		
		List<Map<String, Object>> result = baseDao.findBySql(sql, map);
		return result;
	}

}
