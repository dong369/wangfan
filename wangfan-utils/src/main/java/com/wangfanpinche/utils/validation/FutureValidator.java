package com.wangfanpinche.utils.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.wangfanpinche.utils.validation.anno.Future;

public class FutureValidator implements ConstraintValidator<Future, Temporal> {

	@Override
	public void initialize(Future constraintAnnotation) {
	}

	@Override
	public boolean isValid(Temporal value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		if(LocalDateTime.class.isInstance(value)){
			LocalDateTime ld = LocalDateTime.from(value);
			if (ld.isAfter(LocalDateTime.now())) {
				return true;
			} else {
				return false;
			}
		}
		if(LocalDate.class.isInstance(value)){
			LocalDate ld = LocalDate.from(value);
			if (ld.isAfter(LocalDate.now())) {
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}

}
