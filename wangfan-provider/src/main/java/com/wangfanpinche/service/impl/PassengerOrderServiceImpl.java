package com.wangfanpinche.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Journal;
import com.wangfanpinche.dto.OwnerLocation;
import com.wangfanpinche.dto.OwnerOrder;
import com.wangfanpinche.dto.OwnerOrder.Status;
import com.wangfanpinche.dto.OwnerOrderPublishPassenger;
import com.wangfanpinche.dto.OwnerOrderPublishPassenger.Type;
import com.wangfanpinche.dto.PassengerOrder;
import com.wangfanpinche.dto.PayInfo;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.Journal.BizType;
import com.wangfanpinche.dto.Journal.InOutType;
import com.wangfanpinche.dto.Journal.PayType;
import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.provider.utils.BeanUtils;
import com.wangfanpinche.service.PassengerOrderService;
import com.wangfanpinche.utils.http.HttpUtils;
import com.wangfanpinche.utils.id.SnowFlakeIdGenerator;
import com.wangfanpinche.utils.map.BaiduMapUtils;
import com.wangfanpinche.utils.other.EncryptUtils;
import com.wangfanpinche.utils.pay.AliPayBizContent;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.utils.pay.WechatPreOrder;
import com.wangfanpinche.vo.OwnerOrderVo;
import com.wangfanpinche.vo.PassengerOrderVo;
import com.wangfanpinche.vo.WeChat;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class PassengerOrderServiceImpl implements PassengerOrderService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public String publish(PassengerOrderVo vo) {
		User user = baseDao.get(User.class, " from User where deleted = ? and id = ? ", false, vo.getUserId());
		if (!user.getStatus().equals(StatusEnum.认证通过)) {
			throw new RuntimeException("您还没有实名认证,不能发布车单!");
		}
		
		OwnerLocation fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
		OwnerLocation toLocation = gpsToBean(vo.getToLng(), vo.getToLat());
		PassengerOrder dto = new PassengerOrder();
		
		BeanUtils.copyProperties(vo, dto);
		dto.setFromLocation(fromLocation);
		dto.setToLocation(toLocation);
		dto.setStatus(OwnerOrder.Status.PUBLISH);
		dto.setUser(user);
		dto.setAmount(vo.getAmount().setScale(2, RoundingMode.HALF_UP));
		baseDao.save(fromLocation);
		baseDao.save(toLocation);
		baseDao.save(dto);
		return dto.getId();
	}
	
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
		content.setBody("支付宝乘客支付乘客车单获取订单信息");		
		content.setOut_trade_no(orderId + ""); 
		//获取支付订单信息并保存到支付信息表
		String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/passengerorder/alipayPassenger";
		//String notifyUrl = "http://app.wangfanpinche.com:8889/passengerorder/alipayPassenger";
		String orderInfo = AlipayAppUtils.buildOrderInfo(content, notifyUrl);
		//支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(orderInfo);
		baseDao.save(pay);
		//账单
		Journal j = new Journal();
		j.setType(BizType.乘客支付乘客车单);
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
	public String wechatPayPassengerOrderInfo(OwnerOrderVo vo){
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
		p.setBody("微信乘客支付乘客车单获取订单信息");
		
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
		j.setType(BizType.乘客支付乘客车单);
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
	public void wechatPayPassenger(WechatPayNotify notify){
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
	public String receive(PassengerOrderVo vo) {
		PassengerOrder p = baseDao.getById(PassengerOrder.class, vo.getId());
		if(!p.getStatus().equals(OwnerOrder.Status.PUBLISH)){
			throw new RuntimeException("乘客车单当前不是发布车单状态,不能抢单!");
		}
		if (vo.getUserId().equals(p.getUser().getId())) {
			throw new RuntimeException("您好,您不能对您自己发布的车单进行抢单操作!");
		}
		OwnerOrder oo = baseDao.getById(OwnerOrder.class, vo.getOwnerOrderId());
		if (oo.getCurrentSeat() < p.getSeat()) {
			throw new RuntimeException("您的车主车单剩余座位数不够,不能抢单!");
		}
		p.getOwners().add(oo);
		baseDao.update(p);
		return p.getId();
	}

	@Override
	public String aprove(PassengerOrderVo vo) {
		PassengerOrder p = baseDao.getById(PassengerOrder.class, vo.getId());

		if(p.getApproveOwner() != null){
			throw new RuntimeException("您已经有乘坐的车了,不能再同意别的车主车单了!");
		}
		
		if(!p.getStatus().equals(OwnerOrder.Status.PUBLISH)){
			throw new RuntimeException("您的乘客车单当前不是发布车单状态,不能同意!");
		}		
		Hibernate.initialize(p.getOwners());
		List<OwnerOrder> owners = p.getOwners();

		for (int i = 0; i < owners.size(); i++) {
			if(owners.get(i).getId().equalsIgnoreCase(vo.getOwnerOrderId())){
				p.setApproveOwner(owners.get(i));
				owners.remove(i);
				break;
			}
		}				
		
		//乘客同意乘坐某个车主车单，车主车单剩余座位数改变
		String hqlup = " update OwnerOrder set currentSeat = currentSeat - ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlup, p.getSeat(), LocalDateTime.now(), false, vo.getOwnerOrderId());
		
		//把这个加入车单表
		OwnerOrderPublishPassenger pp = new OwnerOrderPublishPassenger();
		pp.setApproveStatus(true);		
		pp.setMoney(p.getAmount());
		pp.setFromLocation(p.getFromLocation());
		pp.setToLocation(p.getToLocation());
		pp.setSeat(p.getSeat());
		if (StringUtils.hasText(p.getDescription())) {
			pp.setDescript(p.getDescription());
		}
		pp.setUser(p.getUser());
		pp.setType(Type.OWNER);//车主找乘客
		pp.setOwnerOrder(p.getApproveOwner());
		pp.setPassengerOrder(p);
		baseDao.save(pp);
		
		return pp.getId();
	}
	
	@Override
	public String passfindOwner(PassengerOrderVo vo){
		PassengerOrder p = baseDao.getById(PassengerOrder.class, vo.getId());
		OwnerOrder o = baseDao.getById(OwnerOrder.class, vo.getOwnerOrderId());
		if (!o.getStatus().equals(Status.PUBLISH)) {
			throw new RuntimeException("车主车单当前状态不是发布车单状态,不能下单!");
		}
		if (p.getSeat() > o.getCurrentSeat()) {
			throw new RuntimeException("您的座位数大于车单剩余座位数!");
		}		
		if (o.getUser().getId().equals(p.getUser().getId())) {
			throw new RuntimeException("您好,您不能对您自己发布的车单进行下单操作!");
		}
		
		// 验证，是否有未付款或未关闭的订单。  
		String hqlpass = " select count(id) from OwnerOrderPublishPassenger where deleted = ? and user.id = ? and ownerOrder.id != ? and payStatus = ? and closedStatus = ? ";
		Long count = baseDao.count(hqlpass, false, p.getUser().getId(), p.getId(), false, false);
		if (count > 0) {
			throw new RuntimeException("您还有未付款或未关闭的订单,不能下单!");
		}
		
		OwnerOrderPublishPassenger pp = new OwnerOrderPublishPassenger();		
		pp.setMoney(o.getFare());//乘客主动找车主车单，支付车主车单的车费
		pp.setFromLocation(p.getFromLocation());
		pp.setToLocation(p.getToLocation());
		pp.setSeat(p.getSeat());
		if (StringUtils.hasText(p.getDescription())) {
			pp.setDescript(p.getDescription());
		}
		pp.setUser(p.getUser());
		pp.setType(Type.PASSENGER);//乘客主动找车主
		pp.setOwnerOrder(o);
		pp.setPassengerOrder(p);
		baseDao.save(pp);
		return pp.getId();
	}


	@Override
	public PassengerOrderVo show(PassengerOrderVo vo) {
		PassengerOrderVo pvo = baseDao.get(PassengerOrderVo.class, " select new com.wangfanpinche.vo.PassengerOrderVo( p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.seat, p.publishDateTime, p.description, p.amount, p.status, a.id) from PassengerOrder p left join p.fromLocation f left join p.toLocation t left join p.approveOwner a where p.deleted = ? and p.id = ? ", false, vo.getId());
		PassengerOrder p = baseDao.getById(PassengerOrder.class, pvo.getId());		
		List<OwnerOrder> owners = p.getOwners();
		if (owners.size() > 0) {
			List<OwnerOrderVo> ownerList = new ArrayList<>();
			for (OwnerOrder ownerOrder : owners) {
				String hql = " select new com.wangfanpinche.vo.OwnerOrderVo( o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.fare, o.honesty, o.description, u.id, u.userIcon, u.mobilePhone, u.username, u.status, c.id, c.carColor, c.carNumber, o.brandSystem) from OwnerOrder o left join o.fromLocation f left join o.toLocation t left join o.user u left join o.car c where o.deleted = ? and o.id = ? ";
				OwnerOrderVo ovo = baseDao.get(OwnerOrderVo.class, hql, false, ownerOrder.getId());
				//拼车次数
				String hqlototal = " select count(id) from OwnerOrder where deleted = ? and user.id = ? and status = ? ";
				String hqlptotal = " select count(id) from PassengerOrder where deleted = ? and user.id = ? and status = ? ";
				Long total = baseDao.count(hqlototal, false, ovo.getUserId(), Status.FINISH) + baseDao.count(hqlptotal, false, ovo.getUserId(), Status.FINISH);
				ovo.setTotal(total);
				//评价车主的平均分
				String hqlAvg = " select new com.wangfanpinche.vo.OwnerOrderVo(avg(description), avg(trustworthy), avg(service)) from OwnerOrderFinishOwnerEvaluate where deleted = ? and toUser.id = ? ";
				OwnerOrderVo rvo = baseDao.get(OwnerOrderVo.class, hqlAvg, false, ovo.getUserId());
				if (rvo.getDescriptions() == null) {
					ovo.setDescriptions(5.0);
					ovo.setTrustworthy(5.0);
					ovo.setService(5.0);
				}else{
					ovo.setDescriptions(rvo.getDescriptions());
					ovo.setTrustworthy(rvo.getTrustworthy());
					ovo.setService(rvo.getService());
				}
				ownerList.add(ovo);
			}
			pvo.setOwnerList(ownerList);
		}
		//乘客同意车主抢单列表中某个车主的车单
		if (StringUtils.hasText(pvo.getOwnerOrderId())) {			
			String hqlow = " select new com.wangfanpinche.vo.OwnerOrderVo( o.id, f.city, f.sematicDescription, t.city, t.sematicDescription, o.departDateTime, o.currentSeat, o.fare, o.honesty, o.description, u.id, u.userIcon, u.mobilePhone, u.username, u.status, c.id, c.carColor, c.carNumber, o.brandSystem) from OwnerOrder o left join o.fromLocation f left join o.toLocation t left join o.user u left join o.car c where o.deleted = ? and o.id = ? ";
			OwnerOrderVo ovo = baseDao.get(OwnerOrderVo.class, hqlow, false, pvo.getOwnerOrderId());
			//拼车次数
			String hqlototal = " select count(id) from OwnerOrder where deleted = ? and user.id = ? and status = ? ";
			String hqlptotal = " select count(id) from PassengerOrder where deleted = ? and user.id = ? and status = ? ";
			Long total = baseDao.count(hqlototal, false, ovo.getUserId(), Status.FINISH) + baseDao.count(hqlptotal, false, ovo.getUserId(), Status.FINISH);
			ovo.setTotal(total);
			//评价车主的平均分
			String hqlAvg = " select new com.wangfanpinche.vo.OwnerOrderVo(avg(description), avg(trustworthy), avg(service)) from OwnerOrderFinishOwnerEvaluate where deleted = ? and toUser.id = ? ";
			OwnerOrderVo rvo = baseDao.get(OwnerOrderVo.class, hqlAvg, false, ovo.getUserId());
			if (rvo.getDescriptions() == null) {
				ovo.setDescriptions(5.0);
				ovo.setTrustworthy(5.0);
				ovo.setService(5.0);
			}else{
				ovo.setDescriptions(rvo.getDescriptions());
				ovo.setTrustworthy(rvo.getTrustworthy());
				ovo.setService(rvo.getService());
			}
			pvo.setApproveOwner(ovo);
		}
		return pvo;
	}


	@Override
	public PassengerOrderVo passOrderInfo(PassengerOrderVo vo) {
		String hql = " select new com.wangfanpinche.vo.PassengerOrderVo(p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.seat, p.publishDateTime, p.description, p.amount, u.id, u.userIcon, u.mobilePhone, u.username, u.profession, u.status, f.lng, f.lat, t.lng, t.lat) from PassengerOrder p left join p.fromLocation f left join p.toLocation t left join p.user u where p.deleted = ? and p.id = ? ";
		PassengerOrderVo pvo = baseDao.get(PassengerOrderVo.class, hql, false, vo.getId());
		//拼车次数
		String hqlototal = " select count(id) from OwnerOrder where deleted = ? and user.id = ? and status = ? ";
		String hqlptotal = " select count(id) from PassengerOrder where deleted = ? and user.id = ? and status = ? ";
		Long total = baseDao.count(hqlototal, false, pvo.getUserId(), Status.FINISH) + baseDao.count(hqlptotal, false, pvo.getUserId(), Status.FINISH);		
		pvo.setTotal(total);
		//评价乘客的标签
		String sql = "select count(t.tags) count,t.tags from t_ownerorderfinishpassengerevaluate p left join t_ownerorderfinishpassengerevaluate_tag t on p.ID = t.OwnerOrderFinishPassengerEvaluate_ID where p.DELETED = :deleted and p.toUser_ID = :userId GROUP BY t.tags ";
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
	public List<PassengerOrderVo> passOrderlist(PassengerOrderVo vo, PageHelper ph) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select new com.wangfanpinche.vo.PassengerOrderVo(p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.seat, p.publishDateTime, p.amount, u.id, u.userIcon, u.mobilePhone, u.username, u.status, f.lng, f.lat) from PassengerOrder p left join p.fromLocation f left join p.toLocation t left join p.user u where p.deleted = :deleted and p.status = :status ");
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("status", OwnerOrder.Status.PUBLISH);
		
		addWhere(sb, params, vo);
		
		return baseDao.find(PassengerOrderVo.class, sb.toString() + " order by p.modifyDateTime desc ", params, ph);
	}

	private void addWhere(StringBuilder sb, Map<String, Object> params, PassengerOrderVo vo) {
		
		if(vo.getFromLng() != null){
			OwnerLocation fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
			sb.append(" and f.city = :fromCity ");
			params.put("fromCity", fromLocation.getCity());
		}
		
	}

	@Override
	public List<PassengerOrderVo> passStrokeRecord(PassengerOrderVo vo) {
		//用户的乘客行程记录
		StringBuilder sb = new StringBuilder();
		sb.append(" select new com.wangfanpinche.vo.PassengerOrderVo(p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.publishDateTime, p.status, o.status, o.id) from PassengerOrder p left join p.fromLocation f left join p.toLocation t left join p.user u left join p.approveOwner o where p.deleted = :deleted and u.id = :userId ");
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("userId", vo.getUserId());
		addSelect(sb, params, vo);
		return baseDao.find(PassengerOrderVo.class, sb.toString(), params);
	}
	
	private void addSelect(StringBuilder sb, Map<String, Object> params, PassengerOrderVo vo) {
		
		if(vo.getStatus() != null){			
			sb.append(" and p.status = :status ");
			params.put("status", vo.getStatus());
		}
		
	}
	
	@Override
	public List<PassengerOrderVo> recommendPass(OwnerOrderVo vo){
		//给车主推荐的乘客车单列表  根据车主的 ：起始城市-结束城市 ，大于当前时间 ，坐位数判断  不能大于目前所剩余的座位数
		OwnerOrder o = baseDao.getById(OwnerOrder.class, vo.getId());
		String hql = " select new com.wangfanpinche.vo.PassengerOrderVo(p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.seat, p.publishDateTime, p.amount, u.id, u.userIcon, u.mobilePhone, u.username, u.status) from PassengerOrder p left join p.fromLocation f left join p.toLocation t left join p.user u where p.deleted = :deleted and p.fromLocation.city = :fromCity and p.toLocation.city = :toCity and p.publishDateTime > :now and p.seat <= :currentSeat ";
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("fromCity", o.getFromLocation().getCity());
		params.put("toCity", o.getToLocation().getCity());
		LocalDateTime now = LocalDateTime.now();
		params.put("now", now);
		params.put("currentSeat", o.getCurrentSeat());
		List<PassengerOrderVo> pList = baseDao.find(PassengerOrderVo.class, hql, params);
		return pList;
	}

	@Override
	public List<PassengerOrderVo> getPassOrderByUserId(PassengerOrderVo vo) {
		String hql = " select new com.wangfanpinche.vo.PassengerOrderVo( p.id, f.city, f.sematicDescription, t.city, t.sematicDescription, p.seat, p.publishDateTime) from PassengerOrder p left join p.fromLocation f left join p.toLocation t where p.deleted = ? and p.user.id = ? and p.status = ? ";
		List<PassengerOrderVo> pList = baseDao.find(PassengerOrderVo.class, hql, false, vo.getUserId(), Status.PUBLISH);
		return pList;
	}

}
