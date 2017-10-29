package com.wangfanpinche.utils.validation.anno;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.wangfanpinche.utils.validation.IdCardValidator;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })  
@Retention(RUNTIME)  
//指定验证器  
@Constraint(validatedBy = IdCardValidator.class)  
@Documented  
public @interface IdCard {
	//默认错误消息  
    String message() default "身份证填写错误";  
  
    //分组  
    Class<?>[] groups() default { };  
  
    //负载  
    Class<? extends Payload>[] payload() default { };  
  
    //指定多个时使用  
    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })  
    @Retention(RUNTIME)  
    @Documented  
    @interface List {  
    	IdCard[] value();  
    }  
}
