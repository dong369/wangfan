package com.wangfanpinche.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Tag;
import com.wangfanpinche.service.TagService;
import com.wangfanpinche.vo.TagVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class TagServiceImpl implements TagService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public BootstrapTable table(TagVo tagVo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql = "select new com.wangfanpinche.vo.TagVo( t.id, t.createDateTime, t.modifyDateTime, t.tagName, t.seq) from Tag t where t.deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		table.setTotal(baseDao.count("select count(id) from Tag where deleted = :deleted ", param));
		
		hql = addOrder(hql, ph);
		
		table.setRows(baseDao.find(TagVo.class, hql + " order by t.modifyDateTime desc ", param, ph));
		return table;
	}

	private String addOrder(String hql, PageHelper ph) {
		if(StringUtils.hasText(ph.getSort())){
			hql += " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return hql;
		
	}

	@Override
	public TagVo get(TagVo vo) {		
		Tag tag = baseDao.getById(Tag.class, vo.getId());
		BeanUtils.copyProperties(tag, vo);
		return vo;
	}

	@Override
	public void delete(TagVo vo) {
		String hql = "update Tag set deleted = ?, modifyDateTime = ? where deleted = ? and id = ? ";
		baseDao.execute(hql, true, LocalDateTime.now(), false, vo.getId());
	}

	@Override
	public void edit(TagVo vo) {
		Tag ta = baseDao.getById(Tag.class, vo.getId());
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, ta, "tagName", "seq");
		baseDao.update(ta);
	}

	@Override
	public void save(TagVo vo) {
		Tag ta = new Tag();
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, ta, "tagName", "seq");
		baseDao.save(ta);		
	}

	@Override
	public List<TagVo> list() {
		String hql = " select new com.wangfanpinche.vo.TagVo( t.id, t.tagName ) from Tag t where t.deleted = ? ";
		List<TagVo> tList = baseDao.find(TagVo.class, hql, false);
		return tList;
	}

}
