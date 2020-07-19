<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  부모 아이디
	String token = request.getParameter("token");    //  FCM 토큰
	
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.setFCMToken(id, token);
	
	String returns = "";
	
	if(result == 1) {
		returns = "SetFCMSuccess";
	} else if(result == -1) {
		returns = "DBError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>