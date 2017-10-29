package com.wangfanpinche.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.BizNumber;
import com.wangfanpinche.dto.Journal;
import com.wangfanpinche.dto.Journal.BizType;
import com.wangfanpinche.dto.Journal.InOutType;
import com.wangfanpinche.dto.Journal.PayType;
import com.wangfanpinche.dto.PayInfo;
import com.wangfanpinche.dto.PushEntity;
import com.wangfanpinche.dto.PushEntity.PushType;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.dto.UserPush;
import com.wangfanpinche.dto.order.Evaluate;
import com.wangfanpinche.dto.order.Evaluate.EvaluateType;
import com.wangfanpinche.dto.order.Location;
import com.wangfanpinche.dto.order.OrderOwner;
import com.wangfanpinche.dto.order.OrderOwner.OrderOwnerStatus;
import com.wangfanpinche.dto.order.OrderPassenger;
import com.wangfanpinche.dto.order.OrderPassenger.OrderPassengerStatus;
import com.wangfanpinche.dto.order.OrderPassenger.OrderPassengerType;
import com.wangfanpinche.provider.utils.BeanUtils;
import com.wangfanpinche.service.OrderPassengerService;
import com.wangfanpinche.utils.http.HttpUtils;
import com.wangfanpinche.utils.id.SnowFlakeIdGenerator;
import com.wangfanpinche.utils.map.BaiduMapUtils;
import com.wangfanpinche.utils.other.DateUtils;
import com.wangfanpinche.utils.other.EncryptUtils;
import com.wangfanpinche.utils.pay.AliPayBizContent;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.utils.pay.WechatPreOrder;
import com.wangfanpinche.utils.push.JPushUtils;
import com.wangfanpinche.vo.PushEntityVo;
import com.wangfanpinche.vo.WeChat;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.order.OrderOwnerVo;
import com.wangfanpinche.vo.order.OrderPassengerVo;

@Service
public class OrderPassengerServiceImpl implements OrderPassengerService {

	@Autowired
	private BaseDao baseDao;

	@Override
	public List<OrderPassengerVo> listAll(OrderPassengerVo vo) {
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo(p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.status) from OrderPassenger p left join p.fromLocation f left join p.toLocation t where p.user.id = ? and p.deleted = ? order by p.createDateTime desc ";
		List<OrderPassengerVo> list = baseDao.find(OrderPassengerVo.class, hql, vo.getUserId(), false);
		return list;
	}

	@Override
	public List<OrderPassengerVo> listByPublish(OrderPassengerVo vo) {
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo(p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.status) from OrderPassenger p left join p.fromLocation f left join p.toLocation t where p.user.id = ? and p.deleted = ? and p.status = ? ";
		List<OrderPassengerVo> list = baseDao.find(OrderPassengerVo.class, hql, vo.getUserId(), false, OrderPassengerStatus.PUBLISH);
		return list;
	}

	@Override
	public List<OrderPassengerVo> listByFromLocation(OrderPassengerVo vo, PageHelper ph) {
		StringBuilder hql = new StringBuilder(" select new com.wangfanpinche.vo.order.OrderPassengerVo( p.id, u.id, u.userIcon, u.mobilePhone, u.username, u.status, f.lng, f.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.seat, p.singleFare, p.status) from OrderPassenger p left join p.fromLocation f left join p.toLocation t left join p.user u where p.id is not null ");
		Map<String, Object> params = new HashMap<>();

		addWhereByFromLocation(hql, params, vo);

		List<OrderPassengerVo> l = baseDao.find(OrderPassengerVo.class, hql.toString(), params, ph);

		return l;
	}

	private void addWhereByFromLocation(StringBuilder hql, Map<String, Object> params, OrderPassengerVo vo) {
		hql.append(" and p.deleted = :deleted ");
		params.put("deleted", false);
		
		LocalDateTime now = LocalDateTime.now();
		if (vo.getFromLng() != null && vo.getFromLat() != null) {
			Location fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
			hql.append(" and f.city = :fromCity ");
			params.put("fromCity", fromLocation.getCity());
		}

		if (vo.getToLng() != null && vo.getToLat() != null) {
			Location toLocation = gpsToBean(vo.getToLng(), vo.getToLat());
			hql.append(" and t.city = :toCity ");
			params.put("toCity", toLocation.getCity());
		}

		hql.append(" and p.status = :status ");
		params.put("status", OrderPassengerStatus.PUBLISH);
		
		hql.append(" and p.departDateTime > :now ");
		params.put("now", now);
	}
	
