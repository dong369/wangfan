package com.wangfanpinche.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.vo.Version;

/**
 * 版本更新
 *
 */
@Controller
@RequestMapping("/v")
public class VersionController {
	
	@RequestMapping("/show")
	public @ResponseBody Map<String, String> getVersion(){
		Map<String, String> map = new HashMap<>();
		map.put("version", Version.getVersionNumber());
		map.put("iosUrl", Version.getIosDownloadUrl());
		map.put("androidUrl", Version.getAndroidDownloadUrl());
		return map;
	}

}
