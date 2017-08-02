<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="com.qiniu.*,net.sf.json.JSONObject"%>
<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	response.setContentType("application/json");
	String bucketName = request.getParameter("bucketName");
	JSONObject json = new JSONObject();
	json.put("token", Uptoken.makeUptoken(bucketName,request.getContextPath()));
	response.getWriter().print(json.toString());
%>
