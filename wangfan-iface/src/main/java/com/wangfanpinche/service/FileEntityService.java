package com.wangfanpinche.service;

import com.wangfanpinche.dto.FileEntity;
import com.wangfanpinche.vo.FileEntityVo;

public interface FileEntityService {

	void save(FileEntity fileEntity);
	
	/**
	 * 通过url获取图片详细信息
	 * @param vo
	 * @return
	 */
	public FileEntityVo getInfoByUrl(FileEntityVo vo);

}
