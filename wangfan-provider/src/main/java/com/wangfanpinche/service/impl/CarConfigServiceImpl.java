package com.wangfanpinche.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.car.CarConfig;
import com.wangfanpinche.service.CarConfigService;
import com.wangfanpinche.vo.car.CarConfigVo;

@Service
public class CarConfigServiceImpl implements CarConfigService {
	
	@Autowired
	private BaseDao baseDao;
	
	private static BigDecimal postage;//油费
	
	private static BigDecimal toll;//过路费
	

	private void editField(CarConfigVo vo) {
		if(vo != null){
			postage = vo.getPostage();
			toll = vo.getToll();
		}
	}

	@Override
	public CarConfigVo add(CarConfigVo vo) {
		CarConfigVo cvo = baseDao.get(CarConfigVo.class, " select new com.wangfanpinche.vo.car.CarConfigVo(c.id, c.postage, c.toll) from CarConfig c ");
		CarConfig cc = new CarConfig();
		if(cvo != null){
			String hql = " update CarConfig set postage = ?, toll = ?, modifyDateTime = ? where deleted = ? and id = ? ";
			baseDao.execute(hql, vo.getPostage(), vo.getToll(), LocalDateTime.now(), false, cvo.getId());
		} else {
			cc.setPostage(vo.getPostage());
			cc.setToll(vo.getToll());
			baseDao.save(cc);
		}
		editField(vo);
		return vo;
	}

	@Override
	public CarConfigVo get() {
		return baseDao.get(CarConfigVo.class, " select new com.wangfanpinche.vo.car.CarConfigVo(c.id, c.postage, c.toll) from CarConfig c ");
	}


	public BigDecimal getPostage() {
		if(postage == null){
			CarConfigVo vo = baseDao.get(CarConfigVo.class, " select new com.wangfanpinche.vo.car.CarConfigVo(c.id, c.postage, c.toll) from CarConfig c ");
			editField(vo);
		}
		return postage;
	}

	public BigDecimal getToll() {
		if(toll == null){
			CarConfigVo vo = baseDao.get(CarConfigVo.class, " select new com.wangfanpinche.vo.car.CarConfigVo(c.id, c.postage, c.toll) from CarConfig c ");
			editField(vo);
		}
		return toll;
	}
	
	

}
