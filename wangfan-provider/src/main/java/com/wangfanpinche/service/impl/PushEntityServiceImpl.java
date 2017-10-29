package com.wangfanpinche.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.PushEntity;
import com.wangfanpinche.dto.PushEntity.PushType;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.provider.utils.BeanUtils;
import com.wangfanpinche.service.PushEntityService;
import com.wangfanpinche.vo.PushEntityVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class PushEntityServiceImpl implements PushEntityService{

	@Autowired
	private BaseDao baseDao;

	@Override
	public BootstrapTable table(PushEntityVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();
		String hql = "select new com.wangfanpinche.vo.PushEntityVo( p.id, p.title, p.pageUrl, p.bannerUrl, p.startDateTime, p.endDateTime, p.bizNumber) from PushEntity p where p.deleted = :deleted and p.type = :type  ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		param.put("type", PushType.SYSTEM);
		table.setRows(baseDao.find(PushEntityVo.class, hql + " order by p.modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from PushEntity where deleted = :deleted and type = :type ", param));
		return table;
	}

	@Override
	public void save(PushEntityVo vo) {
		PushEntity p = new PushEntity();
		BeanUtils.copySomeProperties(vo, p, "title","pageUrl","bannerUrl","startDateTime","endDateTime","bizNumber");
		p.setType(PushType.SYSTEM);
		p.setUser(new User(vo.getUserId()));
		baseDao.save(p);
	}

	@Override
	public void edit(PushEntityVo vo) {
		PushEntity p = baseDao.getById(PushEntity.class, vo.getId());
		BeanUtils.copySomeProperties(vo, p, "title","pageUrl","bannerUrl","startDateTime","endDateTime","bizNumber");
		baseDao.update(p);
	}

	@Override
	public void delete(PushEntityVo vo) {
		LocalDateTime now = LocalDateTime.now();
		String hql = "update PushEntity set deleted = ?, modifyDateTime= ? where deleted = ? and id = ? ";
		baseDao.execute(hql, true, now, false, vo.getId());
	}

	@Override
	public PushEntityVo get(PushEntityVo vo) {
		PushEntity p = baseDao.getById(PushEntity.class, vo.getId());
		BeanUtils.copyProperties(p, vo);
		return vo;
	}

	
}
