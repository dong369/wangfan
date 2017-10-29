package com.wangfanpinche.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Car;
import com.wangfanpinche.dto.Journal;
import com.wangfanpinche.dto.Journal.BizType;
import com.wangfanpinche.dto.Journal.InOutType;
import com.wangfanpinche.dto.Journal.PayType;
import com.wangfanpinche.dto.OwnerLocation;
import com.wangfanpinche.dto.OwnerOrder;
import com.wangfanpinche.dto.OwnerOrder.Status;
import com.wangfanpinche.dto.OwnerOrderDepart;
import com.wangfanpinche.dto.OwnerOrderDepartPassenger;
import com.wangfanpinche.dto.OwnerOrderFinish;
import com.wangfanpinche.dto.OwnerOrderFinish.FinishStatus;
import com.wangfanpinche.dto.OwnerOrderFinishOwnerEvaluate;
import com.wangfanpinche.dto.OwnerOrderFinishPassengerEvaluate;
import com.wangfanpinche.dto.OwnerOrderPublish;
import com.wangfanpinche.dto.OwnerOrderPublishPassenger;
import com.wangfanpinche.dto.OwnerOrderPublishPassenger.Type;
import com.wangfanpinche.dto.OwnerOrderReceive;
import com.wangfanpinche.dto.OwnerOrderReceivePassenger;
import com.wangfanpinche.dto.PassengerOrder;
import com.wangfanpinche.dto.PayInfo;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.service.OwnerApproveService;
import com.wangfanpinche.service.OwnerOrderService;
import com.wangfanpinche.service.UserService;
import com.wangfanpinche.utils.http.HttpUtils;
import com.wangfanpinche.utils.id.SnowFlakeIdGenerator;
import com.wangfanpinche.utils.map.BaiduMapUtils;
import com.wangfanpinche.utils.other.EncryptUtils;
import com.wangfanpinche.utils.pay.AliPayBizContent;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.utils.pay.WechatPreOrder;
import com.wangfanpinche.vo.CarVo;
import com.wangfanpinche.vo.OwnerApproveVo;
import com.wangfanpinche.vo.OwnerOrderVo;
import com.wangfanpinche.vo.PassengerListDepartVo;
import com.wangfanpinche.vo.PassengerListPublishVo;
import com.wangfanpinche.vo.PassengerListReceiveVo;
import com.wangfanpinche.vo.PassengerListVo;
import com.wangfanpinche.vo.PassengerOrderVo;
import com.wangfanpinche.vo.UserVo;
import com.wangfanpinche.vo.WeChat;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class OwnerOrderServiceImpl implements OwnerOrderService {

	@Autowired
	private BaseDao baseDao;

	@Autowired
	private OwnerApproveService ownerApproveService;

	@Autowired
	private UserService userService;
		

	@Override
	public List<OwnerOrderVo> ownerOrderlist(OwnerOrderVo vo, PageHelper ph) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select new com.wangfanpinche.vo.OwnerOrderVo(o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.fare, o.honesty, u.id, u.userIcon, u.mobilePhone, u.username, u.status, o.brandSystem, f.lng, f.lat) from OwnerOrder o left join o.user u left join o.fromLocation f left join o.toLocation t where o.deleted = :deleted and o.status = :status ");
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("status", Status.PUBLISH);
		
		addWhere(sb, params, vo);
		
		return baseDao.find(OwnerOrderVo.class, sb.toString() + " order by o.modifyDateTime desc ", params, ph);
	}
	
	private void addWhere(StringBuilder sb, Map<String, Object> params, OwnerOrderVo vo) {
		
		if(vo.getFromLng() != null){
			OwnerLocation fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
			sb.append(" and f.city = :fromCity ");
			params.put("fromCity", fromLocation.getCity());
		}				
		
	}

	@Override
	public List<OwnerOrderVo> ownerStrokeRecord(OwnerOrderVo vo) {
		//用户的车主行程记录
		StringBuilder sb = new StringBuilder();
		sb.append(" select new com.wangfanpinche.vo.OwnerOrderVo(o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.status) from OwnerOrder o left join o.user u left join o.fromLocation f left join o.toLocation t where o.deleted = :deleted and u.id = :userId ");
		
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("userId", vo.getUserId());
		
		addSelect(sb, params, vo);
		
		return baseDao.find(OwnerOrderVo.class, sb.toString(), params);
	}
	
	private void addSelect(StringBuilder sb, Map<String, Object> params, OwnerOrderVo vo) {
		
		if(vo.getStatus() != null){
			sb.append(" and o.status = :status ");
			params.put("status", vo.getStatus());
		}				
		
	}
	
	@Override
	public Boolean hasRunningOrder(OwnerOrderVo vo) {
		String hqlown = " select count(id) from OwnerOrder where deleted = ? and user.id = ? and (status = ? or status = ?) ";
		Long countown = baseDao.count(hqlown, false, vo.getUserId(), Status.RECEIVE, Status.DEPART);		
		String hqlpass = " select count(id) from PassengerOrder where deleted = ? and user.id = ? and (status = ? or status = ?)";
		Long countpass = baseDao.count(hqlpass, false, vo.getUserId(), Status.RECEIVE, Status.DEPART);
		if (countpass+countown == 0) {
			return false;
		}
		return true;
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

	public OwnerLocation getOwnerLocation(String latAndLng) {
		String geocoderRevers = BaiduMapUtils.geocoderRevers(latAndLng);
		OwnerLocation o = new OwnerLocation();
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

	/**
	 * GPS转换成系统可识别的Bean对象
	 * 
	 * @param lng
	 * @param lat
	 * @return
	 */
	public OwnerLocation gpsToBean(BigDecimal lng, BigDecimal lat) {
		String gpsToBaidu = gpsToBaidu(lng, lat);
		return getOwnerLocation(gpsToBaidu);
	}

	@Override
	public Serializable publish(OwnerOrderVo vo) {
		OwnerOrder o = new OwnerOrder();
		OwnerLocation fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
		baseDao.persist(fromLocation);
		o.setFromLocation(fromLocation);
		OwnerLocation toLocation = gpsToBean(vo.getToLng(), vo.getToLat());
		baseDao.persist(toLocation);
		o.setToLocation(toLocation);

		BeanUtils.copyProperties(vo, o);
		o.setCurrentSeat(o.getSeat());
		o.setStatus(OwnerOrder.Status.PUBLISH);
		
		// 1、验证是否车主认证过
		if (!ownerApproveService.hasOwnerApprove(new OwnerApproveVo(vo.getUserId()))) {
			throw new RuntimeException("您还没有通过车主认证，不能发布!");
		}
		// 2、验证同一城市，同一人，发布信息应该间隔两个小时  
		LocalDateTime ldt = LocalDateTime.now();
		ldt = ldt.minusHours(2);
		String hqlc = " select count(id) from OwnerOrder where deleted = ? and createDateTime > ? and user.id = ? and fromLocation.city = ? ";
		Long count = baseDao.count(hqlc, false, ldt, vo.getUserId(), fromLocation.getCity());
		// XXX
//		if (count > 0) {
//			 throw new RuntimeException("您在同一城市发布车单信息请间隔两个小时!");
//		}
		// 3、取到当前登陆的人和登陆人的车
		CarVo cvo = userService.getCarByUserId(vo.getUserId());
		Car car = new Car();
		car.setId(cvo.getId());
		o.setCar(car);
		CarVo ccvo = userService.getCarByCarInfoId(cvo.getCarInfoId());
		o.setBrandSystem(ccvo.getBrandName()+" "+ccvo.getSystemName());
		User user = new User();
		user.setId(vo.getUserId());
		o.setUser(user);
		
		//标签有值就存
		List<String> tagList = vo.getCarTags();
		if (tagList != null && tagList.size() > 0) {			
			o.setCarTags(tagList);
		}
		
		o.setFare(vo.getFare().setScale(2, RoundingMode.HALF_UP));
		// 2、保存订单状态对象
		baseDao.save(o);
		// 3、保存发布车单对象
		OwnerOrderPublish publish = new OwnerOrderPublish();
		publish.setOwnerOrder(o);
		baseDao.persist(publish);

		// 4、返回ID
		return o.getId();
	}

	@Override
	public BigDecimal carSalary(OwnerOrderVo vo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String alipayHonestyInfo(OwnerOrderVo vo) {
		OwnerOrder o = baseDao.getById(OwnerOrder.class, vo.getId());
		long orderId = SnowFlakeIdGenerator.getInstance().generateLongId();
		//订单号
		o.setOrderNumber(orderId);		
		
		AliPayBizContent content = new AliPayBizContent();
		//XXX 判断发布时间
		content.setTimeout_express("15m");
		content.setProduct_code("QUICK_MSECURITY_PAY");		
		BigDecimal totalMoney = new BigDecimal("100");
totalMoney = new BigDecimal("0.01");
		content.setTotal_amount(totalMoney.toString());
		content.setSubject("往返拼车");
		content.setBody("支付宝车主支付诚信必发获取订单信息");		
		content.setOut_trade_no(orderId + ""); 
		//获取支付订单信息并保存到支付信息表
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/ownerorder/alipayHonesty";
		String notifyUrl = "http://app.wangfanpinche.com:8889/ownerorder/alipayHonesty";
		String orderInfo = AlipayAppUtils.buildOrderInfo(content, notifyUrl);
		
		//支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(orderInfo);
		baseDao.save(pay);
		//账单
		Journal j = new Journal();
		j.setType(BizType.诚信必发);
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.支付宝);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);		
		j.setOrderNumber(orderId);
		j.setMoney(totalMoney);
		j.setUser(new User(vo.getUserId()));
		j.setPayInfo(pay);
		baseDao.save(j);
		
		return orderInfo;
	}

	@Override
	public void alipayHonesty(Map<String, String> map) {
		//1.判断计算金额和支付金额是否一致  
		//订单号
		String orderNumber = map.get("out_trade_no");
		String hql = " from OwnerOrder where deleted = ? and orderNumber = ? ";
		OwnerOrder o = baseDao.get(OwnerOrder.class, hql, false, Long.parseLong(orderNumber));
		BigDecimal totalMoney = new BigDecimal("100");
totalMoney = new BigDecimal("0.01");		
		//支付宝返回的支付金额
		BigDecimal receipt_amount = new BigDecimal(map.get("receipt_amount"));		
		if (!totalMoney.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.payInfo where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(orderNumber));
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		j.getPayInfo().setNotifyBody(JSON.toJSONString(map));
		//支付结果-更改车主车单诚信必发状态
		o.setHonesty(true);
	}
	
	
	@Override
	public String wechatPayHonestyInfo(OwnerOrderVo vo) {
		OwnerOrder o = baseDao.getById(OwnerOrder.class, vo.getId());
		long orderId = SnowFlakeIdGenerator.getInstance().generateLongId();
		o.setOrderNumber(orderId);
		
		WechatPreOrder p = new WechatPreOrder();
		p.setOut_trade_no(orderId + "");
		//按照分来计算，10000就是100元，1就是0.01元
		BigDecimal totalMoney = new BigDecimal("10000");
totalMoney = new BigDecimal("1");
		p.setTotal_fee(totalMoney.intValue());
		p.setSpbill_create_ip(vo.getIp());
		p.setTime_expire(LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/ownerorder/wechatPayHonesty";
		String notifyUrl = "http://app.wangfanpinche.com:8889/ownerorder/wechatPayHonesty";
		p.setNotify_url(notifyUrl);
		p.setBody("微信车主支付诚信必发获取订单信息");
		
		Map<String, Object> params = new HashMap<>();
		params.put("hello", "world");
		params.put("hello1", 1);
		p.setDetail(JSON.toJSONString(params));
		
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
		j.setOrderNumber(orderId);
		j.setMoney(totalMoney.divide(new BigDecimal("100")));
		j.setUser(new User(vo.getUserId()));
		j.setPayInfo(pay);
		baseDao.save(j);
		
		try {
			XmlMapper mapper = new XmlMapper();
			HashMap<String, String> readValue = mapper.readValue(preOrder, HashMap.class);
			WeChat we = new WeChat();
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return preOrder;
	}
	
	@Override
	public void wechatPayHonesty(WechatPayNotify notify){
		String orderNumber = notify.getOut_trade_no();
		String hql = " from OwnerOrder where deleted = ? and orderNumber = ? ";
		OwnerOrder o = baseDao.get(OwnerOrder.class, hql, false, Long.parseLong(orderNumber));
		BigDecimal totalMoney = new BigDecimal("10000");
totalMoney = new BigDecimal("1");
		Integer total_amount = totalMoney.intValue();
		//微信返回的支付金额
		Integer receipt_amount = Integer.valueOf(notify.getTotal_fee());
		if (!total_amount.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.payInfo where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(orderNumber));
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		j.getPayInfo().setNotifyBody(JSON.toJSONString(notify));
		//支付结果-更改车主车单诚信必发状态
		o.setHonesty(true);
	}

	/*
	@Override
	public Serializable passengerReserve(OwnerOrderVo vo) {
		// 验证是否是发布车单状态，车主车单的钱，判断当前座位数
		String hql = "select new com.wangfanpinche.vo.OwnerOrderVo(o.id, o.currentSeat, o.fare, o.status, u.id) from OwnerOrder o left join o.user u where o.deleted = ? and o.id = ? ";
		OwnerOrderVo ownerOrderVo = baseDao.get(OwnerOrderVo.class, hql, false, vo.getId());
		if (!ownerOrderVo.getStatus().equals(Status.PUBLISH)) {
			throw new RuntimeException("车单当前状态不是发布车单状态,不能下单!");
		}
		if (vo.getSeat() > ownerOrderVo.getCurrentSeat()) {
			throw new RuntimeException("您选择的座位数大于车单剩余座位数，请重新选择!");
		}
		if (vo.getSeat() < 1 || vo.getSeat() > 2) {
			throw new RuntimeException("您选择的座位数应该在1-2之间!");
		}
		if (ownerOrderVo.getUserId().equals(vo.getUserId())) {
			throw new RuntimeException("您好,您不能对您自己发布的车单进行下单操作!");
		}
		
		// 验证，是否有未付款或未关闭的订单。  
		String hqlpass = " select count(id) from OwnerOrderPublishPassenger where deleted = ? and user.id = ? and ownerOrder.id != ? and payStatus = ? and closedStatus = ? ";
		Long count = baseDao.count(hqlpass, false, vo.getUserId(), vo.getId(), false, false);
		if (count > 0) {
			throw new RuntimeException("您还有未付款或未关闭的订单,不能下单!");
		}
		
		User u = new User();
		u.setId(vo.getUserId());// 乘客的用户id

		OwnerOrderPublishPassenger passenger = new OwnerOrderPublishPassenger();
		passenger.setMoney(ownerOrderVo.getFare());// 车主车单的车费
		passenger.setUser(u);

		passenger.setType(OwnerOrderPublishPassenger.Type.OWNER);
		passenger.setSeat(vo.getSeat());
		if (StringUtils.hasText(vo.getDescription())) {
			passenger.setDescript(vo.getDescription());
		}

		OwnerLocation fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
		baseDao.persist(fromLocation);
		passenger.setFromLocation(fromLocation);
		OwnerLocation toLocation = gpsToBean(vo.getToLng(), vo.getToLat());
		baseDao.persist(toLocation);
		passenger.setToLocation(toLocation);

		OwnerOrder o = new OwnerOrder();
		o.setId(vo.getId());
		passenger.setOwnerOrder(o);

		baseDao.save(passenger);
		
		return passenger.getId();
	}
	*/
	
	@Override
	public String alipayPassengerOrderInfo(OwnerOrderVo vo) {
		OwnerOrderPublishPassenger oop = baseDao.getById(OwnerOrderPublishPassenger.class, vo.getPublishId());
		AliPayBizContent content = new AliPayBizContent();
		//XXX 判断发布时间
		content.setTimeout_express("15m");
		content.setProduct_code("QUICK_MSECURITY_PAY");
		//订单号
		long orderId = SnowFlakeIdGenerator.getInstance().generateLongId();
		oop.setOrderNumber(orderId);
		BigDecimal totalMoney = oop.getMoney().multiply(new BigDecimal(oop.getSeat()).setScale(2, RoundingMode.HALF_UP));
totalMoney = new BigDecimal("0.01");		
		content.setTotal_amount(totalMoney.toString());
		content.setSubject("往返拼车");
		content.setBody("支付宝乘客支付车主车单获取订单信息");		
		content.setOut_trade_no(orderId + ""); 
		//获取支付订单信息并保存到支付信息表
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/ownerorder/alipayPassenger";
		String notifyUrl = "http://app.wangfanpinche.com:8889/ownerorder/alipayPassenger";
		String orderInfo = AlipayAppUtils.buildOrderInfo(content, notifyUrl);
		//支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(orderInfo);
		baseDao.save(pay);
		//账单
		Journal j = new Journal();
		j.setType(BizType.乘客支付车主车单);
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.支付宝);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);		
		j.setOrderNumber(orderId);
		j.setMoney(totalMoney);
		j.setUser(new User(vo.getUserId()));
		j.setPayInfo(pay);
		baseDao.save(j);
		
		return orderInfo;
	}

	@Override
	public void alipayPassenger(Map<String, String> map) {
		//1.判断计算金额和支付金额是否一致  
		//订单号
		String orderNumber = map.get("out_trade_no");
		String hql = " from OwnerOrderPublishPassenger where deleted = ? and orderNumber = ? ";
		OwnerOrderPublishPassenger oop = baseDao.get(OwnerOrderPublishPassenger.class, hql, false, Long.parseLong(orderNumber));		
		BigDecimal totalMoney = oop.getMoney().multiply(new BigDecimal(oop.getSeat()).setScale(2, RoundingMode.HALF_UP));
totalMoney = new BigDecimal("0.01");
		//支付宝返回的支付金额
		BigDecimal receipt_amount = new BigDecimal(map.get("receipt_amount"));
		if (!totalMoney.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.payInfo where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(orderNumber));
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);		
		j.getPayInfo().setNotifyBody(JSON.toJSONString(map));
		//支付结果-更改乘客支付状态
		oop.setPayStatus(true);
		oop.setPayDateTime(LocalDateTime.now());
	}
	
	@Override
	public String wechatPayPassengerOrderInfo(OwnerOrderVo vo) {
		OwnerOrderPublishPassenger oop = baseDao.getById(OwnerOrderPublishPassenger.class, vo.getPublishId());
		long orderId = SnowFlakeIdGenerator.getInstance().generateLongId();
		oop.setOrderNumber(orderId);
		
		WechatPreOrder p = new WechatPreOrder();
		p.setOut_trade_no(orderId + "");
		//按照分来计算，10000就是100元，1就是0.01元
		BigDecimal totalMoney = oop.getMoney().multiply(new BigDecimal(oop.getSeat())).multiply(new BigDecimal("100"));
totalMoney = new BigDecimal("1");
		p.setTotal_fee(totalMoney.intValue());
		p.setSpbill_create_ip(vo.getIp());
		p.setTime_expire(LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/ownerorder/wechatPayPassenger";
		String notifyUrl = "http://app.wangfanpinche.com:8889/ownerorder/wechatPayPassenger";
		p.setNotify_url(notifyUrl);
		p.setBody("微信乘客支付车主车单获取订单信息");
		
		Map<String, Object> params = new HashMap<>();
		params.put("hello", "world");
		params.put("hello1", 1);
		p.setDetail(JSON.toJSONString(params));
		
		String preOrder = WechatPayAppUtils.preOrder(p);
		
		//支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(preOrder);
		baseDao.save(pay);
		//账单
		Journal j = new Journal();
		j.setType(BizType.乘客支付车主车单);
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.微信);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);		
		j.setOrderNumber(orderId);
		j.setMoney(totalMoney.divide(new BigDecimal("100")));
		j.setUser(new User(vo.getUserId()));
		j.setPayInfo(pay);
		baseDao.save(j);
		
		try {
			XmlMapper mapper = new XmlMapper();
			HashMap<String, String> readValue = mapper.readValue(preOrder, HashMap.class);
			WeChat we = new WeChat();
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return preOrder;
	}

	@Override
	public void wechatPayPassenger(WechatPayNotify notify) {
		String orderNumber = notify.getOut_trade_no();
		String hql = " from OwnerOrderPublishPassenger where deleted = ? and orderNumber = ? ";
		OwnerOrderPublishPassenger oop = baseDao.get(OwnerOrderPublishPassenger.class, hql, false, Long.parseLong(orderNumber));		
		BigDecimal totalMoney = oop.getMoney().multiply(new BigDecimal(oop.getSeat())).multiply(new BigDecimal("100"));
totalMoney = new BigDecimal("1");
		Integer total_amount = totalMoney.intValue();
		//微信返回的支付金额
		Integer receipt_amount = Integer.valueOf(notify.getTotal_fee());
		if (!total_amount.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.payInfo where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(orderNumber));
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		j.getPayInfo().setNotifyBody(JSON.toJSONString(notify));
		//支付结果-更改乘客支付状态
		oop.setPayStatus(true);
		oop.setPayDateTime(LocalDateTime.now());
	}
	
	@Override
	public void approvePassenger(OwnerOrderVo vo) {
		
		// 1.判断车单是不是publish, 乘客是不是已支付，没同意和拒绝，不能是乘客关闭的。剩余座位数改变
		String hql = " from OwnerOrderPublishPassenger p left join fetch p.ownerOrder po where p.id = ? ";
		OwnerOrderPublishPassenger p = baseDao.get(OwnerOrderPublishPassenger.class, hql, vo.getPublishId());
		if(!p.getOwnerOrder().getStatus().equals(Status.PUBLISH)){
			throw new RuntimeException("车单当前状态不是发布车单状态,不能同意搭载乘客!");
		}
		
		if(p.getPayStatus() == false){
			throw new RuntimeException("乘客还没有支付车单，您不能同意!");
		}
		if(p.getApproveStatus() == true){
			throw new RuntimeException("您已经同意该乘客!");
		}
		if (p.getDisapproveStatus() == true) {
			throw new RuntimeException("您已经拒绝该乘客!");
		}
		if (p.getClosedStatus() == true) {
			throw new RuntimeException("乘客已经关闭车单，您不能同意!");
		}
		//如果是乘客主动找车主，车主同意之后乘客车单车主增加
		if (p.getType().equals(Type.PASSENGER)) {
			p.getPassengerOrder().setApproveOwner(p.getOwnerOrder());
		}
		
		LocalDateTime now = LocalDateTime.now();
		String hqlapprove = " update OwnerOrderPublishPassenger set approveStatus = ?, approveDateTime = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlapprove, true, now, now, false, p.getId());
		String hqlown = " update OwnerOrder set currentSeat = currentSeat - ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlown, p.getSeat(), now, false, p.getOwnerOrder().getId());
	}

	@Override
	public void disapprovePassenger(OwnerOrderVo vo) {
		// 1.判断车单是不是publish, 乘客是不是已支付，没同意和拒绝，不能是乘客关闭的。
		String hql = " from OwnerOrderPublishPassenger p left join fetch p.ownerOrder po where p.id = ? ";
		OwnerOrderPublishPassenger p = baseDao.get(OwnerOrderPublishPassenger.class, hql, vo.getPublishId());
		if(!p.getOwnerOrder().getStatus().equals(Status.PUBLISH)){
			throw new RuntimeException("车单当前状态不是发布车单状态,不能拒绝搭载乘客!");
		}
		
		if(p.getPayStatus() == false){
			throw new RuntimeException("乘客还没有支付车单，您不能拒绝!");
		}
		if(p.getApproveStatus() == true){
			throw new RuntimeException("您已经同意该乘客!");
		}
		if (p.getDisapproveStatus() == true) {
			throw new RuntimeException("您已经拒绝该乘客!");
		}
		if (p.getClosedStatus() == true) {
			throw new RuntimeException("乘客已经关闭车单，您不能拒绝!");
		}
		
		LocalDateTime now = LocalDateTime.now();
		String hqlapprove = " update OwnerOrderPublishPassenger set disapproveStatus = ?, disapproveDateTime = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlapprove, true, now, now, false, vo.getPublishId());
	}

	@Override
	public void passengerClosed(OwnerOrderVo vo) {
		// 车单是否是发布车单状态
		String hql = " from OwnerOrderPublishPassenger p left join fetch p.ownerOrder po left join fetch p.passengerOrder pp where p.id = ? ";
		OwnerOrderPublishPassenger p = baseDao.get(OwnerOrderPublishPassenger.class, hql, vo.getPublishId());
		if(!p.getOwnerOrder().getStatus().equals(Status.PUBLISH)){
			throw new RuntimeException("车单当前状态不是发布车单状态,不能关闭车单!");
		}
		//乘客车单状态改变
		p.getPassengerOrder().setStatus(Status.CLOSED);
		
		LocalDateTime now = LocalDateTime.now();
		String hqlClose = " update OwnerOrderPublishPassenger set closedStatus = ?, closedDateTime = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlClose, true, now, now, false, vo.getPublishId());
		// XXX 退款,支付过的乘客退钱，车单当前座位数增加
		if (p.getPayStatus() == true) {
			String hqltype = " from Journal where InOutType = ? and status = ? and orderNumber = ? ";
			Journal j = baseDao.get(Journal.class, hqltype, InOutType.收入, com.wangfanpinche.dto.Journal.Status.完成, p.getOrderNumber());
			if (j.getPayType().equals(PayType.支付宝)) {
				AlipayAppUtils.refund(p.getOrderNumber().toString(), j.getMoney().toString());
			}
			if (j.getPayType().equals(PayType.微信)) {
				WechatPayAppUtils.refound(p.getOrderNumber(), j.getMoney().multiply(new BigDecimal("100")).intValue(), j.getMoney().multiply(new BigDecimal("100")).intValue());
			}
		}
		// 乘客关闭之后,车主同意的乘客的座位数退回到车主车单（增加剩余座位数）
		if (p.getApproveStatus() == true) {
			String hqlseat = " update OwnerOrder set currentSeat = currentSeat + ?, modifyDateTime = ? where deleted = ? and id = ? ";
			baseDao.execute(hqlseat, p.getSeat(), now, false, p.getOwnerOrder().getId());
		}
	}

	@Override
	public void ownerClosed(OwnerOrderVo vo) {
		// 车单是否是发布车单状态 ，车单状态改变
		String hql = "select new com.wangfanpinche.vo.OwnerOrderVo(o.id, o.status) from OwnerOrder o where o.deleted = ? and o.id = ? ";
		OwnerOrderVo ownerOrderVo = baseDao.get(OwnerOrderVo.class, hql, false, vo.getId());
		if (!ownerOrderVo.getStatus().equals(Status.PUBLISH)) {
			throw new RuntimeException("车单当前状态不是发布车单状态,不能关闭车单!");
		}
		
		LocalDateTime now = LocalDateTime.now();
		String hqlownClose = " update OwnerOrder set status = ?, modifyDateTime = ?  where deleted = ? and id = ? ";
		baseDao.execute(hqlownClose, Status.CLOSED, LocalDateTime.now(), false, vo.getId());
		String hqlpublish = " update OwnerOrderPublish set closedDateTime = ?, modifyDateTime = ? where deleted = ? and ownerOrder.id = ? ";
		baseDao.execute(hqlpublish, now, now, false, vo.getId());
		// XXX 退款,支付过的乘客退钱，乘客状态改变
		String hqlpass = " from OwnerOrderPublishPassenger op left join fetch op.passengerOrder p where op.deleted = ? and op.ownerOrder.id = ? ";
		List<OwnerOrderPublishPassenger> passList = baseDao.find(OwnerOrderPublishPassenger.class, hqlpass, false, vo.getId());
		if (passList.size() > 0) {
			for (OwnerOrderPublishPassenger pass : passList) {
				if (pass.getPayStatus() == true) {
					String hqltype = " from Journal where InOutType = ? and status = ? and orderNumber = ? ";
					Journal j = baseDao.get(Journal.class, hqltype, InOutType.收入, com.wangfanpinche.dto.Journal.Status.完成, pass.getOrderNumber());
					if (j.getPayType().equals(PayType.支付宝)) {
						AlipayAppUtils.refund(pass.getOrderNumber().toString(), j.getMoney().toString());
					}
					if (j.getPayType().equals(PayType.微信)) {
						WechatPayAppUtils.refound(pass.getOrderNumber(), j.getMoney().multiply(new BigDecimal("100")).intValue(), j.getMoney().multiply(new BigDecimal("100")).intValue());
					}
				}
				String hqlup = " update OwnerOrderPublishPassenger set closedStatus = ?, closedDateTime = ?, modifyDateTime = ? where deleted = ? and id = ? ";
				baseDao.execute(hqlup, true, now, now, false, pass.getId());
				//乘客车单状态改变
				pass.getPassengerOrder().setStatus(Status.CLOSED);
			}
		}
		

	}

	@Override
	public PassengerListVo passengerList(OwnerOrderVo vo) {
		String hql = "select new com.wangfanpinche.vo.OwnerOrderVo(o.id, o.status) from OwnerOrder o where o.deleted = ? and o.id = ? ";
		OwnerOrderVo ownerOrderVo = baseDao.get(OwnerOrderVo.class, hql, false, vo.getId());		
		PassengerListVo passVo = new PassengerListVo();		
		if (ownerOrderVo.getStatus().equals(Status.PUBLISH)) {
			passVo.setStatus(Status.PUBLISH);
			String hqlpublish = " select new com.wangfanpinche.vo.PassengerListPublishVo(p.id, p.payStatus, p.approveStatus, p.disapproveStatus, p.closedStatus, f.city, f.sematicDescription, t.city, t.sematicDescription, p.money, p.seat, p.descript, u.id, u.userIcon, u.mobilePhone, u.username) from OwnerOrderPublishPassenger p left join p.fromLocation f left join p.toLocation t left join p.user u where p.deleted = ? and p.ownerOrder.id = ? ";
			List<PassengerListPublishVo> publish = baseDao.find(PassengerListPublishVo.class, hqlpublish, false, vo.getId());
			passVo.setPublish(publish);
		}
		if (ownerOrderVo.getStatus().equals(Status.RECEIVE)) {
			passVo.setStatus(Status.RECEIVE);
			String hqlreceive = " select new com.wangfanpinche.vo.PassengerListReceiveVo(p.id, p.goinStatus, g.city, g.sematicDescription, u.id, u.userIcon, u.mobilePhone, u.username) from OwnerOrderReceivePassenger p left join p.receive r left join p.goinLocation g left join p.user u where p.deleted = ? and r.ownerOrder.id = ? ";
			List<PassengerListReceiveVo> receive = baseDao.find(PassengerListReceiveVo.class, hqlreceive, false, vo.getId());
			passVo.setReceive(receive);
		}
		if (ownerOrderVo.getStatus().equals(Status.DEPART)) {
			passVo.setStatus(Status.DEPART);
			String hqldepart = " select new com.wangfanpinche.vo.PassengerListDepartVo(p.id, p.gooutStatus, g.city, g.sematicDescription, u.id, u.userIcon, u.mobilePhone, u.username) from OwnerOrderDepartPassenger p left join p.depart d left join p.gooutLocation g left join p.user u where p.deleted = ? and d.ownerOrder.id = ? ";
			List<PassengerListDepartVo> depart = baseDao.find(PassengerListDepartVo.class, hqldepart, false, vo.getId());
			passVo.setDepart(depart);
		}
		
		return passVo;
	}

	@Override
	public void receive(OwnerOrderVo vo) {
		// 1. 判断车单是不是publish, 有没有同意或拒绝
		String hql = "select new com.wangfanpinche.vo.OwnerOrderVo(o.id, o.status) from OwnerOrder o where o.deleted = ? and o.id = ? ";
		OwnerOrderVo ownerOrderVo = baseDao.get(OwnerOrderVo.class, hql, false, vo.getId());
		if (!ownerOrderVo.getStatus().equals(Status.PUBLISH)) {
			throw new RuntimeException("车单当前状态不是发布车单状态,不能去接乘客!");
		}
		// 支付过之后，并且 同意和拒绝都没有点
		String hqlpass = "select count(id) from OwnerOrderPublishPassenger where deleted = ? and ownerOrder.id = ? and payStatus = ? and approveStatus = ? and disapproveStatus = ? ";
		Long count = baseDao.count(hqlpass, false, vo.getId(), true, false, false);
		if (count > 0) {
			throw new RuntimeException("有[" + count + "]个乘客您还没有同意或拒绝搭载，不能去接乘客!");
		}
		//更改乘客车单状态
		PassengerOrder p = baseDao.get(PassengerOrder.class, " from PassengerOrder where deleted = ? and approveOwner.id = ? ", false, ownerOrderVo.getId());
		p.setStatus(Status.RECEIVE);
		// 2. 更改车单状态
		String hqlup = " update OwnerOrder set status = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlup, Status.RECEIVE, LocalDateTime.now(), false, ownerOrderVo.getId());

		OwnerOrderReceive ownerOrderReceive = new OwnerOrderReceive();
		OwnerOrder o = new OwnerOrder();
		o.setId(ownerOrderVo.getId());
		ownerOrderReceive.setOwnerOrder(o);
		baseDao.save(ownerOrderReceive);
		// 3. 支付过和同意的乘客继续流程
		String hqlapprove = " from OwnerOrderPublishPassenger p left join fetch p.user where p.deleted = ? and p.ownerOrder.id = ? and p.payStatus = ? and p.approveStatus = ? ";
		List<OwnerOrderPublishPassenger> publishPassengers = baseDao.find(OwnerOrderPublishPassenger.class, hqlapprove, false, ownerOrderReceive.getOwnerOrder().getId(), true, true);
		for (OwnerOrderPublishPassenger publishPassenger : publishPassengers) {
			OwnerOrderReceivePassenger receivePassenger = new OwnerOrderReceivePassenger();
			receivePassenger.setUser(publishPassenger.getUser());
			receivePassenger.setReceive(ownerOrderReceive);
			baseDao.save(receivePassenger);
		}
	}

	@Override
	public void userReceive(OwnerOrderVo vo) {
		// 1.判断车单是不是receive
		String hql = " from OwnerOrderReceivePassenger p left join fetch p.receive r left join fetch p.receive.ownerOrder po where p.deleted = ? and p.id = ? ";
		OwnerOrderReceivePassenger receivePassenger = baseDao.get(OwnerOrderReceivePassenger.class, hql, false, vo.getReceiveId());
		if(!receivePassenger.getReceive().getOwnerOrder().getStatus().equals(Status.RECEIVE)){
			throw new RuntimeException("车单当前状态不是接乘客状态,不能上车!");
		}
		// 2.某个乘客上车 ,更新上车地点
		OwnerLocation goinLocation = gpsToBean(vo.getGoinLng(), vo.getGoinLat());
		baseDao.persist(goinLocation);
		receivePassenger.setGoinLocation(goinLocation);
		receivePassenger.setGoinStatus(true);
		receivePassenger.setGoinDateTime(LocalDateTime.now());
		baseDao.update(receivePassenger);
	}		

	@Override
	public void depart(OwnerOrderVo vo) {
		// 1.判断车单是不是receive
		String hql = "select new com.wangfanpinche.vo.OwnerOrderVo(o.id, o.status) from OwnerOrder o where o.deleted = ? and o.id = ? ";
		OwnerOrderVo ownerOrderVo = baseDao.get(OwnerOrderVo.class, hql, false, vo.getId());
		if (!ownerOrderVo.getStatus().equals(Status.RECEIVE)) {
			throw new RuntimeException("车单当前状态不是接乘客状态,不能去送乘客!");
		}
		// 2.判断所有乘客是否已经上车
		String hqlreceive = " select count(rp.id) from OwnerOrderReceivePassenger rp left join rp.receive o where rp.deleted = ? and o.ownerOrder.id = ? and rp.goinStatus = ? ";
		Long count = baseDao.count(hqlreceive, false, ownerOrderVo.getId(), false);
		if (count > 0) {
			throw new RuntimeException("有[" + count + "]个乘客还没有上车，不能去送乘客!");
		}
		//更改乘客车单状态
		PassengerOrder p = baseDao.get(PassengerOrder.class, " from PassengerOrder where deleted = ? and approveOwner.id = ? ", false, ownerOrderVo.getId());
		p.setStatus(Status.DEPART);
		// 3.更改车单状态，已经接上车的乘客继续流程,车单完成表增加一条数据
		String hqlup = " update OwnerOrder set status = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlup, Status.DEPART, LocalDateTime.now(), false, ownerOrderVo.getId());

		OwnerOrderDepart ownerOrderDepart = new OwnerOrderDepart();
		OwnerOrder o = new OwnerOrder();
		o.setId(vo.getId());
		ownerOrderDepart.setOwnerOrder(o); 
		baseDao.save(ownerOrderDepart);
		
		String hqldepart = "select rp from OwnerOrderReceivePassenger rp left join rp.user u where rp.deleted = ? and rp.receive.ownerOrder.id = ? and rp.goinStatus = ? ";
		List<OwnerOrderReceivePassenger> receivePassengers = baseDao.find(OwnerOrderReceivePassenger.class, hqldepart, false, ownerOrderVo.getId(), true);
		for (OwnerOrderReceivePassenger receivePassenger : receivePassengers) {
			OwnerOrderDepartPassenger departPassenger = new OwnerOrderDepartPassenger();
			departPassenger.setUser(receivePassenger.getUser());
			departPassenger.setDepart(ownerOrderDepart);
			baseDao.save(departPassenger);
		}

		OwnerOrderFinish ownerOrderFinish = new OwnerOrderFinish();
		ownerOrderFinish.setOwnerOrder(o);
		ownerOrderFinish.setStatus(FinishStatus.NO_FINISH);
		baseDao.save(ownerOrderFinish);
	}

	@Override
	public void userFinish(OwnerOrderVo vo) {
		// 1.判断车单是不是receive		
		String hql = " from OwnerOrderDepartPassenger p left join fetch p.depart d left join fetch p.depart.ownerOrder o where p.deleted = ? and p.id = ? ";
		OwnerOrderDepartPassenger departPassenger = baseDao.get(OwnerOrderDepartPassenger.class, hql, false, vo.getDepartId());
		if (!departPassenger.getDepart().getOwnerOrder().getStatus().equals(Status.DEPART)) {
			throw new RuntimeException("车单当前状态不是送乘客状态,不能完成!");
		}
		//更改乘客车单状态
		PassengerOrder p = baseDao.get(PassengerOrder.class, " from PassengerOrder where deleted = ? and approveOwner.id = ? ", false, departPassenger.getDepart().getOwnerOrder().getId());
		p.setStatus(Status.FINISH);
		// 2.乘客下车完成 ,更新下车地点
		OwnerLocation gooutLocation = gpsToBean(vo.getGooutLng(), vo.getGooutLat());
		baseDao.persist(gooutLocation);
		departPassenger.setGooutLocation(gooutLocation);
		departPassenger.setGooutStatus(true);
		departPassenger.setGooutDateTime(LocalDateTime.now());
		baseDao.update(departPassenger);
	}

	@Override
	public void userRated(OwnerOrderVo vo) {
		OwnerOrderDepartPassenger departPassenger = baseDao.get(OwnerOrderDepartPassenger.class, " from OwnerOrderDepartPassenger where deleted = ? and id = ? ", false, vo.getDepartId());
		if (departPassenger.getGooutStatus() == false) {
			throw new RuntimeException("您还没有下车，不能评价车主");
		}
		//乘客评价车主
		OwnerOrderFinishOwnerEvaluate passOwner = new OwnerOrderFinishOwnerEvaluate();
		if (vo.getDescriptions() == null) {
			passOwner.setDescription(Double.valueOf(5));
		}else{
			passOwner.setDescription(vo.getDescriptions());
		}
		
		if (vo.getService() == null) {
			passOwner.setService(Double.valueOf(5));
		}else{
			passOwner.setService(vo.getService());
		}
		
		if (vo.getTrustworthy() == null) {
			passOwner.setTrustworthy(Double.valueOf(5));
		}else{
			passOwner.setTrustworthy(vo.getTrustworthy());
		}
		
		if (StringUtils.hasText(vo.getContent())) {
			passOwner.setContent(vo.getContent());
		}
		passOwner.setFromUser(new User(vo.getUserId()));
		passOwner.setToUser(new User(vo.getToUserId()));
		baseDao.save(passOwner);
	}

	@Override
	public void ownerRated(OwnerOrderVo vo) {
		// 1.判断车单是不是finish
		String hql = "select new com.wangfanpinche.vo.OwnerOrderVo(o.id, o.status) from OwnerOrder o where o.deleted = ? and o.id = ? ";
		OwnerOrderVo ownerOrderVo = baseDao.get(OwnerOrderVo.class, hql, false, vo.getId());
		if (!ownerOrderVo.getStatus().equals(Status.FINISH)) {
			throw new RuntimeException("车单当前状态不是完成状态,不能评价!");
		}
		//车主评价乘客
		OwnerOrderFinishPassengerEvaluate ownerPass = new OwnerOrderFinishPassengerEvaluate();
		//司机给用户评价的标签有值就存
		List<String> tagList = vo.getTags();
		if (tagList != null && tagList.size() > 0) {
			ownerPass.setTags(tagList);
		}
		ownerPass.setFromUser(new User(vo.getUserId()));
		ownerPass.setToUser(new User(vo.getToUserId()));
		baseDao.save(ownerPass);
	}
	
	@Override
	public void finish(OwnerOrderVo vo) { 
		String hql = "select count(p.id) from OwnerOrderDepartPassenger p left join p.depart d where p.deleted = ? and d.ownerOrder.id = ? and p.gooutStatus = ? ";
		Long count = baseDao.count(hql, false, vo.getId(), false);
		if (count > 0) {
			throw new RuntimeException("有[" + count + "]个乘客还没有下车，您不能结束行程!");
		}
		// XXX 查询当前车单的乘客的总座位数和支付的总金额，然后给车主余额
		String hqlapprove = " select new com.wangfanpinche.vo.OwnerOrderVo(p.seat, p.money) from OwnerOrderPublishPassenger p where p.deleted = ? and p.ownerOrder.id = ? and p.payStatus = ? and p.approveStatus = ? ";
		List<OwnerOrderVo> ovoList = baseDao.find(OwnerOrderVo.class, hqlapprove, false, vo.getId(), true, true);
		Integer passengerSeat = 0;
		BigDecimal allfare = new BigDecimal("0");
		if (ovoList.size() > 0) {
			for (OwnerOrderVo ovo : ovoList) {
				passengerSeat = passengerSeat + ovo.getPassengerSeat();
				allfare = allfare.add(ovo.getAllfare().multiply(new BigDecimal(ovo.getPassengerSeat())));
			}
		}
					
		LocalDateTime now = LocalDateTime.now();
		String hqluser = " update User set balance = balance + ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqluser, allfare, now, false, vo.getUserId());
		// XXX 车单完成时诚信必发的钱退款(支付宝或微信)
		OwnerOrder o = baseDao.getById(OwnerOrder.class, vo.getId());
		if (o.getHonesty() == true) {
			String hqltype = " from Journal where type = ? and InOutType = ? and status = ? and orderNumber = ? ";
			Journal j = baseDao.get(Journal.class, hqltype, BizType.诚信必发, InOutType.收入, com.wangfanpinche.dto.Journal.Status.完成, o.getOrderNumber());
			if (j.getPayType().equals(PayType.支付宝)) {
				String refund_amount = j.getMoney().toString();
				AlipayAppUtils.refund(o.getOrderNumber().toString(), refund_amount);
			}
			if (j.getPayType().equals(PayType.微信)) {
				WechatPayAppUtils.refound(o.getOrderNumber(), j.getMoney().multiply(new BigDecimal("100")).intValue(), j.getMoney().multiply(new BigDecimal("100")).intValue());
			}
			
		}
		String hqlup = " update OwnerOrderFinish set status = ?, modifyDateTime = ?, passengerSeat = ?, allfare = ? where deleted = ? and ownerOrder.id = ? ";
		baseDao.execute(hqlup, FinishStatus.FINISH, now, passengerSeat, allfare, false, vo.getId());
		//车单状态改变 - 完成
		String hqlorder = " update OwnerOrder set status = ?, modifyDateTime = ? where deleted = ? and id = ?　";
		baseDao.execute(hqlorder, Status.FINISH, now, false, vo.getId());
		
	}

	@Override
	public PassengerListVo running(OwnerOrderVo vo) {
		String hql = "select new com.wangfanpinche.vo.OwnerOrderVo( o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.status) from OwnerOrder o left join o.fromLocation f left join o.toLocation t where o.deleted = ? and o.id = ? ";
		OwnerOrderVo ownerOrderVo = baseDao.get(OwnerOrderVo.class, hql, false, vo.getId());
				
		PassengerListVo passVo = new PassengerListVo();
		BeanUtils.copyProperties(ownerOrderVo, passVo);
		
		if (ownerOrderVo.getStatus().equals(Status.PUBLISH)) {
			passVo.setStatus(Status.PUBLISH);
			String hqlpublish = " select new com.wangfanpinche.vo.PassengerListPublishVo(p.id, p.payStatus, p.approveStatus, p.disapproveStatus, p.closedStatus, f.city, f.sematicDescription, t.city, t.sematicDescription, p.money, p.seat, p.descript, u.id, u.userIcon, u.mobilePhone, u.username) from OwnerOrderPublishPassenger p left join p.fromLocation f left join p.toLocation t left join p.user u where p.deleted = ? and p.ownerOrder.id = ? ";
			List<PassengerListPublishVo> publish = baseDao.find(PassengerListPublishVo.class, hqlpublish, false, vo.getId());
			passVo.setPublish(publish);
		}
		if (ownerOrderVo.getStatus().equals(Status.RECEIVE)) {
			passVo.setStatus(Status.RECEIVE);
			String hqlreceive = " select new com.wangfanpinche.vo.PassengerListReceiveVo(p.id, p.goinStatus, g.city, g.sematicDescription, u.id, u.userIcon, u.mobilePhone, u.username) from OwnerOrderReceivePassenger p left join p.receive r left join p.goinLocation g left join p.user u where p.deleted = ? and r.ownerOrder.id = ? ";
			List<PassengerListReceiveVo> receive = baseDao.find(PassengerListReceiveVo.class, hqlreceive, false, vo.getId());
			passVo.setReceive(receive);
		}
		if (ownerOrderVo.getStatus().equals(Status.DEPART)) {
			passVo.setStatus(Status.DEPART);
			String hqldepart = " select new com.wangfanpinche.vo.PassengerListDepartVo(p.id, p.gooutStatus, g.city, g.sematicDescription, u.id, u.userIcon, u.mobilePhone, u.username) from OwnerOrderDepartPassenger p left join p.depart d left join p.gooutLocation g left join p.user u where p.deleted = ? and d.ownerOrder.id = ? ";
			List<PassengerListDepartVo> depart = baseDao.find(PassengerListDepartVo.class, hqldepart, false, vo.getId());
			passVo.setDepart(depart);
		}
		return passVo;
	}

	@Override
	public OwnerOrderVo ownerfindPassInfo(OwnerOrderVo vo) {
		//车主查看乘客信息
		String hql = " select new com.wangfanpinche.vo.UserVo( u.username, u.mobilePhone, u.userIcon, u.homeAddress, u.status) from User u where u.deleted = ? and u.id = ? ";
		UserVo uvo = baseDao.get(UserVo.class, hql, false, vo.getUserId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(uvo, vo, "username","mobilePhone","userIcon","homeAddress");
		vo.setUserstatus(uvo.getStatus());
		//拼车次数
		String hqlototal = " select count(id) from OwnerOrder where deleted = ? and user.id = ? and status = ? ";
		String hqlptotal = " select count(id) from PassengerOrder where deleted = ? and user.id = ? and status = ? ";
		Long total = baseDao.count(hqlototal, false, vo.getUserId(), Status.FINISH) + baseDao.count(hqlptotal, false, vo.getUserId(), Status.FINISH);
		vo.setTotal(total);
		//评价乘客的标签
		String sql = "select count(t.tags) count,t.tags from t_ownerorderfinishpassengerevaluate p left join t_ownerorderfinishpassengerevaluate_tag t on p.ID = t.OwnerOrderFinishPassengerEvaluate_ID where p.DELETED = :deleted and p.toUser_ID = :userId GROUP BY t.tags ";
		Map<String , Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("userId", vo.getUserId());
		List<String> passList = baseDao.findBySql(sql, params);
		if (passList != null && passList.size() > 0) {			
			vo.setTags(passList);
		}
		return vo;
	}

	@Override
	public OwnerOrderVo passfindOwnerInfo(OwnerOrderVo vo) {
		//乘客查看车主信息
		String hql = " select new com.wangfanpinche.vo.UserVo( u.username, u.mobilePhone, u.userIcon, u.status, o.status, u.homeAddress, c.id, c.carColor, c.carNumber, ci.carmodel, c.carInfoId) from User u left join OwnerApprove o on u.id = o.userId left join Car c on u.id = c.userId left join CarInfo ci on c.carInfoId = ci.id where u.deleted = ? and u.id = ? ";
		UserVo uvo = baseDao.get(UserVo.class, hql, false, vo.getUserId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(uvo, vo, "username","mobilePhone","userIcon","homeAddress","carColor","carNumber","carmodel","carInfoId");
		vo.setUserstatus(uvo.getStatus());
		vo.setOwnerstatus(uvo.getOstatus());
		CarVo cvo = userService.getCarByCarInfoId(uvo.getCarInfoId());
		vo.setBrandSystem(cvo.getBrandName()+" "+cvo.getSystemName());
		//拼车次数
		String hqlototal = " select count(id) from OwnerOrder where deleted = ? and user.id = ? and status = ? ";
		String hqlptotal = " select count(id) from PassengerOrder where deleted = ? and user.id = ? and status = ? ";
		Long total = baseDao.count(hqlototal, false, vo.getUserId(), Status.FINISH) + baseDao.count(hqlptotal, false, vo.getUserId(), Status.FINISH);
		vo.setTotal(total);
		//评价车主的平均分
		String hqlAvg = " select new com.wangfanpinche.vo.OwnerOrderVo(avg(description), avg(trustworthy), avg(service)) from OwnerOrderFinishOwnerEvaluate where deleted = ? and toUser.id = ? ";
		OwnerOrderVo rvo = baseDao.get(OwnerOrderVo.class, hqlAvg, false, vo.getUserId());
		if (rvo.getDescriptions() == null) {
			vo.setDescriptions(5.0);
			vo.setTrustworthy(5.0);
			vo.setService(5.0);
		}else{
			vo.setDescriptions(rvo.getDescriptions());
			vo.setTrustworthy(rvo.getTrustworthy());
			vo.setService(rvo.getService());
		}
		return vo;
	}

	@Override
	public List<OwnerOrderVo> getOwnerOrderByUserId(OwnerOrderVo vo) {
		//根据用户id返回publish的车单信息(id,开始城市，开始城市描述，结束城市，结束城市描述，出发时间，剩余座位数)
		String hql = " select new com.wangfanpinche.vo.OwnerOrderVo(o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat) from OwnerOrder o left join o.fromLocation f left join o.toLocation t where o.deleted = ? and o.user.id = ? and o.status = ? ";
		List<OwnerOrderVo> owner = baseDao.find(OwnerOrderVo.class, hql, false, vo.getUserId(), Status.PUBLISH);
		return owner;
	}
	
	@Override
	public OwnerOrderVo ownerOrderInfo(OwnerOrderVo vo){
		//乘客查询车主车单详情(id,开始城市，开始城市描述，结束城市，结束城市描述，出发时间，剩余座位数,途径，备注,起点经纬度，终点经纬度)
		Map<String, Object> params = new HashMap<>();
		params.put("id", vo.getId());
		String hql = " select new com.wangfanpinche.vo.OwnerOrderVo(o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.via, o.description, f.lng, f.lat, t.lng, t.lat) from OwnerOrder o left join o.fromLocation f left join o.toLocation t where o.deleted = ? and o.id = ? ";
		OwnerOrderVo owner = baseDao.get(OwnerOrderVo.class, hql, false, vo.getId());
		String sql = " select t.tagName from t_ownerorder_tag o left join t_tag t on o.carTags = t.ID where o.OwnerOrder_ID = :id ";
		List<String> carTags = baseDao.findBySql(sql, params);
		if (carTags != null && carTags.size() > 0) {
			owner.setCarTags(carTags);
		}
		return owner;
	}
	
	@Override
	public List<OwnerOrderVo> recommendOwner(PassengerOrderVo vo){
		//给乘客推荐的车主车单列表 * 起始城市 * 结束城市 * 大于当前时间 * 坐位数判断  剩余的座位数不能小于目前所
		PassengerOrder p = baseDao.getById(PassengerOrder.class, vo.getId());
		String hql = " select new com.wangfanpinche.vo.OwnerOrderVo(o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.fare, o.honesty, u.id, u.userIcon, u.mobilePhone, u.username, u.status, o.brandSystem) from OwnerOrder o left join o.fromLocation f left join o.toLocation t left join o.user u where o.deleted = :deleted and o.fromLocation.city = :fromCity and o.toLocation.city = :toCity and o.departDateTime > :now and o.currentSeat >= :currentSeat ";
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("fromCity", p.getFromLocation().getCity());
		params.put("toCity", p.getToLocation().getCity());
		LocalDateTime now = LocalDateTime.now();
		params.put("now", now);
		params.put("currentSeat", p.getSeat());
		List<OwnerOrderVo> oList = baseDao.find(OwnerOrderVo.class, hql, params);
		return oList;
	}

}
