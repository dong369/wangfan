package com.wangfanpinche.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.BizNumber;
import com.wangfanpinche.dto.Car;
import com.wangfanpinche.dto.Journal;
import com.wangfanpinche.dto.PayInfo;
import com.wangfanpinche.dto.PushEntity;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.UserPush;
import com.wangfanpinche.dto.Journal.BizType;
import com.wangfanpinche.dto.Journal.InOutType;
import com.wangfanpinche.dto.Journal.PayType;
import com.wangfanpinche.dto.PushEntity.PushType;
import com.wangfanpinche.dto.order.Evaluate;
import com.wangfanpinche.dto.order.Location;
import com.wangfanpinche.dto.order.OrderOwner;
import com.wangfanpinche.dto.order.OrderOwner.OrderOwnerStatus;
import com.wangfanpinche.dto.order.OrderPassenger;
import com.wangfanpinche.dto.order.OrderPassenger.OrderPassengerStatus;
import com.wangfanpinche.dto.order.OrderPassenger.OrderPassengerType;
import com.wangfanpinche.provider.utils.BeanUtils;
import com.wangfanpinche.service.OrderOwnerService;
import com.wangfanpinche.service.OwnerApproveService;
import com.wangfanpinche.service.UserService;
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
import com.wangfanpinche.vo.CarVo;
import com.wangfanpinche.vo.OwnerApproveVo;
import com.wangfanpinche.vo.PushEntityVo;
import com.wangfanpinche.vo.WeChat;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.order.OrderOwnerVo;
import com.wangfanpinche.vo.order.OrderPassengerVo;

@Service
public class OrderOwnerServiceImpl implements OrderOwnerService {

	@Autowired
	private BaseDao baseDao;

	@Autowired
	private UserService userService;

	@Autowired
	private OwnerApproveService ownerApproveService;

	@Override
	public List<OrderOwnerVo> listAll(OrderOwnerVo vo) {
		StringBuilder hql = new StringBuilder(" select new com.wangfanpinche.vo.order.OrderOwnerVo( o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.status) from OrderOwner o left join o.user u left join o.fromLocation f left join o.toLocation t where o.id is not null ");
		Map<String, Object> params = new HashMap<>();

		addWhereByListAll(hql, params, vo);

		List<OrderOwnerVo> l = baseDao.find(OrderOwnerVo.class, hql.toString() + " order by o.createDateTime desc ", params);
 
		return l;
	}

	private void addWhereByListAll(StringBuilder hql, Map<String, Object> params, OrderOwnerVo vo) {

		hql.append(" and u.id = :uid ");
		params.put("uid", vo.getUserId());

		hql.append(" and o.deleted = :odelete ");
		params.put("odelete", false);

	}

	@Override
	public List<OrderOwnerVo> listByFromLocation(OrderOwnerVo vo, PageHelper ph) {
		StringBuilder hql = new StringBuilder(" select new com.wangfanpinche.vo.order.OrderOwnerVo( o.id, o.singleFare, f.lng, f.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.honesty, u.id, u.userIcon, u.mobilePhone, u.username, u.status, o.carBrandAndSystem) from OrderOwner o left join o.user u left join o.fromLocation f left join o.toLocation t where o.id is not null ");
		Map<String, Object> params = new HashMap<>();

		addWhereByListLocation(hql, params, vo);

		List<OrderOwnerVo>	l = baseDao.find(OrderOwnerVo.class, hql.toString(), params, ph);

		return l;
	}

	private void addWhereByListLocation(StringBuilder hql, Map<String, Object> params, OrderOwnerVo vo) {
		hql.append(" and o.deleted = :odelete ");
		params.put("odelete", false);
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

		hql.append(" and o.status = :status ");
		params.put("status", OrderOwner.OrderOwnerStatus.PUBLISH);
		
		hql.append(" and o.departDateTime > :now ");
		params.put("now", now);

	}
	
