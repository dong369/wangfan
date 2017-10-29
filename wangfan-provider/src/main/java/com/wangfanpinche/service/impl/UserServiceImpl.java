package com.wangfanpinche.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Journal;
import com.wangfanpinche.dto.OwnerApprove;
import com.wangfanpinche.dto.PayInfo;
import com.wangfanpinche.dto.OwnerApprove.OwnStatusEnum;
import com.wangfanpinche.dto.Resource;
import com.wangfanpinche.dto.Resource.TypeEnum;
import com.wangfanpinche.dto.Role;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.dto.order.Location;
import com.wangfanpinche.dto.order.OrderOwner.OrderOwnerStatus;
import com.wangfanpinche.dto.order.OrderPassenger.OrderPassengerStatus;
import com.wangfanpinche.dto.Verify;
import com.wangfanpinche.dto.Journal.BizType;
import com.wangfanpinche.dto.Journal.InOutType;
import com.wangfanpinche.dto.Journal.PayType;
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
import com.wangfanpinche.vo.JournalVo;
import com.wangfanpinche.vo.ResourceVo;
import com.wangfanpinche.vo.UserVo;
import com.wangfanpinche.vo.WeChat;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public List<String> getResourceList(String id) {
		List<String> resourceList = new ArrayList<String>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("deleted", false);
		User t = baseDao.get(User.class," from User t left join fetch t.roles role left join fetch role.resources resource where t.id = :id and t.deleted = :deleted ", params);
		if (t != null) {
			Set<Role> roles = t.getRoles();
			if (roles != null && !roles.isEmpty()) {
				for (Role role : roles) {
					Set<Resource> resources = role.getResources();
					if (resources != null && !resources.isEmpty()) {
						for (Resource resource : resources) {
							if (resource != null && StringUtils.hasText(resource.getUrl())) {
								resourceList.add(resource.getUrl());
							}
						}
					}
				}
			}
		}
		return resourceList;
	}
	
	@Override
	public User getUserByPhoneToken(String phoneToken) {
		Map<String,Object> map = new HashMap<>();
		map.put("phoneToken", phoneToken);
		map.put("deleted", false);
		String hql = " from User where phoneToken = :phoneToken and deleted = :deleted ";
		return baseDao.get(User.class, hql, map);
	}

	@Override
	public UserVo getByPhoneAndPwd(UserVo vo) {
		String hql = "select new com.wangfanpinche.vo.UserVo(u.id) from User u where u.mobilePhone = ? and u.pwd = ? and u.deleted = ? ";
		return baseDao.get(UserVo.class,hql, vo.getMobilePhone(), vo.getPwd(),false);
	}

	@Override
	public List<ResourceVo> getMenuList(String id) {
		String hql = "select distinct new com.wangfanpinche.vo.ResourceVo(t.id, r.id,r.name, t.name, t.seq, t.url, t.icon) from Resource t left join t.resource r join t.roles role join role.users user where t.type = :type and user.id = :userId order by t.seq";
		Map<String,Object> map = new HashMap<>();
		map.put("type", TypeEnum.菜单);
		map.put("userId", id);
		return baseDao.find(ResourceVo.class, hql, map);
	}

	@Override
	public BootstrapTable table(UserVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql = "select new com.wangfanpinche.vo.UserVo(u.id, u.deleted, u.username, u.mobilePhone, u.status, o.status, u.balance) from User u left join OwnerApprove o on u.id = o.userId ";
		table.setRows(baseDao.find(UserVo.class, hql + " order by u.modifyDateTime desc ", ph));
		table.setTotal(baseDao.count("select count(id) from User "));
		return table;
	}

	@Override
	public void sendVerify(UserVo vo) {
		
		//如果有定位就保存
		if (vo.getFromLat() != null && vo.getFromLng() != null) {			
			Location fromLocation = gpsToBean(vo.getFromLng(), vo.getFromLat());
			baseDao.persist(fromLocation);
			User user = baseDao.get(User.class, " from User where deleted = ? and mobilePhone = ? ", false, vo.getMobilePhone());
			user.setFromLocation(fromLocation);
		}
		
		LocalDateTime ldt = LocalDateTime.now();
		ldt = ldt.minusMinutes(1);
		String hql = " select count(*) from Verify where deleted = ? and mobilePhone = ? and createDateTime > ?";
		Long count = baseDao.count(hql, false, vo.getMobilePhone(),ldt);
		if (count > 0) {
			throw new RuntimeException("60秒内不能重复发送!");
		}
		String phone = vo.getMobilePhone();
		//String code = String.format("%04d", new Random().nextInt(10000));
		String code = "9527";
		String content = "[往返]您的手机验证码是：" + code;		
		//AliSmsUtils.sendValidCode(phone, code);
		vo.setCode(code);
		
		Verify verify = new Verify();
		verify.setCode(code);
		verify.setMobilePhone(phone);
		verify.setMessageContent(content);
		baseDao.save(verify);
	}

	@Override
	public UserVo validPhoneAndVerify(UserVo vo) {		
		LocalDateTime ldt = LocalDateTime.now();
		ldt = ldt.minusMinutes(10);		
		
		String hql = " select count(id) from Verify where deleted = ? and mobilePhone = ? and code = ? and createDateTime > ? ";
		Long verify = baseDao.count( hql, false, vo.getMobilePhone(), vo.getCode(), ldt);
		if(verify == 0){
			throw new RuntimeException("手机号或验证码错误");
		}
		
		baseDao.execute(" update Verify set deleted = ?, modifyDateTime = ? where deleted = ? and mobilePhone = ? and code = ? and createDateTime > ? ", true, LocalDateTime.now(), false, vo.getMobilePhone(), vo.getCode(), ldt);
		
		User user = baseDao.get(User.class," select u from User u where u.mobilePhone = ? and u.deleted = ? ", vo.getMobilePhone(), false);
		user.setPhoneToken(vo.getPhoneToken());
		if (user.getStatus().equals(StatusEnum.待验证)) {
			user.setStatus(StatusEnum.待认证);
		}

		UserVo uservo = new UserVo();

		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(user, uservo, "id","mobilePhone","status","phoneToken");

		return uservo;		
	} 

	@Override
	public Boolean hasPhone(UserVo vo) {
		String hql = " select count(id) from User where mobilePhone = ? ";
		Long count = baseDao.count(hql, vo.getMobilePhone());
		if (count == 0) {
			return false;
		}
		
		User u = baseDao.get(User.class, " from User where mobilePhone = ? ", vo.getMobilePhone());
		if (u.getDeleted() == true) {
			throw new RuntimeException("您的手机号已经被禁用,请联系客服或更换手机号!");
		}
		return true;
			
	}

	@Override
	public String add(UserVo vo) {
		User user = new User();
		BeanUtils.copyProperties(vo, user);
		
		user.setStatus(StatusEnum.待验证);
		Role app = new Role();
		app.setId("app");
		user.getRoles().add(app);
		user.setBalance(new BigDecimal("0.00").setScale(2, RoundingMode.HALF_UP));
		user.setUserIcon("static/images/user.jpg");
		Serializable id = baseDao.save(user);
		
		OwnerApprove owner = new OwnerApprove();
		owner.setStatus(OwnStatusEnum.待认证);
		owner.setUserId(id.toString());
		baseDao.save(owner);
		return user.getId();
	}

	@Override
	public UserVo getUserInfo(String userId) {
		String hql = "select new com.wangfanpinche.vo.UserVo(u.id, u.username, u.mobilePhone, u.userIcon, u.status, o.status, u.characterSignature) from User u left join OwnerApprove o on u.id = o.userId where u.id = ? and u.deleted = ? ";
		UserVo vo = baseDao.get(UserVo.class, hql, userId, false);
		//拼车次数		
		String hqlototal = " select count(id) from OrderOwner where deleted = ? and user.id = ? and status = ? ";
		String hqlptotal = " select count(id) from OrderPassenger where deleted = ? and user.id = ? and status = ? ";
		Long total = baseDao.count(hqlototal, false, userId, OrderOwnerStatus.FINISH) + baseDao.count(hqlptotal, false, userId, OrderPassengerStatus.FINISH);
		vo.setTotal(total);
		return vo;
	}

	@Override
	public UserVo getUserDetail(String userId) {
		String hql = "select new com.wangfanpinche.vo.UserVo(u.id, u.username, u.idNumber, u.mobilePhone, u.userIcon, u.status, u.industry, u.company, u.profession, u.characterSignature, u.homeAddress, u.residentAddress) from User u where u.id = ? and u.deleted = ? ";
		return baseDao.get(UserVo.class, hql, userId, false);
	}

	@Override
	public void edit(UserVo vo) {
		User user = baseDao.getById(User.class, vo.getId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, user, "userIcon", "industry", "company", "profession", "characterSignature", "homeAddress", "residentAddress");		
		baseDao.update(user);		
	}

	@Override
	public Boolean hasUserApprove(UserVo vo) {
		String hql = " select count(id) from User where deleted = ? and id = ? and status = ? ";
		Long count = baseDao.count(hql, false, vo.getId(), StatusEnum.认证通过);
		if (count == 0) {
			return false;
		}else{
			return true;
		}		
	}

	@Override
	public void userApprove(UserVo vo) {
		String hql = " update User set username = ?,idNumber = ?,status = ?,modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hql, vo.getUsername(), vo.getIdNumber(), StatusEnum.认证通过,LocalDateTime.now(),false, vo.getId());
	}

	@Override
	public CarVo getCarByUserId(String userId) {
		String hql = " select new com.wangfanpinche.vo.CarVo(c.id, ci.carmodel, c.carNumber, c.carColor, c.carInfoId) from Car c left join CarInfo ci on c.carInfoId = ci.id where c.deleted = ? and c.userId = ? ";
		CarVo cvo = baseDao.get(CarVo.class, hql, false, userId);
		return cvo;
	}
	
	@Override
	public CarVo getCarByCarInfoId(String carInfoId){
		String hql = " select new com.wangfanpinche.vo.CarVo(cb.name, cs.name) from CarInfo ci left join ci.displacement cd left join cd.carYear cy left join cy.carSystem cs left join cs.carBrand cb where ci.deleted = ? and ci.id = ? ";
		CarVo cvo = baseDao.get(CarVo.class, hql, false, carInfoId);
		return cvo;
	}

	@Override
	public void addSos(UserVo vo) {
		String hql = " update User set sosName = ? , sosMobilePhone = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hql, vo.getSosName(), vo.getSosMobilePhone(), LocalDateTime.now(), false, vo.getId());
	}

	@Override
	public UserVo getSos(UserVo vo) {
		String hql = " select count(id) from User where deleted = ? and id = ? and sosName is not null ";
		Long count = baseDao.count(hql, false, vo.getId());
		if (count == 0) {
			return new UserVo();
		}
		
		String hqlsos = " select new com.wangfanpinche.vo.UserVo(u.id, u.sosName, u.sosMobilePhone) from User u where u.deleted = ? and u.id = ? ";
		UserVo uvo = baseDao.get(UserVo.class, hqlsos, false, vo.getId());
		return uvo;
	}

	@Override
	public void enableUser(String id) {
		String hql = " update User set deleted = ?, modifyDateTime = ? where id = ?　";
		baseDao.execute(hql, true, LocalDateTime.now(), id);
	}

	@Override
	public void ableUser(String id) {
		String hql = " update User set deleted = ?, modifyDateTime = ? where id = ?　";
		baseDao.execute(hql, false, LocalDateTime.now(), id);
	}

	@Override
	public String alipayCashInInfo(UserVo vo) {
		BigDecimal totalMoney = vo.getBalance();
		if (totalMoney.doubleValue() <= 0.00) {
			throw new RuntimeException("充值金额必须大于0!");
		}
totalMoney = new BigDecimal("0.01");		
		User u = baseDao.getById(User.class, vo.getId());
		// 要判断他是否生成过订单信息，如果生成过。则返回原来生成的订单信息
		if (u.getOrderNumber() != null) {
			Journal j = baseDao.get(Journal.class, " from Journal where orderNumber = ? ", u.getOrderNumber());
			if(PayType.支付宝.equals(j.getPayType())){
				return j.getPayInfo().getRequestBody();
			} else {
				baseDao.delete(j);
			}
		}
		
		//订单号
		long orderId = SnowFlakeIdGenerator.getInstance().generateLongId();
		u.setOrderNumber(orderId);
		AliPayBizContent content = new AliPayBizContent();
		//XXX 判断发布时间		
		content.setTimeout_express("15m");
		content.setProduct_code("QUICK_MSECURITY_PAY");
		content.setTotal_amount(totalMoney.toString());
		content.setSubject("往返拼车");
		content.setBody("支付宝充值获取订单信息");		
		content.setOut_trade_no(orderId + ""); 
		//获取支付订单信息并保存到支付信息表
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/user/alipayCashIn";
		String notifyUrl = "http://app.wangfanpinche.com:8889/user/alipayCashIn";
		String orderInfo = AlipayAppUtils.buildOrderInfo(content, notifyUrl);
		//支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(orderInfo);
		baseDao.save(pay);
		//账单
		Journal j = new Journal();
		j.setType(BizType.充值);
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.支付宝);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);		
		j.setOrderNumber(orderId);
		j.setMoney(totalMoney);
		j.setUser(u);
		j.setPayInfo(pay);
		baseDao.save(j);
		
		return orderInfo;
	}

	@Override
	public void alipayCashIn(Map<String, String> map) {
		//1.判断计算金额和支付金额是否一致  
		//订单号
		String orderNumber = map.get("out_trade_no");
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.user u left join fetch j.payInfo p where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(orderNumber));
		BigDecimal totalMoney = j.getMoney();	
		//支付宝返回的支付金额
		BigDecimal receipt_amount = new BigDecimal(map.get("receipt_amount"));
		if (!totalMoney.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台填写金额不一致!");
		}
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);		
		j.getPayInfo().setNotifyBody(JSON.toJSONString(map));
		// 支付结果-更改乘客余额
		String hqlup = " update User set balance = balance + ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlup, j.getMoney(), LocalDateTime.now(), false, j.getUser().getId());
	}
	
	@Override
	public String wechatPayCashInInfo(UserVo vo){
		BigDecimal totalMoney = vo.getBalance();
		if (totalMoney.doubleValue() <= 0.00) {
			throw new RuntimeException("充值金额必须大于0!");
		}
		User u = baseDao.getById(User.class, vo.getId());
		// 要判断他是否生成过订单信息，如果生成过。则返回原来生成的订单信息
		if (u.getOrderNumber() != null) {
			Journal j = baseDao.get(Journal.class, " from Journal where orderNumber = ? ", u.getOrderNumber());
			if(PayType.支付宝.equals(j.getPayType())){
				return j.getPayInfo().getRequestBody();
			} else {
				baseDao.delete(j);
			}
		}
		
		long orderId = SnowFlakeIdGenerator.getInstance().generateLongId();
		u.setOrderNumber(orderId);
		
		WechatPreOrder p = new WechatPreOrder();
		p.setOut_trade_no(orderId + "");		
totalMoney = new BigDecimal("0.01");
		p.setTotal_fee(totalMoney.multiply(new BigDecimal("100")).intValue());
		p.setSpbill_create_ip(vo.getIp());
		p.setTime_expire(LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		//String notifyUrl = "http://1j5943176d.iok.la/wangfan-web/user/wechatPayCashIn";
		String notifyUrl = "http://app.wangfanpinche.com:8889/user/wechatPayCashIn";
		p.setNotify_url(notifyUrl);
		p.setBody("微信充值");
		
		String preOrder = WechatPayAppUtils.preOrder(p);
		
		//支付信息
		PayInfo pay = new PayInfo();
		pay.setRequestBody(preOrder);
		baseDao.save(pay);
		//账单
		Journal j = new Journal();
		j.setType(BizType.充值);
		j.setInOutType(InOutType.收入);
		j.setPayType(PayType.微信);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.未支付);		
		j.setOrderNumber(orderId);
		j.setMoney(totalMoney);
		j.setUser(new User(vo.getId()));
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
	public void wechatPayCashIn(WechatPayNotify notify){
		String orderNumber = notify.getOut_trade_no();
		Journal j = baseDao.get(Journal.class, " from Journal j left join fetch j.user u left join fetch j.payInfo p where j.deleted = ? and j.orderNumber = ? ", false, Long.parseLong(orderNumber));
		BigDecimal totalMoney = j.getMoney();
totalMoney = new BigDecimal("0.01");
		//微信返回的支付金额
		Integer receipt_amountInt = Integer.valueOf(notify.getTotal_fee());
		BigDecimal receipt_amount = new BigDecimal(receipt_amountInt).divide(new BigDecimal("100"));
		if (!totalMoney.equals(receipt_amount)) {
			throw new RuntimeException("您支付的金额与平台计算金额不一致!");
		}
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		j.getPayInfo().setNotifyBody(JSON.toJSONString(notify));
		// 支付结果-更改乘客余额
		String hqlup = " update User set balance = balance + ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlup, j.getMoney(), LocalDateTime.now(), false, j.getUser().getId());
	}

	@Override
	public void cashOutInfo(UserVo vo) {
		BigDecimal totalMoney = vo.getBalance();
		if (totalMoney.doubleValue() <= 0.00) {
			throw new RuntimeException("提现金额必须大于0!");
		}
		User u = baseDao.getById(User.class, vo.getId());
		if (totalMoney.doubleValue() > u.getBalance().doubleValue()) {
			throw new RuntimeException("提现金额不能大于余额!");
		}
		String hqlc = " select count(id) from Journal where deleted = ? and user.id = ? and status = ? ";
		Long count = baseDao.count(hqlc, false, u.getId(), com.wangfanpinche.dto.Journal.Status.审核中);
		if (count > 0) {
			throw new RuntimeException("您有一笔提现账单正在处理，请等待审核完成后再继续提现!");
		}
		//订单号
		long orderId = SnowFlakeIdGenerator.getInstance().generateLongId();
		u.setOrderNumber(orderId);
		
		//账单
		Journal j = new Journal();
		j.setType(BizType.提现);
		j.setInOutType(InOutType.支出);
		//j.setPayType(PayType.支付宝);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.审核中);		
		j.setOrderNumber(orderId);
		j.setMoney(totalMoney);
		j.setUser(u);
		baseDao.save(j);
		
	}

	@Override
	public BootstrapTable tableOut(PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql = " select new com.wangfanpinche.vo.JournalVo(j.id, j.money, j.status, u.id, u.username, r.id, r.username, j.reviewDateTime) from Journal j left join j.user u left join j.reviewer r where j.deleted = ? and j.type = ? ";
		table.setRows(baseDao.find(JournalVo.class, hql, false, BizType.提现));
		table.setTotal(baseDao.count("select count(id) from Journal where deleted = ? and type = ? " , false, BizType.提现));
		return table;
	}

	@Override
	public void cashOutSucc(JournalVo vo) {
		Journal j = baseDao.getById(Journal.class, vo.getId());
		User u = new User();
		u.setId(vo.getReviewerId());
		j.setReviewer(u);
		LocalDateTime now = LocalDateTime.now();
		j.setReviewDateTime(now);
		j.setStatus(com.wangfanpinche.dto.Journal.Status.完成);
		//支付结果-更改乘客余额
		String hqlup = " update User set balance = balance - ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hqlup, j.getMoney(), now, false, j.getUser().getId());
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
	public void userForget(UserVo vo) {
		LocalDateTime ldt = LocalDateTime.now();
		ldt = ldt.minusMinutes(10);		
		
		String hql = " select count(id) from Verify where deleted = ? and mobilePhone = ? and code = ? and createDateTime > ? ";
		Long verify = baseDao.count( hql, false, vo.getMobilePhone(), vo.getCode(), ldt);
		if(verify == 0){
			throw new RuntimeException("手机号或验证码错误");
		}
		
		baseDao.execute(" update Verify set deleted = ?, modifyDateTime = ? where deleted = ? and mobilePhone = ? and code = ? and createDateTime > ? ", true, LocalDateTime.now(), false, vo.getMobilePhone(), vo.getCode(), ldt);
		
		User user = baseDao.get(User.class," select u from User u where u.mobilePhone = ? and u.deleted = ? ", vo.getMobilePhone(), false);
		user.setPhoneToken(vo.getPhoneToken());
		if (user.getStatus().equals(StatusEnum.待验证)) {
			user.setStatus(StatusEnum.待认证);
		}
		
		user.setPwd(vo.getPwd());
	}
	
}
