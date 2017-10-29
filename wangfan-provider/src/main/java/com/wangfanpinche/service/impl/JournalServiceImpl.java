package com.wangfanpinche.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Journal;
import com.wangfanpinche.dto.Journal.Status;
import com.wangfanpinche.service.JournalService;
import com.wangfanpinche.vo.JournalVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class JournalServiceImpl implements JournalService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public JournalVo getDetail(JournalVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JournalVo> list(JournalVo vo) {
		String hql = " select new com.wangfanpinche.vo.JournalVo(j.id, j.type, j.InOutType, j.payType, j.money, j.modifyDateTime) from Journal j where j.deleted = ? and j.user.id = ? and j.status = ? ";
		List<JournalVo> jList = baseDao.find(JournalVo.class, hql, false, vo.getUserId(), Status.完成);
		return jList;
	}

	@Override
	public BootstrapTable table(JournalVo vo, PageHelper ph) {

		BootstrapTable table = new BootstrapTable();
		Map<String, Object> params = new HashMap<>();

		StringBuilder hql = new StringBuilder(" from Journal j where j.id is not null ");
		
		addWhere(hql, params, vo);

		table.setRows(baseDao.find(Journal.class, hql.toString(), params, ph));
		table.getRows().forEach(  e -> {
			Journal f = (Journal) e;
			System.out.println(f.getOrderNumber());
		});

		addOrder(hql, params, ph);
		
		table.setTotal(baseDao.count(" select count(j.id) " + hql.toString(), params));
		return table;
	}

	private void addOrder(StringBuilder hql, Map<String, Object> params, PageHelper ph) {
		hql.append(" order by j.createDateTime desc");
	}

	private void addWhere(StringBuilder hql, Map<String, Object> params, JournalVo vo) {
		//判断订单创建时间
		if(vo.getSearchDate() != null){
			LocalDateTime start = LocalDateTime.of(vo.getSearchDate(), LocalTime.of(0, 0, 0));
			LocalDateTime end = LocalDateTime.of(vo.getSearchDate(), LocalTime.of(23, 59, 59));
			hql.append(" and j.createDateTime >= :startDate and j.createDateTime <= :endDate");
			params.put("startDate", start);
			params.put("endDate", end);
		}

		//判断订单号
		if(vo.getOrderNumber() != null){
			hql.append(" and j.orderNumber = :orderNumber");
			params.put("orderNumber", vo.getOrderNumber());
		}
		
	}

}
