package com.wangfanpinche.test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;

public class Test {
	public static void main(String[] args) throws Exception {
		List<String> readLines = FileUtils.readLines(new File("F:/aaa.txt"), Charset.forName("UTF-8"));
		
		List<Map<String, String>> collect = readLines.stream().map(e ->{
			Map<String, String> map = new HashMap<>();
			String[] split = e.split("\\s+");
			map.put("code", split[0]);
			map.put("name", split[1]);
			return map;
		}).collect(Collectors.toList());
		
		FileUtils.write(new File("F:/cityCodeList.js"), JSON.toJSONString(collect), Charset.forName("UTF-8"));
	}

}
