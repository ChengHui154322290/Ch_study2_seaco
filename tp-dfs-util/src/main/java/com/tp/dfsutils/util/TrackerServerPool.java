package com.tp.dfsutils.util;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.fastdfs.TrackerServer;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-17 下午7:42:17
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class TrackerServerPool extends GenericObjectPool<TrackerServer> {

	public TrackerServerPool(PooledObjectFactory factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}

}
