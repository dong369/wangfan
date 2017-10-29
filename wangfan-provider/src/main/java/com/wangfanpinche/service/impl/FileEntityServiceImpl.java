package com.wangfanpinche.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.FileEntity;
import com.wangfanpinche.service.FileEntityService;
import com.wangfanpinche.vo.FileEntityVo;

@Service
public class FileEntityServiceImpl implements FileEntityService {
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public void save(FileEntity fileEntity) {
		baseDao.save(fileEntity);
	}

	@Override
	public FileEntityVo getInfoByUrl(FileEntityVo vo) {
		String hql = " select new com.wangfanpinche.vo.FileEntityVo(f.id, f.md5, f.filename, f.oldFilename, f.filesize, f.path, f.url, f.extension, f.userIp) from FileEntity f where f.deleted = ? and f.url = ? ";
		FileEntityVo fvo = baseDao.get(FileEntityVo.class, hql, false, vo.getUrl());
		return fvo;
	}

}
