package com.tp.mq.util;

import java.util.HashMap;
import java.util.Map;

public class MqQueueHaPolicySingleton {
	
	private static volatile MqQueueHaPolicySingleton singleton;
	
	private Map<String, Object> queueArgs;

	private MqQueueHaPolicySingleton() {
		try {
			queueArgs = new HashMap<String, Object>();
			queueArgs.put("x-ha-policy", "all");
		} catch (Exception e) {
			singleton = null;
		}
	}

	public static MqQueueHaPolicySingleton getInstance() {
		if (singleton == null) {
			synchronized (MqQueueHaPolicySingleton.class) {
				if (singleton == null) {
					singleton = new MqQueueHaPolicySingleton();
				}
			}
		}
		return singleton;
	}

	public Map<String, Object> getQueueArgs() {
		return queueArgs;
	}

	public void setQueueArgs(Map<String, Object> queueArgs) {
		this.queueArgs = queueArgs;
	}

//	public static void main(String[] args) {
//		Map<String, Object> obj = QueueHaPolicySingleton.getInstance().getQueueArgs();
//		System.out.println(obj);
//	}

}
