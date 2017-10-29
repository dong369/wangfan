package com.wangfanpinche.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.car.CarDisplacement;
import com.wangfanpinche.dto.car.CarInfo;
import com.wangfanpinche.service.CarInfoService;
import com.wangfanpinche.utils.map.BaiduMapUtils;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarConfigVo;
import com.wangfanpinche.vo.car.CarInfoVo;


@Service
public class CarInfoServiceImpl implements CarInfoService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public BootstrapTable table(CarInfoVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select new com.wangfanpinche.vo.car.CarInfoVo(ci.id, ci.outsideColor, ci.outsideColorCode, ci.seat, ci.fuel, ci.actfuel,ci.carmodel, cid.id, cid.name, ciy.id, ciy.name, cis.id, cis.name, cib.id, cib.name ) from CarInfo ci left join ci.displacement cid left join ci.displacement.carYear ciy left join ci.displacement.carYear.carSystem cis left join ci.displacement.carYear.carSystem.carBrand cib where ci.id is not null and ci.deleted = :deleted ");		
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		addWhere(sb, params, vo);
		
		List<CarInfoVo> list = baseDao.find(CarInfoVo.class, sb.toString(), params, ph);
		table.setRows(list);
		StringBuilder totalHql = new StringBuilder();
		totalHql.append(" select count(ci.id) from CarInfo ci left join ci.displacement cid left join ci.displacement.carYear ciy left join ci.displacement.carYear.carSystem cis left join ci.displacement.carYear.carSystem.carBrand cib where ci.id is not null and ci.deleted = :deleted ");
		addWhere(totalHql, params, vo);
		
		table.setTotal(baseDao.count(totalHql.toString(), params));		
		return table;
	}

	private void addWhere(StringBuilder sb, Map<String, Object> params, CarInfoVo vo) {
		if(StringUtils.hasText(vo.getDisplacementId())){
			sb.append(" and cid.id = :cidId ");
			params.put("cidId", vo.getDisplacementId());
		}
		
		if(StringUtils.hasText(vo.getInitial())){
			sb.append(" and cib.initial = :initial ");
			params.put("initial", vo.getInitial());
		}
		
		if(StringUtils.hasText(vo.getYearId())){
			sb.append(" and ciy.id = :ciyId ");
			params.put("ciyId", vo.getYearId());
		}
		
		if(StringUtils.hasText(vo.getSystemId())){
			sb.append(" and cis.id = :cisId ");
			params.put("cisId", vo.getSystemId());
		}
		
		if(StringUtils.hasText(vo.getBrandId())){
			sb.append(" and cib.id = :cibId ");
			params.put("cibId", vo.getBrandId());
		}
	}

	@Override
	public Serializable save(CarInfoVo vo) {
		CarInfo cinfo = new CarInfo();
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, cinfo, "carmodel","seat","fuel","actfuel","outsideColor","displacementId");
		
		if (StringUtils.hasText(vo.getDisplacementId())) {
			CarDisplacement cd = new CarDisplacement();
			cd.setId(vo.getDisplacementId());
			cinfo.setDisplacement(cd);			
		}
		
		return baseDao.save(cinfo);
	}

	@Override
	public void updateFuelOrSeat(CarInfoVo vo) {
		
		if (vo.getFuel() != null) {
			if (vo.getFuel().intValue() <= 0) {
				throw new RuntimeException("工信部油量必须大于0!");
			}else{
				String hqlfuel = " update CarInfo set fuel = ?, modifyDateTime = ? where deleted = ? and id = ? ";
				baseDao.execute(hqlfuel, vo.getFuel(), LocalDateTime.now(), false,vo.getId());
			}
		}
		if (vo.getSeat() != null) {
			if (vo.getSeat().intValue() < 1) {
				throw new RuntimeException("座位数必须大于1!");
			}else{
				String hqlseat = " update CarInfo set seat = ?, modifyDateTime = ? where deleted = ? and id = ?　";
			    baseDao.execute(hqlseat, vo.getSeat(), LocalDateTime.now(), false,vo.getId());
			}
		}		
		
	}
	

	@Override
	public CarInfoVo get(CarInfoVo vo) {
		CarInfo cd = baseDao.get(CarInfo.class, " from CarInfo ci left join fetch ci.displacement cd left join fetch ci.displacement.carYear cy left join fetch ci.displacement.carYear.carSystem cys left join fetch ci.displacement.carYear.carSystem.carBrand where ci.deleted = ? and ci.id = ? ",false,vo.getId());		
		BeanUtils.copyProperties(cd, vo);
		vo.setDisplacementId(cd.getDisplacement().getId());
		vo.setDisplacementName(cd.getDisplacement().getName());
		vo.setYearId(cd.getDisplacement().getCarYear().getId());
		vo.setYearName(cd.getDisplacement().getCarYear().getName());
		vo.setSystemId(cd.getDisplacement().getCarYear().getCarSystem().getId());
		vo.setSystemName(cd.getDisplacement().getCarYear().getCarSystem().getName());
		vo.setBrandId(cd.getDisplacement().getCarYear().getCarSystem().getCarBrand().getId());
		vo.setBrandName(cd.getDisplacement().getCarYear().getCarSystem().getCarBrand().getName());		
		return vo;
	}

	@Override
	public void delete(CarInfoVo vo) {
		String shql = "update CarInfo set deleted = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(shql, true, LocalDateTime.now(),  false, vo.getId());
	}

	@Override
	public void update(CarInfoVo vo) {
		
		CarInfo cinfo = baseDao.get(CarInfo.class, " from CarInfo ci left join fetch ci.displacement cd left join fetch ci.displacement.carYear cy left join fetch ci.displacement.carYear.carSystem cys left join fetch ci.displacement.carYear.carSystem.carBrand where ci.deleted = ? and ci.id = ? ",false,vo.getId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, cinfo, "carmodel","seat","fuel","actfuel","outsideColor","displacementId");	
		
		if (StringUtils.hasText(vo.getDisplacementId())) {
			CarDisplacement cd = new CarDisplacement();
			cd.setId(vo.getDisplacementId());
			cinfo.setDisplacement(cd);			
		}
		
		baseDao.update(cinfo);
	}

	@Override
	public List<CarInfoVo> getByDisplacementId(String displacementId) {		
		String hql = " select new com.wangfanpinche.vo.car.CarInfoVo(ci.id, ci.outsideColor, ci.outsideColorCode, ci.seat, ci.fuel, ci.actfuel,ci.carmodel) from CarInfo ci left join ci.displacement cid left join ci.displacement.carYear ciy left join ci.displacement.carYear.carSystem cis left join ci.displacement.carYear.carSystem.carBrand cib where ci.deleted = ? and cid.id = ? ";
		List<CarInfoVo> vo = baseDao.find(CarInfoVo.class,hql,false,displacementId);
		return vo;
	}

	@Override
	public BigDecimal amout(String id, BigDecimal fromLng, BigDecimal fromLat, BigDecimal toLng, BigDecimal toLat){
		//通过车辆信息ID和距离计算价格
		CarInfo c = baseDao.getById(CarInfo.class, id);
		CarConfigVo cvo = baseDao.get(CarConfigVo.class, " select new com.wangfanpinche.vo.car.CarConfigVo(c.id, c.postage, c.toll) from CarConfig c ");
		if (cvo == null) {
//			throw new RuntimeException("没有油费和过路费，请联系管理员添加!");
			return new BigDecimal(-1);
		}
		if (cvo.getPostage() == null) {
//			throw new RuntimeException("没有油费，请联系管理员添加!");
			return new BigDecimal(-1);
		}
		if (cvo.getToll() == null) {
//			throw new RuntimeException("没有过路费，请联系管理员添加!");
			return new BigDecimal(-1);
		}
		if (c.getFuel() == null) {
//			throw new RuntimeException("没有工信部综合油耗，请联系管理员添加!");
			return new BigDecimal(-1);
		}
		BigDecimal youfei = c.getFuel().divide(new BigDecimal("100")).multiply(cvo.getPostage());
		BigDecimal guolufei = cvo.getToll();
		BigDecimal seat = new BigDecimal(c.getSeat() - 1);
		BigDecimal ygadd = youfei.add(guolufei);
		BigDecimal amount = ygadd.multiply(new BigDecimal("1.2")).divide(seat);
		String source = fromLat+","+fromLng;
		String target = toLat+","+toLng;
		String routematrix = BaiduMapUtils.routematrix(source, target);
		JSONObject j = JSON.parseObject(routematrix);
		String distance = j.getJSONArray("result").getJSONObject(0).getString("distance");
		JSONObject v = JSON.parseObject(distance);
		Double juli = v.getDouble("value");
		BigDecimal gongli = new BigDecimal(juli/1000);
		BigDecimal all = amount.multiply(gongli).setScale(2, RoundingMode.HALF_UP);
		return all;
	}
	
}