	@Validated
	public List<OrderOwnerVo> listByLocation(OrderOwnerVo vo){
		LocalDateTime now = LocalDateTime.now();
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getOrderPassengerId());
		String hql = " select new com.wangfanpinche.vo.order.OrderOwnerVo( o.id, o.singleFare, f.lng, f.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.honesty, u.id, u.userIcon, u.mobilePhone, u.username, u.status, o.carBrandAndSystem) from OrderOwner o left join o.user u left join o.fromLocation f left join o.toLocation t "
		+ " where o.id is not null and o.deleted = :odelete and f.city = :fromCity and t.city = :toCity and o.status = :status and o.departDateTime > :now and o.currentSeat >= :seat";
		Map<String, Object> params = new HashMap<>();
		params.put("odelete", false);
		params.put("fromCity", p.getFromLocation().getCity());
		params.put("toCity", p.getToLocation().getCity());
		params.put("status", OrderOwner.OrderOwnerStatus.PUBLISH);
		params.put("now", now);
		params.put("seat", p.getSeat());
		List<OrderOwnerVo>	l = baseDao.find(OrderOwnerVo.class, hql.toString(), params);
		return l;
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
	public List<OrderOwnerVo> listByPublish(OrderOwnerVo vo) {
		StringBuilder hql = new StringBuilder(" select new com.wangfanpinche.vo.order.OrderOwnerVo( o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.status) from OrderOwner o left join o.user u left join o.fromLocation f left join o.toLocation t where o.id is not null ");
		Map<String, Object> params = new HashMap<>();

		addWhereByListPublish(hql, params, vo);

		List<OrderOwnerVo> l = baseDao.find(OrderOwnerVo.class, hql.toString(), params);

		return l;
	}

	private void addWhereByListPublish(StringBuilder hql, Map<String, Object> params, OrderOwnerVo vo) {
		hql.append(" and u.id = :uid ");
		params.put("uid", vo.getUserId());

		hql.append(" and o.deleted = :odelete ");
		params.put("odelete", false);

		hql.append(" and o.status = :status ");
		params.put("status", OrderOwner.OrderOwnerStatus.PUBLISH);
	}

