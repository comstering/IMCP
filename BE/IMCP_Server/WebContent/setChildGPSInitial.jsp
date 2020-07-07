<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("childKey");    //  아이 식별값
	String gpsData = request.getParameter("gps");    //  아이 초기 위치 데이터(JSON 데이터)
	
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.setChildIntial(childKey, gpsData);
	
	String returns = "";
	
	if(result == 1) {
		returns = "InitailSuccess";
	} else if(result == 0) {
		returns = "NoChildInfo";
	} else if(result == -1) {
		returns = "DBError";
	} else if(result == -2) {
		returns = "JSONError";
	} else if(result == -3) {
		returns = "DeleteError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>
