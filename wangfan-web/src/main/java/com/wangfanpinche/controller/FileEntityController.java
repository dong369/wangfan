package com.wangfanpinche.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wangfanpinche.dto.FileEntity;
import com.wangfanpinche.provider.utils.BeanUtils;
import com.wangfanpinche.service.FileEntityService;
import com.wangfanpinche.utils.file.FileUtils;
import com.wangfanpinche.utils.file.MkFile;
import com.wangfanpinche.vo.FileEntityVo;
import com.wangfanpinche.vo.base.Json;


/**
 * 文件类
 * @author Thanatos
 *
 */
@Controller
@RequestMapping("/fileentity")
public class FileEntityController {
	
	@Autowired
	private FileEntityService fileEntityService;
	
	/**
	 * 上传
	 * @throws Exception 
	 */
	@RequestMapping("/upload")
	public @ResponseBody Json upload(MultipartFile file, HttpServletRequest request, HttpSession session) throws Exception{
		try {
			MkFile mkFile = FileUtils.conveterMkFile(file, request);
			FileEntity fileEntity = new FileEntity();
			BeanUtils.copyProperties(mkFile, fileEntity);
			fileEntityService.save(fileEntity);
			return new Json(true, null, fileEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 通过url获取图片详细信息
	 * @param vo
	 * @return
	 */
	@RequestMapping("/getInfoByUrl")
	public @ResponseBody FileEntityVo getInfoByUrl(FileEntityVo vo){
		return fileEntityService.getInfoByUrl(vo);
	}
	
	
}