	@Override
	public OrderOwnerVo listByRunning(OrderOwnerVo vo) {

		StringBuilder hql = new StringBuilder(" select new com.wangfanpinche.vo.order.OrderOwnerVo( o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.status) from OrderOwner o left join o.user u left join o.fromLocation f left join o.toLocation t where o.id is not null ");
		Map<String, Object> params = new HashMap<>();

		addWhereByListRunning(hql, params, vo);
		OrderOwnerVo orderOwnerVo = null;
		try {
			orderOwnerVo = baseDao.get(OrderOwnerVo.class, hql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return orderOwnerVo;
	}

	private void addWhereByListRunning(StringBuilder hql, Map<String, Object> params, OrderOwnerVo vo) {
		hql.append(" and u.id = :uid ");
		params.put("uid", vo.getUserId());

		hql.append(" and o.deleted = :odelete ");
		params.put("odelete", false);

		hql.append(" and o.status in (:status) ");
		params.put("status", new OrderOwnerStatus[] { OrderOwnerStatus.RECEIVE, OrderOwnerStatus.DEPART });
	}

	@Override
	public OrderOwnerVo detail(OrderOwnerVo vo) {
		String hql = " select new com.wangfanpinche.vo.order.OrderOwnerVo( o.id, f.lng, f.lat, t.lng, t.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.singleFare, o.via, o.description) from OrderOwner o left join o.fromLocation f left join o.toLocation t where  o.id = ? ";
		OrderOwnerVo result = baseDao.get(OrderOwnerVo.class, hql, vo.getId());
		return result;
	}

	@Override
	public String publish(OrderOwnerVo vo) {
		OrderOwner o = new OrderOwner();

		// 初始化起始位置
		Location fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
		baseDao.persist(fromLocation);
		o.setFromLocation(fromLocation);

		// 初始化目标位置
		Location toLocation = gpsToBean(vo.getToLng(), vo.getToLat());
		baseDao.persist(toLocation);
		o.setToLocation(toLocation);

		// 判断是否认证
		if (!ownerApproveService.hasOwnerApprove(new OwnerApproveVo(vo.getUserId()))) {
			throw new RuntimeException("您还没有通过车主认证，不能发布!");
		}

		// 判断两个小时内是否发布过
		LocalDateTime ldt = LocalDateTime.now();
		ldt = ldt.minusHours(2);
//		String timeOutCountHql = " select count(oo.id) from OrderOwner oo where oo.deleted = ? and oo.createDateTime > ? and oo.user.id = ? and oo.fromLocation.city = ? and oo.status != ? ";
//		Long timeOutCount = baseDao.count(timeOutCountHql, false, ldt, vo.getUserId(), fromLocation.getCity(), OrderOwnerStatus.CLOSED);
//		if (timeOutCount > 0) {
//			throw new RuntimeException("您在同一城市发布车单信息请间隔两个小时!");
//		}

		// 赋值需要的属性
		BeanUtils.copyProperties(vo, o);
		o.setCurrentSeat(o.getSeat());
		o.setStatus(OrderOwnerStatus.PUBLISH);
		o.setHonesty(false);
		o.setResultFare(new BigDecimal(0));

		User user = new User();
		user.setId(vo.getUserId());
		o.setUser(user);

		CarVo cvo = userService.getCarByUserId(vo.getUserId());
		CarVo vo2 = userService.getCarByCarInfoId(cvo.getCarInfoId());
		Car car = new Car();
		car.setId(cvo.getId());
		o.setCar(car);
		o.setCarBrandAndSystem(vo2.getBrandName() + " " + vo2.getSystemName());
		o.setCarColor(cvo.getCarColor());
		o.setCarNumber(cvo.getCarNumber());

		o.setSingleFare(vo.getSingleFare().setScale(2, RoundingMode.HALF_UP));

		baseDao.save(o);
		
		//车主匹配乘客----1.发车时间同一天 2.乘客发布车单，没有同意车主 3.匹配开始结束城市
		String sql = " select p.user_ID userId from t_orderpassenger p LEFT JOIN t_location f on p.fromLocation_ID = f.ID LEFT JOIN t_location t on p.toLocation_ID = t.ID LEFT JOIN t_user u on p.user_ID = u.ID WHERE p.DELETED = :deleted and p.orderOwner_ID IS NULL and p.`status` = :status and DATE_FORMAT(p.departDateTime,'%Y-%m-%d') = :departDateTime and f.city = :fromCity and t.city = :toCity ";
		Map<String, Object> map = new HashMap<>();
		map.put("deleted", false);
		map.put("status", OrderPassengerStatus.PUBLISH);
		map.put("departDateTime", DateUtils.toString(o.getDepartDateTime(), "yyyy-MM-dd"));
		map.put("fromCity", o.getFromLocation().getCity());
		map.put("toCity", o.getToLocation().getCity());
		List<OrderPassengerVo> orderPassList = baseDao.findBySql(OrderPassengerVo.class, sql, map);
		if (orderPassList != null && orderPassList.size() > 0) {
			for (OrderPassengerVo p : orderPassList) {
				System.out.println("乘客的用户id------------"+p.getUserId());
				//推送-车主匹配乘客
				initPushMatchPassenger(o, p);
			}
		}
		
		return o.getId();
	}

	private void initPushMatchPassenger(OrderOwner o, OrderPassengerVo p) {
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("有车主匹配您的车单!");
		push.setBizNumber(BizNumber.MATCHPASSENGER);
		push.setBody(o.getId());
		push.setUser(o.getUser());
		baseDao.save(push);
				
		UserPush userPush = new UserPush();
		userPush.setPushEntity(push);
		userPush.setToUser(new User(p.getUserId()));
		userPush.setRead(false);
		baseDao.save(userPush);
		
		PushEntityVo pvo = new PushEntityVo();
		BeanUtils.copySomeProperties(push, pvo, "type","title","bizNumber","body");
		pvo.setUserpushId(userPush.getId());
		
		Map<String, String> params = new HashMap<>();
		params.put("entity", JSON.toJSONString(pvo));
		JPushUtils.sendByAlias(push.getTitle(), params, userPush.getToUser().getId());
	}

	@Override
	public OrderOwnerVo getUserinfo(OrderOwnerVo vo) {

		String hqlUser = " select new com.wangfanpinche.vo.order.OrderOwnerVo( u.id, u.userIcon, u.mobilePhone, u.username, u.status, oa.status, u.homeAddress, o.carBrandAndSystem, o.carColor, o.carNumber) from OrderOwner o left join o.user u left join OwnerApprove oa on u.id = oa.userId where o.deleted = ? and o.id = ? ";
		OrderOwnerVo ovo = baseDao.get(OrderOwnerVo.class, hqlUser, false, vo.getId());
		//拼车次数
		String hqlototal = " select count(id) from OrderOwner where deleted = ? and user.id = ? and status = ? ";
		String hqlptotal = " select count(id) from OrderPassenger where deleted = ? and user.id = ? and status = ? ";
		Long total = baseDao.count(hqlototal, false, ovo.getUserId(), OrderOwnerStatus.FINISH) + baseDao.count(hqlptotal, false, ovo.getUserId(), OrderPassengerStatus.FINISH);
		ovo.setTotal(total);
		//车主评分
		String hqlavg = " select new com.wangfanpinche.vo.order.OrderOwnerVo(avg(description), avg(trustworthy), avg(service)) from Evaluate where deleted = ? and toUser.id = ? ";
		OrderOwnerVo rvo = baseDao.get(OrderOwnerVo.class, hqlavg, false, ovo.getUserId());
		if (rvo.getDescriptions() == null) {
			ovo.setDescriptions(Double.valueOf(5));
		}else{
			ovo.setDescriptions(rvo.getDescriptions());
		}
		if (rvo.getTrustworthy() == null) {
			ovo.setTrustworthy(Double.valueOf(5));
		}else{
			ovo.setTrustworthy(rvo.getTrustworthy());
		}
		if (rvo.getService() == null) {
			ovo.setService(Double.valueOf(5));
		}else{
			ovo.setService(rvo.getService());
		}
		return ovo;
	}


	@Override
	public String honestyAliPayPreOrder(OrderOwnerVo vo) {
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		// 要判断他是否生成过订单信息，如果生成过。则返回原来生成的订单信息
		if (o.getHonestyOrderNumber() != null) {
			Journal j = baseDao.get(Journal.class, " from Journal where orderNumber = ? ", o.getHonestyOrderNumber());
			if(PayType.支付宝.equals(j.getPayType())){
				return j.getPayInfo().getRequestBody();
			} else {
				baseDao.delete(j);
			}
		}

		// 是否支付过
		if (o.getHonesty() == true) {
			throw new RuntimeException("支付错误:已支付诚信必发");
		}
		
		// 订单号
		o.setHonestyOrderNumber(SnowFlakeIdGenerator.getInstance().generateLongId());

		AliPayBizContent content = new AliPayBizContent();
		content.setTimeout_express("15m");
		content.setProduct_code("QUICK_MSECURITY_PAY");
		BigDecimal totalMoney = new BigDecimal("100");
totalMoney = new BigDecimal("0.01");
		content.setTotal_amount(totalMoney.toString());
		content.setSubject("往返拼车");
		content.setBody("车主-诚信必发");
		content.setOut_trade_no(o.getHonestyOrderNumber() + "");
		// 获取支付订单信息并保存到支付信息表
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/OrderOwner/honestyAliPayNotify";
		String notifyUrl = "http://app.wangfanpinche.com:8889/OrderOwner/honestyAliPayNotify";
		String orderInfo = AlipayAppUtils.buildOrderInfo(content, notifyUrl);

		// 支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(orderInfo);
		baseDao.save(pay);
		// 账单
		Journal j = new Journal();
		j.setType(BizType.诚信必发);
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.支付宝);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);
		j.setOrderNumber(o.getHonestyOrderNumber());
		j.setMoney(totalMoney);
		j.setUser(o.getUser());
		j.setPayInfo(pay);
		baseDao.save(j);

		return orderInfo;
	}

