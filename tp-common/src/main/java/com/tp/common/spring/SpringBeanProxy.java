package com.tp.common.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * @ClassName: SpringBeanProxy
 * @Description: Spring context代理，用于获取容器内的实例
 * @author szy
 * 
 */
@Component
public class SpringBeanProxy  implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public synchronized void setApplicationContext(ApplicationContext arg0) {
		SpringBeanProxy.applicationContext = arg0;
	}

	public static <T> T getBean(Class<T> clazz, String beanName) {
		Object o = applicationContext.getBean(beanName);
		if (o != null) {
			return clazz.cast(o);
		}
		return null;
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
}