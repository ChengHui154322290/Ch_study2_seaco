package com.qiniu;

import com.qiniu.Mac;
import com.qiniu.Config;
import com.qiniu.ListItem;
import com.qiniu.ListPrefixRet;
import com.qiniu.RSFClient;

public class ListPrefix {

	public static void main(String[] args) {
		Config.ACCESS_KEY = "hHxSIgvdRg4y-xA1dzoCigBmQreP4KtSkvHuq3MV";
		Config.SECRET_KEY = "cIOm4gyYuwlQNpqBMOv2nCTQvR3bQ0gKGUvo-8at";
		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
		RSFClient client = new RSFClient(mac);
		ListPrefixRet list = client.listPrifix("duoduo", "", "", 10);
		System.out.println(list.results.size());
		for (ListItem item : list.results) {
			System.out.println(item.key);
		}
	}
}
