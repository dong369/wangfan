package com.wangfanpinche.service.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.car.CarBrand;
import com.wangfanpinche.dto.car.CarSystem;
import com.wangfanpinche.provider.utils.BeanUtils;
import com.wangfanpinche.service.CarSystemService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarSystemVo;


@Service
public class CarSystemServiceImpl implements CarSystemService {
	
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public List<CarSystemVo> list(CarSystemVo vo) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		
		hql.append(" select new com.wangfanpinche.vo.car.CarSystemVo(cs.id, cs.name) from CarSystem cs where cs.deleted = :deleted");
		params.put("deleted", false);
		
		addWhere(hql, params, vo);
		
		addOrder(hql);
		
		List<CarSystemVo> carSystemVoList = baseDao.find(CarSystemVo.class, hql.toString(), params);

		return carSystemVoList;
	}

	private void addOrder(StringBuilder hql) {
		hql.append(" order by cs.modifyDateTime desc");
	}

	private void addWhere(StringBuilder hql, Map<String, Object> params, CarSystemVo vo) {
		if(vo.getBrandId() != null){
			hql.append(" and cs.carBrand.id = :brandId ");
			params.put("brandId", vo.getBrandId());
		}
	}		

	@Override
	public List<CarSystemVo> listByBrandId(String brandId) {
		String hql = " select new com.wangfanpinche.vo.car.CarSystemVo(cs.id, cs.name) from CarSystem cs where cs.deleted = ? and cs.carBrand.id = ? ";
		List<CarSystemVo> list = baseDao.find(CarSystemVo.class, hql, false,brandId);
		return list;
	}
	
	@Override
	public BootstrapTable table(CarSystemVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql=" select new com.wangfanpinche.vo.car.CarSystemVo(cs.id, cb.id, cb.name, cs.name, cs.createDateTime) from CarSystem cs left join cs.carBrand cb where cs.deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		table.setRows(baseDao.find(CarSystemVo.class, hql + " order by cs.modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from CarSystem where deleted = :deleted" , param));
		return table;
	}

	@Override
	public Serializable save(CarSystemVo vo) {
		CarSystem cs = new CarSystem();
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, cs, "name", "brandId");
		
		if(StringUtils.hasText(vo.getBrandId())){
			CarBrand c = new CarBrand();
			c.setId(vo.getBrandId());
			cs.setCarBrand(c);
		}
		
		return baseDao.save(cs);
	}

	@Override
	public CarSystemVo get(CarSystemVo vo) {
				
		CarSystem carSystem = baseDao.get(CarSystem.class, " from CarSystem cs left join fetch cs.carBrand where cs.id = ? ",vo.getId());
		BeanUtils.copyProperties(carSystem, vo);
		vo.setBrandId(carSystem.getCarBrand().getId());
		vo.setBrandName(carSystem.getCarBrand().getName());
		return vo;
	}

	@Override
	public void delete(CarSystemVo vo) {
		//查看当前车系下有没有年代款
		String hql = "select count(cy.id) from CarYear cy where cy.carSystem.id = ? and cy.deleted = ? ";
		Long count = baseDao.count(hql, vo.getId(), false);
		if (count > 0) {
			throw new RuntimeException("您要删除的车系下有年代款,不能删除此车系!");
		}else{
			String shql = "update CarSystem set deleted = ?, modifyDateTime = ? where deleted = ? and id = ? ";
			baseDao.execute(shql, true, LocalDateTime.now(), false, vo.getId());	
		}		
	}

	@Override
	public void update(CarSystemVo vo) {
		CarSystem cars = baseDao.get(CarSystem.class, " from CarSystem cs left join fetch cs.carBrand where cs.id = ? ", vo.getId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, cars, "name", "brandId");
		
		if(StringUtils.hasText(vo.getBrandId())){
			CarBrand c = new CarBrand();
			c.setId(vo.getBrandId());
			cars.setCarBrand(c);
		}
		
		baseDao.update(cars);
	}
	

}
