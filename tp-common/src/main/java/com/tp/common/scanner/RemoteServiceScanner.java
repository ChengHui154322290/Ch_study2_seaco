package com.tp.common.scanner;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.PriorityOrdered;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * 远程Service扫描器
 * 
 * <pre>
 * <b>自动扫描并加载远程服务，</b>
 * <ul>
 * 	<li>数据库相关的远程服务必须依Service结尾</li>
 *  <li>非数据库的远程服务依Srvc</li>
 * </ul>
 * </pre>
 * 
 */

@Component
public class RemoteServiceScanner extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryPostProcessor, ApplicationContextAware, PriorityOrdered {

	private static Logger log = LoggerFactory.getLogger(RemoteServiceScanner.class);

	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;
	private static final String PACKAGE_SEPARATER = ".";
	private static final String root = "com.tp.service";
	private static final String domain = ".soa.url";
	private String basePackage;
	
	private String soaUrl;
	private String soaNosqlUrl;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private DefaultListableBeanFactory beanFactory;

	@Override
	public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				Autowired cfg = field.getAnnotation(Autowired.class);
				if (cfg != null && field.getName().endsWith("Service")) {

					//检查指定的bean name 是否在容器中已经存在，如果存在，不进行覆盖
					boolean beanExists = beanFactory.isBeanNameInUse(field.getName());
					if (beanExists){
						return;
					}

					//按优安java规范，将接口类转换成实现类
					String beanClassName = field.getType().getCanonicalName();
					String simpleClassName = beanClassName.replace(root + PACKAGE_SEPARATER, "");
					String beanName = null;
					if (simpleClassName.contains(PACKAGE_SEPARATER)) {
						beanName = simpleClassName.replace(PACKAGE_SEPARATER, "/");
					} else {
						beanName = simpleClassName;
					}

					String[] parts = beanName.split("/");
					String serviceName = parts[parts.length - 1];
					if (serviceName.startsWith("I")) {
						serviceName = serviceName.substring(1, serviceName.length());
					}
					parts[parts.length - 1] = serviceName;
					
					//组装远程服务地址
					String holderSOAURL = parserSOAURL(field);
					if(holderSOAURL == null) {
						if(field.getName().endsWith("Srvc")){
							holderSOAURL = soaNosqlUrl;
						}else{
							holderSOAURL = soaUrl;;
						}
					}
					serviceName = holderSOAURL + "/" + StringUtils.join(parts, "/");

					log.info("Mapping instance " + field.getName() + " of class " + beanClassName + " at " + serviceName);

					GenericBeanDefinition bd = new GenericBeanDefinition();
					bd.setBeanClassName(HessianProxyFactoryBean.class.getName());
					bd.setBeanClass(HessianProxyFactoryBean.class);
					bd.getPropertyValues().add("serviceInterface", beanClassName);
					bd.getPropertyValues().add("serviceUrl", serviceName);
					bd.getPropertyValues().add("overloadEnabled", true);
					
					Reger scanner = new Reger((BeanDefinitionRegistry) beanFactory);
					BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(bd, field.getName());
					scanner.registerBeanDefinition(definitionHolder);

				}
			}
		});
		return true;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	/**
	 * 系统未拆分之前采用这个方法构建代理服务，系统拆分完成之后，此方法需要重新调整
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
		this.soaUrl = (String) ((java.util.Properties) beanFactory.getBean("settings")).get("soa.url");
		this.soaNosqlUrl = (String) ((java.util.Properties) beanFactory.getBean("settings")).get("soa.nosql.url");
	}
	
	/**
	 * 系统拆分后，根据成员变量的包路径构建代理服务
	 * 例：com.tp.account.service包，构建服务路径为
	 * http://${account.soa.url}/xxx.依account为子服务，后缀.sao.url(account.soa.url),在配置文件中查找
	 * @param field
	 * @return
	 */
	private String parserSOAURL(Field field) {
		String system = field.getGenericType().toString();
		system = system.substring(10,system.length());
		system = system.substring(0, system.indexOf("."));
		return (String) ((java.util.Properties) beanFactory.getBean("settings")).get(system+domain);
	}
	
	private class Reger extends ClassPathBeanDefinitionScanner {

		private BeanDefinitionRegistry registry;

		public Reger(BeanDefinitionRegistry registry) {
			super(registry);
			this.registry = registry;
		}

		public void registerBeanDefinition(BeanDefinitionHolder definitionHolder) {
			registerBeanDefinition(definitionHolder, registry);
		}
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
