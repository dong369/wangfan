package com.wangfanpinche.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfanpinche.service.ProcityService;
import com.wangfanpinche.service.UserService;
import com.wangfanpinche.vo.ResourceVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-hibernate.xml", "classpath:spring/spring-druid.xml" })
public class ResourceServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProcityService pService;
	
	@Test
	public void procity(){
		pService.export();
	}
		
	@Test
	public void finAllTest(){
		List<ResourceVo> menuList = userService.getMenuList("1");
		menuList.forEach(e->{
			System.out.println(e.getId());
			System.out.println(e.getResourceId());
			System.out.println(e.getResourceName());
			System.out.println("----------");
		});
		System.out.println(menuList.size());
	}
		
}
