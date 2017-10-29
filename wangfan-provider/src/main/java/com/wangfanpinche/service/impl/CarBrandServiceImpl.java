package com.wangfanpinche.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.car.CarBrand;
import com.wangfanpinche.service.CarBrandService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarBrandVo;

@Service
public class CarBrandServiceImpl implements CarBrandService{

	@Autowired
	private BaseDao baseDao;	

	@Override
	public List<CarBrandVo> list() {
		String hql = " select new com.wangfanpinche.vo.car.CarBrandVo(id, name, initial, picUrl) from CarBrand where deleted = :deleted ";
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		return baseDao.find(CarBrandVo.class, hql, params);
	}
	
	public List<CarBrandVo> listByInitial(CarBrandVo vo){
		String hql = " select new com.wangfanpinche.vo.car.CarBrandVo(id, name, initial, picUrl) from CarBrand where deleted = :deleted and initial = :initial ";
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("initial", vo.getInitial());
		return baseDao.find(CarBrandVo.class, hql, params);
	}
	
	@Override
	public BootstrapTable table(CarBrandVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();
		String hql=" select new com.wangfanpinche.vo.car.CarBrandVo(id, createDateTime, name, initial, picUrl) from CarBrand where deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		table.setRows(baseDao.find(CarBrandVo.class, hql + " order by modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from CarBrand where deleted = :deleted ", param));		
		return table;
	}
		
	
	@Override
	public void save(CarBrandVo vo) {
		CarBrand carBrand = new CarBrand();
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, carBrand, "name", "initial", "picUrl");
		baseDao.save(carBrand);
	}

	@Override
	public CarBrandVo get(CarBrandVo vo) {	
		String hql = " from CarBrand where id = ? and deleted = ? ";
		CarBrand carBrand = baseDao.get(CarBrand.class, hql, vo.getId(), false);
		BeanUtils.copyProperties(carBrand, vo);
		return vo;
	}

	@Override
	public void delete(CarBrandVo vo) {
		//查看当前车辆品牌下有没有车系
		String hql = " select count(cs.id) from CarSystem cs where cs.carBrand.id = ? and cs.deleted = ? ";
		Long count = baseDao.count(hql, vo.getId(),false);
		if (count > 0) {
			throw new RuntimeException("您要删除的品牌下有车系,不能删除此品牌!");
		}else{
			String shql = "update CarBrand set deleted = ?, modifyDateTime = ? where deleted = ? and id = ? ";
			baseDao.execute(shql, true, LocalDateTime.now(), false, vo.getId());			
		}		
	}

	@Override
	public void update(CarBrandVo vo) {
		String hql = " from CarBrand where id = ? and deleted = ? ";
		CarBrand carBrand = baseDao.get(CarBrand.class, hql, vo.getId(), false);
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, carBrand, "name", "initial", "picUrl");
		baseDao.update(carBrand);
	}

	

}
