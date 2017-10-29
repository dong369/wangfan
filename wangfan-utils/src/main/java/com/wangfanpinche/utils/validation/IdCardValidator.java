package com.wangfanpinche.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.wangfanpinche.utils.other.IdcardUtils;
import com.wangfanpinche.utils.validation.anno.IdCard;

/**
 * 身份证验证
 * @author kevin
 * @date 2016-10-17 15:20:28
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

	    @Override  
	    public void initialize(IdCard constraintAnnotation) {  
	        //初始化，得到注解数据  
	    	System.out.println(JSON.toJSONString(constraintAnnotation));
	    }  
	  
	    @Override  
	    public boolean isValid(String value, ConstraintValidatorContext context) {
	        if(StringUtils.isEmpty(value)) {  
	            return true;  
	        }

	       return IdcardUtils.validateCard(value);
	    }  

}
