package com.wangfanpinche.service.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.FeedBack;
import com.wangfanpinche.dto.FeedBack.FeedStatusEnum;
import com.wangfanpinche.service.FeedBackService;
import com.wangfanpinche.vo.FeedBackVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class FeedBackServiceImpl implements FeedBackService{

	@Autowired
	private BaseDao baseDao;	

	@Override
	public Serializable save(FeedBackVo vo) {
		FeedBack feed = new FeedBack();
		vo.setReadStatus(FeedStatusEnum.未读);
		BeanUtils.copyProperties(vo, feed);
		return baseDao.save(feed);
	}

	@Override
	public BootstrapTable table(FeedBackVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();
		String hqlget = " select new com.wangfanpinche.vo.FeedBackVo(f.id, u.id, u.username, f.content, f.linkType, f.readStatus, f.readDateTime, f.createDateTime) from FeedBack f left join User u on f.userId = u.id where f.deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);		
		table.setRows(baseDao.find(FeedBackVo.class, hqlget + " order by f.modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from FeedBack where deleted = :deleted " , param));
		return table;
	}

	@Override
	public FeedBackVo updateAndGet(FeedBackVo vo) {
		LocalDateTime now = LocalDateTime.now();
		String hql = " update FeedBack set readStatus = ?, readDateTime = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hql, FeedStatusEnum.已读, now, now, false, vo.getId());
		String hqlget = " select new com.wangfanpinche.vo.FeedBackVo(f.id, u.username, f.content, f.linkType) from FeedBack f left join User u on f.userId = u.id where f.deleted = ? and f.id = ? ";
		FeedBackVo fvo = baseDao.get(FeedBackVo.class, hqlget, false, vo.getId());
		return fvo;
	}	
	
	
}
