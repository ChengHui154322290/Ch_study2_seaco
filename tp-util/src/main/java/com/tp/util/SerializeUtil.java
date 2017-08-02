package com.tp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 序列化反序列化工具类
 * @author szy
 *
 */
public class SerializeUtil {

	private static Logger log = LoggerFactory.getLogger(SerializeUtil.class);

	/**
	 * 序列化
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		if (null == object){
			return null;
		}
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			try {
				if (null != baos) {
					baos.close();
				}
			} catch (IOException e) {
				log.info(e.getMessage());
			}
			try {
				if (null != oos) {
					oos.close();
				}
			} catch (IOException e) {
				log.info(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		if (null == bytes) {
			return null;
		}
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			return o;
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			try {
				if (null != ois) {
					ois.close();
				}
			} catch (IOException e) {
				log.info(e.getMessage());
			}
			try {
				if (null != bais) {
					bais.close();
				}
			} catch (IOException e) {
				log.info(e.getMessage());
			}
		}
		return null;
	}

}
