package com.wangfanpinche.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Tip;
import com.wangfanpinche.service.TipService;
import com.wangfanpinche.vo.TipVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class TipServiceImpl implements TipService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public BootstrapTable table(TipVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql = "select new com.wangfanpinche.vo.TipVo( t.id, t.createDateTime, t.modifyDateTime, t.tipName, t.tipDescription) from Tip t where t.deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		table.setRows(baseDao.find(TipVo.class, hql + " order by t.modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from Tip where deleted = :deleted ", param));
		return table;
	}

	@Override
	public TipVo get(TipVo vo) {		
		Tip tip = baseDao.getById(Tip.class, vo.getId());
		BeanUtils.copyProperties(tip, vo);
		return vo;
	}

	@Override
	public void delete(TipVo vo) {
		String hql = "update Tip set deleted = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hql, true, LocalDateTime.now(), false, vo.getId());
	}

	@Override
	public void edit(TipVo vo) {
		Tip tip = baseDao.getById(Tip.class, vo.getId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, tip, "tipName", "tipDescription");
		baseDao.update(tip);
	}

	@Override
	public void save(TipVo vo) {
		Tip tip = new Tip();
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, tip, "tipName", "tipDescription");
		baseDao.save(tip);		
	}

}
