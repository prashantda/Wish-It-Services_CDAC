package com.utility.aopconfig;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.utility.entity.ServiceType;
import com.utility.model.User;
import com.utility.service.SupplierService;
import com.utility.valueobjects.ResponseTemplate;
import com.utility.valueobjects.USC;

@Aspect
@Component
@CrossOrigin
public class AspectConfiguration {
	@Autowired
	private SupplierService supplierService;
	
	@Pointcut ("execution(*  com.utility.contoller.*.getAllServices(..))")
	public void logging() {}
	
	@Around("logging()")	
	public Object supplierAOP(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("Before");
		Object[] args=pjp.getArgs();
		System.out.println(pjp.getSignature());
//		System.out.println(pjp.getTarget());
		String token=(String)args[0];
		System.out.println("AOP get all services"+token);
		User user=supplierService.getCustomerUser(token);
		Object object=null;
		System.out.println(user);
		object=pjp.proceed();
		List<ServiceType> list=(List<ServiceType>)object;
		object=(Object)list;
		Object object1=null;
		System.out.println("After");
		if(user !=null)
			return object;
		else 
			return object1;
	}
	
	
	
}
