package com.wangfanpinche.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Advertise;
import com.wangfanpinche.service.AdvertiseService;
import com.wangfanpinche.vo.AdvertiseVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class AdvertiseServiceImpl implements AdvertiseService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public BootstrapTable table(AdvertiseVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql = "select new com.wangfanpinche.vo.AdvertiseVo( t.id, t.title, t.descript, t.picUrl, t.adLocation, t.h5url, t.second) from Advertise t where t.deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		table.setRows(baseDao.find(AdvertiseVo.class, hql + " order by t.modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from Advertise where deleted = :deleted ", param));
		return table;
	}

	@Override
	public AdvertiseVo get(AdvertiseVo vo) {		
		Advertise advertise = baseDao.getById(Advertise.class, vo.getId());
		BeanUtils.copyProperties(advertise, vo);
		return vo;
	}

	@Override
	public void delete(AdvertiseVo vo) {
		String hql = "update Advertise set deleted = ?, modifyDateTime= ? where deleted = ? and id = ? ";
		baseDao.execute(hql, true, LocalDateTime.now(), false, vo.getId());
	}

	@Override
	public void edit(AdvertiseVo vo) {
		Advertise advertise = baseDao.getById(Advertise.class, vo.getId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, advertise, "title", "descript", "picUrl", "adLocation", "h5url", "second");
		baseDao.update(advertise);
	}

	@Override
	public void save(AdvertiseVo vo) {
		Advertise advertise = new Advertise();
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, advertise, "title", "descript", "picUrl", "adLocation", "h5url", "second");
		baseDao.save(advertise);		
	}

	@Override
	public List<AdvertiseVo> list(AdvertiseVo vo) {
		String hql = "select new com.wangfanpinche.vo.AdvertiseVo( t.id, t.title, t.descript, t.picUrl, t.adLocation, t.h5url, t.second) from Advertise t where t.deleted = ? and t.adLocation = ? ";
		List<AdvertiseVo> aList = baseDao.find(AdvertiseVo.class, hql, false, vo.getAdLocation());
		return aList;
	}

}
