package com.wangfanpinche.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfanpinche.service.TagService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-hibernate.xml", "classpath:spring/spring-druid.xml" })
public class DemoTest2 {
	@Autowired
	TagService tagService;

	@Test
	public void finAllTest() {
		//tagService.test();
	}

}
