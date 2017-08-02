package com.qiniu;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class ListPrefixRet extends CallRet {
	public String marker;
	public List<ListItem> results = new ArrayList<ListItem>();
	
	public ListPrefixRet(CallRet ret) {
		super(ret);
		if (ret.ok() && ret.getResponse() != null) {
			try {
				unmarshal(ret.getResponse());
			} catch (Exception e) {
				e.printStackTrace();
				this.exception = e;
			}
		}
	}
	
	private void unmarshal(String response) throws JSONException {
		JSONObject obj = JSONObject.fromObject(response);
		if (obj.has("marker")) {
			this.marker = obj.getString("marker");
		}
		JSONArray arr = obj.getJSONArray("items");
		for (int i = 0; i < arr.toArray().length; i++) {
			JSONObject jsonObj = arr.getJSONObject(i);
			ListItem ret = new ListItem(jsonObj);
			results.add(ret);
		}
	}
	
}