	@Override
	public void honestyAliPayNotify(Map<String, String> map) {
		// 1.判断计算金额和支付金额是否一致
		// 订单号
		String honestyOrderNumber = map.get("out_trade_no");
		String hql = " from OrderOwner where deleted = ? and honestyOrderNumber = ? ";
		OrderOwner o = baseDao.get(OrderOwner.class, hql, false, Long.parseLong(honestyOrderNumber));
		BigDecimal totalMoney = new BigDecimal("100");
totalMoney = new BigDecimal("0.01");
		// 支付宝返回的支付金额
		BigDecimal receipt_amount = new BigDecimal(map.get("receipt_amount"));
		if (!totalMoney.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.payInfo where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(honestyOrderNumber));
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		j.getPayInfo().setNotifyBody(JSON.toJSONString(map));
		// 支付结果-更改车主车单诚信必发状态
		o.setHonesty(true);	
		baseDao.update(j);
		baseDao.update(o);
	}

	@Override
	public String honestyWechatPayPreOrder(OrderOwnerVo vo) {
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		// 要判断他是否生成过订单信息，如果生成过。则返回原来生成的订单信息
		if (o.getHonestyOrderNumber() != null) {
			Journal j = baseDao.get(Journal.class, " from Journal where orderNumber = ? ", o.getHonestyOrderNumber());
			if(PayType.支付宝.equals(j.getPayType())){
				return j.getPayInfo().getRequestBody();
			} else {
				baseDao.delete(j);
			}
		}

		// 是否支付过
		if (o.getHonesty() == true) {
			throw new RuntimeException("支付错误:已支付诚信必发");
		}
				
		long honestyOrderNumber = SnowFlakeIdGenerator.getInstance().generateLongId();
		o.setHonestyOrderNumber(honestyOrderNumber);
		
		WechatPreOrder p = new WechatPreOrder();
		p.setOut_trade_no(honestyOrderNumber + "");
		//按照分来计算，10000就是100元，1就是0.01元
		BigDecimal totalMoney = new BigDecimal("100");
totalMoney = new BigDecimal("0.01");
		p.setTotal_fee(totalMoney.multiply(new BigDecimal("100")).intValue());
		p.setSpbill_create_ip(vo.getIp());
		p.setTime_expire(LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/OrderOwner/honestyWechatPayNotify";
		String notifyUrl = "http://app.wangfanpinche.com:8889/OrderOwner/honestyWechatPayNotify";
		p.setNotify_url(notifyUrl);
		p.setBody("微信车主支付诚信必发");
		
		String preOrder = WechatPayAppUtils.preOrder(p);
		
		//支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(preOrder);
		baseDao.save(pay);
		//账单
		Journal j = new Journal();
		j.setType(BizType.诚信必发);
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.微信);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);		
		j.setOrderNumber(honestyOrderNumber);
		j.setMoney(totalMoney);
		j.setUser(new User(o.getUser().getId()));
		j.setPayInfo(pay);
		baseDao.save(j);
		
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
	public void honestyWechatPayNotify(WechatPayNotify notify) {
		String honestyOrderNumber = notify.getOut_trade_no();
		String hql = " from OrderOwner where deleted = ? and orderNumber = ? ";
		OrderOwner o = baseDao.get(OrderOwner.class, hql, false, Long.parseLong(honestyOrderNumber));
		BigDecimal totalMoney = new BigDecimal("100");
totalMoney = new BigDecimal("0.01");		
		//微信返回的支付金额		
		Integer receipt_amountInt = notify.getTotal_fee();
		BigDecimal receipt_amount = new BigDecimal(receipt_amountInt).divide(new BigDecimal("100"));
		if (!totalMoney.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.payInfo where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(honestyOrderNumber));
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		j.getPayInfo().setNotifyBody(JSON.toJSONString(notify));
		//支付结果-更改车主车单诚信必发状态
		o.setHonesty(true);
		baseDao.update(j);
		baseDao.update(o);
	}

