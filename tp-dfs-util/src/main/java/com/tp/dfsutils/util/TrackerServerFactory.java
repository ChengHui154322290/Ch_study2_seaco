package com.tp.dfsutils.util;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-17 下午6:02:14
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class TrackerServerFactory extends BasePooledObjectFactory<TrackerServer> {

	private TrackerClient tracker = null;

	private TrackerGroup group = null;

	public TrackerServerFactory(String[] address) {
		try {
			int i = 0;
			InetSocketAddress isas[] = new InetSocketAddress[address.length];
			for (String ip : address) {
				String kv[] = ip.split(":");
				isas[i++] = new InetSocketAddress(kv[0], Integer.parseInt(kv[1]));
			}
			ClientGlobal.setG_charset("UTF-8");
			ClientGlobal.setG_tracker_group(new TrackerGroup(isas));
			tracker = new TrackerClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public TrackerServer create() throws Exception {
		TrackerServer trackerServer = null;
		try {
			trackerServer = tracker.getConnection();// 获得连接
		} catch (IOException e) {
			e.printStackTrace();
		}
		return trackerServer;
	}

	@Override
	public PooledObject<TrackerServer> wrap(TrackerServer value) {
		return new DefaultPooledObject<TrackerServer>(value);
	}

}
