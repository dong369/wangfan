package com.wangfanpinche.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.JournalService;
import com.wangfanpinche.utils.HibernateProxyFastJsonFilter;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.utils.pay.WechatDownloadBillResponse;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.vo.JournalVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

/**
 * 账单
 *
 */
@Controller
@RequestMapping("/journal")
public class JournalController {
	
	@Autowired
	private JournalService journalService;
	
	@RequestMapping("/getDetail")
	public @ResponseBody JournalVo getDetail(JournalVo vo){
		return journalService.getDetail(vo);
	}
	
	
	@RequestMapping("/list")
	public @ResponseBody List<JournalVo> list(JournalVo vo, HttpSession session){
		vo.setUserId(SessionUtils.getUserId(session));
		return journalService.list(vo);
	}
	
	
	@RequestMapping("/tongji")
	public String tongji(JournalVo vo, HttpSession session){
		return "/journal/tongji";
	}
	
	@RequestMapping("/downloadAliJournal")
	public @ResponseBody Json downloadAliJournal(String date){
		Json j = new Json();
		try {
			AlipayDataDataserviceBillDownloadurlQueryResponse response = AlipayAppUtils.downloadJournal(date);
			if(response.isSuccess()){
				j.setSuccess(true);
				j.setObj(response.getBillDownloadUrl());
			} else {
				j.setMessage(response.getSubMsg());
			}
			return j;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	@RequestMapping("/downloadWechatJournal")
	public @ResponseBody Json downloadWechatJournal(String date, HttpServletRequest request){
		String absFilePath = com.wangfanpinche.utils.file.FileUtils.getAbsFilePath(request) + "." + "csv";
		String path = request.getServletContext().getRealPath("/") + absFilePath;
		String url = request.getContextPath() + "/" + absFilePath;
		date = date.replaceAll("-", "");
		Json j = new Json();
		try {
			byte[] downloadbill = WechatPayAppUtils.downloadbill(date);
			String s = new String(downloadbill);
			if(s.startsWith("<xml>")){
				XmlMapper mapper = new XmlMapper();
				WechatDownloadBillResponse response = mapper.readValue(s, WechatDownloadBillResponse.class);
				j.setSuccess(false);
				j.setMessage(response.getReturn_msg());
			} else {
				j.setSuccess(true);
				FileUtils.writeByteArrayToFile(new File(path), downloadbill);
				j.setObj(url);
			}
			return j;
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(JournalVo vo, PageHelper ph){
		return journalService.table(vo, ph);
	}
	

}