	@Override
	public List<OrderPassengerVo> listByLocation(OrderPassengerVo vo) {
		LocalDateTime now = LocalDateTime.now();
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getOrderOwnerId());
		
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo( p.id, u.id, u.userIcon, u.mobilePhone, u.username, u.status, f.lng, f.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.seat, p.singleFare, p.status) from OrderPassenger p left join p.fromLocation f left join p.toLocation t left join p.user u "
		+" where p.id is not null and p.deleted = :deleted and f.city = :fromCity and t.city = :toCity and p.status = :status and p.departDateTime > :now and p.seat <= :current ";
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("fromCity", o.getFromLocation().getCity());
		params.put("toCity", o.getToLocation().getCity());
		params.put("status", OrderPassengerStatus.PUBLISH);
		params.put("now", now);
		params.put("current", o.getCurrentSeat());
		List<OrderPassengerVo> l = baseDao.find(OrderPassengerVo.class, hql.toString(), params);
		return l;
	}

	@Override
	public OrderPassengerVo listByRunning(OrderPassengerVo vo) {
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo(p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.status) from OrderPassenger p left join p.fromLocation f left join p.toLocation t where p.user.id = :userId and p.deleted = :deleted and p.status in (:status) ";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", vo.getUserId());
		params.put("deleted", false);
		params.put("status", new OrderPassengerStatus[] { OrderPassengerStatus.NO_RECEIVE, OrderPassengerStatus.RECEIVE });
		OrderPassengerVo pvo = baseDao.get(OrderPassengerVo.class, hql, params);
		return pvo;
	}

	@Override
	public void receive(OrderPassengerVo vo) {
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getId());
		if (!p.getOrderOwner().getStatus().equals(OrderOwnerStatus.RECEIVE)) {
			throw new RuntimeException("车主车单当前状态不是接乘客状态,不能上车!");
		}
		if (!p.getStatus().equals(OrderPassengerStatus.NO_RECEIVE)) {
			throw new RuntimeException("乘客车单当前状态不是未上车状态,不能上车!");
		}
		Location location = gpsToBean(vo.getGoinLng(), vo.getGoinLat());
		baseDao.persist(location);
		p.setGoinLocation(location);
		p.setGoinStatus(true);
		p.setGoinDateTime(LocalDateTime.now());
		p.setStatus(OrderPassengerStatus.RECEIVE);
		baseDao.update(p);
	}

	@Override
	public void finish(OrderPassengerVo vo) {
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getId());
		if (!p.getOrderOwner().getStatus().equals(OrderOwnerStatus.DEPART)) {
			throw new RuntimeException("车主车单当前状态不是送乘客状态,不能完成!");
		}
		if (!p.getStatus().equals(OrderPassengerStatus.RECEIVE)) {
			throw new RuntimeException("乘客车单当前状态不是已上车状态,不能完成!");
		}
		Location location = gpsToBean(vo.getGooutLng(), vo.getGooutLat());
		baseDao.persist(location);
		p.setGooutLocation(location);
		p.setGooutStatus(true);
		p.setGooutDateTime(LocalDateTime.now());
		p.setStatus(OrderPassengerStatus.FINISH);
		
		Evaluate fromEvaluate = new Evaluate();
		fromEvaluate.setFromUser(p.getUser());
		fromEvaluate.setToUser(p.getOrderOwner().getUser());
		fromEvaluate.setEvaluateType(EvaluateType.Passenger);
		fromEvaluate.setDescription(Double.valueOf(5));
		fromEvaluate.setTrustworthy(Double.valueOf(5));
		fromEvaluate.setService(Double.valueOf(5));
		baseDao.save(fromEvaluate);
		Evaluate toEvaluate = new Evaluate();
		toEvaluate.setFromUser(p.getOrderOwner().getUser());
		toEvaluate.setToUser(p.getUser());
		toEvaluate.setEvaluateType(EvaluateType.Owner);
		baseDao.save(toEvaluate);
		
		p.setToEvaluate(fromEvaluate);
		p.setToEvaluateModify(false);
		p.setFromEvaluate(toEvaluate);
		p.setFromEvaluateModify(false);
		baseDao.update(p);
	}

	@Override
	public void evaluate(OrderPassengerVo vo) {
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getId());
		if (!p.getStatus().equals(OrderPassengerStatus.FINISH)) {
			throw new RuntimeException("您还没有下车，不能评价车主!");
		}
		// 乘客评价车主
		Evaluate e = baseDao.get(Evaluate.class, " from Evaluate e where e.id = ( select op.toEvaluate.id from OrderPassenger op where op.id = ? ) ", vo.getId());
		if (!e.getFromUser().getId().equals(vo.getUserId())) {
			throw new RuntimeException("只能评价自己的车单!");
		}
		if (vo.getDescriptions() == null) {
			e.setDescription(Double.valueOf(5));
		} else {
			e.setDescription(vo.getDescriptions());
		}

		if (vo.getService() == null) {
			e.setService(Double.valueOf(5));
		} else {
			e.setService(vo.getService());
		}

		if (vo.getTrustworthy() == null) {
			e.setTrustworthy(Double.valueOf(5));
		} else {
			e.setTrustworthy(vo.getTrustworthy());
		}

		if (StringUtils.hasText(vo.getContent())) {
			e.setContent(vo.getContent());
		}
		baseDao.update(e);
		p.setToEvaluateModify(true);
		baseDao.update(p);
	}

	@Override
	public void passengerReserve(OrderPassengerVo vo) {
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getOrderOwnerId());
		if (!o.getStatus().equals(OrderOwnerStatus.PUBLISH)) {
			throw new RuntimeException("车主车单当前不是发布车单状态,不能预定!");
		}
		if (vo.getUserId().equals(o.getUser().getId())) {
			throw new RuntimeException("您好,您不能对您自己发布的车单进行预定操作!");
		}
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getId());
		if (p.getSeat() > o.getCurrentSeat()) {
			throw new RuntimeException("您的乘客车单座位数大于车主车单剩余座位数,不能预定!");
		}
		o.getPassengers().add(p);
		baseDao.update(o);
		//推送-乘客邀请车主
		initPushOp001(o, p);

	}

	private void initPushOp001(OrderOwner o, OrderPassenger p) {
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("有乘客想乘坐您的车!");
		push.setBizNumber(BizNumber.OP_001);
		push.setBody(o.getId());
		push.setUser(p.getUser());
		baseDao.save(push);
		
		UserPush u = new UserPush();
		u.setPushEntity(push);
		u.setToUser(o.getUser());
		u.setRead(false);
		baseDao.save(u);
		
		PushEntityVo pvo = new PushEntityVo();
		BeanUtils.copySomeProperties(push, pvo, "type","title","bizNumber","body");
		pvo.setUserpushId(u.getId());
		
		Map<String, String> params = new HashMap<>();
		params.put("entity", JSON.toJSONString(pvo));
		JPushUtils.sendByAlias(push.getTitle(), params, u.getToUser().getId());
	}

	public OrderPassengerVo detail(OrderPassengerVo vo){
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo( p.id, u.id, u.userIcon, u.mobilePhone, u.username, u.profession, u.status, f.lng, f.lat, t.lng, t.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.seat, p.singleFare, p.descript) from OrderPassenger p left join p.fromLocation f left join p.toLocation t left join p.user u where p.deleted = ? and p.id = ? ";		
		OrderPassengerVo pvo = baseDao.get(OrderPassengerVo.class, hql, false, vo.getId());
		//拼车次数
		String hqlototal = " select count(id) from OrderOwner where deleted = ? and user.id = ? and status = ? ";
		String hqlptotal = " select count(id) from OrderPassenger where deleted = ? and user.id = ? and status = ? ";
		Long total = baseDao.count(hqlototal, false, pvo.getUserId(), OrderOwnerStatus.FINISH) + baseDao.count(hqlptotal, false, pvo.getUserId(), OrderPassengerStatus.FINISH);
		pvo.setTotal(total);
		//车主评价乘客的标签
		String sql = "select count(t.tags) count,t.tags from t_evaluate p left join t_evaluate_tag t on p.ID = t.Evaluate_ID where p.DELETED = :deleted and p.toUser_ID = :userId GROUP BY t.tags ";
		Map<String , Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("userId", pvo.getUserId());
		List<String> passList = baseDao.findBySql(sql, params);
		if (passList != null && passList.size() > 0) {	
			pvo.setTags(passList);
		}
		return pvo;
	}

	@Override
	public OrderPassengerVo userinfo(OrderPassengerVo vo){
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo( u.id, u.userIcon, u.mobilePhone, u.username, u.status, u.homeAddress) from OrderPassenger p left join p.user u where p.deleted = ? and p.id = ? ";
		OrderPassengerVo pvo = baseDao.get(OrderPassengerVo.class, hql, false, vo.getId());
		//拼车次数
		String hqlototal = " select count(id) from OrderOwner where deleted = ? and user.id = ? and status = ? ";
		String hqlptotal = " select count(id) from OrderPassenger where deleted = ? and user.id = ? and status = ? ";
		Long total = baseDao.count(hqlototal, false, pvo.getUserId(), OrderOwnerStatus.FINISH) + baseDao.count(hqlptotal, false, pvo.getUserId(), OrderPassengerStatus.FINISH);
		pvo.setTotal(total);
		//车主评价乘客的标签
		String sql = "select count(t.tags) count,t.tags from t_evaluate p left join t_evaluate_tag t on p.ID = t.Evaluate_ID where p.DELETED = :deleted and p.toUser_ID = :userId GROUP BY t.tags ";
		Map<String , Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("userId", pvo.getUserId());
		List<String> passList = baseDao.findBySql(sql, params);
		if (passList != null && passList.size() > 0) {	
			pvo.setTags(passList);
		}
		return pvo;
	}
	
	@Override
	public void approveOwner(OrderPassengerVo vo) {
		
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getId());
		if (!p.getStatus().equals(OrderPassengerStatus.PUBLISH)) {
			throw new RuntimeException("乘客车单当前不是发布状态，不能同意乘坐!");
		}
				
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getOrderOwnerId());
		List<OrderOwner> owners = p.getOwners();
		for (int i = 0; i < owners.size(); i++) {
			OrderOwner oo = owners.get(i);
			if (oo.getId().equals(vo.getOrderOwnerId())) {
				o = owners.remove(i);
				break;
			}
		}
		p.setApproveStatus(true);
		p.setApproveDateTime(LocalDateTime.now());
		p.setOrderOwner(o);
		p.setPassengerType(OrderPassengerType.Owner);
		baseDao.update(p);
		
		//推送-乘客同意
		initPushOp002(p, o);
	}

	private void initPushOp002(OrderPassenger p, OrderOwner o) {
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("有乘客同意乘坐您的车!");
		push.setBizNumber(BizNumber.OP_002);
		push.setBody(o.getId());
		push.setUser(p.getUser());
		baseDao.save(push);
				
		UserPush u = new UserPush();
		u.setPushEntity(push);
		u.setToUser(o.getUser());
		u.setRead(false);
		baseDao.save(u);
		
		PushEntityVo pvo = new PushEntityVo();
		BeanUtils.copySomeProperties(push, pvo, "type","title","bizNumber","body");
		pvo.setUserpushId(u.getId());
		
		Map<String, String> params = new HashMap<>();
		params.put("entity", JSON.toJSONString(pvo));
		JPushUtils.sendByAlias(push.getTitle(), params, u.getToUser().getId());
	}

	@Override
	public String publish(OrderPassengerVo vo) {
		User u = baseDao.getById(User.class, vo.getUserId());
		if (!u.getStatus().equals(StatusEnum.认证通过)) {
			throw new RuntimeException("您还没有实名认证,不能发布车单!");
		}
		Location fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
		baseDao.persist(fromLocation);
		Location toLocation = gpsToBean(vo.getToLng(), vo.getToLat());
		baseDao.persist(toLocation);
		
		OrderPassenger p = new OrderPassenger();
		p.setUser(u);
		p.setFromLocation(fromLocation);
		p.setToLocation(toLocation);
		p.setStatus(OrderPassengerStatus.PUBLISH);
		p.setSeat(vo.getSeat());
		p.setSingleFare(vo.getSingleFare().setScale(2, RoundingMode.HALF_UP));		
		p.setResultFare(vo.getSingleFare().multiply(new BigDecimal(vo.getSeat())).setScale(2, RoundingMode.HALF_UP));
		p.setDepartDateTime(vo.getDepartDateTime());
		p.setApproveStatus(false);
		p.setDisapproveStatus(false);
		p.setPayStatus(false);
		p.setClosedStatus(false);
		p.setGoinStatus(false);
		p.setGooutStatus(false);
		if (StringUtils.hasText(vo.getDescript())) {
			p.setDescript(vo.getDescript());
		}
		Serializable id = baseDao.save(p);
		
		//乘客匹配车主---1.发车时间同一天  2.车主剩余座位数>=乘客座位数 3.匹配开始结束城市
		String sql = " select o.user_ID userId from t_orderowner o LEFT JOIN t_location f on o.fromLocation_ID = f.ID LEFT JOIN t_location t on o.toLocation_ID = t.ID LEFT JOIN t_user u on o.user_ID = u.ID WHERE o.DELETED = :deleted and o.`status` = :status and o.currentSeat >= :seat and f.city = :fromCity and t.city = :toCity ";
		Map<String, Object> map = new HashMap<>();
		map.put("deleted", false);
		map.put("status", OrderOwnerStatus.PUBLISH);
		map.put("seat", p.getSeat());
		map.put("fromCity", p.getFromLocation().getCity());
		map.put("toCity", p.getToLocation().getCity());
		List<OrderOwnerVo> oList = baseDao.findBySql(OrderOwnerVo.class, sql, map);
		if (oList != null && oList.size() > 0) {
			for (OrderOwnerVo o : oList) {
				//推送-乘客车单匹配的车主车单
				initPushMatchOwner(p, o);
			}
		}
		return id.toString();
	}
	
	private void initPushMatchOwner(OrderPassenger p, OrderOwnerVo o) {
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("有乘客匹配您的车单!");
		push.setBizNumber(BizNumber.MATCHOWNER);
		push.setBody(p.getId());
		push.setUser(p.getUser());
		baseDao.save(push);
				
		UserPush userPush = new UserPush();
		userPush.setPushEntity(push);
		userPush.setToUser(new User(o.getUserId()));
		userPush.setRead(false);
		baseDao.save(userPush);
		
		PushEntityVo pvo = new PushEntityVo();
		BeanUtils.copySomeProperties(push, pvo, "type","title","bizNumber","body");
		pvo.setUserpushId(userPush.getId());
		
		Map<String, String> params = new HashMap<>();
		params.put("entity", JSON.toJSONString(pvo));
		JPushUtils.sendByAlias(push.getTitle(), params, userPush.getToUser().getId());
	}
	
	/**
	 * GPS转换成系统可识别的Bean对象
	 * 
	 * @param lng
	 * @param lat
	 * @return
	 */
	public Location gpsToBean(BigDecimal lng, BigDecimal lat) {
		String gpsToBaidu = gpsToBaidu(lng, lat);
		return getOwnerLocation(gpsToBaidu);
	}	

	/**
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 纬度 经度
	 */
	public String gpsToBaidu(BigDecimal lng, BigDecimal lat) {
		String point = lng + "," + lat;
		String json = BaiduMapUtils.geoconvRevers(point);
		JSONObject j = JSON.parseObject(json);
		String x = j.getJSONArray("result").getJSONObject(0).getString("x");
		String y = j.getJSONArray("result").getJSONObject(0).getString("y");
		return y + "," + x;
	}

	public Location getOwnerLocation(String latAndLng) {
		String geocoderRevers = BaiduMapUtils.geocoderRevers(latAndLng);
		Location o = new Location();
		JSONObject j = JSON.parseObject(geocoderRevers);
		JSONObject result = j.getJSONObject("result");

		o.setLng(result.getJSONObject("location").getBigDecimal("lng"));
		o.setLat(result.getJSONObject("location").getBigDecimal("lat"));
		o.setProvince(result.getJSONObject("addressComponent").getString("province"));
		o.setCity(result.getJSONObject("addressComponent").getString("city"));
		o.setDistrict(result.getJSONObject("addressComponent").getString("district"));
		o.setStreet(result.getJSONObject("addressComponent").getString("street"));
		o.setStreetNumber(result.getJSONObject("addressComponent").getString("streetNumber"));
		o.setSematicDescription(result.getString("sematic_description"));
		return o;
	}
	
	@Override
	public BigDecimal passPayMoney(OrderPassengerVo vo) {
		OrderPassenger op = baseDao.getById(OrderPassenger.class, vo.getId());
		// 根据类型取钱
		BigDecimal money = null;		
		if (op.getPassengerType().equals(OrderPassengerType.Owner)) {
			BigDecimal resultFare = op.getResultFare();
			money = resultFare.setScale(2, RoundingMode.HALF_UP);
		} else {
			money = op.getOrderOwner().getSingleFare().multiply(new BigDecimal(op.getSeat())).setScale(2, RoundingMode.HALF_UP);
		}
		return money;
	}

	@Override
	public String orderPassengerAliPayPreOrder(OrderPassengerVo vo) {
		OrderPassenger op = baseDao.getById(OrderPassenger.class, vo.getId());
		// 要判断他是否生成过订单信息，如果生成过。则返回原来生成的订单信息
		if (op.getPayOrderNumber() != null) {
			Journal j = baseDao.get(Journal.class, " from Journal where orderNumber = ? ", op.getPayOrderNumber());
			if(PayType.支付宝.equals(j.getPayType())){
				return j.getPayInfo().getRequestBody();
			} else {
				baseDao.delete(j);
			}
		}

		// 是否支付过
		if (op.getPayStatus() == true) {
			throw new RuntimeException("支付错误:已支付此订单");
		}

		// 是否同意过
		if (op.getOrderOwner() == null) {
			throw new RuntimeException("支付错误:还没有关联司机的车单");
		}

		// 生成订单信息的对象,时间相减
		long until = LocalDateTime.now().until(op.getOrderOwner().getDepartDateTime(), ChronoUnit.MINUTES) + 1;

		// 根据类型取钱
		BigDecimal money = null;
		Journal j = new Journal();
		if (op.getPassengerType().equals(OrderPassengerType.Owner)) {
			BigDecimal resultFare = op.getResultFare();
			money = resultFare.setScale(2, RoundingMode.HALF_UP);
			j.setType(BizType.乘客支付乘客车单);
		} else {
			money = op.getOrderOwner().getSingleFare().multiply(new BigDecimal(op.getSeat())).setScale(2, RoundingMode.HALF_UP);
			j.setType(BizType.乘客支付车主车单);
		}

money = new BigDecimal("0.01");
		op.setRealFare(money);
		AliPayBizContent content = new AliPayBizContent();
		// 判断发布时间
		content.setTimeout_express(until + "m");
		content.setProduct_code("QUICK_MSECURITY_PAY");
		// 订单号
		long orderId = SnowFlakeIdGenerator.getInstance().generateLongId();
		op.setPayOrderNumber(orderId);
		content.setTotal_amount(money.toString());
		content.setSubject("往返拼车");
		content.setBody("支付宝-乘客上车");
		content.setOut_trade_no(orderId + "");
		// 获取支付订单信息并保存到支付信息表
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/OrderPassenger/orderPassengerAliPayNotify";
		String notifyUrl = "http://app.wangfanpinche.com:8889/OrderPassenger/orderPassengerAliPayNotify";
		String orderInfo = AlipayAppUtils.buildOrderInfo(content, notifyUrl);
		// 支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(orderInfo);
		baseDao.save(pay);
		// 账单
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.支付宝);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);
		j.setOrderNumber(orderId);
		j.setMoney(money);
		j.setUser(new User(vo.getUserId()));
		j.setPayInfo(pay);
		baseDao.save(j);
		baseDao.update(op);
		return orderInfo;
	}

	@Override
	public void orderPassengerAliPayNotify(Map<String, String> map) {
		// 1.判断计算金额和支付金额是否一致
		// 订单号
		String orderNumber = map.get("out_trade_no");
		OrderPassenger op = baseDao.get(OrderPassenger.class, " from OrderPassenger op left join fetch op.user u left join fetch op.orderOwner oo where op.payOrderNumber = ? ", Long.parseLong(orderNumber));
		OrderOwner oo = op.getOrderOwner();
		// 根据类型取钱
		BigDecimal money = null;
		if (op.getPassengerType().equals(OrderPassengerType.Owner)) {
			BigDecimal resultFare = op.getResultFare();
			money = resultFare.setScale(2, RoundingMode.HALF_UP);
		} else {
			money = oo.getSingleFare().multiply(new BigDecimal(op.getSeat())).setScale(2, RoundingMode.HALF_UP);
		}
		BigDecimal receipt_amount = new BigDecimal(map.get("receipt_amount"));
money = new BigDecimal("0.01");		
		if(!receipt_amount.equals(money)){
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}

		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.payInfo where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(orderNumber));
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		j.getPayInfo().setNotifyBody(JSON.toJSONString(map));
		
		// 先判断座位数是否够，如果不是，原路退回。推送提示消息
		if(op.getOrderOwner().getCurrentSeat() < op.getSeat()){
			op.setOrderOwner(null);
			op.setPayStatus(false);
			op.setApproveStatus(false);
			AlipayAppUtils.refund(orderNumber, receipt_amount.toString());
			//退款记录
			Journal jo = new Journal();
			jo.setType(j.getType());
			jo.setInOutType(InOutType.支出);
			jo.setPayType(j.getPayType());
			jo.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
			jo.setOrderNumber(j.getOrderNumber());
			jo.setMoney(j.getMoney());
			jo.setUser(j.getUser());			
			baseDao.save(jo);
			baseDao.update(op);
			return;
		}
		//判断车主是否发车，如果发车，原路退回。推送提示消息
		if (oo.getStatus().equals(OrderOwnerStatus.RECEIVE)) {
			op.setOrderOwner(null);
			op.setPayStatus(false);
			op.setApproveStatus(false);
			AlipayAppUtils.refund(orderNumber, receipt_amount.toString());
			//退款记录
			Journal jo = new Journal();
			jo.setType(j.getType());
			jo.setInOutType(InOutType.支出);
			jo.setPayType(j.getPayType());
			jo.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
			jo.setOrderNumber(j.getOrderNumber());
			jo.setMoney(j.getMoney());
			jo.setUser(j.getUser());			
			baseDao.save(jo);
			baseDao.update(op);
			return;
		}
		//判断车主是否关闭，如果关闭，原路退回。推送提示消息
		if (oo.getStatus().equals(OrderOwnerStatus.CLOSED)) {
			op.setOrderOwner(null);
			op.setPayStatus(false);
			op.setApproveStatus(false);
			AlipayAppUtils.refund(orderNumber, receipt_amount.toString());
			//退款记录
			Journal jo = new Journal();
			jo.setType(j.getType());
			jo.setInOutType(InOutType.支出);
			jo.setPayType(j.getPayType());
			jo.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
			jo.setOrderNumber(j.getOrderNumber());
			jo.setMoney(j.getMoney());
			jo.setUser(j.getUser());			
			baseDao.save(jo);
			baseDao.update(op);
			return;
		}
		
		// 支付结果-更改乘客支付状态
		op.setPayStatus(true);
		op.setPayDateTime(LocalDateTime.now());
		op.setStatus(OrderPassengerStatus.TAKER);
		
		oo.setCurrentSeat( op.getOrderOwner().getCurrentSeat() - op.getSeat());
		oo.setResultFare(oo.getResultFare().add(money));
		
		baseDao.update(j);
		baseDao.update(op);
		baseDao.update(oo);
		
		//推送-支付宝乘客支付
		initPushOp003(op, oo);
	}

	private void initPushOp003(OrderPassenger op, OrderOwner oo) {
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("有乘客支付了!");
		push.setBizNumber(BizNumber.OP_003);
		push.setBody(oo.getId());
		push.setUser(op.getUser());
		baseDao.save(push);
				
		UserPush u = new UserPush();
		u.setPushEntity(push);
		u.setToUser(oo.getUser());
		u.setRead(false);
		baseDao.save(u);
		
		PushEntityVo pvo = new PushEntityVo();
		BeanUtils.copySomeProperties(push, pvo, "type","title","bizNumber","body");
		pvo.setUserpushId(u.getId());
		
		Map<String, String> params = new HashMap<>();
		params.put("entity", JSON.toJSONString(pvo));
		JPushUtils.sendByAlias(push.getTitle(), params, u.getToUser().getId());
	}

	@Override
	public String orderPassengerWechatPayPreOrder(OrderPassengerVo vo) {
		OrderPassenger op = baseDao.getById(OrderPassenger.class, vo.getId());
		// 要判断他是否生成过订单信息，如果生成过。则返回原来生成的订单信息
		if (op.getPayOrderNumber() != null) {
			Journal j = baseDao.get(Journal.class, " from Journal where orderNumber = ? ", op.getPayOrderNumber());
			if(PayType.微信.equals(j.getPayType())){
				return j.getPayInfo().getRequestBody();
			} else {
				baseDao.delete(j);
			}
		}

		// 是否支付过
		if (op.getPayStatus() == true) {
			throw new RuntimeException("支付错误:已支付此订单");
		}

		// 是否同意过
		if (op.getOrderOwner() == null) {
			throw new RuntimeException("支付错误:还没有关联司机的车单");
		}

		// 根据类型取钱
		//按照分来计算，10000就是100元，1就是0.01元
		BigDecimal money = null;
		Journal j = new Journal();
		if (op.getPassengerType().equals(OrderPassengerType.Owner)) {
			BigDecimal resultFare = op.getResultFare();
			money = resultFare;
			j.setType(BizType.乘客支付乘客车单);
		} else {
			money = op.getOrderOwner().getSingleFare().multiply(new BigDecimal(op.getSeat()));
			j.setType(BizType.乘客支付车主车单);
		}
money = new BigDecimal("0.01");
		op.setRealFare(money);		
		long orderNumber = SnowFlakeIdGenerator.getInstance().generateLongId();
		op.setPayOrderNumber(orderNumber);
		
		WechatPreOrder p = new WechatPreOrder();
		p.setOut_trade_no(orderNumber + "");	
		p.setTotal_fee(money.multiply(new BigDecimal("100")).intValue());
		p.setSpbill_create_ip(vo.getIp());
		p.setTime_expire(op.getOrderOwner().getDepartDateTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/OrderPassenger/orderPassengerWechatPayNotify";
		String notifyUrl = "http://app.wangfanpinche.com:8889/OrderPassenger/orderPassengerWechatPayNotify";
		p.setNotify_url(notifyUrl);
		p.setBody("微信乘客支付车单");
		
		String preOrder = WechatPayAppUtils.preOrder(p);
		
		//支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(preOrder);
		baseDao.save(pay);
		//账单
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.微信);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);		
		j.setOrderNumber(orderNumber);
		j.setMoney(money);
		j.setUser(new User(vo.getUserId()));
		j.setPayInfo(pay);
		baseDao.save(j);
		baseDao.update(op);
		try {
			XmlMapper mapper = new XmlMapper();
			HashMap<String, String> readValue = mapper.readValue(preOrder, HashMap.class);
			WeChat we = new WeChat();
			String return_code = (String)readValue.get("return_code");
			if(return_code != null && "FAIL".equals(return_code) ){
				throw new RuntimeException((String) readValue.get("return_msg"));
			}
			we.setAppid(readValue.get("appid"));
			we.setPartnerid(readValue.get("mch_id"));
			we.setPrepayid(readValue.get("prepay_id"));
			we.setPackages("Sign=WXPay");
			we.setNoncestr(UUID.randomUUID().toString().replaceAll("-", ""));
			we.setTimestamp(System.currentTimeMillis()/1000 + "");
			String serializeObject = HttpUtils.serializeObject(we, true, false);
			serializeObject = serializeObject.replaceAll("packages", "package");
			String key = WechatPayAppUtils.getKey();
			serializeObject = serializeObject + "&key=" + key;

			we.setSign(EncryptUtils.md5(serializeObject).toUpperCase());

			preOrder = JSON.toJSONString(we);
			pay.setRequestBody(preOrder);
			baseDao.update(pay);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return preOrder;
	}

	@Override
	public void orderPassengerWechatPayNotify(WechatPayNotify notify) {
		String orderNumber = notify.getOut_trade_no();
		String hql = " from OrderPassenger op left join fetch op.user u left join fetch op.orderOwner oo where op.payOrderNumber = ? ";
		OrderPassenger op = baseDao.get(OrderPassenger.class, hql, Long.parseLong(orderNumber));		
		OrderOwner oo = op.getOrderOwner();
		// 根据类型取钱
		BigDecimal money = null;
		if (op.getPassengerType().equals(OrderPassengerType.Owner)) {
			BigDecimal resultFare = op.getResultFare();
			money = resultFare;
		} else {
			money = oo.getSingleFare().multiply(new BigDecimal(op.getSeat()));
		}
money = new BigDecimal("0.01");		
		//微信返回的支付金额
		Integer receipt_amountInt = notify.getTotal_fee();
		BigDecimal receipt_amount = new BigDecimal(receipt_amountInt).divide(new BigDecimal("100"));
		if (!money.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}
		
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.user u left join fetch j.payInfo p where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(orderNumber));
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		j.getPayInfo().setNotifyBody(JSON.toJSONString(notify));
		
		// 先判断座位数是否大于等于0，如果不是，原路退回。发消息
		if(op.getOrderOwner().getCurrentSeat() < op.getSeat()){
			op.setOrderOwner(null);
			op.setPayStatus(false);
			op.setApproveStatus(false);
			WechatPayAppUtils.refound(j.getOrderNumber(), money.multiply(new BigDecimal("100")).intValue(), money.multiply(new BigDecimal("100")).intValue());
			//退款记录
			Journal jo = new Journal();
			jo.setType(j.getType());
			jo.setInOutType(InOutType.支出);
			jo.setPayType(j.getPayType());
			jo.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
			jo.setOrderNumber(j.getOrderNumber());
			jo.setMoney(j.getMoney());
			jo.setUser(j.getUser());			
			baseDao.save(jo);
			baseDao.update(op);
			return;
		}
		//判断车主是否发车，如果发车，原路退回。推送提示消息
		if (oo.getStatus().equals(OrderOwnerStatus.RECEIVE)) {
			op.setOrderOwner(null);
			op.setPayStatus(false);
			op.setApproveStatus(false);
			WechatPayAppUtils.refound(j.getOrderNumber(), money.multiply(new BigDecimal("100")).intValue(), money.multiply(new BigDecimal("100")).intValue());
			//退款记录
			Journal jo = new Journal();
			jo.setType(j.getType());
			jo.setInOutType(InOutType.支出);
			jo.setPayType(j.getPayType());
			jo.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
			jo.setOrderNumber(j.getOrderNumber());
			jo.setMoney(j.getMoney());
			jo.setUser(j.getUser());			
			baseDao.save(jo);
			baseDao.update(op);
			return;
		}
		//判断车主是否关闭，如果关闭，原路退回。推送提示消息
		if (oo.getStatus().equals(OrderOwnerStatus.CLOSED)) {
			op.setOrderOwner(null);
			op.setPayStatus(false);
			op.setApproveStatus(false);
			WechatPayAppUtils.refound(j.getOrderNumber(), money.multiply(new BigDecimal("100")).intValue(), money.multiply(new BigDecimal("100")).intValue());
			//退款记录
			Journal jo = new Journal();
			jo.setType(j.getType());
			jo.setInOutType(InOutType.支出);
			jo.setPayType(j.getPayType());
			jo.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
			jo.setOrderNumber(j.getOrderNumber());
			jo.setMoney(j.getMoney());
			jo.setUser(j.getUser());			
			baseDao.save(jo);
			baseDao.update(op);
			return;
		}
		
		// 支付结果-更改乘客支付状态
		op.setPayStatus(true);
		op.setPayDateTime(LocalDateTime.now());
		op.setStatus(OrderPassengerStatus.TAKER);
		
		oo.setCurrentSeat( op.getOrderOwner().getCurrentSeat() - op.getSeat());
		oo.setResultFare(oo.getResultFare().add(money));
		
		baseDao.update(j);
		baseDao.update(op);
		baseDao.update(oo);
		
		//推送-微信乘客支付
		initPushOp003(op, oo);
	}
	
	@Validated
	public OrderPassengerVo ownerList(OrderPassengerVo vo){
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo( p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.seat, p.singleFare, p.descript, p.status) from OrderPassenger p left join p.fromLocation f left join p.toLocation t left join p.user u where p.deleted = ? and p.id = ? ";
		OrderPassengerVo pvo = baseDao.get(OrderPassengerVo.class, hql, false, vo.getId());

		//同意的车主
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getId());
		if (p.getOrderOwner() == null) {
			pvo.setApproveOwner(null);
		}else{
			String approvehql = " select new com.wangfanpinche.vo.order.OrderOwnerVo(o.id, o.singleFare, f.lng, f.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.honesty, o.status, u.id, u.userIcon, u.mobilePhone, u.username, u.status, o.carBrandAndSystem) from OrderOwner o left join o.fromLocation f left join o.toLocation t left join o.user u where o.deleted = ? and o.id = ? ";
			OrderOwnerVo approveOwner = baseDao.get(OrderOwnerVo.class, approvehql, false, p.getOrderOwner().getId());
			pvo.setApproveOwner(approveOwner);
		}
		//抢单的车主
		String sql = " select o.id id, o.singleFare singleFare, f.lng fromLng, f.lat fromLat, f.city fromCity, f.sematicDescription fromSematicDescription, t.city toCity, t.sematicDescription toSematicDescription, o.departDateTime departDateTime, o.currentSeat currentSeat, o.honesty honesty, u.ID userId, u.userIcon userIcon, u.mobilePhone mobilePhone, u.username username, u.status userstatusIndex, o.carBrandAndSystem carBrandAndSystem from t_orderowner o left join t_location f on o.fromLocation_ID = f.ID left join t_location t on o.toLocation_ID = t.ID left join t_user u on o.user_ID = u.ID where o.ID in (select op.owners_ID from t_orderpassenger_owners op LEFT JOIN t_orderpassenger p on op.OrderPassenger_ID = p.ID where p.DELETED = :deleted and p.ID = :id) and o.DELETED = :deleted ";
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("id", vo.getId());
		List<OrderOwnerVo> owners = baseDao.findBySql(sql, params);	
