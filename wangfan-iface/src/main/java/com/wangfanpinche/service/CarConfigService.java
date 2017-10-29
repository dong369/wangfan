package com.wangfanpinche.service;

import java.math.BigDecimal;
import com.wangfanpinche.vo.car.CarConfigVo;

public interface CarConfigService {
	
	public CarConfigVo add(CarConfigVo vo);
	
	public CarConfigVo get();	
	
	public BigDecimal getPostage();
	
	public BigDecimal getToll() ;

}
