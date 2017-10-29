package com.wangfanpinche.service.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.car.CarSystem;
import com.wangfanpinche.dto.car.CarYear;
import com.wangfanpinche.service.CarYearService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarYearVo;


@Service
public class CarYearServiceImpl implements CarYearService {
	
	@Autowired
	private BaseDao baseDao;
	

	@Override
	public List<CarYearVo> list(CarYearVo vo) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		
		hql.append(" select new com.wangfanpinche.vo.car.CarYearVo(cy.id,cy.name) from CarYear cy where cy.deleted = :deleted ");
		params.put("deleted", false);
		
		addWhere(hql, params, vo);
		
		addOrder(hql);
		
		List<CarYearVo> l = baseDao.find(CarYearVo.class, hql.toString(), params);

		return l;
	}

	private void addOrder(StringBuilder hql) {
		hql.append(" order by cy.modifyDateTime desc ");
	}

	private void addWhere(StringBuilder hql, Map<String, Object> params, CarYearVo vo) {
		if(vo.getSystemId() != null){
			hql.append(" and cy.carSystem.id = :carSystemId ");
			params.put("carSystemId", vo.getSystemId());
		}
	}
	
	@Override
	public List<CarYearVo> listBySystemId(String systemId) {
		String hql = " select new com.wangfanpinche.vo.car.CarYearVo(cy.id,cy.name) from CarYear cy where cy.deleted = ? and cy.carSystem.id = ? ";
		List<CarYearVo> list = baseDao.find(CarYearVo.class, hql, false,systemId);
		return list;
	}	

	@Override
	public BootstrapTable table(CarYearVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql=" select new com.wangfanpinche.vo.car.CarYearVo( cy.id, cy.name, cs.id, cs.name, cb.id, cb.name, cy.createDateTime) from CarYear cy left join cy.carSystem cs left join cy.carSystem.carBrand cb where cy.deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		table.setRows(baseDao.find(CarYearVo.class, hql + " order by cy.modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from CarYear where deleted = :deleted" , param));
		return table;
	}
	
	@Override
	public Serializable save(CarYearVo vo) {
		CarYear carYear = new CarYear();
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, carYear, "name", "systemId");
		
		if (StringUtils.hasText(vo.getSystemId())) {
			CarSystem cs = new CarSystem();
			cs.setId(vo.getSystemId());
			carYear.setCarSystem(cs);
		}
		return baseDao.save(carYear);
	}

	@Override
	public CarYearVo get(CarYearVo vo) {
		CarYear carYear = baseDao.get(CarYear.class, " from CarYear cy left join fetch cy.carSystem cys left join fetch cys.carBrand where cy.deleted = ? and cy.id = ? ",false,vo.getId());		
		BeanUtils.copyProperties(carYear, vo);
		vo.setSystemId(carYear.getCarSystem().getId());
		vo.setSystemName(carYear.getCarSystem().getName());
		vo.setBrandId(carYear.getCarSystem().getCarBrand().getId());
		vo.setBrandName(carYear.getCarSystem().getCarBrand().getName());
		return vo;
	}

	@Override
	public void delete(CarYearVo vo) {
		//查看当前年代款下有没有排量
		String hql = "select count(cd.id) from CarDisplacement cd where cd.carYear.id = ? and cd.deleted = ? ";
		Long count = baseDao.count(hql, vo.getId(), false);
		if (count > 0) {
			throw new RuntimeException("您要删除的年代款下有排量,不能删除此年代款!");
		}else{
			String shql = "update CarYear set deleted = ?, modifyDateTime = ? where deleted = ? and id = ? ";
			baseDao.execute(shql, true, LocalDateTime.now(), false, vo.getId());	
		}		
	}

	@Override
	public void update(CarYearVo vo) {
		CarYear carYear = baseDao.get(CarYear.class, " from CarYear cy left join fetch cy.carSystem cys left join fetch cys.carBrand  where cy.deleted = ? and cy.id = ? ",false,vo.getId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, carYear, "name", "systemId");		
		
		if (StringUtils.hasText(vo.getSystemId())) {
			CarSystem cs = new CarSystem();
			cs.setId(vo.getSystemId());
			carYear.setCarSystem(cs);
		}
		
		baseDao.update(carYear);
	}

	

}
