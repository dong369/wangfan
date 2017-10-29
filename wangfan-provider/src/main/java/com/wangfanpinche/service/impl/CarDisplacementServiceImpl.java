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
import com.wangfanpinche.dto.car.CarDisplacement;
import com.wangfanpinche.dto.car.CarYear;
import com.wangfanpinche.service.CarDisplacementService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarDisplacementVo;


@Service
public class CarDisplacementServiceImpl implements CarDisplacementService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public List<CarDisplacementVo> list(CarDisplacementVo vo) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		
		hql.append(" select new com.wangfanpinche.vo.car.CarDisplacementVo(cd.id,cd.name) from CarDisplacement cd where cd.deleted = :deleted ");
		params.put("deleted", false);
		
		addWhere(hql, params, vo);
		
		addOrder(hql);
		
		List<CarDisplacementVo> l = baseDao.find(CarDisplacementVo.class, hql.toString(), params);

		return l;
	}

	private void addOrder(StringBuilder hql) {
		hql.append(" order by cd.modifyDateTime desc ");
	}

	private void addWhere(StringBuilder hql, Map<String, Object> params, CarDisplacementVo vo) {
		if(vo.getYearId() != null){
			hql.append(" and cd.carYear.id = :carYearId ");
			params.put("carYearId", vo.getYearId());
		}
	}

	@Override
	public List<CarDisplacementVo> listByYearId(String yearId) {
		String hql = " select new com.wangfanpinche.vo.car.CarDisplacementVo(cd.id,cd.name) from CarDisplacement cd where cd.deleted = ? and cd.carYear.id = ? ";
		List<CarDisplacementVo> list = baseDao.find(CarDisplacementVo.class, hql, false,yearId);
		return list;
	}
	
	@Override
	public BootstrapTable table(CarDisplacementVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql=" select new com.wangfanpinche.vo.car.CarDisplacementVo(cd.id, cd.name, cy.id, cy.name, cs.id, cs.name, cb.id, cb.name, cd.createDateTime) from CarDisplacement cd left join cd.carYear cy left join cy.carSystem cs left join cy.carSystem.carBrand cb where cd.deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		table.setRows(baseDao.find(CarDisplacementVo.class, hql + " order by cd.modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from CarDisplacement where deleted = :deleted" , param));
		return table;
	}

	@Override
	public Serializable save(CarDisplacementVo vo) {
		CarDisplacement card = new CarDisplacement();		
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, card, "name", "yearId");
		
		if (StringUtils.hasText(vo.getYearId())) {
			CarYear cy = new CarYear();
			cy.setId(vo.getYearId());
			card.setCarYear(cy);
		}
		return baseDao.save(card);
	}

	@Override
	public CarDisplacementVo get(CarDisplacementVo vo) {
		CarDisplacement cd = baseDao.get(CarDisplacement.class, " from CarDisplacement cd left join fetch cd.carYear cy left join fetch cd.carYear.carSystem cys left join fetch cd.carYear.carSystem.carBrand where cd.deleted = ? and cd.id = ? ",false,vo.getId());
		BeanUtils.copyProperties(cd, vo);
		vo.setYearId(cd.getCarYear().getId());
		vo.setYearName(cd.getCarYear().getName());
		vo.setSystemId(cd.getCarYear().getCarSystem().getId());
		vo.setSystemName(cd.getCarYear().getCarSystem().getName());
		vo.setBrandId(cd.getCarYear().getCarSystem().getCarBrand().getId());
		vo.setBrandName(cd.getCarYear().getCarSystem().getCarBrand().getName());
		return vo;
	}

	@Override
	public void delete(CarDisplacementVo vo) {
		//查看当前排量下有没有车信息
		String hql = "select count(ca.id) from CarInfo ca where ca.displacement.id = ? and ca.deleted = ? ";
		Long count = baseDao.count(hql, vo.getId(), false);
		if (count > 0) {
			throw new RuntimeException("您要删除的排量下有车信息,不能删除此排量!");
		}else{
			String shql = "update CarDisplacement set deleted = ?, modifyDateTime = ? where deleted = ? and id = ? ";
			baseDao.execute(shql, true, LocalDateTime.now(), false, vo.getId());	
		}		
	}

	@Override
	public void update(CarDisplacementVo vo) {
		CarDisplacement card = baseDao.get(CarDisplacement.class, " from CarDisplacement cd left join fetch cd.carYear cy left join fetch cd.carYear.carSystem cys left join fetch cd.carYear.carSystem.carBrand where cd.deleted = ? and cd.id = ? ",false,vo.getId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, card, "name", "yearId");	
		
		if (StringUtils.hasText(vo.getYearId())) {
			CarYear cy = new CarYear();
			cy.setId(vo.getYearId());
			card.setCarYear(cy);
		}
		
		baseDao.update(card);
	}

	

}
