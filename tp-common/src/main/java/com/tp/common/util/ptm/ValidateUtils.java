package com.tp.common.util.ptm;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections.CollectionUtils;


public class ValidateUtils <T>{

	/**
	 * 验证参数，采用Javax.validation.Validator验证标准，支持注解
	 * @param t
	 * @return
	 */
	public static <T> ResultMessage validate(T t){
		//验证参数
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(t); 
		if(CollectionUtils.isNotEmpty(violations)){
			for (ConstraintViolation<T> constraintViolation : violations) {
				ResultMessage message = new ResultMessage(ResultMessage.FAIL,constraintViolation.getPropertyPath()+":"+constraintViolation.getMessage(),"001");
				return message;
			}
		}
		return new ResultMessage(ResultMessage.SUCCESS,"true");
	}
	
	public static <T> ResultMessage validate(List<T> details){
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		for (T t2 : details) {
			Set<ConstraintViolation<T>> violationDetails = validator.validate(t2); 
			if(CollectionUtils.isNotEmpty(violationDetails)){
				for (ConstraintViolation<T> constraintViolation : violationDetails) {
					ResultMessage message = new ResultMessage(ResultMessage.FAIL,constraintViolation.getPropertyPath()+":"+constraintViolation.getMessage(),"001");
					return message;
				}
			}
		}
		return new ResultMessage(ResultMessage.SUCCESS,"true");
	}
}