	@Override
	public void invitationPassenger(OrderOwnerVo vo) {
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getOrderPassengerId());
		if(!p.getStatus().equals(OrderPassengerStatus.PUBLISH)){
			throw new RuntimeException("乘客车单当前不是发布车单状态,不能抢单!");
		}
		if (vo.getUserId().equals(p.getUser().getId())) {
			throw new RuntimeException("您好,您不能对您自己发布的车单进行抢单操作!");
		}
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		if (o.getCurrentSeat() < p.getSeat()) {
			throw new RuntimeException("您的车主车单剩余座位数不够,不能抢单!");
		}
		p.getOwners().add(o);
		baseDao.update(p);	
		
		//推送-车主邀请乘客
		initPushOo001(vo, p, o);
	}

	private void initPushOo001(OrderOwnerVo vo, OrderPassenger p, OrderOwner o) {
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("有车主邀请您乘坐!");
		push.setBizNumber(BizNumber.OO_001);
		push.setBody(p.getId());
		push.setUser(o.getUser());
		baseDao.save(push);
		
		UserPush u = new UserPush();
		u.setPushEntity(push);
		u.setToUser(p.getUser());
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
	public void approvePassenger(OrderOwnerVo vo) {
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		if (!o.getStatus().equals(OrderOwnerStatus.PUBLISH)) {
			throw new RuntimeException("车主车单当前不是发布状态，不能同意搭载乘客!");
		}
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getOrderPassengerId());		
		
		List<OrderPassenger> passengers = o.getPassengers();
		for (int i = 0; i < passengers.size(); i++) {
			OrderPassenger op =  passengers.get(i);
			if(op.getId().equals(vo.getOrderPassengerId())){
				p = passengers.remove(i);
				break;
			}
		}
		
		p.setApproveStatus(true);
		p.setApproveDateTime(LocalDateTime.now());
		p.setOrderOwner(o);
		p.setPassengerType(OrderPassengerType.Passenger);
		baseDao.update(p);
		
		//推送-车主同意
		initPushOo002(o, p);
	}

	private void initPushOo002(OrderOwner o, OrderPassenger p) {
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("车主同意您乘坐!");
		push.setBizNumber(BizNumber.OO_002);
		push.setBody(p.getId());
		push.setUser(o.getUser());
		baseDao.save(push);
		
		UserPush u = new UserPush();
		u.setPushEntity(push);
		u.setToUser(p.getUser());
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
	public void disapprovePassenger(OrderOwnerVo vo) {
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		OrderPassenger p = null;
		List<OrderPassenger> passengers = o.getPassengers();
		for (int i = 0; i < passengers.size(); i++) {
			OrderPassenger op =  passengers.get(i);
			if(op.getId().equals(vo.getOrderPassengerId())){
				p = passengers.remove(i);
				break;
			}
		}
		
		p.setDisapproveStatus(true);
		p.setDisapproveDateTime(LocalDateTime.now());
	}

	@Override
	public OrderOwnerVo passengerList(OrderOwnerVo vo) {
		String hql = " select new com.wangfanpinche.vo.order.OrderOwnerVo( o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.status, u.id) from OrderOwner o left join o.fromLocation f left join o.toLocation t left join o.user u where o.deleted = ? and o.id = ? ";
		OrderOwnerVo ovo = baseDao.get(OrderOwnerVo.class, hql, false, vo.getId());
		
		//预定的乘客
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		List<OrderPassenger> pList = o.getPassengers();
		ovo.setRegTotal(pList.size());
		if (pList != null && pList.size() > 0) {
			String sql = "SELECT p.ID id, u.ID userId, u.userIcon userIcon, u.mobilePhone mobilePhone, u.username username, u.status userstatus, f.lng fromLng, f.lat fromLat, f.city fromCity, f.sematicDescription fromSematicDescription, t.city toCity, t.sematicDescription toSematicDescription, p.departDateTime departDateTime, p.seat seat, p.singleFare singleFare, p.status status, p.descript descript, p.passengerType passengerType, p.closedStatus closedStatus, p.payStatus payStatus, p.approveStatus approveStatus, p.disapproveStatus disapproveStatus FROM t_orderpassenger p LEFT JOIN t_location f on p.fromLocation_ID = f.ID LEFT JOIN t_location t on p.toLocation_ID = t.ID LEFT JOIN t_user u on p.user_ID = u.ID where p.ID IN (SELECT oop.passengers_ID FROM t_orderowner_passengers oop LEFT JOIN t_orderowner o on oop.OrderOwner_ID = o.ID where o.DELETED = :deleted and o.ID = :id) and p.DELETED = :deleted ";
			Map<String, Object> params = new HashMap<>();
			params.put("deleted", false);
			params.put("id", o.getId());
			List<OrderPassengerVo> passengers = baseDao.findBySql(sql, params);
			ovo.setPassengers(passengers);
		}
		
		//进行中的乘客
		String hqlRun = " select new com.wangfanpinche.vo.order.OrderPassengerVo( p.id, u.id, u.userIcon, u.mobilePhone, u.username, u.status, f.lng, f.lat, f.city, f.sematicDescription, t.city, t.sematicDescription, p.departDateTime, p.seat, p.singleFare, p.status, p.descript, p.passengerType, p.closedStatus, p.closedDateTime, p.closedDescirpt, p.payStatus, p.payDateTime, p.approveStatus, p.approveDateTime, p.disapproveStatus, p.disapproveDateTime, p.goinStatus, p.goinDateTime, gi.lng, gi.lat, gi.city, gi.sematicDescription, p.gooutStatus, p.gooutDateTime, go.lng, go.lat, go.city, go.sematicDescription, p.toEvaluateModify, p.fromEvaluateModify) from OrderPassenger p left join p.fromLocation f left join p.toLocation t left join p.user u left join p.goinLocation gi left join p.gooutLocation go where p.deleted = ? and p.orderOwner.id = ? ";
		List<OrderPassengerVo> runPassenger = baseDao.find(OrderPassengerVo.class, hqlRun, false, o.getId());
		if (runPassenger != null && runPassenger.size() > 0) {
			ovo.setRunPassengers(runPassenger);
		}
		
		return ovo;
	}

	@Override
	public void receive(OrderOwnerVo vo) {
		LocalDateTime now = LocalDateTime.now();
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		if (!o.getStatus().equals(OrderOwnerStatus.PUBLISH)) {
			throw new RuntimeException("车主车单当前不是发布状态，不能出发去接乘客!");
		}		
		String hqlown = " update OrderOwner set status = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlown, OrderOwnerStatus.RECEIVE, now, false, vo.getId());
		String hqlpass = " update OrderPassenger set status = ?, modifyDateTime = ? where deleted = ? and orderOwner.id = ? and payStatus = ? and approveStatus = ? and status = ? ";
		baseDao.execute(hqlpass, OrderPassengerStatus.NO_RECEIVE, now, false, vo.getId(), true, true, OrderPassengerStatus.TAKER);
		
		//推送-车主出发接乘客
		String hql = " select new com.wangfanpinche.vo.order.OrderPassengerVo(p.id, u.id) from OrderPassenger p left join p.user u where p.deleted = ? and p.orderOwner.id = ? and p.payStatus = ? and p.approveStatus = ? ";
		List<OrderPassengerVo> pList = baseDao.find(OrderPassengerVo.class, hql, false, vo.getId(), true, true);
		if (pList != null && pList.size() > 0) {
			for (OrderPassengerVo op : pList) {
				initPushOo003(o, op);
			}
		}
		
	}

	private void initPushOo003(OrderOwner o, OrderPassengerVo op) {
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("车主出发了!");
		push.setBizNumber(BizNumber.OO_003);
		push.setBody(op.getId());
		push.setUser(o.getUser());
		baseDao.save(push);
		
		UserPush u = new UserPush();
		u.setPushEntity(push);			
		u.setToUser(new User(op.getUserId()));
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
	public void depart(OrderOwnerVo vo) {
		LocalDateTime now = LocalDateTime.now();
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		if (!o.getStatus().equals(OrderOwnerStatus.RECEIVE)) {
			throw new RuntimeException("车主车单当前不是出发接乘客状态，不能发车!");
		}
		
		String hql = " select count(id) from OrderPassenger where deleted = ? and goinStatus = ? and orderOwner.id = ? and status = ? ";
		Long count = baseDao.count( hql, false, false, vo.getId(), OrderPassengerStatus.NO_RECEIVE);
		if (count > 0) {
			throw new RuntimeException("有[" + count + "]个乘客还没有上车，您不能发车!");
		}
		
		String hqlown = " update OrderOwner set status = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlown, OrderOwnerStatus.DEPART, now, false, vo.getId());
	}

	@Override
	public void finish(OrderOwnerVo vo) {
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		if (!o.getStatus().equals(OrderOwnerStatus.DEPART)) {
			throw new RuntimeException("车主车单当前不是发车状态，不能结束行程!");
		}
		
		String hql = " select count(id) from OrderPassenger where deleted = ? and gooutStatus = ? and orderOwner.id = ? and status = ? ";
		Long count = baseDao.count( hql, false, false, vo.getId(), OrderPassengerStatus.RECEIVE);
		if (count > 0) {
			throw new RuntimeException("有[" + count + "]个乘客还没有下车，您不能结束行程!");
		}
		
		//查询当前车单的乘客支付的总金额，然后给车主余额
		LocalDateTime now = LocalDateTime.now();
		String hqluser = " update User set balance = balance + ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqluser, o.getResultFare(), now, false, vo.getUserId());
		//车主完成诚信必发退款
		if (o.getHonesty() == true) {
			String hqltype = " from Journal j left join fetch j.user u where j.type = ? and j.InOutType = ? and j.status = ? and j.orderNumber = ? ";
			Journal j = baseDao.get(Journal.class, hqltype, BizType.诚信必发, InOutType.收入, com.wangfanpinche.dto.Journal.Status.完成, o.getHonestyOrderNumber());
			if (j.getPayType().equals(PayType.支付宝)) {
				String refund_amount = j.getMoney().toString();
				AlipayAppUtils.refund(o.getHonestyOrderNumber().toString(), refund_amount);				
			}
			if (j.getPayType().equals(PayType.微信)) {
				WechatPayAppUtils.refound(o.getHonestyOrderNumber(), j.getMoney().multiply(new BigDecimal("100")).intValue(), j.getMoney().multiply(new BigDecimal("100")).intValue());				
			}	
			Journal jo = new Journal();
			jo.setType(j.getType());
			jo.setInOutType(InOutType.支出);
			jo.setPayType(j.getPayType());
			jo.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
			jo.setOrderNumber(o.getHonestyOrderNumber());
			jo.setMoney(j.getMoney());
			jo.setUser(j.getUser());
			baseDao.save(jo);
		}		
		//车单状态改变 - 完成
		o.setStatus(OrderOwnerStatus.FINISH);
		baseDao.update(o);
	}

	@Override
	public void evaluate(OrderOwnerVo vo) {
		OrderPassenger o = baseDao.getById(OrderPassenger.class, vo.getOrderPassengerId());
		o.setFromEvaluateModify(true);
		baseDao.update(o);
		if (!o.getStatus().equals(OrderPassengerStatus.FINISH)) {
			throw new RuntimeException("乘客车单当前状态不是完成状态,不能评价!");
		}
		//车主评价乘客
		Evaluate e = baseDao.get(Evaluate.class, " from Evaluate e where e.id = ( select op.fromEvaluate.id from OrderPassenger op where op.id = ? ) ", vo.getOrderPassengerId());
		List<String> tagList = vo.getTags();
		if (tagList != null && tagList.size() > 0) {
			e.setTags(tagList);
		}
		baseDao.update(e);	
	}
	
	@Validated
	public void close(OrderOwnerVo vo) {
		OrderOwner o = baseDao.getById(OrderOwner.class, vo.getId());
		if (!o.getStatus().equals(OrderOwnerStatus.PUBLISH)) {
			throw new RuntimeException("车主车单当前状态不是发布车单状态,不能关闭车单!");
		}
		o.setStatus(OrderOwnerStatus.CLOSED);
		//车主关闭车单：1.车主诚信必发的钱不退 2. 查询车主车单所有支付过的乘客,退钱
		String hql = " from OrderPassenger p left join fetch p.orderOwner o where p.deleted = ? and p.orderOwner.id = ? and p.payStatus = ? and p.closedStatus = ? ";
		List<OrderPassenger> pList = baseDao.find(OrderPassenger.class, hql, false, vo.getId(), true, false);
		if (pList != null && pList.size() > 0) {
			for (OrderPassenger p : pList) {
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
			}
		}
	}

	@Override
	public void warn(OrderOwnerVo vo) {
		OrderPassenger p = baseDao.getById(OrderPassenger.class, vo.getOrderPassengerId());
		
		//推送-车主提醒乘客支付
		PushEntity push = new PushEntity();
		push.setType(PushType.BIZ);
		push.setTitle("车主提醒您支付车单!");
		push.setBizNumber(BizNumber.OO_004);
		push.setBody(p.getId());
		push.setUser(new User(vo.getUserId()));
		baseDao.save(push);
		
		UserPush u = new UserPush();
		u.setPushEntity(push);			
		u.setToUser(p.getUser());
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