//		owners.forEach(e->{
//			Map<String, Object> map = (Map)e ;
//			Integer i = (Integer) map.get("userstatus");
//			e.put("userstatus", User.StatusEnum.values()[i]);
//		});
		pvo.setOwnerTotal(owners.size());
//		List<OrderOwnerVo> l = (List)owners;
		if (owners != null && owners.size() > 0) {
			pvo.setOwners(owners);
		}
		return pvo;
	}
	
	@Validated
	public OrderPassengerVo show(OrderPassengerVo vo) {
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo( p.id, u.id, u.userIcon, u.mobilePhone, u.username, u.status, f.lng, f.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.seat, p.singleFare, p.status, p.descript, p.passengerType, p.closedStatus, p.closedDateTime, p.closedDescirpt, p.payStatus, p.payDateTime, p.approveStatus, p.approveDateTime, p.disapproveStatus, p.disapproveDateTime, p.goinStatus, p.goinDateTime, gi.lng, gi.lat, gi.city, gi.sematicDescription, p.gooutStatus, p.gooutDateTime, go.lng, go.lat, go.city, go.sematicDescription, p.toEvaluateModify, p.fromEvaluateModify) from OrderPassenger p left join p.fromLocation f left join p.toLocation t left join p.user u left join p.goinLocation gi left join p.gooutLocation go where p.deleted = ? and p.id = ? ";
		OrderPassengerVo pvo = baseDao.get(OrderPassengerVo.class, hql, false, vo.getId());
		return pvo;
	}

	@Validated
	public void close(OrderPassengerVo vo) {
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getId());
		if (!p.getStatus().equals(OrderPassengerStatus.PUBLISH) && !p.getStatus().equals(OrderPassengerStatus.TAKER)) {
			throw new RuntimeException("您在当前状态下不能关闭乘客车单!");
		}
		
		if (!p.getOrderOwner().getStatus().equals(OrderOwnerStatus.PUBLISH)) {
			throw new RuntimeException("车主车单不是发布状态，不能关闭!");
		}
		LocalDateTime now = LocalDateTime.now();
		p.setStatus(OrderPassengerStatus.CLOSED);
		p.setClosedStatus(true);
		p.setClosedDateTime(now);		
		if (StringUtils.hasText(vo.getClosedDescirpt())) {
			p.setClosedDescirpt(vo.getClosedDescirpt());
		}
		//XXX 支付过的乘客退钱，车主车单剩余座位数增加，惩罚
		if (p.getPayStatus() == true) {
			String hqltype = " from Journal j left join fetch j.user u where j.InOutType = ? and j.status = ? and j.orderNumber = ? ";
			Journal j = baseDao.get(Journal.class, hqltype, InOutType.收入, com.wangfanpinche.dto.Journal.Status.完成, p.getPayOrderNumber());
			if (j.getPayType().equals(PayType.支付宝)) {
				AlipayAppUtils.refund(j.getOrderNumber().toString(), j.getMoney().toString());
			}
			if (j.getPayType().equals(PayType.微信)) {
				WechatPayAppUtils.refound(j.getOrderNumber(), j.getMoney().multiply(new BigDecimal("100")).intValue(), j.getMoney().multiply(new BigDecimal("100")).intValue());
			}
			//退款记录
			Journal jo = new Journal();
			jo.setType(j.getType());
			jo.setInOutType(InOutType.支出);
			jo.setPayType(j.getPayType());
			jo.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
			jo.setOrderNumber(j.getOrderNumber());
			jo.setMoney(j.getMoney());
			jo.setUser(j.getUser());		
			baseDao.save(jo);
			String hqlup = " update OrderOwner set currentSeat = currentSeat + ?, modifyDateTime = ? where deleted = ? and id = ? ";
			baseDao.execute(hqlup, p.getSeat(), now, false, p.getOrderOwner().getId());
		}
	}

	@Override
	public void warn(OrderPassengerVo vo) {
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getOrderOwnerId());
		//推送-乘客提醒车主出发
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("有乘客提醒您出发!");
		push.setBizNumber(BizNumber.OP_004);
		push.setBody(o.getId());
		push.setUser(new User(vo.getUserId()));
		baseDao.save(push);
				
		UserPush u = new UserPush();
		u.setPushEntity(push);
		u.setToUser(o.getUser());
		u.setRead(false);
		baseDao.save(u);
		
		PushEntityVo pvo = new PushEntityVo();
		BeanUtils.copySomeProperties(push, pvo, "type","title","bizNumber","body");
		pvo.setUserpushId(u.getId());
		
		Map<String, String> params = new HashMap<>();
		params.put("entity", JSON.toJSONString(pvo));
		JPushUtils.sendByAlias(push.getTitle(), params, u.getToUser().getId());
	}
}
