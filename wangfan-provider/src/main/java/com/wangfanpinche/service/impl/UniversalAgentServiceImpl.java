package com.wangfanpinche.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.agent.UniversalAgent;
import com.wangfanpinche.service.UniversalAgentService;
import com.wangfanpinche.utils.other.DateUtils;
import com.wangfanpinche.vo.agent.UniversalAgentVo;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class UniversalAgentServiceImpl implements UniversalAgentService {
	
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public void recommend(UniversalAgentVo vo) {
		String hasUserHql = " select count(u.id) from User u where u.mobilePhone = ? ";

		if (vo.getFromUserPhone().equals(vo.getToUserPhone())) {
			throw new RuntimeException("不能推荐自己!");
		}
		
		//判断推荐人是否存在
		Long hasFromUserCount = baseDao.count(hasUserHql, vo.getFromUserPhone());
		if(hasFromUserCount <= 0){
			throw new RuntimeException("您是无效的推荐人!");
		}
		
		//判断被推荐人是否推荐过
		String hqlcount = " select count(ua.id) from UniversalAgent ua left join ua.toUser tu where ua.deleted = ? and ua.toUser.mobilePhone = ? ";
		Long uaCount = baseDao.count(hqlcount, false, vo.getToUserPhone());
		if (uaCount > 0) {
			throw new RuntimeException("已经被推荐过，不能再推荐!");
		}
		
		//判断被推荐人是否存在
		Long hasToUserCount = baseDao.count(hasUserHql, vo.getToUserPhone());
		if(hasToUserCount == 0){
			throw new RuntimeException("请先点击获取验证码!");
		}	
		
		//手机验证码
		LocalDateTime ldt = LocalDateTime.now();
		ldt = ldt.minusMinutes(10);

		String hql = " select count(id) from Verify where deleted = ? and mobilePhone = ? and code = ? and createDateTime > ? ";
		Long verify = baseDao.count(hql, false, vo.getToUserPhone(), vo.getCode(), ldt);
		if (verify == 0) {
			throw new RuntimeException("手机号或验证码错误");
		}

		baseDao.execute(" update Verify set deleted = ?, modifyDateTime = ? where deleted = ? and mobilePhone = ? and code = ? and createDateTime > ? ", true, LocalDateTime.now(), false, vo.getToUserPhone(), vo.getCode(), ldt);

		User user = baseDao.get(User.class, " select u from User u where u.mobilePhone = ? and u.deleted = ? ", vo.getToUserPhone(), false);

		//添加全民代理
		UniversalAgent ua = new UniversalAgent();
		String uaHql = " select new User(u.id) from User u where u.mobilePhone = ? ";
		User fromUser = baseDao.get(User.class, uaHql, vo.getFromUserPhone());
		User toUser = user;		

		ua.setFromUser(fromUser);
		ua.setToUser(toUser);
		ua.setRegType(vo.getRegType());

		baseDao.save(ua);

	}

	@Override
	public UniversalAgentVo statistics(UniversalAgentVo vo) {
		String hql = " select count(u2.id) from UniversalAgent tu left join tu.fromUser u1 left join tu.toUser u2 where u1.mobilePhone = :fromMobilePhone ";
		String sql = "select count(oo.id) totalOrder, sum(oo.resultFare) totalOrderMoney "
				+ "from t_universalagent ua "
				+ "LEFT JOIN t_user u on u.id = ua.toUser_ID "
				+ "LEFT JOIN t_orderowner oo on oo.user_ID = u.id "
				+ "LEFT JOIN t_user tu ON tu.id = ua.fromUser_ID "
				+ "where tu.mobilePhone = :fromMobilePhone and oo.`status` = :status ";
		
		Map<String, Object> map = new HashMap<>();
		map.put("fromMobilePhone", vo.getFromUserPhone());
		Long totalPeople = baseDao.count(hql, map);
		if (totalPeople == 0) {
			vo.setTotalPeople(Long.valueOf(0));
		}else{
			vo.setTotalPeople(totalPeople);
		}		
		map.put("status", 3);
		List<UniversalAgentVo> list = baseDao.findBySql(UniversalAgentVo.class, sql, map);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				UniversalAgentVo uvo =  list.get(i);
				if (uvo.getTotalOrder() == null) {
					vo.setTotalOrder(BigInteger.valueOf(0));
				}else{
					vo.setTotalOrder(uvo.getTotalOrder());
				}
				if (uvo.getTotalOrderMoney() == null) {
					vo.setTotalOrderMoney(new BigDecimal("0.00"));
				}else{
					vo.setTotalOrderMoney(uvo.getTotalOrderMoney().multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP));
				}
			}
		}else {
			vo.setTotalOrder(new BigInteger("0"));
			vo.setTotalOrderMoney(new BigDecimal("0.00"));
		}
		return vo;
	}

	@Override
	public UniversalAgentVo today(UniversalAgentVo vo) {
		String sql = "select sum(oo.resultFare) totalOrderMoney "
				+ "from t_universalagent ua "
				+ "LEFT JOIN t_user u on u.id = ua.toUser_ID "
				+ "LEFT JOIN t_orderowner oo on oo.user_ID = u.id "
				+ "LEFT JOIN t_user tu ON tu.id = ua.fromUser_ID "
				+ "where tu.mobilePhone = :fromMobilePhone and oo.`status` = :status and DATE_FORMAT(oo.departDateTime, '%Y-%m-%d') = :todayDate ";

		Map<String, Object> map = new HashMap<>();
		map.put("fromMobilePhone", vo.getFromUserPhone());
		map.put("status", 3);
		map.put("todayDate", DateUtils.toString(LocalDate.now(), "yyyy-MM-dd"));
		List<UniversalAgentVo> list = baseDao.findBySql(UniversalAgentVo.class, sql, map);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				UniversalAgentVo uvo =  list.get(i);
				if (uvo.getTotalOrderMoney() == null) {
					vo.setTotalOrderMoney(new BigDecimal("0.00"));
				}else{
					vo.setTotalOrderMoney(uvo.getTotalOrderMoney().multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP));
				}
			}
		}else {
			vo.setTotalOrderMoney(new BigDecimal("0.00"));
		}
		return vo;
	}

	@Override
	public List<UniversalAgentVo> detail(UniversalAgentVo vo, PageHelper ph) {

		String sql = "select u.mobilePhone toUserPhone, oo.departDateTime departDateTime, oo.resultFare resultFare "
				+ "from t_universalagent ua "
				+ "LEFT JOIN t_user u on u.id = ua.toUser_ID "
				+ "LEFT JOIN t_orderowner oo on oo.user_ID = u.id "
				+ "LEFT JOIN t_user tu ON tu.id = ua.fromUser_ID "
				+ "where tu.mobilePhone = :fromMobilePhone and oo.`status` = :status "
				+ "order by oo.departDateTime desc "; 
		
//		String date = "2016-12";
//		Integer year = Integer.parseInt(date.split("-")[0]);
//		Integer month = Integer.parseInt(date.split("-")[1]);
//		LocalDateTime start = LocalDateTime.of(year, month, 1,0,0,0);
//		LocalDateTime end = start.plusMonths(1).minusSeconds(1);
		
		Map<String, Object> map = new HashMap<>();
		map.put("fromMobilePhone", vo.getFromUserPhone());
		map.put("status", 3);
		
		List<Map<String, Object>> list = baseDao.findBySql(sql, map, ph);
		List<UniversalAgentVo> collect = list.stream().map(m ->{
			UniversalAgentVo uav = new UniversalAgentVo();
			Timestamp t = (Timestamp) m.get("departDateTime");
			uav.setDepartDateTime(t.toLocalDateTime());
			uav.setToUserPhone((String)m.get("toUserPhone"));
			uav.setReaultFare((BigDecimal)m.get("resultFare"));
			uav.setTotalOrderMoney(uav.getReaultFare().multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP));
			return uav;
		}).collect(Collectors.toList());
		return collect;
	}

}
