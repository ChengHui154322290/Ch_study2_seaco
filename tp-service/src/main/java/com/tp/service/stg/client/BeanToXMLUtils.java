package com.tp.service.stg.client;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 封装xstream bean转为xml
 * @author szy
 * 2015年1月14日 下午1:09:13
 *
 */
public class BeanToXMLUtils {

	/**
	 * 将bean转为xml
	 * @param res
	 * 		对象
	 * @param classAlias
	 * 		类别名，即类应转为的节点名称，例如：
	 * @return
	 */
	public static String beansToXml(Object res,Map<Class<?>, String> classAlias){
		if(null==res){
			return null;
		}
		XStream stream = new XStream();
		if(null!=classAlias&&classAlias.size()>0){
			for (Entry<Class<?>, String> item : classAlias.entrySet()) {
				String alias = item.getValue();
				Class<?> clz = item.getKey();
				stream.alias(alias, clz);
			}
		}
		String xml = stream.toXML(res);
		return xml;
	}
	
	
	/**
	 * 将bean转为xml
	 * @param res
	 * 		对象
	 * @param classAlias
	 * 		类别名，即类应转为的节点名称，例如：
	 * 		<pre>
	 * 			package com.meitun;
	 * 			class Student{}
	 * 				
	 * 			stream.alias("stu", Student.class);			
	 * 			
	 * 			<stu>
	 * 
	 * 			</stu>
	 * 		</pre>
	 * @return
	 */
	public static String beansToXml(Object res,Map<Class<?>, String> classAlias,Map<Class<?>, String> implicitCollections){
		if(null==res){
			return null;
		}
		XStream stream = new XStream(new DomDriver());
		stream.autodetectAnnotations(true);
		if(null!=classAlias&&classAlias.size()>0){
			for (Entry<Class<?>, String> item : classAlias.entrySet()) {
				String alias = item.getValue();
				Class<?> clz = item.getKey();
				stream.alias(alias, clz);
			}
		}
		if(null!=implicitCollections&&implicitCollections.size()>0){
			for (Entry<Class<?>, String> item : implicitCollections.entrySet()) {
				String fieldName = item.getValue();
				Class<?> clz = item.getKey();
				stream.addImplicitCollection(clz, fieldName);
			}
		}
		String xml = stream.toXML(res);
		//由于xstream会将特殊字符转移为html编码，所以这里将编码转回特殊字符
		xml = StringEscapeUtils.unescapeHtml4(xml);
		return xml;
	}
	
	/**
	 * xml 转为bean
	 * @param xml
	 * @param classAlias
	 * @return
	 */
	public static Object xmlToBean(String xml,Map<Class<?>, String> classAlias){
		XStream stream = new XStream();
		if(null!=classAlias&&classAlias.size()>0){
			for (Entry<Class<?>, String> item : classAlias.entrySet()) {
				String alias = item.getValue();
				Class<?> clz = item.getKey();
				stream.alias(alias, clz);
			}
		}
		Object object = stream.fromXML(xml);
		return object;
	}

	
	/**
	 * xml 转为bean
	 * @param xml
	 * @param classAlias
	 * @return
	 */
	public static Object xmlToBean(String xml,Map<Class<?>, String> classAlias,Map<Class<?>, String> implicitCollections){
		XStream stream = new XStream();
		if(null!=classAlias&&classAlias.size()>0){
			for (Entry<Class<?>, String> item : classAlias.entrySet()) {
				String alias = item.getValue();
				Class<?> clz = item.getKey();
				stream.alias(alias, clz);
			}
		}
		if(null!=implicitCollections&&implicitCollections.size()>0){
			for (Entry<Class<?>, String> item : implicitCollections.entrySet()) {
				String fieldName = item.getValue();
				Class<?> clz = item.getKey();
				stream.addImplicitCollection(clz, fieldName);
			}
		}
		Object object = stream.fromXML(xml);
		return object;
	}
	
}
