package com.wangfanpinche.test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.wangfanpinche.service.OwnerOrderService;
import com.wangfanpinche.vo.OwnerOrderVo;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-hibernate.xml", "classpath:spring/spring-druid.xml" })
public class OwnerOrderServiceTest {
	
	@Autowired
	OwnerOrderService userService;
		
	@Test
	public void publishTest(){
		//lat:34.745813; lon:113.776052  34.8209475511,114.8407016107
		OwnerOrderVo vo = new OwnerOrderVo();
		vo.setFromLng(new BigDecimal("113.776052"));
		vo.setFromLat(new BigDecimal("34.745813"));
		vo.setToLng(new BigDecimal("114.8407016107"));
		vo.setToLat(new BigDecimal("34.8209475511"));
		vo.setSeat(4);
		vo.setDepartDateTime(LocalDateTime.now());
		vo.setVia("途径1-2-3-4");
		vo.setDescription("备注");
		vo.setFare(new BigDecimal(12));
		Serializable id = userService.publish(vo);
//		System.out.println("id--------"+id);
	}
		
}
