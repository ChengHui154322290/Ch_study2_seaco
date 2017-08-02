<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="com.qiniu.*,net.sf.json.JSONObject"%>
<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	String upload_ret = request.getParameter("upload_ret");
	if(null!=upload_ret){
	JSONObject callback = JSONObject.fromObject(Base64Coder.decode(upload_ret));
	JSONObject json = new JSONObject();
	if (callback.has("error")) {
		json.put("state", callback.get("error"));
	} else {
		json.put("original", callback.get("name"));
		json.put("url", callback.get("key"));
		json.put("state", "SUCCESS");
	}
	response.getWriter().print(json.toString());
	}
%>
